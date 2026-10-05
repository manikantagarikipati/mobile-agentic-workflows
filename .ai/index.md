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
| [figma-compose-builder](skills/figma-compose-builder/) | Figma URL → multi-agent Compose pipeline |

## Agents

| Agent pack | Role |
| :--- | :--- |
| [figma-compose-builder](agents/figma-compose-builder/) | Orchestrator + specialists (architect, strings, images, translator, validator, reviewer) |

## OpenCode

```sh
export OPENCODE_CONFIG="$PWD/.ai/opencode/opencode.json"
export FIGMA_API_KEY="<token>"
opencode
```

See [opencode/README.md](opencode/README.md).
