"""Split the captured console output into the groups used for the report screenshots."""
import pathlib
import re

ROOT = pathlib.Path(__file__).resolve().parents[1]
FULL = ROOT / "screenshots" / "full-output.txt"
SEP = "=" * 54

lines = FULL.read_text(encoding="utf-8").splitlines()

# collect the body of every numbered section: "1. Factory Method: ..." etc.
sections = {}
current = None
for line in lines:
    match = re.match(r"^(\d+)\.\s+(.*)$", line)
    if match and not line.startswith(" "):
        current = int(match.group(1))
        sections[current] = {"title": line, "body": []}
        continue
    if line.startswith(SEP) or line.strip() == "Done":
        continue
    if current is not None:
        sections[current]["body"].append(line)


def render(numbers):
    out = []
    for number in numbers:
        section = sections[number]
        body = list(section["body"])
        while body and not body[0].strip():
            body.pop(0)
        while body and not body[-1].strip():
            body.pop()
        if out:
            out.append("")
        out.append(SEP)
        out.append(section["title"])
        out.append(SEP)
        out.extend(body)
    return out


groups = {
    "shot1-creation-patterns.txt": [1, 2, 3],
    "shot2-serving-and-pricing.txt": [4, 5, 6],
    "shot3-facade-order.txt": [7],
    "shot4-facade-rollback.txt": [8],
}

for filename, numbers in groups.items():
    content = render(numbers)
    (ROOT / "screenshots" / filename).write_text("\n".join(content) + "\n", encoding="utf-8")
    print(f"{filename}: {len(content)} lines")
