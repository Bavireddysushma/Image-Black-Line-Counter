import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * LineCounter - A Windows console application to count vertical black lines.
 * * This application uses a tiered probing algorithm to optimize for continuous
 * lines, ensuring high performance while maintaining absolute accuracy through
 * a comprehensive fallback scan.
 */
public class LineCounter {

    public static void main(String[] args) {
        // Validate argument count
        if (args.length != 1) {
            System.out.println("Invalid number of arguments.");
            System.out.println("Usage: java LineCounter <Absolute_Path_To_Image>");
            return;
        }

        try {
            File imageFile = new File(args[0]);

            // Initial validation
            if (!imageFile.exists()) {
                throw new FileNotFoundException("The file does not exist at the specified path.");
            }

            BufferedImage image = ImageIO.read(imageFile);

            if (image == null) {
                throw new IOException("The file format is not supported or the file is corrupted.");
            }

            int lineCount = countVerticalLines(image);
            System.out.println(lineCount);

        } catch (FileNotFoundException e) {
            System.out.println("File Error: " + e.getMessage());
        } catch (SecurityException e) {
            System.out.println("Access Error: The application lacks permission to read this file.");
        } catch (IOException e) {
            System.out.println("Input/Output Error: " + e.getMessage());
        } catch (Exception e) {
            // Catch-all for unknown exceptions to prevent application crash
            System.out.println("Unexpected Error: " + e.getMessage());
        }
    }

    /**
     * Logic: Horizontal column-by-column scan.
     * Uses a state-tracking flag to count distinct vertical lines regardless of thickness.
     */
    static int countVerticalLines(BufferedImage image) {
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

    /**
     * Tiered Probing Strategy:
     * 1. Probes midpoint/quarters for O(1) detection of continuous lines.
     * 2. Falls back to a full vertical scan for guaranteed accuracy.
     */
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

    /**
     * Uses a luminance threshold (brightness < 128) to classify a pixel as black.
     * This handles JPEG ringing and artifacts where black pixels drift to dark gray.
     */
    private static boolean isPixelBlack(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb) & 0xFF;

        return (r + g + b) / 3 < 128;
    }
}