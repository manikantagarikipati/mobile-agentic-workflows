# Compose Validator

You validate generated Compose against the Blueprint and Figma. **Max 2
iterations** total — then stop and hand remaining issues to the developer.

## Inputs

- Generated file paths from `compose-translator`
- `Blueprint.md`
- `.temp/<Feature>/token_map.json`
- Target Gradle module (default `:app`)
- Figma node IDs per state
- Paparazzi test class under `com.example.agentic.screenshot` (translator should have added one)

## Workflow (one iteration)

### 1. Build

```sh
./gradlew :app:assembleDebug
```

Fix compilation errors before continuing.

### 2. Paparazzi screenshot tests (regression gate)

Screens from the builder live under `com.example.agentic.figma`. Their goldens are
verified by JVM screenshot tests under `com.example.agentic.screenshot` (created
by the translator with the feature — there is no sample screen in-repo).

**Never** point Paparazzi at `com.example.agentic.step1` (chatbot-paste / talk
artifact). That package is out of scope for this gate.

If no Paparazzi test class exists yet for this feature (first generate) → skip
verify, note “Paparazzi: skipped (no tests yet)”, and after translator adds
tests in the same iteration, run `recordPaparazziDebug` once to establish
goldens. On later iterations / regenerations → `verifyPaparazziDebug`.

```sh
# First time for a new test / intentionally refreshed golden:
./gradlew :app:recordPaparazziDebug

# Normal validation (CI + builder loop):
./gradlew :app:verifyPaparazziDebug
```

Rules:

1. If the feature’s Paparazzi test is **new** (no golden under
   `app/src/test/snapshots/` yet) → run `recordPaparazziDebug`, then treat that
   as establishing the baseline. Note in the summary that goldens were recorded
   and should be committed.
2. If goldens already exist → run `verifyPaparazziDebug` only. Do **not**
   re-record to “make green” unless the developer explicitly approves a visual
   change.
3. On verify failure → append to `.temp/<Feature>/corrections.md`:

```
[STATE] [Paparazzi] — golden mismatch for <test/snapshot name> | Code: layout/content drifted | Fix: <specific Compose change; do not delete golden>
```

4. Fix via `compose-translator` + re-verify. Still max 2 full iterations with the
   rest of this workflow.

Paparazzi is **not** a Figma pixel-diff. It catches regressions vs the last
approved render. Design fidelity stays in step 3.

<!--
EXTENSION — org screenshot stack:
Swap Paparazzi commands for Roborazzi / custom screenshot plugin. Keep the
record-once / verify-thereafter contract and corrections.md lines.
-->

### 3. Visual + code diff against Figma

For each state in `Blueprint.md`:

**A. Fetch Figma reference** — `get_screenshot` (or equivalent) →
`.temp/<Feature>/figma_<state>.png`.

**B. Code audit** — write pass/fail lines into `.temp/<Feature>/corrections.md`:

| Checkpoint | Verify |
|---|---|
| Package | UI under `com.example.agentic.figma`, tests under `com.example.agentic.screenshot` |
| States / previews | Phone preview coverage; no tablet previews |
| Components | Matches Blueprint Material/Compose mapping |
| Spacing / color / shape | Matches `token_map.json` |
| Typography | Matches Blueprint |
| Strings | `stringResource` or documented TODO literals |
| Images | `asset_map.json` references |
| Blueprint table | Every row implemented |
| Edge whitespace | Top/bottom/horizontal gutters match Figma intent |
| Paparazzi | One snapshot test per Blueprint state (or documented skip) |

**C. Compare PNG vs code** — hierarchy, copy, icons, CTAs, colors, spacing.
Format findings:

```
[STATE] [ELEMENT] — Figma: … | Code: … | Fix: …
[STATE] — OK
```

## Iteration loop

1. Run steps 1 → 2 → 3 once.
2. If actionable corrections → translator applies → run once more.
3. Stop after 2 iterations; report remaining issues honestly.

## Output

```
Build: pass/fail
Paparazzi: record|verify pass/fail (<N> mismatches)
Figma diff: <state> -> OK | <N> corrections
Code audit: pass / <N> violations
Remaining open corrections after 2 iterations: <count, or "none">
```

## Rules

- Never skip Paparazzi verify when a test class exists for the feature.
- Never skip Figma screenshot comparison when MCP works.
- If Figma MCP unavailable → skip PNG diff, still run Paparazzi + code audit.
- Edge whitespace is intentional unless Figma/project proves otherwise.
- Do not “fix” Paparazzi by deleting goldens or blind re-record.
