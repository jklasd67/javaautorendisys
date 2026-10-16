# Sõidukite rendisüsteem

Väike Java käsurearakendus autode ja kaubikute rentimiseks. Projekt demonstreerib pärilust, kapseldamist, polümorfismi, abstraktsiooni ning kollektsioonide kasutamist. Kasutajaliides ja õppematerjalid on eesti keeles.

## Võimalused

- Kõigi või ainult vabade sõidukite kuvamine päevahinna järgi kasvavalt.
- Tõstutundetu otsing registrinumbri, margi, mudeli ja sõidukitüübi järgi.
- Rendihinna arvutamine, rentimine, tagastamine ja rendiajaloo vaatamine.
- Vigase sisendi, tundmatu sõiduki ja topeltrentimise käsitlemine.

Auto hind on päevahind × päevad; alates 7 päevast rakendub 10% soodustus. Kaubiku hind on päevahind × päevad + ühekordne 15 € ettevalmistustasu. Raha arvutatakse `BigDecimal`-iga ja soodustuse tulemus ümardatakse sendini (`HALF_UP`).

## Käivitamine

Vaja on **JDK 17 või uuemat** ja UTF-8 kodeeringut toetavat terminali. Väliseid Java teeke, Mavenit ega andmebaasi pole vaja. Kontrolli, et `java -version` ja `javac -version` töötavad.

Käivita projekti juurkaustas:

```sh
mkdir -p out
javac -encoding UTF-8 -d out src/autorent/*.java
java -cp out autorent.Main
```

Windows PowerShellis võib esimese rea asemel kasutada `New-Item -ItemType Directory -Force out`. Ülejäänud käsud on samad. Failide kodeering peab olema UTF-8.

Menüü valikud on 0–7. Sisesta küsimustele vastused eraldi ridadel. Päevade arv peab olema positiivne `int`; programm küsib vigase arvu uuesti. Väljumiseks vali `0`. Ka sisendi lõpp lõpetab programmi.

## Näidiskasutus

1. Vali `1`: näed nelja näidissõidukit (123ABC, 456DEF, 789GHI, 321JKL).
2. Vali `4`, sisesta `123ABC` ja `7`: rendihind on **220.50 €**.
3. Vali `5`, sisesta `123ABC`, `Mari` ja `7`: luuakse rent #1.
4. Vali `2`: renditud auto vabade sõidukite seas enam ei ole.
5. Vali `6` ja sisesta `123ABC`: auto muutub vabaks.
6. Vali `7`: lõpetatud rent jääb ajalukku.
7. Vali `0`: rakendus sulgub.

## Kontrollimine

```sh
mkdir -p out
javac -encoding UTF-8 -Xlint:all -d out src/autorent/*.java tests/autorent/*.java
java -cp out autorent.RenditeenusTest
```

Eduka jooksu lõpus kuvatakse `Kõik 36 kontrolli läbisid edukalt.` Testid kontrollivad hinnareegleid, ümardamist, otsingut, sorteerimist, vigaseid andmeid, rentimist, tagastamist ja nimekirjade kaitsmist välise muutmise eest. Testid ei vaja `-ea` lippu.

## Ülesehitus

| Fail | Vastutus |
| --- | --- |
| `src/autorent/Soiduk.java` | Abstraktne ülemklass: ühised andmed, valideerimine ja baashind |
| `src/autorent/Auto.java` | Auto istekohtade arv ja pika rendi soodustus |
| `src/autorent/Kaubik.java` | Kaubaruumi maht ja ettevalmistustasu |
| `src/autorent/Rent.java` | Ühe tehingu andmed ja aktiivsuse olek |
| `src/autorent/Renditeenus.java` | Sõidukite ja rentide haldamise ärireeglid |
| `src/autorent/Main.java` | Käsureamenüü ja kasutajasisend |
| `tests/autorent/RenditeenusTest.java` | Automaatkontrollid |
| `vastused.md` | Nõuete selgitused, kaitsmise küsimused ja muudatuste harjutused |

## Piirangud

Andmed püsivad ainult programmi mälus. Igal käivitusel laaditakse samad näidissõidukid ja ajalugu algab tühjalt. Tegemist on ühe kasutaja rakendusega. Rent algab tänasest, päevade arv on hinnastamise alus; automaatset tagastamist, tulevikubroneeringuid ega hilinemistasusid pole. Sorteerimine kasutab baaspäevahinda, mitte konkreetse rendi lõpphinda.

## GitHubi esitamine

Kohalikud commit'id ei avalda projekti GitHubis. Loo oma GitHubi kontol tühi repositoorium ja käivita järgmised käsud, asendades `KASUTAJANIMI` oma kasutajanimega:

```sh
git remote add origin https://github.com/KASUTAJANIMI/javaautorendisys.git
git push -u origin HEAD
```

Kui `origin` on juba olemas, vaata seda käsuga `git remote -v`; vajadusel paranda aadress käsuga `git remote set-url origin ...`. GitHub võib küsida autentimist. Õpetajale esita loodud repositooriumi päris veebiaadress; ülaltoodud näidisaadress ei ole valmis esitamislink.

Kontrolli ajalugu ja töökataloogi käsuga `git log --oneline` ning `git status`. Edasiste muudatuste puhul kasuta näiteks `git add src/autorent/Auto.java` ja `git commit -m "Muuda auto pika rendi soodustust"`, seejärel `git push`.

Projekt on loodud AI abiga. Enne kaitsmist loe lähtekoodi, tööta läbi `vastused.md` ning proovi näidismuudatusi ise teha ja testida.
