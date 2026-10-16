# Selgitused ja kaitsmiseks valmistumine

See materjal selgitab projektis tehtud valikuid ning vastab ülesande küsimustele. Kood on loodud AI abiga. Kaitsmisel tuleb näidata oma arusaamist: käivita programm, leia mainitud kohad lähtekoodist ning harjuta muudatusi ise.

## 1. Mis probleemi rakendus lahendab?

Rakendus aitab rendiettevõttel näha sõidukeid, arvutada hinda, registreerida renti ning tagastada sõiduki. Sama sõidukit ei saa korraga kahele kliendile rentida. Lõpetatud rendid säilivad ajaloos. Andmebaasi ja graafilist liidest pole; andmed on mälus.

Näide: Toyota päevahind on 35 €. Kuue päeva hind on 210 €. Seitsme päeva hind on 35 × 7 × 0,90 = 220,50 €. Fordi kaubiku seitsme päeva hind on 60 × 7 + 15 = 435 €.

## 2. Klassid ja nende vastutused

| Klass | Mida teeb ja miks vajalik? |
| --- | --- |
| `Soiduk` | Hoidab kõigi sõidukite registrinumbrit, marki, mudelit ja päevahinda; kontrollib ühiseid reegleid. |
| `Auto` | Lisab istekohtade arvu ja rakendab vähemalt 7 päeva korral soodustust. |
| `Kaubik` | Lisab kaubaruumi mahu ja ühekordse ettevalmistustasu. |
| `Rent` | Seob sõiduki kliendi, päevade, alguskuupäeva, hinna ja aktiivsuse olekuga. |
| `Renditeenus` | Haldab kollektsioone, otsib, rendib ja tagastab. Väldib topeltrentimist. |
| `Main` | Küsib sisendit, näitab menüüd ja kutsub teenust. |

Oma põhiklasse on kuus, lisaks testiklass ja sisendi lõpu erindiklass. Selline jaotus võimaldab äriloogikat testida ilma klaviatuuri või menüüta. `Main` ei arvuta ise soodustusi ja `Renditeenus` ei prindi kasutajale küsimusi.

## 3. Kus on OOP põhimõtted?

### Pärilus

`Auto extends Soiduk` ja `Kaubik extends Soiduk`. Mõlemad alamklassid pärivad ühised meetodid ja saavad kasutada ülemklassi hinnaloogikat. `super(...)` käivitab ülemklassi konstruktori; `super.arvutaHind(paevad)` arvutab baashinna. Privaatsed väljad pole alamklassi koodist otse ligipääsetavad, seepärast kasutatakse ülemklassi meetodeid.

### Kapseldamine

Väljad on `private`. Sõiduki andmed on lisaks `final`, seega viidet või primitiivi väärtust ei saa pärast loomist ümber määrata. Getterid võimaldavad lugemist, kuid suvalisi settereid pole. Konstruktor keeldub näiteks tühjast registrinumbrist ja mittepositiivsest hinnast.

`Rent.aktiivne` muutub ainult meetodis `lopeta()`, mis keelab korduva lõpetamise. Meetod on paketi nähtavusega: teine klass samas paketis saaks seda kutsuda, seega on see piirang, mitte täielik ligipääsukeeld. Tavapärases programmis kutsub seda `Renditeenus.tagasta()`.

`getRendid()` ja otsing tagastavad `List.copyOf(...)`, mistõttu kutsuja ei saa sisemisse nimekirja elemente lisada ega neid kustutada. See on **madal koopia**: `Rent`-objektid on samad, seega nende aktiivsuse muutus on varasemas koopias nähtav. Nimekirja elementide arv varasemas koopias ei muutu.

### Polümorfism

```java
Soiduk esimene = new Auto("123ABC", "Toyota", "Corolla", new BigDecimal("35.00"), 5);
Soiduk teine = new Kaubik("789GHI", "Ford", "Transit", new BigDecimal("60.00"), 10.5);
System.out.println(esimene.arvutaHind(7)); // 220.50
System.out.println(teine.arvutaHind(7));   // 435.00
```

