package autorent;

import java.math.BigDecimal;
import java.util.List;

/** Sõltuvusteta testid. Ebaõnnestumine annab nullist erineva väljumiskoodi. */
public class RenditeenusTest {
    private static int kontrolle;

    public static void main(String[] args) {
        Soiduk auto = new Auto("123abc", "Toyota", "Corolla", new BigDecimal("35"), 5);
        Soiduk kaubik = new Kaubik("789GHI", "Ford", "Transit", new BigDecimal("60"), 10.5);
        raha("210.00", auto.arvutaHind(6));
        raha("220.50", auto.arvutaHind(7));
        raha("75.00", kaubik.arvutaHind(1));
        raha("435.00", kaubik.arvutaHind(7));
        // Kontrollime soodustuse ümardamist sendini.
        raha("6.24", new Auto("A", "A", "A", new BigDecimal("0.99"), 1).arvutaHind(7));
        ootaViga(IllegalArgumentException.class, () -> auto.arvutaHind(0));
        ootaViga(IllegalArgumentException.class, () -> kaubik.arvutaHind(-1));
        ootaViga(IllegalArgumentException.class,
                () -> new Auto(" ", "A", "B", BigDecimal.ONE, 5));
        ootaViga(IllegalArgumentException.class,
                () -> new Auto("A", "A", "B", BigDecimal.ZERO, 5));
        ootaViga(ArithmeticException.class,
                () -> new Auto("A", "A", "B", new BigDecimal("1.001"), 5));
        ootaViga(IllegalArgumentException.class,
                () -> new Auto("A", "A", "B", BigDecimal.ONE, 10));
        ootaViga(IllegalArgumentException.class,
                () -> new Kaubik("A", "A", "B", BigDecimal.ONE, Double.NaN));

        Renditeenus teenus = new Renditeenus();
        teenus.lisaSoiduk(kaubik);
        teenus.lisaSoiduk(auto);
        kontroll(teenus.leiaSoiduk(" 123AbC ") == auto, "Numbri normaliseerimine");
        ootaViga(IllegalArgumentException.class, () -> teenus.lisaSoiduk(auto));
        ootaViga(IllegalArgumentException.class, () -> teenus.lisaSoiduk(null));
        ootaViga(IllegalArgumentException.class, () -> teenus.leiaSoiduk("PUUDUB"));
        kontroll(teenus.otsiSoidukeid("", false).get(0) == auto, "Hinnajärjekord");
        kontroll(teenus.otsiSoidukeid("tOyOtA", false).size() == 1, "Margiotsing");
        kontroll(teenus.otsiSoidukeid("Transit", false).size() == 1, "Mudeliotsing");
        kontroll(teenus.otsiSoidukeid("KAUBIK", false).size() == 1, "Tüübiotsing");
        kontroll(teenus.otsiSoidukeid("ei leidu", false).isEmpty(), "Tühi tulemus");
        ootaViga(IllegalArgumentException.class, () -> teenus.rendi("123ABC", " ", 2));
        ootaViga(IllegalArgumentException.class, () -> teenus.rendi("123ABC", "Mari", 0));
        kontroll(teenus.getRendid().isEmpty() && teenus.onVaba("123ABC"), "Vigane rent ei muuda olekut");

        Rent rent = teenus.rendi("123ABC", " Mari ", 7);
        kontroll(rent.getId() == 1 && rent.getKliendiNimi().equals("Mari"), "Rendi andmed");
        raha("220.50", rent.getKoguhind());
        kontroll(!teenus.onVaba("123ABC"), "Renditud auto pole vaba");
        kontroll(teenus.otsiSoidukeid("", true).size() == 1, "Vabade filter");
        ootaViga(IllegalStateException.class, () -> teenus.rendi("123ABC", "Jüri", 1));
        List<Rent> ajalooKoopia = teenus.getRendid();
        ootaViga(UnsupportedOperationException.class, ajalooKoopia::clear);
        ootaViga(UnsupportedOperationException.class, () -> teenus.otsiSoidukeid("", false).clear());
        kontroll(teenus.tagasta("123ABC") == rent && !rent.isAktiivne(), "Tagastamine");
        kontroll(teenus.onVaba("123ABC") && teenus.getRendid().size() == 1, "Ajalugu säilib");
        ootaViga(IllegalStateException.class, () -> teenus.tagasta("123ABC"));
        Rent uusRent = teenus.rendi("123ABC", "Jüri", 1);
        kontroll(uusRent.getId() == 2 && uusRent.isAktiivne(), "Uuesti rentimine");
        kontroll(ajalooKoopia.size() == 1, "Varem väljastatud nimekiri ei kasva");
        System.out.println("Kõik " + kontrolle + " kontrolli läbisid edukalt.");
    }

    private static void raha(String oodatud, BigDecimal tegelik) {
        kontroll(new BigDecimal(oodatud).equals(tegelik), "Oodatud " + oodatud + ", saadi " + tegelik);
    }

    private static void kontroll(boolean tingimus, String selgitus) {
        if (!tingimus) throw new AssertionError(selgitus);
        kontrolle++;
    }

    private static void ootaViga(Class<? extends Throwable> tyyp, Runnable tegevus) {
        try {
            tegevus.run();
        } catch (Throwable erind) {
            if (!tyyp.isInstance(erind)) throw new AssertionError("Vale erind", erind);
            kontrolle++;
            return;
        }
        throw new AssertionError("Oodati erindit: " + tyyp.getSimpleName());
    }
}
