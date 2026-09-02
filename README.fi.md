# 👁️ Mob Controller

> **Muutu miksi tahansa Minecraft-mobiksi ja elä sen sisällä.**
> Ota se haltuun, näe sen silmien läpi ensimmäisessä persoonassa ja liiku kuten se —kävele,
> lennä, ui, hyökkää—. Kuin vaihtaisit nahkaa pelin sisällä.

<p align="center">
  <a href="README.md"><img alt="Español" src="https://img.shields.io/badge/Español-6b7280?style=for-the-badge"></a>
  <a href="README.en.md"><img alt="English" src="https://img.shields.io/badge/English-6b7280?style=for-the-badge"></a>
  <a href="README.fi.md"><img alt="Suomi" src="https://img.shields.io/badge/Suomi-b9791a?style=for-the-badge"></a>
</p>

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-6b8f3a)
![Loaderit](https://img.shields.io/badge/Loaderit-Fabric%20·%20NeoForge%20·%20Forge-b9791a)
![Versio](https://img.shields.io/badge/versio-1.0.0-2f8f4e)
![Java](https://img.shields.io/badge/Java-21-informational)
![Lisenssi](https://img.shields.io/badge/lisenssi-CC0--1.0-lightgrey)

Modin tekijä **Kalevi Latva-äijö**. **Mob Director** -modin kumppani: siinä missä se *ohjaa*
mobeja komennoilla, **Mob Controller** *muuttaa sinut* sellaiseksi. Tehty kokemaan hetki
sisältäpäin —elokuvakohtaukset, dokumentit, tapahtumat— sen sijaan että mob jätettäisiin
tekoälynsä varaan.

> 📁 **Tämä repo:** `downloads/` sisältää **valmiit jarit pelaamiseen** · `source/` sisältää
> **koodin** (vain jos haluat kääntää sen itse).

---

## ✨ Mitä se tekee

- **Sen silmien läpi.** Kamera siirtyy mobin päähän, ensimmäinen persoona, **välittömällä**
  hiiriohjauksella (ei verkkoviivettä).
- **Täysi hallinta.** WASD liikuttaa, hiiri suuntaa, se hyppää ja nousee lohkojen yli pelin
  oikealla fysiikalla. Kun otat mobin haltuun, sen **tekoäly kytkeytyy pois**: sinä määräät.
- **Lento, uinti ja hyökkäykset.** Lentävät lentävät ja **mikä tahansa mob ui** veden alla
  (välilyönti ylös, kyykky alas). Vasen napsautus hyökkää; **karhu nousee takajaloilleen**
  karjahtaen.
- **Näkyvä keho + HUD.** Näet kehosi ja jalkasi ensimmäisessä persoonassa, sekä HUDin, jossa
  näkyvät **elämät, nälkä ja ilmakuplat**, jottet huku huomaamatta.
- **Siisti sisään ja ulos.** Ratsastat mobilla näkymättömänä (ei nykimistä); poistuessasi
  ilmestyt **eläimen viereen**.

---

## 🎮 Ohjaus

| Näppäin | Vaikutus |
|---|---|
| **WASD** | Liiku / etene katseen suuntaan |
| **Hiiri** | Suuntaa pää |
| **Välilyönti** | Hyppää · **ylös** lennossa/uinnissa |
| **Shift** | Liiku hitaasti · **alas** lennossa/uinnissa |
| **Vasen napsautus** | Mob **hyökkää** edessä olevaan (äänellä) |
| **R** (pidä pohjassa) | Valmiusasento · **karhu nousee takajaloilleen** karjahtaen |
| **V** | Poistu haltuunotosta |

**Automaattiset tilat** mobin mukaan: 🐾 maa · 🕊️ lento (lentävät) · 🌊 uinti (mikä tahansa mob
veden alla).

### Komennot

```
/possess                       ota haltuun mob, jota katsot (raycast, 64 lohkoa)
/possess @e[tag=karhu,limit=1] ota haltuun entiteettivalitsimella
/unpossess                     poistu haltuunotosta
```

> Komennot ja haltuunotto oikealla napsautuksella vaativat **OP-tason 2**.

---

## 📦 Asennus (pelaajat)

1. Lataa **loaderisi** jar **[`downloads/`](downloads/)**-kansiosta tai **[Releases]**-
   välilehdeltä → `mobcontroller-<loader>-1.0.0.jar`.
2. Aseta se kansioon `.minecraft/mods`. **Fabricissa** lisää myös **[Fabric API]**.
3. Sen on oltava **asiakkaalla** (client) ja moninpelissä myös **palvelimella**
   (kamera on asiakaspuolella).
4. Mene maailmaan, jossa **huijaukset** ovat päällä (OP-taso 2), katso mobia ja käytä
   `/possess` — tai kyykisty ja **napsauta oikealla** sitä.

[Releases]: ../../releases
[Fabric API]: https://modrinth.com/mod/fabric-api

---

## 🧩 Loaderit

| Loader | Versio | Tila |
|---|---|---|
| **Fabric** | loader 0.19.3 · Fabric API 0.116.13+1.21.1 | ✅ testattu pelissä |
| **NeoForge** | 21.1.248 | ◐ kääntyy ja pakkautuu · pelitestaus kesken |
| **Forge** | 1.21.1-52.1.16 | ◐ kääntyy ja pakkautuu · pelitestaus kesken |

---

## 🗺️ Versiohistoria (0.1.0 → 1.0.0)

Modin matka Fabricilla ensimmäiseen vakaaseen versioon, vaiheittain.

| Versio | Vaihe | Muutokset |
|---|---|---|
| **0.1.0** | POV | Kamera mobin silmiin (`setCameraEntity`); `/possess`, kyykky+napsautus, poistu V/`/unpossess`; automaattinen poistuminen kuollessa/yhteyden katketessa. |
| **0.2.0** | Hallinta | Täysi WASD+hiiri-hallinta `DriveInput`-paketilla; tekoäly pois. *(liikkeessä bugi, korjattu 0.3.0:ssa)* |
| **0.3.0** | Hallinta | NoAI:lla `travel()` ei liikuta → **manuaalinen liike** `mob.move()`:lla. Client-mixin, joka peruu hyökkäyksen (estää kaatumisen, kun osuisit itseesi). |
| **0.4.0** | Toiminta | **Lento/uinti** 3D:ssä; **hyökkäys** mobina (napsautus, raycast); **asento** R:llä (`setAggressive`). |
| **0.5.0** | Viimeistely | **Näkymätön ratsastus** teleportin sijaan (ei nykimistä; poistut eläimen viereen). Mixin `wantsToStopRiding→false` (kyykky ei pudota selästä). |
| **0.6.0–0.6.2** | Viimeistely | **Näkyvä keho** ensimmäisessä persoonassa (pää piilotettu ihmismäisillä); asento ja hyökkäysanimaatio. Yrityksiä nelijalkaisten näkymään (0.6.2 nostettu kamera, peruttu). |
| **0.7.0** | Viimeistely | **Välitön kamera** (`setRotation` per ruutu), ei "ratsastuksen" tuntua; nelijalkaisilla siisti näkymä. |
| **0.8.0** | Viimeistely | **Näkyvät jalat** nelijalkaisilla/sudella (piilota pää+vartalo accessoreilla); **karhu nousee** (`setStanding`) + äänet. |
| **0.9.0–0.9.4** | Viimeistely | **Kamera päässä** kalibroituna mallin oikeasta pään sijainnista (`head.z`); **uinti** kaikille mobeille; keho kohdistuu heti katseeseen; hyökkäysääni. |
| **1.0.0** | Vakaa | **Haltuunotto-HUD** (elämät, nälkä, ilma); ensimmäinen vakaa versio. Jälkeenpäin: **multiloader** (Fabric + NeoForge + Forge). |

---

## 🛠️ Kääntäminen lähdekoodista (kehittäjät)

Koodi on kansiossa **`source/`** — **multiloader**-monorepo: `common/` sisältää logiikan ja
`fabric/`, `neoforge/`, `forge/` vain kunkin loaderin käynnistyksen (tapahtumat + verkko)
abstraktiokerroksen päällä (`Services`/`NetworkPlatform`).

```bash
# Java 21:
export JAVA_HOME=".../.minecraft/runtime/java-runtime-delta/windows/java-runtime-delta"

cd source
./gradlew build              # kaikki kolme loaderia
./gradlew :fabric:build      # tai vain yksi (:neoforge:build / :forge:build)
```

Jarit ilmestyvät kansioon `source/<loader>/build/libs/mobcontroller-<loader>-1.0.0.jar`.

**Build-matriisi (MC 1.21.1, JDK 21, Gradle 8.10.2):** Fabric Loom 1.7.4 · NeoForge
ModDevGradle 2.0.144 · Forge ForgeGradle 6.0.54.

### Arkkitehtuuri

```
mob-controller/                    · repositorio
├── downloads/                     · 3 valmista ladattavaa jaria
├── README.md · README.en.md · README.fi.md · pagina-web.html
└── source/                        · Gradle-projekti (kääntämiseen)
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

Käyttää **virallisia Mojang-mappauksia**. Mixinit tulevat loaderkohtaisena kopiona (Fabric
käyttää refmapia intermediaryllä; NeoForge/Forge käyttävät virallisia mappauksia ajossa, ilman
refmapia).

---

## 📄 Lisenssi ja tekijät

Modin loi **Kalevi Latva-äijö**. Lisenssi **CC0-1.0** (public domain): käytä, muokkaa ja jaa
vapaasti. **Mob Director** -modin kumppani.
