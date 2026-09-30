---
inclusion: always
---

# Compose Performance Skills

This workspace has skydoves' `compose-performance-skills` library cloned locally at
`.compose-performance-skills-source/`. It is a curated set of Agent Skills (`SKILL.md` files)
for diagnosing and fixing Jetpack Compose performance issues. Treat each `SKILL.md` as
operational instructions: read it, follow its workflow, satisfy its verification checklist.

## How to use these skills

1. When the user mentions a Compose performance symptom, map it to a skill via the table below
   (or via `.compose-performance-skills-source/INDEX.md` for the full lookup).
2. Read the specific `SKILL.md` file before writing any code. Cite the skill path inline when
   proposing a fix so the user can audit the source.
3. Follow the skill's workflow step by step. Apply its mandatory rules (`MUST` / `MUST NOT`)
   and run its verification checklist before declaring a fix complete.
4. For broad symptoms ("the app feels sluggish", "kicking off a perf sprint"), enter through
   `.compose-performance-skills-source/audit/auditing-compose-performance/SKILL.md`
   — the orchestrator that sequences Measure → Diagnose → Fix → Verify across the focused skills.

## Local skill catalog

All paths are relative to `.compose-performance-skills-source/`.

### Stability — types that block skipping
- `stability/diagnosing-compose-stability/SKILL.md` — enable and read Compose Compiler reports
- `stability/stabilizing-compose-types/SKILL.md` — three-tier fix (rewrite, annotate, configure)
- `stability/understanding-stability-inference/SKILL.md` — 12-phase algorithm, `$stable` bitmasks
- `stability/enforcing-stability-in-ci/SKILL.md` — `stabilityDump` + `stabilityCheck` gate
- `stability/using-stability-analyzer-ide-plugin/SKILL.md` — IDE plugin for inline inspection
- `stability/visualizing-recomposition-cascades/SKILL.md` — call-graph + heatmap

### Recomposition — eliminate unnecessary work
- `recomposition/deferring-state-reads/SKILL.md` — push hot reads to Layout/Draw via lambda modifiers
- `recomposition/choosing-derivedstateof/SKILL.md` — input frequency must exceed output frequency
- `recomposition/using-strong-skipping-correctly/SKILL.md` — Kotlin 2.0.20+ auto-memoization rules
- `recomposition/avoiding-subcomposition-pitfalls/SKILL.md` — `SubcomposeLayout` / `BoxWithConstraints` / `Scaffold`
- `recomposition/debugging-recompositions/SKILL.md` — Layout Inspector counts + Argument Change Reasons

### Lists
- `lists/optimizing-lazy-layouts/SKILL.md` — stable `key`, `contentType`, `Modifier.animateItem()`
- `lists/configuring-lazy-prefetch/SKILL.md` — `LazyLayoutCacheWindow`, pausable prefetch (1.10+)

### Modifiers
- `modifiers/migrating-to-modifier-node/SKILL.md` — `Modifier.Node` over `composed { }`
- `modifiers/ordering-modifier-chains/SKILL.md` — wrap-the-next-modifier mental model

### Side effects
- `side-effects/collecting-flows-safely/SKILL.md` — `collectAsStateWithLifecycle`, don't pass `Flow<T>` as params
- `side-effects/using-efficient-effects/SKILL.md` — `LaunchedEffect` vs `RememberedEffect` vs `DisposableEffect`

### Measurement
- `measurement/testing-compose-in-release-mode/SKILL.md` — debug builds lie
- `measurement/generating-baseline-profiles/SKILL.md` — Macrobenchmark, Baseline Profile Generator
- `measurement/tracing-recompositions-at-runtime/SKILL.md` — `@TraceRecomposition` for release-grade tracing

### Build
- `build/configuring-r8-for-compose/SKILL.md` — full mode, optimize ProGuard file, consumer rules

### Audit (orchestrator)
- `audit/auditing-compose-performance/SKILL.md` — entry point for broad symptoms

### Hot reload
- `hot-reload/setting-up-compose-hotswan/SKILL.md`
- `hot-reload/understanding-hot-reload-limits/SKILL.md`
- `hot-reload/preserving-state-across-reloads/SKILL.md`
- `hot-reload/iterating-with-ai-and-mcp/SKILL.md`

## Symptom → skill quick lookup

| Symptom | Skill |
|---|---|
| `LazyColumn` jank, dropped frames during scroll | `lists/optimizing-lazy-layouts` |
| `LazyColumn` jank at high scroll velocity (after item-level fixes) | `lists/configuring-lazy-prefetch` |
| Entire screen recomposes on animation or scroll | `recomposition/deferring-state-reads` |
| Recomposition count high even when params look equal | `stability/diagnosing-compose-stability` then `stabilizing-compose-types` |
| `derivedStateOf` not firing, or firing on every input | `recomposition/choosing-derivedstateof` |
| `Modifier.alpha(state.value)` invalidates the subtree | `recomposition/deferring-state-reads` |
| Compiler report shows unstable third-party type | `stability/stabilizing-compose-types` |
| `BoxWithConstraints` / `Scaffold` regresses scroll or first frame | `recomposition/avoiding-subcomposition-pitfalls` |
| `Flow<T>` parameter on a composable | `side-effects/collecting-flows-safely` |
| `LaunchedEffect` keeps restarting | `side-effects/using-efficient-effects` |
| Modifier chain produces unexpected painting / measurement | `modifiers/ordering-modifier-chains` |
| Custom modifier triggers recomposition on every parent invalidation | `modifiers/migrating-to-modifier-node` |
| Slow cold startup | `measurement/generating-baseline-profiles`, `build/configuring-r8-for-compose` |
| Stability regressions slipping into main | `stability/enforcing-stability-in-ci` |
| Broad sluggishness, no specific entry point | `audit/auditing-compose-performance` |

The full symptom and API tables are in `.compose-performance-skills-source/INDEX.md`.

## Five non-negotiable hot takes (apply everywhere)

1. Skippability is a diagnostic, not a KPI. Do not chase 100%.
2. A stability config is a contract with the compiler, not a magic spell. Break the contract
   and recompositions are silently missed.
3. Inline composables (`Row`, `Column`, `Box`) are not restartable or skippable. Wrapping them
   changes recomposition scoping, not stability.
4. `Flow` parameters are unstable. Collect them in a `ViewModel` or with
   `collectAsStateWithLifecycle`. Do not pass them down.
5. Always measure in release + R8 + a real device. Debug builds lie (Live Literals,
   interpreted mode).

## Project context

- Kotlin **2.3.0** with `org.jetbrains.kotlin.plugin.compose` — Strong Skipping is on by default.
- Compose BOM **2026.01.00** — Compose Foundation ≥ 1.10; pausable prefetch is on by default.
- Kotlin DSL Gradle build. Convention plugins live in `build-logic/convention/`.
- The release variant has `isMinifyEnabled = true` and `isShrinkResources = true` in `app/build.gradle.kts`; R8 full mode is the AGP 8.0+ default.
- Compose Compiler reports are wired in `configureAndroidCompose` behind a Gradle property.
  Generate per-module reports with:
  `./gradlew :<module>:assembleRelease -PcomposeCompilerReports=true`
  Output lands at `<module>/build/compose_compiler/<module>_release-{classes.txt,composables.txt,composables.csv}` —
  the inputs to `stability/diagnosing-compose-stability/SKILL.md`.
