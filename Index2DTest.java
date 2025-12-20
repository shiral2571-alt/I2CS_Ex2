package assignments.Ex2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Index2DTest {

    @Test
    void testCtorAndGetters() {
        Index2D p = new Index2D(3,5);
        assertEquals(3, p.getX());
        assertEquals(5, p.getY());
    }

    @Test
    void testEqualsValues() {
        Index2D p1 = new Index2D(4,6);
        Index2D p2 = new Index2D(4,6);
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
    }


    @Test
    void testEqualsDifferentValue() {
        Index2D p1 = new Index2D(4,6);
        Index2D p2 = new Index2D(3,2);
        assertFalse(p1.equals(p2));
    }

    @Test
    void testDistance2D() {
        Index2D p1 = new Index2D(0,0);
        Index2D p2 = new Index2D(4,6);
        // sqrt(4^2 + 6^2) = sqrt(52) ≈ 7.2111
        assertEquals(7.2111, p1.distance2D(p2), 0.0001);
    }

    @Test
    void testToString() {
        Index2D p1 = new Index2D(0,0);
        assertEquals("(0,0)", p1.toString());
    }
}