Mõlemad viited on tüüpi `Soiduk`, kuid käivituv üle kirjutatud meetod valitakse tegeliku objekti tüübi järgi. Sama toimub `Rent`-konstruktoris ja menüü hinnapäringus. Alamklassi tuvastamiseks ei ole vaja `instanceof`-tingimusi. `@Override` laseb kompilaatoril kontrollida, et meetod tõesti kirjutab päritud meetodi üle.

### Abstraktsioon

`Soiduk` on `abstract`, seega `new Soiduk(...)` pole lubatud. Ülemklass väljendab sõiduki ühist olemust. `getTyyp()` ja `getLisainfo()` on abstraktsed: konkreetne alamklass peab neile teostuse andma. `arvutaHind()` on seevastu ühise teostusega meetod, mida saab vajadusel üle kirjutada.

## 4. Andmetüübid ja nende põhjendused

| Tüüp | Näide | Põhjus |
| --- | --- | --- |
| `int` | Päevad, istekohtade arv, rendi ID | Loendatavad täisarvud; murdosa pole mõistlik. |
| `boolean` | Rendi aktiivsus, vabade filter | Kaks võimalikku olekut. |
| `double` | Kaubaruumi maht | Mõõdetud suurusel võib olla murdosa; kontrollitakse ka NaN-i ja lõpmatust. |
| `String` | Number, mark, nimi | Tekst, mida ei kasutata arvutamiseks. Registrinumbri numbrimärgid ei muuda seda arvuks. |
| `BigDecimal` | Päevahind, koguhind | Täpsed kümnendarvutused rahaga; väldib `double`-tüübi binaarse esituse ümardusvigu. |
| `LocalDate` | Rendi algus | Kuupäev ilma kellaaja ja ajavööndita; `now()` kasutab arvuti kohalikku kuupäeva. |
| `Soiduk`, `Rent` | Viited oma objektidele | Ühendavad omavahel seotud andmed ja käitumise. |

`int`, `boolean` ja `double` on primitiivid, ülejäänud tabeli tüübid on viitetüübid. Viide võib üldiselt olla `null`; primitiiv ei saa olla `null`. Konstruktorid kontrollivad kohustuslikke viiteid.

Raha luuakse tekstist: `new BigDecimal("0.90")`. `new BigDecimal(0.1)` kannaks sisse `double`-väärtuse ebatäpsuse. Soodustus ümardatakse `setScale(2, RoundingMode.HALF_UP)` abil; näiteks 0,99 × 7 × 0,90 = 6,237 → 6,24 €. Päevahinna puhul ei lubata sendist väiksemat täpsust: `1.001` tekitab `ArithmeticException`-i.

## 5. Miks need kollektsioonid?

`Renditeenus` kasutab kahte erinevat teostust:

- `HashMap<String, Soiduk>`: võti on normaliseeritud registrinumber. Otsene numbripäring on keskmiselt O(1), sama võti ei saa esineda kaks korda. `putIfAbsent` väldib olemasoleva sõiduki asendamist. Iteratsioonijärjekord pole tagatud.
- `ArrayList<Rent>`: rentide ajalugu säilib lisamise järjekorras. Lisamine lõppu on amortiseeritult O(1), ajaloo läbivaatamine O(n). Aktiivse rendi leidmiseks käiakse ajalugu läbi.

Väljade tüübid on liidesed `Map` ja `List`, konstruktorites valitakse `HashMap` ning `ArrayList`. Nii sõltub ülejäänud kood kollektsiooni pakutavatest operatsioonidest ja teostust saab lihtsamini vahetada.

Suurtes andmehulkades on praegune vabade sõidukite otsing aeglasem: iga sõiduki jaoks võidakse kõik rendid läbi käia, ligikaudu O(sõidukite arv × rentide arv), lisaks sorteerimine O(sõidukite arv × log(sõidukite arv)). Väikese koolitöö puhul on lahendus lihtne. Suuremas süsteemis võiks aktiivseid rente eraldi kaardis indekseerida, hoides ajaloo endiselt listis.

### Otsing, filtreerimine ja sorteerimine

`otsiSoidukeid` ühendab registrinumbri, margi, mudeli ja tüübi otsingutekstiks ning kasutab `contains`-meetodit. Mõlemad pooled teisendatakse väiketähtedeks `Locale.ROOT` abil, et tulemus ei sõltuks arvuti keeleseadest. Tühi otsing sobib kõikidele.

