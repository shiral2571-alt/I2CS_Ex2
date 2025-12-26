import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Student tests for the Map class.
 * Covers: constructors, init, get/set, isInside, sameDimensions,
 * addMap2D, mul, rescale, drawCircle/Line/Rect, fill, allDistance, shortestPath, toString.
 */
class MapStudentTest {

    private int[][] baseArr;
    private Map2D map;

    @BeforeEach
    void setUp() {
        baseArr = new int[][]{
                {0, 1, 0},
                {1, 0, 1},
                {0, 1, 0}
        };
        map = new Map(baseArr);
    }

    // ---------- 1. Constructors & basic getters ----------

    @Test
    void constructorFromArray_copiesValuesAndNotAlias() {
        assertEquals(3, map.getWidth());
        assertEquals(3, map.getHeight());

        assertEquals(0, map.getPixel(0, 0));
        assertEquals(1, map.getPixel(1, 0));
        assertEquals(0, map.getPixel(2, 2));

        // בודקים שאין alias למערך החיצוני
        baseArr[0][0] = 99;
        assertNotEquals(99, map.getPixel(0, 0));
    }

    @Test
    void constructorSizeAndValue_setsAllToV() {
        Map2D m = new Map(4, 2, 7);
        assertEquals(4, m.getWidth());
        assertEquals(2, m.getHeight());
        for (int x = 0; x < m.getWidth(); x++) {
            for (int y = 0; y < m.getHeight(); y++) {
                assertEquals(7, m.getPixel(x, y));
            }
        }
    }

    // ---------- 2. init(int[][]) ----------

    @Test
    void initWithArray_changesSizeAndValues() {
        int[][] arr = {
                {5, 6},
                {7, 8}
        };
        map.init(arr);

        assertEquals(2, map.getWidth());
        assertEquals(2, map.getHeight());
        assertEquals(5, map.getPixel(0, 0));
        assertEquals(8, map.getPixel(1, 1));
    }

    // ---------- 3. getPixel / setPixel / isInside ----------

    @Test
    void setPixel_withXYAndPixel2D() {
        map.setPixel(0, 0, 9);
        assertEquals(9, map.getPixel(0, 0));

        Pixel2D p = new Index2D(2, 1);
        map.setPixel(p, 4);
        assertEquals(4, map.getPixel(p));
    }

    @Test
    void isInside_trueInside_falseOutside() {
        assertTrue(map.isInside(new Index2D(0, 0)));
        assertTrue(map.isInside(new Index2D(2, 2)));

        assertFalse(map.isInside(new Index2D(-1, 0)));
        assertFalse(map.isInside(new Index2D(3, 0)));
        assertFalse(map.isInside(new Index2D(0, 3)));
    }

    // ---------- 4. sameDimensions & addMap2D & equals ----------

    @Test
    void sameDimensions_works() {
        Map2D m1 = new Map(3, 3, 0);
        Map2D m2 = new Map(3, 3, 1);
        Map2D m3 = new Map(2, 3, 0);

        assertTrue(m1.sameDimensions(m2));
        assertFalse(m1.sameDimensions(m3));
    }

    @Test
    void addMap2D_addsCellByCell_whenSameDimensions() {
        int[][] a = {{1, 1}, {1, 1}};
        int[][] b = {{2, 2}, {2, 2}};
        Map2D m1 = new Map(a);
        Map2D m2 = new Map(b);

        m1.addMap2D(m2);

        assertEquals(3, m1.getPixel(0, 0));
        assertEquals(3, m1.getPixel(1, 1));
    }

    @Test
    void equals_trueWhenSameData_falseWhenDifferent() {
        Map2D m1 = new Map(baseArr);
        Map2D m2 = new Map(baseArr);
        assertEquals(m1, m2);

        m2.setPixel(0, 0, 7);
        assertNotEquals(m1, m2);
    }

    // ---------- 5. mul & rescale ----------

    @Test
    void mul_multipliesAllValuesAndCastToInt() {
        int[][] arr = {
                {1, 2},
                {3, 4}
        };
        Map2D m = new Map(arr);
        m.mul(2.5);  // 1*2.5=2.5→2, 2*2.5=5, 3*2.5=7.5→7, 4*2.5=10

        // שימי לב: map[x][y] → arr[x][y]
        assertEquals(2, m.getPixel(0, 0)); // 1*2.5
        assertEquals(5, m.getPixel(0, 1)); // 2*2.5
        assertEquals(7, m.getPixel(1, 0)); // 3*2.5
        assertEquals(10, m.getPixel(1, 1)); // 4*2.5
    }

