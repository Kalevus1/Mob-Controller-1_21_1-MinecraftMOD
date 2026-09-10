# 👁️ Mob Controller

> **Muuttu miksi tahansa Minecraft-mobiksi ja koe peli sen silmin.**
> Ota mob hallintaasi, näe maailma sen silmien kautta ensimmäisen persoonan näkymässä ja liiku
> kuten se — kävele, lennä, ui ja hyökkää. Kuin vaihtaisit itseäsi toiseen mobiin pelin sisällä.

<p align="center">
  <a href="README.md"><img alt="Español" src="https://img.shields.io/badge/Español-6b7280?style=for-the-badge"></a>
  <a href="README.en.md"><img alt="English" src="https://img.shields.io/badge/English-6b7280?style=for-the-badge"></a>
  <a href="README.fi.md"><img alt="Suomi-badge" src="https://img.shields.io/badge/Suomi-b9791a?style=for-the-badge"></a>
</p>

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-6b8f3a)
![Loaderit](https://img.shields.io/badge/Loaderit-Fabric%20·%20NeoForge%20·%20Forge-b9791a)
![Versio](https://img.shields.io/badge/versio-1.0.0-2f8f4e)
![Java](https://img.shields.io/badge/Java-21-informational)
![Lisenssi](https://img.shields.io/badge/lisenssi-CC0--1.0-lightgrey)

Modin on tehnyt **Kalevi Latva-äijö**. Se toimii **Mob Director** -modin kumppanina:
siinä missä Mob Director ohjaa mobeja komennoilla, **Mob Controller antaa sinun ottaa ne itse
hallintaasi**. Mod on suunniteltu erityisesti siihen, että voit kokea kohtauksen mobin näkökulmasta
— esimerkiksi elokuvakohtauksissa, dokumenteissa ja tapahtumissa — ilman että mobin tekoäly
ohjaa sitä puolestasi.

> 📁 **Tämä repositorio:** `downloads/` sisältää **valmiit pelattavat jar-tiedostot** · `source/`
> sisältää **lähdekoodin** (jos haluat kääntää modin itse).

---

## ✨ Ominaisuudet

* **Näe mobin silmin.** Kamera siirtyy mobin pään kohdalle ensimmäisen persoonan näkymään.
  Hiiren liike välittyy heti ilman verkkoviivettä.
* **Täysi hallinta.** WASD-liikkeillä ohjaat mobia, hiirellä suuntaat sen katsetta ja
  hyppäät tai nouset esteiden yli pelin normaalin fysiikan mukaisesti. Kun otat mobin hallintaasi,
  sen tekoäly poistetaan käytöstä: **sinä ohjaat sitä**.
* **Lento, uinti ja hyökkäykset.** Lentävät mobit voivat lentää, ja **kaikki mobit voivat uida**
  vedessä. Välilyönnillä nouset ja Shiftillä laskeudut. Vasen hiiripainike saa mobin hyökkäämään.
  **Karhu voi nousta takajaloilleen ja karjaista.**
* **Näkyvä keho + HUD.** Näet ensimmäisen persoonan näkymässä oman kehosi ja jalkasi.
  HUD näyttää mobin **terveyden, nälän ja happimäärän**, joten tiedät milloin ilma alkaa loppua.
* **Saumaton sisään- ja ulostulo.** Mobin hallintaan siirtyminen tapahtuu ilman näkyviä
  teleporttauksia tai nykimistä. Kun lopetat hallinnan, ilmestyt mobin viereen.

---

## 🎮 Ohjaus

| Näppäin                | Toiminto                                                       |
| ---------------------- | -------------------------------------------------------------- |
| **WASD**               | Liiku / etene katsesuuntaan                                    |
| **Hiiri**              | Suuntaa katsetta                                               |
| **Välilyönti**         | Hyppää · nouse lennossa/uinnissa                               |
| **Shift**              | Liiku hitaasti · laskeudu lennossa/uinnissa                    |
| **Vasen hiiripainike** | Hyökkää edessä olevaan kohteeseen                              |
| **R** (pidä pohjassa)  | Valmistele hyökkäys · karhu nousee takajaloilleen ja karjaisee |
| **V**                  | Lopeta mobin hallinta                                          |

**Automaattiset liikkumistilat** mobin mukaan: 🐾 maa · 🕊️ lento (lentävät mobit) · 🌊 uinti
(kaikki mobit vedessä).

### Komennot

```text
/possess                       ota katsomasi mob hallintaasi (raycast, 64 lohkoa)
/possess @e[tag=karhu,limit=1] ota mob hallintaan entiteettivalitsimella
/unpossess                     lopeta mobin hallinta
```

> Komennot ja mobin ottaminen hallintaan oikealla hiiripainikkeella vaativat **OP-tason 2**.

---

## 📦 Asennus (pelaajat)

1. Lataa **käyttämääsi loaderia vastaava jar-tiedosto** [`downloads/`](downloads/)-
   kansiosta tai **[Releases]**-välilehdeltä → `mobcontroller-<loader>-1.0.0.jar`.
2. Siirrä jar-tiedosto `.minecraft/mods`-kansioon. **Fabricissa tarvitset lisäksi
   [Fabric API]n**.
3. Modin on oltava asennettuna **clientille** ja moninpelissä myös **palvelimelle**.
   Kamera toimii client-puolella.
4. Mene maailmaan, jossa huijaukset ovat käytössä (OP-taso 2), katso haluamaasi mobia ja
   käytä `/possess`-komentoa — tai kyykisty ja napsauta mobia oikealla hiiripainikkeella.

[Releases]: ../../releases
[Fabric API]: https://modrinth.com/mod/fabric-api

---

## 🧩 Loaderit

| Loader       | Versio                                     | Tila                                        |
| ------------ | ------------------------------------------ | ------------------------------------------- |
| **Fabric**   | loader 0.19.3 · Fabric API 0.116.13+1.21.1 | ✅ testattu pelissä                          |
| **NeoForge** | 21.1.248                                   | ◐ kääntyy ja pakkautuu · pelitestaus kesken |
| **Forge**    | 1.21.1-52.1.16                             | ◐ kääntyy ja pakkautuu · pelitestaus kesken |

---

## 🗺️ Versiohistoria (0.1.0 → 1.0.0)

Modin kehitys Fabricilla ensimmäiseen vakaaseen versioon vaihe vaiheelta.

| Versio          | Vaihe       | Muutokset                                                                                                                                                                                                                     |
| --------------- | ----------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **0.1.0**       | POV         | Kamera siirtyy mobin silmiin (`setCameraEntity`); `/possess`, kyykistyminen + napsautus, poistuminen V-näppäimellä tai `/unpossess`-komennolla; automaattinen poistuminen mobin kuollessa tai yhteyden katketessa.            |
| **0.2.0**       | Hallinta    | Täysi WASD- ja hiiriohjaus `DriveInput`-paketilla; mobin tekoäly poistetaan käytöstä. *(Liikkeessä oli bugi, joka korjattiin versiossa 0.3.0.)*                                                                               |
| **0.3.0**       | Hallinta    | Kun NoAI on käytössä, `travel()` ei liikuta mobia → **liike toteutetaan manuaalisesti** `mob.move()`-kutsulla. Client-mixin estää pelaajan oman hyökkäyksen, jotta mobia ohjattaessa ei synny kaatumista itseensä osumisesta. |
| **0.4.0**       | Toiminta    | **Lento ja uinti** kolmiulotteisesti; **hyökkääminen** mobina (napsautus, raycast); **hyökkäysasento** R-näppäimellä (`setAggressive`).                                                                                       |
| **0.5.0**       | Viimeistely | **Näkymätön ratsastus** teleporttauksen sijaan (ei nykimistä; hallinnan lopettamisen jälkeen ilmestyt mobin viereen). Mixin `wantsToStopRiding→false` estää kyykistymistä lopettamasta hallintaa.                             |
| **0.6.0–0.6.2** | Viimeistely | **Näkyvä keho** ensimmäisen persoonan näkymässä (pää piilotetaan humanoidimobeilla); hyökkäysasento ja hyökkäysanimaatio. Kokeilu nelijalkaisten näkymän kanssa (0.6.2:ssa kameraa nostettiin, mutta muutos peruttiin).       |
| **0.7.0**       | Viimeistely | **Välitön kamera** (`setRotation` jokaisella ruudunpäivityksellä), jolloin näkymä ei tunnu ratsastamiselta; nelijalkaisilla selkeä ensimmäisen persoonan näkymä.                                                              |
| **0.8.0**       | Viimeistely | **Näkyvät jalat** nelijalkaisilla ja sudella (pää ja ylävartalo piilotetaan accessorien avulla); **karhu nousee takajaloilleen** (`setStanding`) ja käyttää siihen liittyviä ääniä.                                           |
| **0.9.0–0.9.4** | Viimeistely | **Kamera pään kohdalle** mallin todellisen pään sijainnin perusteella (`head.z`); **kaikki mobit voivat uida**; keho kohdistetaan välittömästi katseen suuntaan; hyökkäysääni lisätty.                                        |
| **1.0.0**       | Vakaa       | **Hallinta-HUD** (terveys, nälkä, happi); ensimmäinen vakaa versio. Tämän jälkeen lisättiin **multiloader-tuki** (Fabric + NeoForge + Forge).                                                                                 |

---

## 🛠️ Kääntäminen lähdekoodista (kehittäjät)

Lähdekoodi sijaitsee **`source/`**-kansiossa. Kyseessä on **multiloader-monorepo**:
`common/` sisältää varsinaisen pelilogiikan, kun taas `fabric/`, `neoforge/` ja `forge/`
sisältävät kunkin loaderin käynnistyksen, tapahtumat ja verkkoviestinnän abstraktiokerroksen
(`Services` / `NetworkPlatform) avulla.

```bash
# Java 21:
export JAVA_HOME=".../.minecraft/runtime/java-runtime-delta/windows/java-runtime-delta"

cd source
./gradlew build              # kaikki kolme loaderia
./gradlew :fabric:build      # tai vain yksi (:neoforge:build / :forge:build)
```

JAR-tiedostot ilmestyvät kansioihin:

`source/<loader>/build/libs/mobcontroller-<loader>-1.0.0.jar`

**Build-matriisi (MC 1.21.1, JDK 21, Gradle 8.10.2):** Fabric Loom 1.7.4 · NeoForge
ModDevGradle 2.0.144 · Forge ForgeGradle 6.0.54.

### Arkkitehtuuri

```text
mob-controller/                    · repositorio
├── downloads/                     · 3 valmista ladattavaa jaria
├── README.md · README.en.md · README.fi.md · pagina-web.html
└── source/                        · Gradle-projekti
    ├── common/src/main/java/com/mobcontroller/  · JAETTU LOGIIKKA (yksi source set)
    │     MobController · PossessionManager · PossessCommands · Services · NetworkPlatform
    │     net/(PossessSync · Unpossess · DriveInput · MobAttack · Pose)
    │     client/(ClientCore · ClientBridge · ClientPossession · PossessionHud)
    │     client/mixin/(Minecraft · Camera · LevelRenderer · LivingEntityRenderer + accessors)
    │     mixin/(PlayerMixin · MobAmbientSoundAccessor)
    ├── fabric/   → FabricNetwork + fabric.mod.json + mixinit (refmapilla)
    ├── neoforge/ → NeoForgeNetwork + neoforge.mods.toml + mixinit
    └── forge/    → ForgeNetwork + mods.toml + mixinit
```

Käyttää **virallisia Mojang-mappauksia**. Mixin-tiedostot ovat loaderkohtaisia kopioita:
Fabric käyttää refmapia intermediary-mappauksilla, kun taas NeoForge ja Forge käyttävät
ajon aikana virallisia mappauksia ilman refmapia.

---

## 📄 Lisenssi ja tekijät

Modin on luonut **Kalevi Latva-äijö**. Lisenssi on **CC0-1.0 (public domain)**:
saat käyttää, muokata ja jakaa modia vapaasti.

**Mob Controller** on **Mob Director** -modin kumppani.
