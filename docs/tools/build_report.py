"""
Build the final report PDF.

1. assemble docs/report/report.html from part1..part4 + the source code appendix
2. print it to PDF with headless Chrome (pass 1)
3. read the page of every section from the PDF and fill the table of contents (pass 2)
4. stamp "Page N of M" with reportlab and copy the result to the workspace root

usage: python docs/tools/build_report.py
"""
import html
import pathlib
import re
import shutil
import subprocess
import sys

ROOT = pathlib.Path(__file__).resolve().parents[2]          # AITU-SDP-FinalExam
DOCS = ROOT / "docs"
REPORT = DOCS / "report"
SRC = ROOT / "src" / "main" / "java"
CHROME = pathlib.Path(r"C:\Program Files\Google\Chrome\Application\chrome.exe")
WORKSPACE = ROOT.parent
FINAL_NAME = "Final-Project-Report-LiYixin-SE-2526.pdf"

PACKAGES = [
    ("model", "Shared model &mdash; the entities every pattern works with"),
    ("factory", "Factory Method &mdash; creating single dishes"),
    ("builder", "Builder &mdash; assembling combo meals"),
    ("abstractfactory", "Abstract Factory &mdash; cuisine families"),
    ("bridge", "Bridge &mdash; food type and dining mode"),
    ("strategy", "Strategy &mdash; discount rules"),
    ("visitor", "Visitor &mdash; calories and receipt reports"),
    ("facade", "Facade &mdash; checkout with rollback"),
]

TOC_KEYS = {
    "1": "1. Introduction",
    "1.1": "1.1 Background and motivation",
    "1.2": "1.2 Objectives of the project",
    "1.3": "1.3 Scope and technologies",
    "1.4": "1.4 Structure of this report",
    "2": "2. Body",
    "2.1": "2.1 System architecture and package layout",
    "2.2": "2.2 Factory Method",
    "2.3": "2.3 Builder",
    "2.4": "2.4 Abstract Factory",
    "2.5": "2.5 Bridge",
    "2.6": "2.6 Facade",
    "2.7": "2.7 Strategy",
    "2.8": "2.8 Visitor",
    "2.9": "2.9 How the patterns collaborate",
    "2.10": "2.10 Screenshots of the running application",
    "2.11": "2.11 Verification of the results",
    "3": "3. Conclusion",
    "4": "4. Further work",
    "A": "Appendix A. Package dependencies",
    "B": "Appendix B. Full source code",
    "C": "Appendix C. Build, run and repository",
}


def code_appendix() -> str:
    out = []
    for package, description in PACKAGES:
        out.append(f'<div class="package-title">{package} &mdash; {description}</div>')
        for path in sorted((SRC / package).glob("*.java")):
            out.append(render_file(path, f"{package}/{path.name}"))
    out.append('<div class="package-title">client &mdash; the demo entry point</div>')
    out.append(render_file(SRC / "Main.java", "Main.java"))
    return "\n".join(out)


def render_file(path: pathlib.Path, label: str) -> str:
    text = path.read_text(encoding="utf-8").rstrip("\n")
    return (
        f'<div class="file"><div class="file-name">{html.escape(label)}</div>'
        f'<pre class="code">{html.escape(text)}</pre></div>'
    )


def assemble(page_numbers: dict) -> str:
    parts = [
        (REPORT / "part1.html").read_text(encoding="utf-8"),
        (REPORT / "part2.html").read_text(encoding="utf-8"),
        (REPORT / "part3.html").read_text(encoding="utf-8"),
        (REPORT / "part4.html").read_text(encoding="utf-8"),
    ]
    document = "\n".join(parts).replace("{{CODE}}", code_appendix())
    for key, number in page_numbers.items():
        document = document.replace("{{pg:" + key + "}}", str(number))
    document = re.sub(r"\{\{pg:[^}]+\}\}", "&ndash;", document)
    (REPORT / "report.html").write_text(document, encoding="utf-8")
    return document


def print_pdf(target: pathlib.Path) -> None:
    if target.exists():
        target.unlink()
    subprocess.run(
        [
            str(CHROME),
            "--headless",
            "--disable-gpu",
            "--no-pdf-header-footer",
            "--virtual-time-budget=30000",
            f"--print-to-pdf={target}",
            (REPORT / "report.html").as_uri(),
        ],
        check=True,
        stdout=subprocess.DEVNULL,
        stderr=subprocess.DEVNULL,
    )
    if not target.exists():
        sys.exit("Chrome did not produce the PDF")


def read_pages(pdf: pathlib.Path) -> list:
    from pypdf import PdfReader

    reader = PdfReader(str(pdf))
    return [re.sub(r"\s+", " ", page.extract_text() or "") for page in reader.pages]


def locate(pages: list) -> dict:
    result = {}
    for key, needle in TOC_KEYS.items():
        for index, text in enumerate(pages):
            if index < 2:            # skip cover and the table of contents itself
                continue
            if needle in text:
                result[key] = index + 1
                break
        else:
            result[key] = "?"
            print(f"  ! heading not found: {needle}")
    return result


def stamp(pdf: pathlib.Path, output: pathlib.Path) -> None:
    from pypdf import PdfReader, PdfWriter
    from reportlab.lib.colors import Color
    from reportlab.pdfgen import canvas

    writer = PdfWriter(clone_from=str(pdf))
    total = len(writer.pages)

    for index, page in enumerate(writer.pages):
        if index == 0:               # the cover page stays clean
            continue
        width = float(page.mediabox.width)
        height = float(page.mediabox.height)
        overlay_path = REPORT / f"_overlay_{index}.pdf"
        c = canvas.Canvas(str(overlay_path), pagesize=(width, height))
        grey = Color(0.42, 0.47, 0.53)
        c.setFont("Helvetica", 8)
        c.setFillColor(grey)
        c.drawString(51, 26, "LiYixin  \u00b7  SE-2526  \u00b7  Restaurant Ordering System")
        c.drawCentredString(width / 2, 26, f"Page {index + 1} of {total}")
        c.save()
        page.merge_page(PdfReader(str(overlay_path)).pages[0])
        overlay_path.unlink()

    with open(output, "wb") as handle:
        writer.write(handle)


def main() -> None:
    print("1/5 assembling HTML ...")
    assemble({})

    print("2/5 first print (to measure the pagination) ...")
    pass1 = REPORT / "report-pass1.pdf"
    print_pdf(pass1)

    print("3/5 locating sections ...")
    pages = read_pages(pass1)
    numbers = locate(pages)
    print("   ", numbers)

    print("4/5 second print with the table of contents filled ...")
    assemble(numbers)
    pass2 = REPORT / "report.pdf"
    print_pdf(pass2)

    print("5/5 stamping page numbers ...")
    final = REPORT / FINAL_NAME
    stamp(pass2, final)
    shutil.copy2(final, WORKSPACE / FINAL_NAME)

    from pypdf import PdfReader

    print(f"done: {WORKSPACE / FINAL_NAME} ({len(PdfReader(str(final)).pages)} pages)")


if __name__ == "__main__":
    main()