    @Test
    void rescale_upScale2x2To4x4_copiesFromNearestSource() {
        int[][] arr = {
                {1, 2},
                {3, 4}
        };
        Map m = new Map(arr);
        m.rescale(2.0, 2.0);

        assertEquals(4, m.getWidth());
        assertEquals(4, m.getHeight());

        // בלוק עליון-שמאלי (מקור 0,0 → 1)
        assertEquals(1, m.getPixel(0, 0));
        assertEquals(1, m.getPixel(1, 0));
        assertEquals(1, m.getPixel(0, 1));
        assertEquals(1, m.getPixel(1, 1));

        // בלוק עליון-ימני (מקור 1,0 → 3)
        assertEquals(3, m.getPixel(2, 0));
        assertEquals(3, m.getPixel(3, 0));
        assertEquals(3, m.getPixel(2, 1));
        assertEquals(3, m.getPixel(3, 1));
    }

    // ---------- 6. drawCircle / drawLine / drawRect ----------

    @Test
    void drawCircle_radius1_drawsPlusShapeAroundCenter() {
        Map2D m = new Map(5, 5, 0);
        Pixel2D c = new Index2D(2, 2);
        m.drawCircle(c, 1.0, 9);

        assertEquals(9, m.getPixel(2, 2));
        assertEquals(9, m.getPixel(1, 2));
        assertEquals(9, m.getPixel(3, 2));
        assertEquals(9, m.getPixel(2, 1));
        assertEquals(9, m.getPixel(2, 3));

        // נקודה רחוקה לא אמורה להיות צבועה
        assertEquals(0, m.getPixel(0, 0));
    }

    @Test
    void drawLine_horizontal() {
        Map2D m = new Map(5, 5, 0);
        m.drawLine(new Index2D(0, 2), new Index2D(4, 2), 3);

        for (int x = 0; x < 5; x++) {
            assertEquals(3, m.getPixel(x, 2));
        }
        assertEquals(0, m.getPixel(0, 0));
    }

    @Test
    void drawRect_fillsRectangle() {
        Map2D m = new Map(5, 5, 0);
        m.drawRect(new Index2D(1, 1), new Index2D(3, 3), 7);

        // כל הפיקסלים בתוך המלבן (כולל הגבול והפנים) צריכים להיות בצבע
        for (int x = 1; x <= 3; x++) {
            for (int y = 1; y <= 3; y++) {
                assertEquals(7, m.getPixel(x, y),
                        "Pixel (" + x + "," + y + ") should be filled");
            }
        }

        // מחוץ למלבן נשאר 0
        assertEquals(0, m.getPixel(0, 0));
        assertEquals(0, m.getPixel(4, 4));
    }


    // ---------- 7. fill (non-cyclic & cyclic) ----------

    @Test
    void fill_nonCyclic_fillsConnectedComponent() {
        int[][] arr = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        Map2D m = new Map(arr);

        int changed = m.fill(new Index2D(0, 0), 5, false);

        assertEquals(8, changed);           // 8 אפסים
        assertEquals(5, m.getPixel(0, 0));
        assertEquals(5, m.getPixel(2, 2));
        assertEquals(1, m.getPixel(1, 1));  // המכשול נשאר
    }

    @Test
    void fill_cyclic_wrapsAroundEdges() {
        int[][] arr = {
                {0},
                {1},
                {0}
        }; // 3x1: x=0..2, y=0

        Map2D m = new Map(arr);

        int changed = m.fill(new Index2D(0, 0), 7, true);

        assertEquals(2, changed); // שני הקצוות
        assertEquals(7, m.getPixel(0, 0));
        assertEquals(1, m.getPixel(1, 0));
        assertEquals(7, m.getPixel(2, 0));
    }

    // ---------- 8. allDistance & shortestPath ----------

    @Test
    void allDistance_simpleNoObstacles() {
        Map2D m = new Map(3, 3, 0);
        Pixel2D start = new Index2D(0, 0);

        Map2D dist = m.allDistance(start, 1, false);

        assertEquals(0, dist.getPixel(0, 0));
        assertEquals(1, dist.getPixel(1, 0));
        assertEquals(1, dist.getPixel(0, 1));
        assertEquals(2, dist.getPixel(1, 1));
        assertEquals(4, dist.getPixel(2, 2)); // BFS manhattan
    }

