package assignments.Ex2;
import java.io.Serializable;

import java.util.Queue;
import java.util.LinkedList;
/**
 * This class represents a 2D map (int[w][h]) as a "screen" or a raster matrix or maze over integers.
 * This is the main class needed to be implemented.
 *
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D, Serializable {
    private int[][] map;


    // edit this class below

    /**
     * Constructs a w*h 2D raster map with an init value v.
     *
     * @param w
     * @param h
     * @param v
     */
    public Map(int w, int h, int v) {
        init(w, h, v);
    }


    /**
     * Constructs a square map (size*size).
     *
     * @param size
     */
    public Map(int size) {
        this(size, size, 0);
    }

    /**
     * Constructs a map from a given 2D array.
     *
     * @param data
     */
    public Map(int[][] data) {
        init(data);
    }

    /**
     * Initializes this map to size w*h and fills all cells with value v.
     * @param w
     * @param h
     * @param v
     */

    @Override
    public void init(int w, int h, int v) {
        map = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                map[i][j] = v;
            }
        }
    }

    /**
     * Initializes this map from a given 2D int array.
     * The array is copied by value (deep copy).
     * @param arr
     */

    @Override
    public void init(int[][] arr) {
        // לפי הממשק: לזרוק RuntimeException אם null / ריק / ragged
        if (arr == null || arr.length == 0 || arr[0] == null || arr[0].length == 0) {
            throw new RuntimeException("Invalid array");
        }
        int w = arr.length;
        int h = arr[0].length;

        // בדיקת ragged (שורות באורכים שונים / null)
        for (int i = 0; i < w; i++) {
            if (arr[i] == null || arr[i].length != h) {
                throw new RuntimeException("Ragged array");
            }
        }

        map = new int[w][h];
        for (int i = 0; i < w; i++) {
            System.arraycopy(arr[i], 0, map[i], 0, h);
        }
    }
    /**
     * @return a deep copy of the internal 2D array.
     */

    @Override
    public int[][] getMap() {
        int w = getWidth();
        int h = getHeight();
        int[][] copy = new int[w][h];
        for (int x = 0; x < w; x++) {
            System.arraycopy(map[x], 0, copy[x], 0, h);
        }
        return copy;
    }
    /**
     * @return the width (x dimension) of this map
     */

    @Override
    public int getWidth() {
        return map.length;
    }
    /**
     * @return the height (y dimension) of this map
     */
    @Override
    public int getHeight() {
        return map[0].length;

    }
    /**
     * Returns the value at (x,y).
     *
     * @param x x index
     * @param y y index
     * @return integer value stored in map[x][y]
     */

    @Override
    public int getPixel(int x, int y) {
        return map[x][y];
    }

    @Override
    public int getPixel(Pixel2D p) {
        return getPixel(p.getX(), p.getY());
    }
    /**
     * Sets (x,y) to value v.
     *
     * @param x x index
     * @param y y index
     * @param v new value
     */


    @Override
    public void setPixel(int x, int y, int v) {
        map[x][y] = v;

    }

    @Override
    public void setPixel(Pixel2D p, int v) {
        setPixel(p.getX(), p.getY(), v);

    }
    /**
     * Checks if point p is within map boundaries.
     *
     * @param p Pixel2D position
     * @return true if 0 <= x < width and 0 <= y < height
     */

    @Override
    public boolean isInside(Pixel2D p) {
        if (p.getX() < 0 || p.getY() < 0 || p.getX() >= getWidth() || p.getY() >= getHeight()) {
            return false;
        }
        return true;
    }
    /**
     * Checks if this map and map p share the same dimensions.
     *
     * @param p another Map2D object
     * @return true if width and height match
     */

    @Override
    public boolean sameDimensions(Map2D p) {
        if (getWidth() != p.getWidth() || getHeight() != p.getHeight()) {
            return false;
        }

        return true;
    }
    /**
     * Adds another map to this map cell by cell.
     * If dimensions are different, operation is ignored.
     *
     * @param p another Map2D to add
     */


    @Override
    public void addMap2D(Map2D p) {

        if (!sameDimensions(p)) {
            return;
        }
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                int current = getPixel(x, y);
                int add = p.getPixel(x, y);
                setPixel(x, y, current + add);

            }
        }

    }
    /**
     * Multiplies each cell by scalar and casts to int.
     *
     * @param scalar multiplier
     */

    @Override
    public void mul(double scalar) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                int value = getPixel(x, y);
                value = (int) (value * scalar);
                setPixel(x, y, value);
            }
        }

    }
    /**
     * Rescales this map to a new size using nearest neighbor sampling.
     *
     * @param sx scale factor for width
     * @param sy scale factor for height
     */

    @Override
    public void rescale(double sx, double sy) {
        if (sx <= 0 || sy <= 0) {
            return;
        }

        int oldW = getWidth();
        int oldH = getHeight();

        int newW = (int) Math.max(1, Math.round(oldW * sx));
        int newH = (int) Math.max(1, Math.round(oldH * sy));

        int[][] newMap = new int[newW][newH];

        for (int x = 0; x < newW; x++) {
            for (int y = 0; y < newH; y++) {

                int srcX = (int) Math.floor(x / sx);
                int srcY = (int) Math.floor(y / sy);


                if (srcX < 0) srcX = 0;
                if (srcX >= oldW) srcX = oldW - 1;
                if (srcY < 0) srcY = 0;
                if (srcY >= oldH) srcY = oldH - 1;

                newMap[x][y] = map[srcX][srcY];
            }
        }


        map = newMap;
    }
    /**
     * Draws a filled circle with center p and radius rad by writing 'color'
     * to all pixels with distance^2 <= radius^2
     *
     * @param center pixel origin
     * @param rad radius in pixels
     * @param color new value
     */

    @Override
    public void drawCircle(Pixel2D center, double rad, int color) {
        int cx = center.getX();
        int cy = center.getY();

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {

                double dx = x - cx;
                double dy = y - cy;
                double dist2 = dx * dx + dy * dy;

                if (dist2 <= rad * rad) {
                    setPixel(x, y, color);
                }
            }
        }
    }
    /**
     * Draws a line using simple DDA interpolation between p1 and p2.
     *
     * @param p1 starting pixel
     * @param p2 ending pixel
     * @param color value to write
     */

    @Override
    public void drawLine(Pixel2D p1, Pixel2D p2, int color) {
        int x1 = p1.getX();
        int y1 = p1.getY();
        int x2 = p2.getX();
        int y2 = p2.getY();

        int dx = x2 - x1;
        int dy = y2 - y1;

        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        if (steps == 0) { // שתי הנקודות זהות
            if (isInside(p1)) {
                setPixel(p1, color);
            }
            return;
        }

        double xInc = dx / (double) steps;
        double yInc = dy / (double) steps;

        double x = x1;
        double y = y1;

        for (int i = 0; i <= steps; i++) {
            int xi = (int) Math.round(x);
            int yi = (int) Math.round(y);
            Pixel2D p = new Index2D(xi, yi);
            if (isInside(p)) {
                setPixel(p, color);
            }
            x += xInc;
            y += yInc;
        }
    }

    /**
     * Draws a filled rectangle defined by two opposite corners p1 and p2.
     * All pixels within the [p1,p2] range are set to color (clipped to map bounds).
     *
     * @param p1 first corner
     * @param p2 opposite corner
     * @param color value to write
     */


    @Override
    public void drawRect(Pixel2D p1, Pixel2D p2, int color) {
        // לפי הממשק: צריך לצבוע את כל הפיקסלים בתוך המלבן (Filled rectangle), לא רק מסגרת
        int x1 = p1.getX();
        int y1 = p1.getY();
        int x2 = p2.getX();
        int y2 = p2.getY();

        int left = Math.min(x1, x2);
        int right = Math.max(x1, x2);
        int top = Math.min(y1, y2);
        int bottom = Math.max(y1, y2);

        for (int x = left; x <= right; x++) {
            for (int y = top; y <= bottom; y++) {
                Pixel2D p = new Index2D(x, y);
                if (isInside(p)) {
                    setPixel(p, color);
                }
            }
        }
    }
    /**
     * Compares cell-by-cell equality between this map and another.
     * Dimension and content must match to return true.
     *
     * @param ob another object
     * @return true if same type and identical data
     */

    @Override
    public boolean equals(Object ob) {
        if (this == ob) return true;
        if (!(ob instanceof Map2D)) return false;

        Map2D other = (Map2D) ob;

        if (!sameDimensions(other)) return false;

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (getPixel(x, y) != other.getPixel(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    // -------------------- helpers for cyclic --------------------
    private int mod(int a, int m) {
        int r = a % m;
        return (r < 0) ? r + m : r;
    }

    private Pixel2D normalizeIfCyclic(Pixel2D p, boolean cyclic) {
        if (p == null) return null;
        if (!cyclic) return p;
        int w = getWidth(), h = getHeight();
        return new Index2D(mod(p.getX(), w), mod(p.getY(), h));
    }

    /**
     * Fills this map with the new color (new_v) starting from p.
     * https://en.wikipedia.org/wiki/Flood_fill
     * Flood fill algorithm using BFS.
     * Recolors the connected component of xy (old value) to new_v.
     * @param xy start pixel
     * @param new_v new color value
     * @param cyclic if true, edges wrap around
     * @return number of pixels changed
     */
    @Override
    public int fill(Pixel2D xy, int new_v, boolean cyclic) {
        if (xy == null) return 0;

        Pixel2D start = normalizeIfCyclic(xy, cyclic);
        if (!cyclic && !isInside(start)) return 0;

        int old_v = getPixel(start);
        if (old_v == new_v) {
            return 0;
        }

        int count = 0;
        Queue<Pixel2D> q = new LinkedList<>();
        q.add(start);

        int w = getWidth();
        int h = getHeight();

        int[][] dirs = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        // שיפור: נצבע בזמן הכנסה לתור כדי לא להכניס שוב ושוב אותו פיקסל
        setPixel(start, new_v);
        count++;

        while (!q.isEmpty()) {
            Pixel2D p = q.remove();
            int x = p.getX();
            int y = p.getY();

            for (int[] d : dirs) {
                int nx = x + d[0];
                int ny = y + d[1];

                if (cyclic) {
                    nx = mod(nx, w);
                    ny = mod(ny, h);
                }

                Pixel2D np = new Index2D(nx, ny);
                if (!cyclic && !isInside(np)) {
                    continue;
                }

                if (getPixel(np) == old_v) {
                    setPixel(np, new_v);
                    count++;
                    q.add(np);
                }
            }
        }

        return count;
    }
    /**
     * BFS like shortest the computation based on iterative raster implementation of BFS, see:
     * https://en.wikipedia.org/wiki/Breadth-first_search
     * Computes the shortest path between p1 and p2 avoiding cells
     * equal to obsColor using BFS backtracking.
     * @param p1 start pixel
     * @param p2 end pixel
     * @param obsColor obstacle color
     * @param cyclic if true, edges wrap around
     * @return array of pixels representing path (or null if unreachable)
     */

    @Override
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {
        if (p1 == null || p2 == null) return null;

        Pixel2D s = normalizeIfCyclic(p1, cyclic);
        Pixel2D t = normalizeIfCyclic(p2, cyclic);

        if (!cyclic && (!isInside(s) || !isInside(t))) return null;

        // במקרה שהן אותה נקודה
        if (s.getX() == t.getX() && s.getY() == t.getY()) {
            return new Pixel2D[]{s};
        }

        int w = getWidth();
        int h = getHeight();

        // מפה של מרחקים מנקודת ההתחלה p1
        Map2D dist = allDistance(s, obsColor, cyclic);

        int distToP2 = dist.getPixel(t);
        // אם אין דרך להגיע ל-p2
        if (distToP2 == -1) {
            return null;
        }

        // נבנה את המסלול מהסוף להתחלה
        java.util.List<Pixel2D> revPath = new java.util.ArrayList<>();
        Pixel2D cur = t;
        int d = distToP2;

        int[][] dirs = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        while (d > 0) {
            revPath.add(cur);
            int x = cur.getX();
            int y = cur.getY();

            boolean foundPrev = false;

            for (int[] dir : dirs) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if (cyclic) {
                    nx = mod(nx, w);
                    ny = mod(ny, h);
                }

                Pixel2D np = new Index2D(nx, ny);

                if (!cyclic && !isInside(np)) {
                    continue;
                }
                if (!isInside(np)) {
                    continue;
                }

                int nd = dist.getPixel(np);
                if (nd == d - 1) {
                    cur = np;
                    d = nd;
                    foundPrev = true;
                    break;
                }
            }

            if (!foundPrev) {
                // תאורטית לא אמור לקרות אם allDistance תקין
                break;
            }
        }

        // מוסיפים גם את נקודת ההתחלה (מרחק 0)
        revPath.add(s);

        // להפוך את הרשימה כרגע יש לנו [p2, ..., p1]
        java.util.Collections.reverse(revPath);

        //מערך חדש
        Pixel2D[] ans = new Pixel2D[revPath.size()];
        for (int i = 0; i < revPath.size(); i++) {
            ans[i] = revPath.get(i);
        }

        return ans;
    }
    /**
     * Computes the BFS distance map from start pixel to all reachable cells,
     * storing distances in a new Map2D. Unreachable cells are -1.
     *
     * @param start source pixel
     * @param obsColor obstacle color
     * @param cyclic wrapping support
     * @return a new Map2D of distances
     */

    @Override
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {
        int w = getWidth();
        int h = getHeight();

        Map ans = new Map(w, h, -1);

        if (start == null) return ans;

        Pixel2D s = normalizeIfCyclic(start, cyclic);
        if (!cyclic && !isInside(s)) return ans;

        // מתחילים מהנקודה הראשונה, אם היא מכשול נחזיר -1
        if (getPixel(s) == obsColor) {
            return ans;
        }
        Queue<Pixel2D> q = new LinkedList<>();
        q.add(s);
        ans.setPixel(s, 0);

        int[][] dirs = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };
        while (!q.isEmpty()) {
            Pixel2D p = q.remove();
            int x = p.getX();
            int y = p.getY();
            int d = ans.getPixel(p);

            for (int[] dir : dirs) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if (cyclic) {
                    nx = mod(nx, w);
                    ny = mod(ny, h);
                }

                Pixel2D np = new Index2D(nx, ny);
                if (!cyclic && !isInside(np)) {
                    continue;
                }
                if (!isInside(np)) {
                    continue;
                }
                if (getPixel(np) == obsColor) {
                    continue;
                }
                if (ans.getPixel(np) != -1) {
                    continue;
                }
                ans.setPixel(np, d + 1);
                q.add(np);
            }
        }
        return ans;
    }

    /**
     * @return a string representation of the map row by row.
     */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int y = 0; y < getHeight(); y++){
            for(int x = 0; x < getWidth(); x++){
                sb.append(map[x][y]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
