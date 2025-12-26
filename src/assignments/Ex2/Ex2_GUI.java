package assignments.Ex2;

/**
 * Intro2CS_2026A
 * GUI for Map2D visualization using StdDraw.
 * Interprets the original int[][] as a standard matrix: data[row][col].
 */
public class Ex2_GUI {

    public static final int WALL = 1;

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

        // Draw as matrix: data[row][col]
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {

                // Map stores values as map[x][y], and your data is data[row][col],
                // therefore x=row and y=col.
                int v = map.getPixel(row, col);

                if (v == WALL) StdDraw.setPenColor(StdDraw.BLACK);
                else           StdDraw.setPenColor(StdDraw.WHITE);

                int screenY = (h - 1) - row;   // row=0 at top (paper convention)
                StdDraw.filledSquare(col + 0.5, screenY + 0.5, 0.5);

                StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                StdDraw.square(col + 0.5, screenY + 0.5, 0.5);
            }
        }

        StdDraw.show();
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

        drawMap(map);

        int h = map.getHeight();

        // Draw path: map pixel (x=row, y=col) -> screen (col, row)
        if (path != null) {
            StdDraw.setPenColor(StdDraw.YELLOW);
            for (Pixel2D p : path) {
                int screenX = p.getY();
                int screenY = (h - 1) - p.getX();
                StdDraw.filledSquare(screenX + 0.5, screenY + 0.5, 0.5);
            }
        }

        // Draw start
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.filledSquare(start.getY() + 0.5, ((h - 1) - start.getX()) + 0.5, 0.5);

        // Draw end
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledSquare(end.getY() + 0.5, ((h - 1) - end.getX()) + 0.5, 0.5);

        StdDraw.show();

        while (true) {
            StdDraw.pause(20);
        }
    }
}
