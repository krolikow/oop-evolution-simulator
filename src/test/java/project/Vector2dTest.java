package project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class Vector2dTest {

    @Test
    public void testEquals() {
        Vector2d v_1_1 = new Vector2d(1, 1);
        Vector2d v_2_4 = new Vector2d(2, 4);
        Vector2d v_2_4a = new Vector2d(2, 4);
        assertEquals(v_1_1, v_1_1);
        assertNotEquals(v_2_4, v_1_1);
        assertNotEquals(v_1_1, v_2_4a);
        assertNotEquals(v_2_4a, v_1_1);
        assertEquals(v_2_4, v_2_4a);
        assertEquals(v_2_4a, v_2_4);
    }

    @Test
    public void testToString() {
        assertEquals(new Vector2d(0, 1).toString(), "(0,1)");
        assertEquals(new Vector2d(3, -4).toString(), "(3,-4)");
        assertEquals(new Vector2d(-3, -4).toString(), "(-3,-4)");
        assertEquals(new Vector2d(-3, -1).toString(), "(-3,-1)");
    }

    @Test
    public void testPrecedes() {
        Vector2d a = new Vector2d(0, 1);
        Vector2d b = new Vector2d(0, 1);
        Vector2d c = new Vector2d(3, 1);

        assertTrue(a.precedes((a)));
        assertTrue(a.precedes(b));
        assertFalse(c.precedes(b));
    }

    @Test
    public void testFollows() {
        Vector2d a = new Vector2d(0, 1);
        Vector2d b = new Vector2d(0, 1);
        Vector2d c = new Vector2d(3, 1);

        assertTrue(a.follows((a)));
        assertTrue(a.follows(b));
        assertFalse(b.follows(c));
    }


    @Test
    public void testAdd() {
        Vector2d a = new Vector2d(3, 2);
        Vector2d b = new Vector2d(-1, -1);

        assertEquals(a.add(b), new Vector2d(2, 1));
        assertEquals(b.add(a), new Vector2d(2, 1));
        assertEquals(a.add(a), new Vector2d(6, 4));
    }

    @Test
    public void testSubtract() {
        Vector2d a = new Vector2d(3, 2);
        Vector2d b = new Vector2d(-1, -1);

        assertEquals(a.subtract(b), new Vector2d(4, 3));
        assertEquals(b.subtract(a), new Vector2d(-4, -3));
        assertEquals(a.subtract(a), new Vector2d(0, 0));
    }

}

