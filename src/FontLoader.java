import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

public class FontLoader {
    // List of font file names to load
    private static final String[] FONT_FILES = {
        "ARLRDBD.TTF",
        "BAUHS93.TTF",
        "BRLNSB.TTF",
        "BRLNSDB.TTF",
        "BRLNSR.TTF",
    };

    public static void loadFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            
            // Iterate over all font files and load them
            for (String fontFile : FONT_FILES) {
                InputStream fontStream = FontLoader.class.getClassLoader().getResourceAsStream(fontFile);
                if (fontStream == null) {
                    System.out.println("Font not found: " + fontFile);
                    continue;
                }
                // Create the font and register it
                Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(24f); // Adjust size as needed
                ge.registerFont(font);
                //System.out.println("Font successfully loaded and registered: " + fontFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