Tingimus `!ainultVabad || onVaba(...)` tähendab: kui vabade filtrit pole nõutud, sobib iga olek; kui filter on nõutud, peab sõiduk vaba olema. `||` katkestab hindamise, kui vasak pool on juba tõene.

`Comparator.comparing(Soiduk::getPaevahind).thenComparing(Soiduk::getRegistrinumber)` järjestab hinnad kasvavalt ja võrdse hinna puhul registrinumbrid tähestikuliselt. `Soiduk::getPaevahind` on meetodiviide, mis näitab, millist väärtust võrrelda.

## 6. Oluliste koodilõikude selgitused

### Mis juhtub rentimisel?

1. `Main` loeb numbri, nime ja päevade arvu ning kutsub `rendi(...)`.
2. Teenus leiab sõiduki või annab arusaadava erindi.
3. Teenus kontrollib, et aktiivset renti poleks.
4. `Rent`-konstruktor kontrollib andmeid ja arvutab tegeliku sõidukitüübi hinnareegliga koguhinna.
5. Alles eduka loomise järel lisatakse rent ajalukku ja suurendatakse ID loendurit.

Vigane nimi või päevade arv ei jäta poolikut tehingut ega kuluta ID-d. Koguhind fikseeritakse rendi loomisel. Saadavus tuletatakse aktiivsetest rentidest: sõidukis pole eraldi saadavuslippu, mis võiks rendiajalooga vastuollu minna.

### Mis juhtub tagastamisel?

Teenus leiab sõiduki ja sellele vastava aktiivse rendi ning kutsub `lopeta()`. Rent muutub mitteaktiivseks; seda ei eemaldata ajaloost. `onVaba()` annab nüüd `true`. Korduv tagastus tekitab veateate, sest aktiivset renti enam pole.

### Miks `==` sõidukite võrdlemisel siin töötab?

`onVaba` ja `tagasta` võrdlevad rendi sõidukit teenuse kaardist leitud sõidukiga. Rent saab täpselt sama objektiviite ning kaardis pole lubatud sõidukit sama numbri all asendada. Seetõttu on objektide identsuse võrdlus siinkohal sobiv. Kui laadiksime andmebaasist uusi objektikoopiaid, tuleks võrrelda püsivat tunnust, näiteks registrinumbrit, või määratleda `equals` koos `hashCode`-iga.

### Kuidas vigane sisend käitub?

`loePositiivneInt` loeb kogu rea ja kasutab `Integer.parseInt`. Tekst, murdarv, tühi rida ja `int`-piiridest suurem arv annavad veateate; null ja negatiivne arv lükatakse samuti tagasi. Päevade küsimust korratakse. Ainult `nextLine()` kasutamine väldib `nextInt()` järel jääva reavahetuse probleemi.

Teenus kasutab `IllegalArgumentException`-it vigaste argumentide ning `IllegalStateException`-it sobimatu oleku jaoks. Menüü püüab need kinni ja jätkab tööd. Sisendi lõpp kasutab eraldi sisemist `SisendLopp` erindit, mis suunab programmi puhtalt väljuma ka poole küsimustiku pealt. Kõiki võimalikke programmeerimisvigu ei peideta üldise `catch (Exception)` sisse.

## 7. Mis juhtub väärtuse või tingimuse muutmisel?

| Muudatus | Tagajärg |
| --- | --- |
| Auto soodustuse piir `paevad >= 7` → `paevad > 7` | Seitse päeva enam soodustust ei anna; Toyota hind on 245 €. Olemasolev test avastab muutuse. |
| Soodustustegur `0.90` → `0.80` | Soodustus kasvab 20%-ni; Toyota 7 päeva hind on 196 €. |
| Kaubiku tasu 15 € → 20 € | Iga uus kaubikurent kallineb 5 €, mitte 5 € iga päeva kohta. |
| Eemaldada `onVaba` kontroll rentimisest | Sama sõidukit võiks korraga mitu korda rentida. |
| `&&` asendada otsingus `||`-ga | Tagastatakse ka objekte, mis ei vasta tekstile või vabade filtrile. |
| Eemaldada positiivsete päevade kontroll ainult menüüst | Teenuse hinnaloogika kaitseb endiselt otsese vigase meetodikutse eest. |
| Tagastada sisemine `rendid` otse | Kutsuja saaks ajaloo kustutada ja saadavuse arvestuse rikkuda. |
| Muuta `final` väli mitte-`final`-iks | Välja oleks tehniliselt võimalik klassi sees hiljem muuta; avalikku muutmisõigust sellest veel ei teki. |

