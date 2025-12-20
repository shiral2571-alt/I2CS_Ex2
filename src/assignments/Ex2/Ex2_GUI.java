package assignments.Ex2;

/**
 * Intro2CS_2026A
 * This class represents a Graphical User Interface (GUI) for Map2D.
 * The class has save and load functions, and a GUI draw function.
 * It uses the StdDraw class for drawing.
 */
public class Ex2_GUI {

    // ערכים נוחים לצבעים (לא חובה אבל נחמד)
    public static final int EMPTY      = 0;
    public static final int WALL       = 1;
    public static final int COLOR_PATH = 4;
    public static final int COLOR_START = 2;
    public static final int COLOR_END   = 3;

    /** מציירת Map2D על המסך עם StdDraw */
    public static void drawMap(Map2D map) {
        if (map == null) {
            System.out.println("drawMap: map is null");
            return;
        }
        int w = map.getWidth();
        int h = map.getHeight();

        StdDraw.setCanvasSize(600, 600);
        StdDraw.setXscale(0, w);
        StdDraw.setYscale(0, h);
        StdDraw.clear(StdDraw.WHITE);
        StdDraw.enableDoubleBuffering();

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int v = map.getPixel(x, y);

                // בחירת צבע לפי הערך
                if (v == WALL) {
                    StdDraw.setPenColor(StdDraw.BLACK);
                } else {
                    StdDraw.setPenColor(StdDraw.WHITE);
                }

                // ציור ריבוע
                StdDraw.filledSquare(x + 0.5, y + 0.5, 0.5);

                // קווי רשת (לא חובה)
                StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                StdDraw.square(x + 0.5, y + 0.5, 0.5);
            }
        }
        // רק מציירת – בלי while(true) כאן
        StdDraw.show();
    }

    /** כרגע: במקום לקרוא מקובץ, מחזירים מפה לדוגמה */
    public static Map2D loadMap(String mapFileName) {
        // מפה קטנה 5x5 עם "מסדרון"
        int[][] data = {
                {0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0},
                {0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0}
        };
        return new Map(data);
    }

    public static void saveMap(Map2D map, String mapFileName) {
        // תממשי בהמשך לפי הדרישות (שמירה לקובץ)
    }

    public static void main(String[] a) {

        int[][] data = {
                {0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0},
                {0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0}
        };

        Map2D map = new Map(data);

        Pixel2D start = new Index2D(0, 0);
        Pixel2D end   = new Index2D(4, 4);

        Pixel2D[] path = map.shortestPath(start, end, WALL, false);

        // קודם מציירים את המפה
        drawMap(map);

        // צובעים מסלול, התחלה וסוף מעל המפה
        if (path != null) {
            // מסלול בצהוב
            StdDraw.setPenColor(StdDraw.YELLOW);
            for (Pixel2D p : path) {
                StdDraw.filledSquare(p.getX() + 0.5, p.getY() + 0.5, 0.5);
            }
        }

        // התחלה בירוק
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.filledSquare(start.getX() + 0.5, start.getY() + 0.5, 0.5);

        // סוף באדום
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledSquare(end.getX() + 0.5, end.getY() + 0.5, 0.5);

        StdDraw.show();

        // משאירים את החלון פתוח
        while (true) {
            StdDraw.pause(20);
        }
    }
}
