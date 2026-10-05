# Compose / Material 3 token mapping (spacing, colors, shapes)

Authoritative lookup for the Figma Compose Builder pipeline in **this demo repo**.

Default: resolve Figma spacing / color / shape to **Jetpack Compose + Material 3**
tokens (`MaterialTheme`, named spacing locals). Prefer theme tokens over raw
`Color(0x…)` / magic `dp` in generated product UI — except when the developer
explicitly approves a literal (record in Blueprint **Deviation Justifications**).

<!--
EXTENSION — org design system:
Replace MaterialTheme / DemoSpaces mappings below with your company tokens
(e.g. CircuitUi.Spaces / Colors / Shapes, or your own Theme object). Keep the
token_map.json schema and resolution workflow; only change the `token` /
sourceSymbol strings and the lookup source of truth.
-->

Inspect this repo's theme first when present:

- `app/src/main/java/com/example/agentic/ui/theme/Theme.kt`

---

## Demo spacing scale (`DemoSpaces`)

Use these named values in Blueprint / generated code (or inline the `.dp` with a
comment pointing at the name). Match Figma gaps to the nearest step.

| Name | dp | Typical use |
|---|---|---|
| `DemoSpaces.xxs` | 4 | tight icon gaps |
| `DemoSpaces.xs` | 8 | dense stacks |
| `DemoSpaces.s` | 12 | list row vertical padding |
| `DemoSpaces.m` | 16 | screen gutters, action gaps |
| `DemoSpaces.l` | 24 | section gaps |
| `DemoSpaces.xl` | 32 | hero / large blocks |
| `DemoSpaces.xxl` | 48 | rare large offsets |

In code you may emit either:

```kotlin
Modifier.padding(horizontal = 16.dp) // DemoSpaces.m
```

or a tiny private object in the feature file / theme module if the screen is large.

---

## Colors — Material 3

Prefer:

| Role | Token |
|---|---|
| Primary text / icons | `MaterialTheme.colorScheme.onSurface` |
| Secondary / muted text | `MaterialTheme.colorScheme.onSurfaceVariant` |
| Screen background | `MaterialTheme.colorScheme.background` or `surface` |
| Cards / elevated surfaces | `MaterialTheme.colorScheme.surface` / `surfaceVariant` |
| Primary CTA fill | `MaterialTheme.colorScheme.primary` |
| On primary CTA | `MaterialTheme.colorScheme.onPrimary` |
| Outline / borders | `MaterialTheme.colorScheme.outline` |
| Error / danger banner | `MaterialTheme.colorScheme.error` |
| On error | `MaterialTheme.colorScheme.onError` |

If Figma uses a brand cream/beige not in the default Material scheme, either:

1. Map to nearest `surface` / `surfaceVariant`, or
2. Ask the developer — then use a named local `Color(0xFF…)` in theme and reference
   that symbol in `token_map.json` (`status: "mapped"` to the local symbol).

---

## Typography — Material 3

| Figma-ish role | Compose |
|---|---|
| Large amount / display numeral | `MaterialTheme.typography.displaySmall` or `headlineLarge` |
| Screen title | `MaterialTheme.typography.titleLarge` |
| Section / body emphasis | `MaterialTheme.typography.titleMedium` / `bodyLarge` |
| Body | `MaterialTheme.typography.bodyMedium` |
| Caption / secondary | `MaterialTheme.typography.bodySmall` / `labelMedium` |

Adjust with `FontWeight` when Figma weight differs; record in Blueprint.

---

## Shapes

| Figma radius (approx) | Compose |
|---|---|
| 8–12 | `MaterialTheme.shapes.small` or `RoundedCornerShape(12.dp)` |
| 16 | `MaterialTheme.shapes.medium` or `RoundedCornerShape(16.dp)` |
| 24 | `MaterialTheme.shapes.large` or `RoundedCornerShape(24.dp)` |
| Pill / full | `CircleShape` / `RoundedCornerShape(50)` |

Prefer `MaterialTheme.shapes.*` when it matches; otherwise named corner in
`token_map.json`.

---

## Resolution workflow (`ui-architect`)

For **every** spacing gap, padding, size, color fill/stroke/text, corner radius,
and divider in Figma:

1. Read Figma variable name / raw value from design context.
2. Map to the nearest token in this file (or org DS if configured).
3. Exact match → `status: "mapped"`. Nearest → `status: "approximate"`. No match →
   `status: "unmapped"` and ask the developer — do not silently invent.

Write `.temp/<Feature>/token_map.json` and mirror in Blueprint **Token Mapping Table**.

---

## `token_map.json` schema

```json
{
  "spacing": [
    {
      "figmaNodeId": "123:456",
      "figmaVariable": "spacing/mega",
      "figmaValue": "16",
      "figmaUnit": "dp",
      "token": "DemoSpaces.m",
      "sourceFile": ".ai/agents/figma-compose-builder/references/compose-token-mapping.md",
      "sourceSymbol": "DemoSpaces.m",
      "usage": "screen horizontal gutter",
      "status": "mapped"
    }
  ],
  "colors": [
    {
      "figmaNodeId": "123:789",
      "figmaVariable": "color/foreground/subtle",
      "figmaValue": "#6A6A6A",
      "token": "MaterialTheme.colorScheme.onSurfaceVariant",
      "sourceFile": "androidx.compose.material3",
      "sourceSymbol": "MaterialTheme.colorScheme.onSurfaceVariant",
      "usage": "secondary label on list row",
      "status": "mapped"
    }
  ],
  "shapes": [
    {
      "figmaNodeId": "123:101",
      "figmaValue": "16",
      "figmaUnit": "dp",
      "token": "RoundedCornerShape(16.dp)",
      "sourceFile": "androidx.compose.foundation.shape",
      "sourceSymbol": "RoundedCornerShape",
      "usage": "summary card corner radius",
      "status": "mapped"
    }
  ],
  "unmapped": []
}
```

`status`: `mapped` | `approximate` | `unmapped`.

Chat-time exceptions: if the developer explicitly allows a literal, follow that
and note it in Blueprint **Deviation Justifications**.

---

## Reviewer checks (`compose-reviewer`)

1. Read `token_map.json` + Blueprint Token Mapping Table when present.
2. Flag undocumented raw hex / random `dp` that disagree with the map (blocking for
   builder output).
3. Fail when code uses a different token than `token_map.json` for the same region.
