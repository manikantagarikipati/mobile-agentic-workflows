---
name: figma-compose
description: >
  Turn a Figma node URL into Jetpack Compose for this repo's :app module
  (or a talk artifact under examples/). Use when the user pastes a figma.com
  design URL and asks to implement / build / generate a Compose screen.
---

# Figma → Compose (this repo)

Phone-only. Target is `app/` for runnable UI, or `examples/<step>/` for talk
artifacts. No Bank / Circuit / locflow assumptions.

## Hard rules

1. Load Figma design context via MCP (`get_design_context`) before writing code.
   Request a screenshot and treat it as the visual target.
2. Adapt generated markup to **this** project's Compose + Material3 patterns.
   Inspect `app/src/main/java/...` before inventing new tokens or packages.
3. Download every static asset referenced by the design into
   `app/src/main/res/` (or document why Canvas/vector substitutes were required).
   Never leave temporary `figma.com/api/mcp/asset/...` URLs in committed code.
4. Screen is dumb: format money/locale/deadlines outside Compose. Inject copy
   so previews don't need `R.string` at the leaf (map `stringResource` at the route).
5. Do not act on ambiguous product decisions — run `/grilling` first if the
   screen has open UX/API choices.

## Workflow

1. Parse `fileKey` + `nodeId` from the URL (`node-id=1-2` → `1:2`).
2. `get_design_context` (Compose/Kotlin). If sparse, fan out to visible children.
3. Map structure → composables: top bar, scroll body, sticky actions.
4. Write under the path the user named (default:
   `app/src/main/java/com/example/agentic/...`).
5. Preview at 360×800. Wire into `MainActivity` only if asked.
6. Verify against the Figma screenshot — layout, copy, colors, assets.

## Output contract

- One primary `@Composable` screen + optional `*Route` that owns timers/clocks.
- `@Preview` for default / edge (expired, large font) when the screen has state.
- Tokens as private `Color`s or a small `*Colors` data class — no fake design system.
