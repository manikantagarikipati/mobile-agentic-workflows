#!/bin/bash
# Converts an SVG file into an Android VectorDrawable XML file, headlessly.
#
# Usage: svg_to_vector_drawable.sh <input.svg> <output.xml>
#
# Tries, in order:
#   1. Android Studio's bundled `vd-tool.jar` (same converter behind File > New > Vector Asset)
#   2. `svg2vectordrawable` via npx (https://www.npmjs.com/package/svg2vectordrawable) as a
#      fallback when vd-tool isn't found (recent Android Studio releases no longer always ship it
#      as a standalone jar)
#
# If neither is available, exits non-zero with instructions to convert manually via Android
# Studio's Vector Asset Studio (File > New > Vector Asset > Local file (SVG, PSD)) instead of
# hand-transcribing SVG path data.

set -euo pipefail

if [[ $# -ne 2 ]]; then
  echo "Usage: $0 <input.svg> <output.xml>" >&2
  exit 1
fi

INPUT_SVG="$1"
OUTPUT_XML="$2"

if [[ ! -f "$INPUT_SVG" ]]; then
  echo "Input SVG not found: $INPUT_SVG" >&2
  exit 1
fi

find_vd_tool() {
  local candidates=(
    "/Applications/Android Studio.app/Contents/plugins/android/resources/vd-tool.jar"
    "$HOME/Library/Application Support/Google/AndroidStudio"*/plugins/android/resources/vd-tool.jar
    "$HOME/android-studio/plugins/android/resources/vd-tool.jar"
    "${ANDROID_HOME:-}/tools/lib/vd-tool.jar"
  )
  for candidate in "${candidates[@]}"; do
    if [[ -f "$candidate" ]]; then
      echo "$candidate"
      return 0
    fi
  done
  return 1
}

if VD_TOOL_JAR=$(find_vd_tool); then
  echo "Using vd-tool: $VD_TOOL_JAR"
  java -jar "$VD_TOOL_JAR" -c -in "$INPUT_SVG" -out "$(dirname "$OUTPUT_XML")"
  # vd-tool names the output after the input file; rename to the requested output path.
  GENERATED="$(dirname "$OUTPUT_XML")/$(basename "${INPUT_SVG%.*}").xml"
  if [[ -f "$GENERATED" && "$GENERATED" != "$OUTPUT_XML" ]]; then
    mv "$GENERATED" "$OUTPUT_XML"
  fi
  echo "Wrote $OUTPUT_XML"
  exit 0
fi

echo "vd-tool.jar not found on this machine, falling back to svg2vectordrawable via npx." >&2
if npx --yes svg2vectordrawable "$INPUT_SVG" "$OUTPUT_XML"; then
  echo "Wrote $OUTPUT_XML"
  exit 0
fi

cat >&2 <<EOF
Could not convert '$INPUT_SVG' headlessly (no vd-tool.jar, npx svg2vectordrawable failed).
Convert manually instead: Android Studio > File > New > Vector Asset > Local file (SVG, PSD),
then place the result at: $OUTPUT_XML
Do not hand-transcribe SVG path data as a substitute for either tool.
EOF
exit 1