    @Test
    void allDistance_withObstacle_setsUnreachableToMinusOne() {
        // חומה אנכית באמצע (x=1)
        int[][] arr = {
                {0, 0, 0},
                {1, 1, 1},
                {0, 0, 0}
        };
        Map2D m = new Map(arr);
        Pixel2D start = new Index2D(0, 0);

        Map2D dist = m.allDistance(start, 1, false);

        // (2,0) חסום ע"י הקיר
        assertEquals(-1, dist.getPixel(2, 0));
        // (0,2) reachable
        assertTrue(dist.getPixel(0, 2) >= 0);
    }

    @Test
    void shortestPath_returnsValidShortestPath() {
        Map2D m = new Map(3, 3, 0);
        Pixel2D start = new Index2D(0, 0);
        Pixel2D end   = new Index2D(2, 2);

        Pixel2D[] path = m.shortestPath(start, end, 1, false);

        assertNotNull(path);
        assertTrue(path.length >= 3);

        // נקודת התחלה וסיום
        assertEquals(0, path[0].getX());
        assertEquals(0, path[0].getY());
        Pixel2D last = path[path.length - 1];
        assertEquals(2, last.getX());
        assertEquals(2, last.getY());

        // כל צעד צמוד לקודם
        for (int i = 1; i < path.length; i++) {
            int dx = Math.abs(path[i].getX() - path[i - 1].getX());
            int dy = Math.abs(path[i].getY() - path[i - 1].getY());
            assertEquals(1, dx + dy, "Each step must be 4-neighbor");
        }
    }

    @Test
    void shortestPath_returnsNullWhenNoPath() {
        int[][] arr = {
                {0, 1, 0},
                {1, 1, 1},
                {0, 1, 0}
        };
        Map2D m = new Map(arr);
        Pixel2D start = new Index2D(0, 0);
        Pixel2D end   = new Index2D(2, 2);

        Pixel2D[] path = m.shortestPath(start, end, 1, false);
        assertNull(path);
    }

    // ---------- 9. toString ----------

    @Test
    void toString_rowsSeparatedByNewlines() {
        String s = map.toString();
        String[] lines = s.split("\\R");

        assertEquals(3, lines.length);
        assertTrue(lines[0].contains("0 1 0"));
    }

    @Test
    void getMap_returnsDeepCopy_notAlias() {
        Map2D m = new Map(new int[][]{{1, 2}, {3, 4}});
        int[][] copy = m.getMap();

        // משנים את העותק
        copy[0][0] = 999;

        // המפה המקורית לא אמורה להשתנות
        assertEquals(1, m.getPixel(0, 0));
    }

    @Test
    void init_null_throwsRuntimeException() {
        Map2D m = new Map(2, 2, 0);
        assertThrows(RuntimeException.class, () -> m.init((int[][]) null));
    }

    @Test
    void init_empty_throwsRuntimeException() {
        Map2D m = new Map(2, 2, 0);
        assertThrows(RuntimeException.class, () -> m.init(new int[0][0]));
    }

    @Test
    void init_ragged_throwsRuntimeException() {
        Map2D m = new Map(2, 2, 0);
        int[][] ragged = {
                {1, 2, 3},
                {4, 5}
        };
        assertThrows(RuntimeException.class, () -> m.init(ragged));
    }

    // ----------  BFS edge cases ----------

    @Test
    void shortestPath_sameStartEnd_returnsSinglePixel() {
        Map2D m = new Map(3, 3, 0);
        Pixel2D p = new Index2D(1, 1);

        Pixel2D[] path = m.shortestPath(p, p, 1, false);

        assertNotNull(path);
        assertEquals(1, path.length);
        assertEquals(p, path[0]);
    }

    @Test
    void allDistance_startIsObstacle_allMinusOne() {
        int[][] arr = {
                {1, 1},
                {1, 1}
        };
        Map2D m = new Map(arr);

        Map2D dist = m.allDistance(new Index2D(0, 0), 1, false);

        assertEquals(-1, dist.getPixel(0, 0));
        assertEquals(-1, dist.getPixel(1, 1));
    }
    }
