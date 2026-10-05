# Shared OpenCode configuration

OpenCode setup for this talk repo. Config: [`opencode.json`](opencode.json).
Skills: [`../skills/`](../skills/).

Agent-agnostic on purpose — Cursor/Claude/OpenCode all read `.ai/skills/`.
OpenCode also wires MCP via this JSON.

## Usage

From the **repository root**:

```sh
export OPENCODE_CONFIG="$PWD/.ai/opencode/opencode.json"
export FIGMA_API_KEY="<your-personal-figma-token>"
opencode
```

Credentials via env only. Never commit API keys.

Figma is optional — set `"enabled": false` locally if you have no token.

Restart OpenCode after changing config, MCP, or skills.
