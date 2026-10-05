---
name: figma-compose-builder
description: >-
  Multi-agent orchestrator that translates Figma designs into Jetpack Compose
  for this repo's :app module (or examples/). Coordinates screen/state
  consolidation, string and image resolution, component mapping, code
  generation, and build validation. Use when a developer provides Figma node
  links and asks for a Compose screen to be generated.
---

Follow the instructions in
[`.ai/agents/figma-compose-builder/agents/orchestrator.md`](../../agents/figma-compose-builder/agents/orchestrator.md).

For the on-demand, post-hoc reviewer (not part of the main pipeline — invoke
separately after code already exists), follow
[`.ai/agents/figma-compose-builder/agents/compose-reviewer.md`](../../agents/figma-compose-builder/agents/compose-reviewer.md)
instead.
