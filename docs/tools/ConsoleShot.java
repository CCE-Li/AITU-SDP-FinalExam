import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Small helper used only to produce the screenshots for the report.
 * It redraws the captured console output as a terminal window,
 * so the pictures look exactly like the real program run.
 *
 * usage: java ConsoleShot <input.txt> <output.png> <window title>
 */
public class ConsoleShot {

    private static final int SCALE = 2;          // draw at 2x so the text stays sharp in the PDF
    private static final int FONT_SIZE = 15;
    private static final int LINE_HEIGHT = 22;
    private static final int PAD = 20;
    private static final int TITLE_BAR = 36;
    private static final int MAX_CHARS = 104;

    private static final Color BG = new Color(0x1E, 0x1E, 0x1E);
    private static final Color BAR = new Color(0x2D, 0x2D, 0x30);
    private static final Color TEXT = new Color(0xD4, 0xD4, 0xD4);
    private static final Color TITLE = new Color(0xCC, 0xCC, 0xCC);
    private static final Color ACCENT = new Color(0x4E, 0xC9, 0xB0);
    private static final Color BLUE = new Color(0x56, 0x9C, 0xD6);
    private static final Color YELLOW = new Color(0xDC, 0xDC, 0xAA);
    private static final Color GREEN = new Color(0xB5, 0xCE, 0xA8);
    private static final Color CYAN = new Color(0x9C, 0xDC, 0xFE);
    private static final Color GREY = new Color(0x85, 0x85, 0x85);

    public static void main(String[] args) throws Exception {
        String input = args[0];
        String output = args[1];
        String windowTitle = args[2];

        List<String> raw = Files.readAllLines(new File(input).toPath(), StandardCharsets.UTF_8);
        List<String> lines = new ArrayList<>();
        for (String line : raw) {
            lines.addAll(wrap(line));
        }
        while (!lines.isEmpty() && lines.get(lines.size() - 1).isBlank()) {
            lines.remove(lines.size() - 1);
        }

        Font font = pickMonoFont();
        BufferedImage probe = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D pg = probe.createGraphics();
        pg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        pg.scale(SCALE, SCALE);
        pg.setFont(font);
        java.awt.font.FontRenderContext frc = pg.getFontRenderContext();
        int maxWidth = 0;
        for (String line : lines) {
            maxWidth = Math.max(maxWidth,
                    (int) Math.ceil(font.getStringBounds(line, frc).getWidth()));
        }
        FontMetrics fm = pg.getFontMetrics();
        pg.dispose();

        int width = Math.max(maxWidth + PAD * 2 + 12, 760);
        int height = TITLE_BAR + PAD + lines.size() * LINE_HEIGHT + PAD;

        BufferedImage image = new BufferedImage(width * SCALE, height * SCALE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.scale(SCALE, SCALE);

        // window background + title bar
        g.setColor(BG);
        g.fillRect(0, 0, width, height);
        g.setColor(BAR);
        g.fillRect(0, 0, width, TITLE_BAR);

        int dotY = TITLE_BAR / 2 - 6;
        g.setColor(new Color(0xFF, 0x5F, 0x57));
        g.fillOval(16, dotY, 12, 12);
        g.setColor(new Color(0xFF, 0xBD, 0x2E));
        g.fillOval(36, dotY, 12, 12);
        g.setColor(new Color(0x28, 0xC8, 0x40));
        g.fillOval(56, dotY, 12, 12);

        Font titleFont = font.deriveFont(Font.BOLD, FONT_SIZE - 1);
        g.setFont(titleFont);
        g.setColor(TITLE);
        FontMetrics tfm = g.getFontMetrics();
        g.drawString(windowTitle, (width - tfm.stringWidth(windowTitle)) / 2, TITLE_BAR / 2 + 5);

        g.setFont(font);
        int y = TITLE_BAR + PAD + fm.getAscent();
        for (String line : lines) {
            g.setColor(colorOf(line));
            g.drawString(line, PAD, y);
            y += LINE_HEIGHT;
        }
        g.dispose();

        File out = new File(output);
        if (out.getParentFile() != null) {
            out.getParentFile().mkdirs();
        }
        ImageIO.write(image, "png", out);
        System.out.println("written " + output + " (" + width + "x" + height + ")");
    }

    private static Color colorOf(String line) {
        String s = line.trim();
        if (s.startsWith("=====")) {
            return ACCENT;
        }
        if (s.startsWith("---")) {
            return BLUE;
        }
        if (s.startsWith("[Payment]")) {
            return YELLOW;
        }
        if (s.startsWith("[Notify]")) {
            return GREEN;
        }
        if (s.startsWith("[Stock]")) {
            return CYAN;
        }
        if (s.startsWith("[Cashier]") || s.startsWith("[Kitchen]")) {
            return TEXT;
        }
        if (s.isEmpty()) {
            return GREY;
        }
        return TEXT;
    }

    private static List<String> wrap(String line) {
        List<String> out = new ArrayList<>();
        if (line.length() <= MAX_CHARS) {
            out.add(line);
            return out;
        }
        String rest = line;
        while (rest.length() > MAX_CHARS) {
            int cut = rest.lastIndexOf(' ', MAX_CHARS);
            if (cut < MAX_CHARS / 2) {
                cut = MAX_CHARS;
            }
            out.add(rest.substring(0, cut));
            rest = "  " + rest.substring(cut).trim();
        }
        out.add(rest);
        return out;
    }

    private static Font pickMonoFont() {
        List<String> available = Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        String name = available.contains("Consolas") ? "Consolas" : "Monospaced";
        return new Font(name, Font.PLAIN, FONT_SIZE);
    }
}
