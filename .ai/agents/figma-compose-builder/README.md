# Figma Compose Builder

Multi-agent pipeline that turns Figma node links into Jetpack Compose for this
talk demo's **`:app`** module (or `examples/` artifacts), using Material 3 /
plain Compose as the default design mapping.

Scope: Android phone only (no KMP/CMP). One target package/path per run.

> **Extension point — org design system.** Production teams usually swap the
> Material 3 mappings for an internal design lib (tokens, components, icon pack).
> Leave comments in the role prompts and
> [references/compose-token-mapping.md](references/compose-token-mapping.md)
> show where to plug that in. This demo intentionally has **no** design-lib
> checkout requirement.

## Agents

Each file is a standalone, host-agnostic role prompt. A host adapter decides
*how* one role hands off to the next; the `.md` files describe *what* each role does.

| File | Role |
|---|---|
| [agents/orchestrator.md](agents/orchestrator.md) | Coordinates the pipeline. Never edits/writes/builds itself. |
| [agents/ui-architect.md](agents/ui-architect.md) | Consolidates Figma links into screens/states, maps components, produces `Blueprint.md`. |
| [agents/string-resolver.md](agents/string-resolver.md) | Reuse-or-TODO against `app` `strings.xml`. |
| [agents/image-resolver.md](agents/image-resolver.md) | Icons/illustrations → `res/drawable` (+ conversion script). |
| [agents/compose-translator.md](agents/compose-translator.md) | Writes Screen/Content/UiState code from the approved Blueprint. |
| [agents/compose-validator.md](agents/compose-validator.md) | `./gradlew :app:assembleDebug` + Figma visual/code diff (max 2 iterations). |
| [agents/compose-reviewer.md](agents/compose-reviewer.md) | On-demand review. Not part of the main pipeline. |

## Scripts

- [scripts/svg_to_vector_drawable.sh](scripts/svg_to_vector_drawable.sh) — SVG → Android `VectorDrawable`.

## References

- [references/compose-token-mapping.md](references/compose-token-mapping.md) — Figma
  spacing/color/shape → Material 3 / Compose tokens + `token_map.json` schema.

## Portability contract

Every hand-off is a **file on disk** under `.temp/<Feature>/` in the repo root
(`Blueprint.md`, `assets.json`, `localization_map.json`, `asset_map.json`,
`token_map.json`) — never conversation memory alone. Works with real subagents
(Mode A) or sequential single-session execution (Mode B).

## Wiring

- **Cursor / Agent Skills hosts:** [`.ai/skills/figma-compose-builder/SKILL.md`](../../skills/figma-compose-builder/SKILL.md)
  points at `agents/orchestrator.md`.
- **OpenCode:** skills path already includes `../skills` via
  [`.ai/opencode/opencode.json`](../../opencode/opencode.json). Optional: add
  primary/subagent entries that `prompt: {file:...}` each role under this folder.

## Prerequisites

- Figma MCP reachable (`get_design_context`, `get_screenshot`, `get_metadata`,
  `get_variable_defs`, `get_code_connect_map` — names vary by MCP server).
- Runnable Android project (`./gradlew :app:assembleDebug`).