## 8. Teadmiste kontrolli küsimused ja vastused

### Mis juhtub, kui ülemklassi tüüpi viite kaudu kutsuda alamklassis üle kirjutatud meetod?

Käivitub tegeliku objekti alamklassi teostus. `Soiduk s = new Auto(...); s.arvutaHind(7)` kasutab `Auto.arvutaHind` meetodit. See on dünaamiline meetodivalik. Reegel puudutab üle kirjutatud isendimeetodeid; staatiliste meetodite valimine ja väljadele ligipääs ei käitu samal viisil.

### Mille poolest erinevad ArrayList ja HashMap?

`ArrayList` on järjestatud elementide loend: elemente saab indeksiga, näiteks `get(0)`, ja korduvad elemendid on lubatud. Väärtuse otsimine on tavaliselt O(n). `HashMap` seob võtmed väärtustega: `get("123ABC")` leiab sõiduki keskmiselt O(1). Võtmed on unikaalsed, väärtused võivad korduda, kindlat iteratsioonijärjekorda pole. Mõlemas saab kasutada viitetüüpe; primitiivi asemel kasutatakse vajadusel ümbrisklassi.

### Miks ei tohiks kõik väljad olla public?

Muu kood saaks kontrollidest mööda minna, näiteks muuta aktiivse rendi suvaliselt lõpetatuks või määrata negatiivse hinna. Privaatne väli ja valideeriv konstruktor või meetod hoiavad objekti lubatud olekus. Getter kõigile väljadele ja suvaline setter kõigile väljadele ei taga üksi sisulist kapseldamist.

### Millal interface ja millal abstraktne klass?

Liides sobib ühise lepingu jaoks ka omavahel mitteseotud klassidele, näiteks `Renditav` võiks ühendada sõidukeid ja tööriistu. Klass saab rakendada mitut liidest. Abstraktne klass sobib siis, kui seotud klassidel on ühine olek, konstruktor ja teostus. Java klassil saab olla üks otsene ülemklass. Siin on `Soiduk` sobiv, sest kõigil sõidukitel on ühised väljad ja valideerimine. Liidesel võivad olla ka `default`-meetodid, kuid tal pole objekti isendivälju nagu sellel ülemklassil.

### Mis vahe on int, Integer ja String tüüpidel?

`int` on primitiivne täisarv (−2147483648 kuni 2147483647). `Integer` on selle viitetüüpi ümbrisklass, sobib näiteks `List<Integer>` jaoks ja võib olla `null`. Java võib neid automaatselt pakkida ning lahti pakkida; `null` lahtipakkimine tekitab `NullPointerException`-i. `String` on muutumatu tekst: `"12" + "3"` annab `"123"`, aga `12 + 3` annab `15`. Teksti arvuks teisendamiseks kasutatakse näiteks `Integer.parseInt("12")`.

### Mille poolest erinevad == ja .equals()?

Primitiivide `==` võrdleb väärtusi. Viidete `==` kontrollib, kas viidatakse samale objektile. `.equals()` käitumise määrab klass; `String` võrdleb teksti sisu. `Object`-i vaiketeostus võrdleb identsust, kuni klass pole seda üle kirjutanud.

```java
String a = new String("rent");
String b = new String("rent");
System.out.println(a == b);      // false: kaks eri objekti
System.out.println(a.equals(b)); // true: sama tekst
```

String-literaalid võivad olla ühiskasutuses, nii et mõni `==` võrdlus võib juhuslikult oodatud tulemuse anda. Teksti sisu tuleb siiski võrrelda `equals`-iga. `Integer`-väärtuste võrdlemisel ära toetu objektide `==` tulemusele, sest osa väärtusi võidakse puhverdada. `BigDecimal.equals` arvestab ka skaalat: `1.0` ja `1.00` pole selle järgi võrdsed; arvulise võrdsuse jaoks sobib `compareTo(...) == 0`.

