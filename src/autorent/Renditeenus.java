package autorent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Ärireeglid asuvad siin; see klass ei loe klaviatuuri ega prindi menüüd. */
public class Renditeenus {
    // Võti on üheselt määrav registrinumber: leidmine on keskmiselt O(1).
    private final Map<String, Soiduk> soidukid = new HashMap<>();
    // Tehingud säilivad lisamise järjekorras, ka pärast tagastamist.
    private final List<Rent> rendid = new ArrayList<>();
    private int jargmineId = 1;

    public void lisaSoiduk(Soiduk soiduk) {
        if (soiduk == null) {
            throw new IllegalArgumentException("Sõiduk puudub.");
        }
        if (soidukid.putIfAbsent(soiduk.getRegistrinumber(), soiduk) != null) {
            throw new IllegalArgumentException("Sama registrinumbriga sõiduk on juba olemas.");
        }
    }

    public Soiduk leiaSoiduk(String number) {
        Soiduk soiduk = soidukid.get(Soiduk.normaliseeriNumber(number));
        if (soiduk == null) {
            throw new IllegalArgumentException("Sellise numbriga sõidukit ei leitud.");
        }
        return soiduk;
    }

    public boolean onVaba(String number) {
        Soiduk soiduk = leiaSoiduk(number);
        for (Rent rent : rendid) {
            if (rent.isAktiivne() && rent.getSoiduk() == soiduk) {
                return false;
            }
        }
        return true;
    }

    public List<Soiduk> otsiSoidukeid(String otsing, boolean ainultVabad) {
        if (otsing == null) {
            throw new IllegalArgumentException("Otsing ei tohi olla null.");
        }
        String tekst = otsing.trim().toLowerCase(Locale.ROOT);
        List<Soiduk> tulemus = new ArrayList<>();
        for (Soiduk soiduk : soidukid.values()) {
            String kirjeldus = (soiduk.getRegistrinumber() + " " + soiduk.getMark()
                    + " " + soiduk.getMudel() + " " + soiduk.getTyyp()).toLowerCase(Locale.ROOT);
            if (kirjeldus.contains(tekst) && (!ainultVabad || onVaba(soiduk.getRegistrinumber()))) {
                tulemus.add(soiduk);
            }
        }
        // HashMap ei taga järjekorda, seetõttu sorteerime väljundi teadlikult.
        tulemus.sort(Comparator.comparing(Soiduk::getPaevahind)
                .thenComparing(Soiduk::getRegistrinumber));
        return List.copyOf(tulemus);
    }

    public Rent rendi(String number, String klient, int paevad) {
        Soiduk soiduk = leiaSoiduk(number);
        if (!onVaba(number)) {
            throw new IllegalStateException("Sõiduk on juba välja renditud.");
        }
        // Vigase rendi korral konstruktor katkestab tegevuse enne andmete muutmist.
        Rent rent = new Rent(jargmineId, soiduk, klient, paevad);
        rendid.add(rent);
        jargmineId++;
        return rent;
    }

    public Rent tagasta(String number) {
        Soiduk soiduk = leiaSoiduk(number);
        for (Rent rent : rendid) {
            if (rent.isAktiivne() && rent.getSoiduk() == soiduk) {
                rent.lopeta();
                return rent;
            }
        }
        throw new IllegalStateException("Sellel sõidukil ei ole aktiivset renti.");
    }

    public List<Rent> getRendid() {
        // Koopia takistab kutsujal teenuse sisemist nimekirja muuta.
        return List.copyOf(rendid);
    }
}
