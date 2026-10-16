package autorent;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Ühe renditehingu andmed. Hind fikseeritakse rendi loomisel. */
public class Rent {
    private final int id;
    private final Soiduk soiduk;
    private final String kliendiNimi;
    private final int paevad;
    private final LocalDate algus;
    private final BigDecimal koguhind;
    private boolean aktiivne;

    public Rent(int id, Soiduk soiduk, String kliendiNimi, int paevad) {
        if (id <= 0 || soiduk == null || kliendiNimi == null || kliendiNimi.isBlank()) {
            throw new IllegalArgumentException("Rendil peab olema positiivne ID, sõiduk ja kliendi nimi.");
        }
        this.koguhind = soiduk.arvutaHind(paevad);
        this.id = id;
        this.soiduk = soiduk;
        this.kliendiNimi = kliendiNimi.trim();
        this.paevad = paevad;
        this.algus = LocalDate.now();
        this.aktiivne = true;
    }

    public int getId() { return id; }
    public Soiduk getSoiduk() { return soiduk; }
    public String getKliendiNimi() { return kliendiNimi; }
    public int getPaevad() { return paevad; }
    public LocalDate getAlgus() { return algus; }
    public BigDecimal getKoguhind() { return koguhind; }
    public boolean isAktiivne() { return aktiivne; }

    // Ainult sama paketi teenus saab renti lõpetada. Avalikku setAktiivne meetodit pole.
    void lopeta() {
        if (!aktiivne) {
            throw new IllegalStateException("Rent on juba lõpetatud.");
        }
        aktiivne = false;
    }

    @Override
    public String toString() {
        return "Rent #" + id + " | " + soiduk.getRegistrinumber() + " | " + kliendiNimi
                + " | algus " + algus + " | " + paevad + " päeva | "
                + koguhind.toPlainString() + " € | " + (aktiivne ? "aktiivne" : "lõpetatud");
    }
}