### Mida teeb commit ja kuidas see erineb push-ist?

`git add` valib muudatused järgmisse commit'i. `git commit` salvestab valitud muudatuste hetkeseisu kohalikku ajalukku koos sõnumiga. `git push` saadab kohalikud commit'id kaugserverisse, näiteks GitHubi. Commit ei vaja internetti; push vajab kaug-hoidlat ja ligipääsuõigust. `git status` näitab pooleliolevaid muudatusi, `git log --oneline` ajalugu, `git diff` veel lisamata muudatuste sisu.

## 9. Kaitsmise muudatuste harjutused

Allolev kood on harjutusmaterjal. Need täiendused pole praeguses rakenduses kasutusel. Tee üks muudatus korraga, kompileeri ja kontrolli tulemust. Ära piirdu koodi kopeerimisega: seleta, kus ja miks iga uus rida töötab.

### A. Lisa uue käitumisega alamklass

Loo `src/autorent/Mootorratas.java`. Kõik näite failid kasutavad paketti `autorent`.

```java
package autorent;

import java.math.BigDecimal;

public class Mootorratas extends Soiduk {
    public Mootorratas(String number, String mark, String mudel, BigDecimal hind) {
        super(number, mark, mudel, hind);
    }

    @Override
    public String getTyyp() { return "Mootorratas"; }

    @Override
    public String getLisainfo() { return "Kiiver hinna sees"; }

    @Override
    public BigDecimal arvutaHind(int paevad) {
        return super.arvutaHind(paevad).add(new BigDecimal("5.00"));
    }
}
```

Lisa `Main.looNaidisandmed()` meetodisse enne `return teenus;` rida:

```java
teenus.lisaSoiduk(new Mootorratas("111MOTO", "Honda", "CB500", new BigDecimal("30.00")));
```

Kompileeri uuesti. Hinnapäring kahe päeva kohta peab andma 65 €. Olemasolevaid rendimeetodeid pole vaja muuta, sest need töötavad `Soiduk`-viidetega. Näita õpetajale pärilust, abstraktsete meetodite teostamist ja uut hinnakäitumist.

### B. Paranda kapseldamist

Kui saad klassi, kus on `public int istekohti`, muuda see privaatseks ja lisa valideeriv konstruktor ning getter. Olemasolev `Auto` näitab sellist lahendust. Kui õpetaja nõuab istekohtade muutmist pärast loomist, eemalda just selle välja `final` ja lisa:

```java
public void muudaIstekohti(int uusArv) {
    if (uusArv < 1 || uusArv > 9) {
        throw new IllegalArgumentException("Istekohti peab olema 1–9.");
    }
    this.istekohti = uusArv;
}
```

Väli jääb privaatseks. Kontrolli, et 7 lubatakse ning 0 ja 10 lükatakse tagasi ilma varasemat väärtust muutmata. Praeguses rakenduses seda muutmisvajadust pole, seetõttu on väli `final`.

### C. Leia kindlate tingimustega objektid

Lisa `Renditeenus` klassi hinnapiiriga vabade sõidukite otsing:

```java
public List<Soiduk> leiaVabadKuniHinnani(java.math.BigDecimal piir) {
    if (piir == null || piir.signum() < 0) {
        throw new IllegalArgumentException("Hinnapiir ei tohi olla negatiivne ega null-viide.");
    }
    List<Soiduk> tulemus = new ArrayList<>();
    for (Soiduk s : otsiSoidukeid("", true)) {
        if (s.getPaevahind().compareTo(piir) <= 0) tulemus.add(s);
    }
    return List.copyOf(tulemus);
}
```

Kontroll: algandmetega piir 40 annab Toyota ja Skoda, piir 34 ei anna midagi. Kui Toyota välja rentida, jääb piiriga 40 tulemusse vaid Skoda. Piir võrdleb päevahindu, mitte rendi koguhindu. Põhjenda, miks `compareTo` sobib `BigDecimal` võrdlemiseks.

### D. Asenda andmestruktuur sobivamaga

