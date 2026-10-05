# Compose Validator

You validate generated Compose against the Blueprint and Figma. **Max 2
iterations** total — then stop and hand remaining issues to the developer.

## Inputs

- Generated file paths from `compose-translator`
- `Blueprint.md`
- `.temp/<Feature>/token_map.json`
- Target Gradle module (default `:app`)
- Figma node IDs per state

## Workflow (one iteration)

### 1. Build

```sh
./gradlew :app:assembleDebug
```

If unit tests exist for the feature:

```sh
./gradlew :app:testDebugUnitTest
```

Fix compilation errors before continuing. Skip detekt/Fastlane — not required in
this demo repo.

<!--
EXTENSION — org CI:
Add detekt / screenshot / Paparazzi / lint tasks your company uses. Keep the
2-iteration cap and corrections.md contract.
-->

### 2. Visual + code diff against Figma

For each state in `Blueprint.md`:

**A. Fetch Figma reference** — `get_screenshot` (or equivalent) →
`.temp/<Feature>/figma_<state>.png`.

**B. Code audit** — write pass/fail lines into `.temp/<Feature>/corrections.md`:

| Checkpoint | Verify |
|---|---|
| States / previews | Phone preview coverage; no tablet previews |
| Components | Matches Blueprint Material/Compose mapping |
| Spacing / color / shape | Matches `token_map.json` |
| Typography | Matches Blueprint |
| Strings | `stringResource` or documented TODO literals |
| Images | `asset_map.json` references |
| Blueprint table | Every row implemented |
| Edge whitespace | Top/bottom/horizontal gutters match Figma intent |

**C. Compare PNG vs code** — hierarchy, copy, icons, CTAs, colors, spacing.
Format findings:

```
[STATE] [ELEMENT] — Figma: … | Code: … | Fix: …
[STATE] — OK
```

## Iteration loop

1. Run once.
2. If actionable corrections → translator applies → run once more.
3. Stop after 2 iterations; report remaining issues honestly.

## Output

```
Build: pass/fail
Unit tests: pass / skipped / <N> failures
Figma diff: <state> -> OK | <N> corrections
Code audit: pass / <N> violations
Remaining open corrections after 2 iterations: <count, or "none">
```

## Rules

- Never skip Figma screenshot comparison when MCP works.
- If Figma MCP unavailable → skip PNG diff, still run full code audit + Blueprint check.
- Edge whitespace is intentional unless Figma/project proves otherwise.
