#!/usr/bin/env python3
"""
Static JsMacros script scan.

Usage:
  python3 validate-context.py PATH [PATH ...]

Output: JSON findings. Files are read only.
"""

from __future__ import annotations
import json, re, sys
from pathlib import Path

EXTS = {".js", ".mjs", ".cjs"}

RULES = [
    ("node-require", r"\brequire\s*\(", "Node require() pattern"),
    ("node-process", r"\bprocess\.", "Node process global pattern"),
    ("screenshot", r"\bPlayer\.takeScreenshot\s*\(", "Player.takeScreenshot call"),
    ("main-thread", r"\bClient\.runOnMainThread\s*\(", "Client.runOnMainThread call"),
    ("wait-tick", r"\bClient\.waitTick\s*\(", "Client.waitTick call"),
    ("raw-client", r"\bClient\.getMinecraft\s*\(", "Raw Minecraft client access"),
    ("java-type", r"\bJava\.type\s*\(", "Java.type interop"),
    ("interaction", r"\bPlayer\.getInteractionManager\s*\(", "InteractionManager access"),
]

def files_for(arg: str):
    p = Path(arg)
    if p.is_file():
        return [p] if p.suffix.lower() in EXTS else []
    if p.is_dir():
        return [q for q in p.rglob("*") if q.is_file() and q.suffix.lower() in EXTS]
    return []

def main():
    if len(sys.argv) < 2 or sys.argv[1] in {"-h", "--help"}:
        print(__doc__.strip())
        return 0 if len(sys.argv) >= 2 else 2

    files = []
    for arg in sys.argv[1:]:
        files.extend(files_for(arg))

    findings = []
    for p in sorted(set(files)):
        text = p.read_text(encoding="utf-8", errors="replace")
        for rule, pattern, label in RULES:
            for m in re.finditer(pattern, text):
                findings.append({
                    "rule": rule,
                    "label": label,
                    "file": str(p),
                    "line": text.count("\n", 0, m.start()) + 1
                })

    print(json.dumps({
        "target": "JsMacros Reloaded 2.0.3 / Minecraft 26.1.2",
        "files_scanned": len(set(files)),
        "findings": findings,
        "read_only": True
    }, indent=2))
    return 0

if __name__ == "__main__":
    raise SystemExit(main())