Kui nõutakse kaardi enda võtmete järjestust, muuda `new HashMap<>()` kujule `new TreeMap<>()` ja asenda import `java.util.TreeMap`-iga. Välja tüüp võib jääda `Map<String, Soiduk>`. Otsimine muutub keskmisest O(1)-st O(log n)-ks, kuid kaardi läbimine on registrinumbri järgi järjestatud.

Praegune `otsiSoidukeid()` sorteerib tulemuse lõpus uuesti hinna järgi, seega menüü järjekord üksnes kaardi vahetamisel ei muutu. Kui ülesanne nõuab ka menüüs numbrijärjekorda, muuda lõpus olev sortimine kujule `tulemus.sort(Comparator.comparing(Soiduk::getRegistrinumber));` ning kohanda menüü kirjeldust ja hinnajärjekorda kontrollivat testi. Selgita, miks teostuse vahetamine ei nõudnud `rendi()` ümberkirjutamist.

### E. Paranda vigase sisendi käsitlemist

Praegu on iga positiivne `int` lubatud päevade arv. Kui uus nõue piirab rendi 365 päevaga, lisa `Soiduk.arvutaHind()` alguses kontroll `paevad <= 0 || paevad > 365`. Muuda veateade vastavaks.

Soovi korral lisa eraldi päevade lugemise meetod, mis küsib vahemikust väljas oleva väärtuse uuesti; kasuta seda mõlemas menüü tegevuses (hinnapäring ja rentimine). Ärireegel peab jääma ka `Soiduk`-klassi, sest menüüst möödudes peab see samuti kehtima. Testi väärtusi 1, 365, 0 ja 366. Senine üldine positiivse täisarvu lugemise meetod üksi uut ülempiiri ei kontrolli.

## 10. Umbes viieminutiline tutvustus

1. Käivita rakendus ning näita hinnapäringut, rentimist ja tagastamist.
2. Ava `Soiduk`, `Auto` ja `Kaubik`: näita pärilust, abstraktseid meetodeid ja hinnareegleid.
3. Ava `Rent`-konstruktor: näita `Soiduk`-viite kaudu tehtavat polümorfset hinnakutset.
4. Ava `Renditeenus`: põhjenda kaarti ja listi ning selgita saadavuse kontrolli.
5. Näita vigast sisendit, testi käivitamist ja `git log --oneline` väljundit.

Harjuta seejärel ühte uue alamklassi või filtriga muudatust iseseisvalt umbes kümne minutiga. Pärast muudatust kontrolli nii oodatud tulemust kui vigast sisendit. Kui õpetaja muudab tingimust, jälgi selle mõju andmetele, ärireeglitele, menüüle, testidele ja dokumentatsioonile.

## 11. Nõuete ja esitamise kontroll

| Ülesande nõue | Kus täidetud? |
| --- | --- |
| Vähemalt 4 oma klassi | 6 põhiklassi paketis `autorent` |
| Pärilus ja abstraktsioon | `Soiduk`, `Auto`, `Kaubik` |
| Kapseldamine | Privaatsed väljad, konstruktorite kontrollid, kaitstud nimekirjad |
| Polümorfism | Üle kirjutatud hinnareegel `Soiduk`-viite kaudu |
| Primitiivid ja viitetüübid | `int`, `boolean`, `double`, `String`, `BigDecimal`, objektiviited |
| Vähemalt 2 kollektsiooni | `HashMap` ja `ArrayList` |
| Otsing, filter ja järjestamine | `otsiSoidukeid()` |
| Head tavad ja sisendikontroll | Vastutuste jaotus, kommentaarid, valideerimine ja menüü veakäsitlus |
| Käivitusjuhend | `README.md` |
| Töötamise kontroll | `RenditeenusTest` ja README näidiskasutus |
| Vähemalt 5 sisulist commit'i | Vaata kohalikku ajalugu käsuga `git log --oneline` |
| GitHubi link | Pärast GitHubi avaldamist esita päris repositooriumi aadress |

GitHubi avaldamine ja õpetajale lingi esitamine on eraldi samm; kohalik hoidla üksi seda nõuet ei täida. Juhised on README-s. Hinnang sõltub ka iseseisvast kaitsmisest: töötav projekt ja vastuste lugemine üksi ei tõenda õpiväljundite saavutamist.
