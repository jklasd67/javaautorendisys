package autorent;

import java.math.BigDecimal;

public class Kaubik extends Soiduk {
    private final double kaubaruumiMaht;
    private static final BigDecimal ETTEVALMISTUSTASU = new BigDecimal("15.00");

    public Kaubik(String number, String mark, String mudel, BigDecimal paevahind, double maht) {
        super(number, mark, mudel, paevahind);
        if (!Double.isFinite(maht) || maht <= 0) {
            throw new IllegalArgumentException("Kaubaruumi maht peab olema positiivne lõplik arv.");
        }
        this.kaubaruumiMaht = maht;
    }

    public double getKaubaruumiMaht() { return kaubaruumiMaht; }

    @Override
    public String getTyyp() { return "Kaubik"; }

    @Override
    public String getLisainfo() { return "Kaubaruum: " + kaubaruumiMaht + " m³"; }

    @Override
    public BigDecimal arvutaHind(int paevad) {
        // Tasu lisatakse kogu rendile ühe korra, mitte igale päevale.
        return super.arvutaHind(paevad).add(ETTEVALMISTUSTASU);
    }
}
