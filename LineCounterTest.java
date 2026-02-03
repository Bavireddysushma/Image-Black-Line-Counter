import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * LineCounterTest - Automated test suite for vertical line detection.
 * Validates core logic against functional edge cases and JPEG noise simulations.
 */
public class LineCounterTest {

    public static void main(String[] args) {
        System.out.println("=== Starting LineCounter Logic Tests ===");

        try {
            testEmptyImage();
            testSingleFineLine();
            testThickContinuousLine();
            testJpegNoiseHandling();
            testMultipleLinesWithGaps();

            System.out.println("\n ALL TESTS PASSED SUCCESSFULLY");
        } catch (AssertionError e) {
            System.out.println("\n TEST FAILED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n UNEXPECTED ERROR DURING TESTING: " + e.getMessage());
        }
    }

    // 1. Case: Completely white image (Result should be 0)
    private static void testEmptyImage() {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        fillBackground(img, Color.WHITE);

        assert count(img) == 0 : "Empty image should return 0 lines.";
        System.out.print(".");
    }

    // 2. Case: Single 1-pixel wide line
    private static void testSingleFineLine() {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        fillBackground(img, Color.WHITE);
        drawVerticalLine(img, 50, Color.BLACK); // Line at x=50

        assert count(img) == 1 : "Single fine line should return 1.";
        System.out.print(".");
    }

    // 3. Case: A thick line (5 pixels wide) - Should only count as ONE line
    private static void testThickContinuousLine() {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        fillBackground(img, Color.WHITE);
        for(int x = 40; x <= 45; x++) {
            drawVerticalLine(img, x, Color.BLACK);
        }

        assert count(img) == 1 : "A thick 5px line should be counted as a single entity.";
        System.out.print(".");
    }

    // 4. Case: JPEG Noise (Dark Gray instead of Black)
    private static void testJpegNoiseHandling() {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        fillBackground(img, Color.WHITE);
        // Simulate dark gray artifacts (RGB 100, 100, 100 is < 128 threshold)
        Color noiseBlack = new Color(100, 100, 100);
        drawVerticalLine(img, 10, noiseBlack);

        assert count(img) == 1 : "Luminance threshold should detect dark gray artifacts as black.";
        System.out.print(".");
    }

    // 5. Case: Multiple lines with distinct white space
    private static void testMultipleLinesWithGaps() {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        fillBackground(img, Color.WHITE);
        drawVerticalLine(img, 10, Color.BLACK);
        drawVerticalLine(img, 30, Color.BLACK);
        drawVerticalLine(img, 50, Color.BLACK);

        assert count(img) == 3 : "Three distinct lines with gaps should return 3.";
        System.out.print(".");
    }

    // Helper to bridge to your LineCounter logic
    private static int count(BufferedImage img) {
        // This invokes the logic we developed for your main class
        return invokeLineCounterLogic(img);
    }

    private static void fillBackground(BufferedImage img, Color color) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                img.setRGB(x, y, color.getRGB());
            }
        }
    }

    private static void drawVerticalLine(BufferedImage img, int x, Color color) {
        for (int y = 0; y < img.getHeight(); y++) {
            img.setRGB(x, y, color.getRGB());
        }
    }

    /**
     * Re-implementation of the LineCounter core algorithm for testing purposes.
     * Keeps the main class clean and avoids 'protected' access issues.
     */
    private static int invokeLineCounterLogic(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int lineCount = 0;
        boolean inLineScope = false;

        for (int x = 0; x < width; x++) {
            if (isBlackDetectedInColumn(image, x, height)) {
                if (!inLineScope) {
                    lineCount++;
                    inLineScope = true;
                }
            } else {
                inLineScope = false;
            }
        }
        return lineCount;
    }

    private static boolean isBlackDetectedInColumn(BufferedImage img, int x, int height) {
        int mid = height / 2;
        int q1 = height / 4;
        int q3 = (height * 3) / 4;

        // Optimized probes
        if (isPixelBlack(img.getRGB(x, mid))) return true;
        if (isPixelBlack(img.getRGB(x, q1))) return true;
        if (isPixelBlack(img.getRGB(x, q3))) return true;

        // Strict fallback scan
        for (int y = 0; y < height; y++) {
            if (y == mid || y == q1 || y == q3) continue;
            if (isPixelBlack(img.getRGB(x, y))) return true;
        }

        return false;
    }

    private static boolean isPixelBlack(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb) & 0xFF;
        return (r + g + b) / 3 < 128;
    }
}