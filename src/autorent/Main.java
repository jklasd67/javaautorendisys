package autorent;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/** Käsurealiides: sisend, väljund ja teenuse meetodite käivitamine. */
public class Main {
    public static void main(String[] args) {
        Renditeenus teenus = looNaidisandmed();
        try (Scanner scanner = new Scanner(System.in, "UTF-8")) {
            while (true) {
                prindiMenuu();
                try {
                    String valik = loeRida(scanner, "Valik: ");
                    switch (valik) {
                        case "1": kuvaSoidukid(teenus, "", false); break;
                        case "2": kuvaSoidukid(teenus, "", true); break;
                        case "3":
                            kuvaSoidukid(teenus, loeRida(scanner, "Otsi numbri, margi, mudeli või tüübi järgi: "), false);
                            break;
                        case "4":
                            Soiduk soiduk = teenus.leiaSoiduk(loeRida(scanner, "Registrinumber: "));
                            int paevad = loePositiivneInt(scanner, "Päevade arv: ");
                            // Viide on Soiduk-tüüpi, kuid käivitub Auto või Kaubiku hinnareegel.
                            System.out.println("Rendihind: " + soiduk.arvutaHind(paevad).toPlainString() + " €");
                            break;
                        case "5":
                            String number = loeRida(scanner, "Registrinumber: ");
                            String klient = loeRida(scanner, "Kliendi nimi: ");
                            int kestus = loePositiivneInt(scanner, "Päevade arv: ");
                            System.out.println("Rent loodud: " + teenus.rendi(number, klient, kestus));
                            break;
                        case "6":
                            System.out.println("Tagastatud: " + teenus.tagasta(loeRida(scanner, "Registrinumber: ")));
                            break;
                        case "7":
                            List<Rent> rendid = teenus.getRendid();
                            if (rendid.isEmpty()) System.out.println("Rendiajalugu on tühi.");
                            for (Rent rent : rendid) System.out.println(rent);
                            break;
                        case "0": System.out.println("Head aega!"); return;
                        default: System.out.println("Tundmatu valik. Sisesta menüü number.");
                    }
                } catch (SisendLopp erind) {
                    // Ka faili kaudu antud sisendi lõpp lõpetab programmi viisakalt.
                    System.out.println("\nSisend lõppes. Head aega!");
                    return;
                } catch (IllegalArgumentException | IllegalStateException erind) {
                    System.out.println("Viga: " + erind.getMessage());
                }
            }
        }
    }

    private static Renditeenus looNaidisandmed() {
        Renditeenus teenus = new Renditeenus();
        teenus.lisaSoiduk(new Auto("123ABC", "Toyota", "Corolla", new BigDecimal("35.00"), 5));
        teenus.lisaSoiduk(new Auto("456DEF", "Skoda", "Octavia", new BigDecimal("40.00"), 5));
        teenus.lisaSoiduk(new Kaubik("789GHI", "Ford", "Transit", new BigDecimal("60.00"), 10.5));
        teenus.lisaSoiduk(new Kaubik("321JKL", "Renault", "Master", new BigDecimal("70.00"), 13.0));
        return teenus;
    }

    private static void kuvaSoidukid(Renditeenus teenus, String otsing, boolean ainultVabad) {
        List<Soiduk> soidukid = teenus.otsiSoidukeid(otsing, ainultVabad);
        if (soidukid.isEmpty()) System.out.println("Sobivaid sõidukeid ei leitud.");
        for (Soiduk soiduk : soidukid) {
            System.out.println(soiduk + " | " + (teenus.onVaba(soiduk.getRegistrinumber()) ? "vaba" : "renditud"));
        }
    }

    private static String loeRida(Scanner scanner, String kysimus) {
        System.out.print(kysimus);
        if (!scanner.hasNextLine()) throw new SisendLopp();
        return scanner.nextLine().trim();
    }

    private static int loePositiivneInt(Scanner scanner, String kysimus) {
        while (true) {
            String sisend = loeRida(scanner, kysimus);
            try {
                int arv = Integer.parseInt(sisend);
                if (arv > 0) return arv;
            } catch (NumberFormatException erind) {
                // Tekst ja int-piiridest suurem arv suunatakse sama veateate juurde.
            }
            System.out.println("Sisesta positiivne täisarv (1–2147483647).");
        }
    }

    private static void prindiMenuu() {
        System.out.println("\n=== Sõidukite rent ===");
        System.out.println("1 – Kõik sõidukid (päevahinna järgi)");
        System.out.println("2 – Ainult vabad sõidukid");
        System.out.println("3 – Otsi sõidukeid");
        System.out.println("4 – Arvuta rendihind");
        System.out.println("5 – Rendi sõiduk");
        System.out.println("6 – Tagasta sõiduk");
        System.out.println("7 – Rendiajalugu");
        System.out.println("0 – Välju (andmeid ei salvestata)");
    }

    private static class SisendLopp extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
