# AI guidance index

Portable agent workflows for this talk repo. Not locked to Cursor.

| | **Agent skills** | **OpenCode** |
| :--- | :--- | :--- |
| **Location** | [`.ai/skills/`](skills/) | [`.ai/opencode/`](opencode/) |
| **Purpose** | Multi-step procedures | MCP + skill path wiring |

**Rule of thumb:** multi-step procedure across tools → **skill**. IDE-only
conventions stay out of here on purpose.

## Skills

| Skill | Trigger |
| :--- | :--- |
| [grilling](skills/grilling/) | Stress-test a plan — one question at a time |
| [grill-me](skills/grill-me/) | Slash entry that runs `/grilling` |
| [figma-compose](skills/figma-compose/) | Figma URL → Compose for `:app` / `examples/` |
| [tdd](skills/tdd/) | Red → green, seam-first tests |
| [diagnosing-bugs](skills/diagnosing-bugs/) | Hard bugs — build a feedback loop first |
| [domain-modeling](skills/domain-modeling/) | Glossary + ADRs as decisions crystallise |

## OpenCode

```sh
export OPENCODE_CONFIG="$PWD/.ai/opencode/opencode.json"
export FIGMA_API_KEY="<token>"
opencode
```

See [opencode/README.md](opencode/README.md).
