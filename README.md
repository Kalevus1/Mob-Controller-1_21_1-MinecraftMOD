# 👁️ Mob Controller

> **Transfórmate en cualquier mob de Minecraft y vívelo por dentro.**
> Poséelo, ve por sus ojos en primera persona y muévete como él —caminar, volar, nadar,
> atacar—. Como cambiar de piel dentro del juego.

<p align="center">
  <a href="README.md"><img alt="Español" src="https://img.shields.io/badge/Español-b9791a?style=for-the-badge"></a>
  <a href="README.en.md"><img alt="English" src="https://img.shields.io/badge/English-6b7280?style=for-the-badge"></a>
  <a href="README.fi.md"><img alt="Suomi" src="https://img.shields.io/badge/Suomi-6b7280?style=for-the-badge"></a>
</p>

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-6b8f3a)
![Loaders](https://img.shields.io/badge/Loaders-Fabric%20·%20NeoForge%20·%20Forge-b9791a)
![Versión](https://img.shields.io/badge/versión-1.0.0-2f8f4e)
![Java](https://img.shields.io/badge/Java-21-informational)
![Licencia](https://img.shields.io/badge/licencia-CC0--1.0-lightgrey)

Mod por **Kalevi Latva-äijö**. Complemento de **Mob Director**: donde aquel *dirige* a los mobs
con comandos, **Mob Controller** te *convierte* en uno. Pensado para vivir la escena desde
dentro —cinemáticas, documentales, eventos— sin dejar al mob a su IA.

> 📁 **Este repositorio:** `downloads/` tiene los **jars listos para jugar** · `source/` tiene
> el **código** (solo si quieres compilarlo tú).

---

## ✨ Qué hace

- **Por sus ojos.** La cámara pasa a la cabeza del mob, en primera persona, con **giro
  instantáneo** del ratón (sin retardo de red).
- **Control total.** WASD lo mueve, el ratón lo orienta, salta y sube bloques con la física
  real del juego. Mientras posees, **la IA del mob se apaga**: mandas tú.
- **Vuelo, nado y ataque.** Los voladores vuelan y **cualquier mob nada** dentro del agua
  (espacio sube, agacharse baja). Click izquierdo golpea; el **oso se yergue** con un rugido.
- **Cuerpo visible + HUD.** Ves tu cuerpo y patas en primera persona, y un HUD con **vida,
  hambre y burbujas de aire** para no ahogarte sin darte cuenta.
- **Entrar y salir limpio.** Montas al mob de forma invisible (sin tirones); al salir apareces
  **junto al animal**.

---

## 🎮 Controles

| Tecla | Efecto |
|---|---|
| **WASD** | Mover / avanzar hacia donde miras |
| **Ratón** | Orientar la cabeza |
| **Espacio** | Saltar · **subir** en vuelo/nado |
| **Shift** | Ir despacio · **bajar** en vuelo/nado |
| **Click izq.** | El mob **golpea** lo que tiene delante (con sonido) |
| **R** (mantener) | Pose de preparado · el **oso se para en dos patas** con rugido |
| **V** | Salir de la posesión |

**Modos automáticos** según el mob: 🐾 terrestre · 🕊️ vuelo (voladores) · 🌊 nado (cualquier
mob dentro del agua).

### Comandos

```
/possess                      poseer el mob que estás mirando (raycast, 64 bloques)
/possess @e[tag=oso,limit=1]  poseer por selector de entidad
/unpossess                    salir de la posesión
```

> Los comandos y el click derecho para poseer requieren **OP nivel 2**.

---

## 📦 Instalación (jugadores)

1. Descarga el jar de **tu loader** desde la carpeta **[`downloads/`](downloads/)** o desde la
   pestaña **[Releases]** → `mobcontroller-<loader>-1.0.0.jar`.
2. Colócalo en `.minecraft/mods`. En **Fabric** añade también **[Fabric API]**.
3. Debe estar en el **cliente** y, en multijugador, también en el **servidor**
   (la cámara es del cliente).
4. Entra a un mundo con **trucos** (OP nivel 2), mira un mob y usa `/possess` — o agáchate y
   haz **click derecho** sobre él.

[Releases]: ../../releases
[Fabric API]: https://modrinth.com/mod/fabric-api

---

## 🧩 Loaders

| Loader | Versión | Estado |
|---|---|---|
| **Fabric** | loader 0.19.3 · Fabric API 0.116.13+1.21.1 | ✅ probado en juego |
| **NeoForge** | 21.1.248 | ◐ compila y empaqueta · pendiente de probar en juego |
| **Forge** | 1.21.1-52.1.16 | ◐ compila y empaqueta · pendiente de probar en juego |

---

## 🗺️ Historial de versiones (0.1.0 → 1.0.0)

Camino del mod en Fabric hasta la primera versión estable, por fases.

| Versión | Fase | Cambios |
|---|---|---|
| **0.1.0** | POV | Cámara a los ojos del mob (`setCameraEntity`); `/possess`, agacharse+click, salir con V/`/unpossess`; auto-salida al morir/desconectar. |
| **0.2.0** | Control | Control total WASD+ratón vía packet `DriveInput`; se apaga la IA. *(movimiento con fallo, corregido en 0.3.0)* |
| **0.3.0** | Control | Con NoAI el `travel()` no mueve → **movimiento manual** con `mob.move()`. Mixin de cliente que cancela el ataque (evita el crash de golpearte a ti mismo). |
| **0.4.0** | Acción | **Vuelo/nado** en 3D; **atacar** siendo el mob (click, raycast); **pose** con R (`setAggressive`). |
| **0.5.0** | Pulido | **Montar invisible** en vez de teletransportar (sin tirones; sales junto al animal). Mixin `wantsToStopRiding→false` (agacharse no desmonta). |
| **0.6.0–0.6.2** | Pulido | **Cuerpo visible** en 1ª persona (cabeza oculta en humanoides); pose y animación de ataque. Intentos de vista para cuadrúpedos (0.6.2 cámara elevada, revertida). |
| **0.7.0** | Pulido | **Cámara instantánea** (`setRotation` por fotograma), sin sensación de montar; cuadrúpedos con vista limpia. |
| **0.8.0** | Pulido | **Patas visibles** en cuadrúpedos/lobo (ocultar cabeza+torso vía accessors); el **oso se para** (`setStanding`) + sonidos. |
| **0.9.0–0.9.4** | Pulido | **Cámara en la cabeza** calibrada por la posición real del modelo (`head.z`); **nadar** para todos los mobs; cuerpo alineado al instante con la mirada; sonido al atacar. |
| **1.0.0** | Estable | **HUD de posesión** (vida, hambre, aire); primera versión estable. Después: **multiloader** (Fabric + NeoForge + Forge). |

---

## 🛠️ Compilar desde el código (desarrolladores)

El código está en **`source/`** — un monorepo **multiloader**: `common/` tiene la lógica y
`fabric/`, `neoforge/`, `forge/` solo el arranque de cada loader (eventos + red) sobre una capa
abstracta (`Services`/`NetworkPlatform`).

```bash
# Java 21:
export JAVA_HOME=".../.minecraft/runtime/java-runtime-delta/windows/java-runtime-delta"

cd source
./gradlew build              # los 3 loaders
./gradlew :fabric:build      # o solo uno (:neoforge:build / :forge:build)
```

Los jars salen en `source/<loader>/build/libs/mobcontroller-<loader>-1.0.0.jar`.

**Matriz de build (MC 1.21.1, JDK 21, Gradle 8.10.2):** Fabric Loom 1.7.4 · NeoForge
ModDevGradle 2.0.144 · Forge ForgeGradle 6.0.54.

### Arquitectura

```
mob-controller/                    · repositorio
├── downloads/                     · los 3 jars listos para descargar
├── README.md · README.en.md · README.fi.md · pagina-web.html
└── source/                        · proyecto Gradle (para compilar)
    ├── common/src/main/java/com/mobcontroller/  · LÓGICA COMPARTIDA (una sola source set)
    │     MobController · PossessionManager · PossessCommands · Services · NetworkPlatform
    │     net/(PossessSync · Unpossess · DriveInput · MobAttack · Pose)
    │     client/(ClientCore · ClientBridge · ClientPossession · PossessionHud)
    │     client/mixin/(Minecraft · Camera · LevelRenderer · LivingEntityRenderer + accessors)
    │     mixin/(PlayerMixin · MobAmbientSoundAccessor)
    ├── fabric/   → FabricNetwork + fabric.mod.json + mixins (con refmap)
    ├── neoforge/ → NeoForgeNetwork + neoforge.mods.toml + mixins
    └── forge/    → ForgeNetwork + mods.toml + mixins
```

Usa **mappings oficiales de Mojang**. Los mixins llevan copia por loader (Fabric usa refmap
con intermediary; NeoForge/Forge usan mappings oficiales en runtime, sin refmap).

---

## 📄 Licencia y créditos

Mod creado por **Kalevi Latva-äijö**. Licencia **CC0-1.0** (dominio público): úsalo, modifícalo
y compártelo libremente. Complemento de **Mob Director**.
