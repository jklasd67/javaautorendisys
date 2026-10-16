package autorent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/** Ühised andmed ja reeglid kõigile renditavatele sõidukitele. */
public abstract class Soiduk {
    private final String registrinumber;
    private final String mark;
    private final String mudel;
    private final BigDecimal paevahind;

    protected Soiduk(String registrinumber, String mark, String mudel, BigDecimal paevahind) {
        this.registrinumber = normaliseeriNumber(registrinumber);
        this.mark = kontrolliTeksti(mark, "Mark");
        this.mudel = kontrolliTeksti(mudel, "Mudel");
        if (paevahind == null || paevahind.signum() <= 0) {
            throw new IllegalArgumentException("Päevahind peab olema positiivne.");
        }
        // Raha hoitakse täpselt kahe komakohaga; liigseid komakohti ei ümardata salaja.
        this.paevahind = paevahind.setScale(2, RoundingMode.UNNECESSARY);
    }

    public static String normaliseeriNumber(String number) {
        return kontrolliTeksti(number, "Registrinumber").toUpperCase(Locale.ROOT);
    }

    private static String kontrolliTeksti(String tekst, String valjaNimi) {
        if (tekst == null || tekst.isBlank()) {
            throw new IllegalArgumentException(valjaNimi + " ei tohi olla tühi.");
        }
        return tekst.trim();
    }

    public String getRegistrinumber() { return registrinumber; }
    public String getMark() { return mark; }
    public String getMudel() { return mudel; }
    public BigDecimal getPaevahind() { return paevahind; }

    public BigDecimal arvutaHind(int paevad) {
        if (paevad <= 0) {
            throw new IllegalArgumentException("Rendipäevade arv peab olema positiivne.");
        }
        return paevahind.multiply(BigDecimal.valueOf(paevad));
    }

    // Iga alamklass peab andma oma tüübi ja lisainfo.
    public abstract String getTyyp();
    public abstract String getLisainfo();

    @Override
    public String toString() {
        return registrinumber + " | " + mark + " " + mudel + " | " + getTyyp()
                + " | " + paevahind.toPlainString() + " €/päev | " + getLisainfo();
    }
}
