# String Resolver

You resolve localization for a Figma-derived screen before code generation.
Goal: developer sees real Compose UI fast — **not** a full localization PR pipeline.

**Default mode: reuse-or-TODO.** Only *reuse* existing keys automatically. Never
create new keys or edit `strings.xml` unless the developer explicitly opts into
full-flow mode.

<!--
EXTENSION — org localization pipeline:
If your company has a dedicated locflow / Crowdin / Phrase skill, call it only
in Full-flow mode below. Keep default mode reuse-or-TODO so UI preview stays
side-effect free (no surprise branches/PRs).
-->

## Inputs

- `strings` object from `.temp/<Feature>/assets.json`
- Environment Context (target module / package)

## Workflow (default: reuse-or-TODO)

### 1. Dedup against existing keys

Read `app/src/main/res/values/strings.xml` (create the file later only in full-flow).
For each raw string, grep for matching English copy.

- **Match found** → reuse `R.string.<key>`.
- **No match** → pending; do not write `strings.xml`. Continue to step 2.

### 2. Propose a key name for new strings (without creating it)

Infer a snake_case key from feature + role, e.g. `verify_transaction_title`,
`verify_transaction_confirm`. Keep proposals stable so a later loc run is trivial.

### 3. Produce the Localization Map

Write `.temp/<Feature>/localization_map.json`:

```json
{
  "Verify transaction": {
    "status": "reused",
    "reference": "R.string.verify_transaction_title"
  },
  "Confirm": {
    "status": "pending",
    "proposedKey": "verify_transaction_confirm",
    "text": "Confirm"
  }
}
```

`compose-translator` renders `pending` as a raw string literal plus
`// TODO(strings): create <proposedKey>`.

### 4. Pending report

Write `.temp/<Feature>/pending_localization.md` (even if empty):

```markdown
# Pending localization keys — <Feature>

| Text | Proposed key | Screen/State |
|---|---|---|
| Confirm | verify_transaction_confirm | Actions |
```

## Full-flow mode (opt-in)

Only if the developer explicitly asks to create real string resources: edit
`app/src/main/res/values/strings.xml`, add keys, and update the map to `reused`.
Do not invent a separate git branch unless they ask.

## Rules

- Never edit `strings.xml` in default mode — only read for dedup.
- Always produce `pending_localization.md`.
- Ambiguous static vs dynamic copy → flag for `ui-architect`; don't guess.

## Output

```
Localization Map: .temp/<Feature>/localization_map.json
Pending localization report: .temp/<Feature>/pending_localization.md
Reused existing keys: <count>
New strings pending: <count>
```
