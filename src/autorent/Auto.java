package autorent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Auto extends Soiduk {
    private final int istekohti;

    public Auto(String number, String mark, String mudel, BigDecimal paevahind, int istekohti) {
        super(number, mark, mudel, paevahind);
        if (istekohti < 1 || istekohti > 9) {
            throw new IllegalArgumentException("Autol peab olema 1–9 istekohta.");
        }
        this.istekohti = istekohti;
    }

    public int getIstekohti() { return istekohti; }

    @Override
    public String getTyyp() { return "Auto"; }

    @Override
    public String getLisainfo() { return "Istekohti: " + istekohti; }

    @Override
    public BigDecimal arvutaHind(int paevad) {
        BigDecimal hind = super.arvutaHind(paevad);
        // Vähemalt seitsmeks päevaks renditud autole kehtib 10% soodustus.
        return paevad >= 7
                ? hind.multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP)
                : hind;
    }
}
