package core.tui;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MenuStateTest {

    private static List<MenuOption> three() {
        return List.of(
            MenuOption.of("Alpha", "Detail A"),
            MenuOption.of("Beta",  "Detail B"),
            MenuOption.of("Gamma", "Detail C")
        );
    }

    @Test void startsAtIndexZero() {
        assertEquals(0, new Menu(three()).getSelectedIndex());
    }

    @Test void moveDownIncrementsIndex() {
        Menu menu = new Menu(three());
        menu.moveDown();
        assertEquals(1, menu.getSelectedIndex());
    }

    @Test void moveDownIsClampedAtLastOption() {
        Menu menu = new Menu(three());
        menu.moveDown(); menu.moveDown(); menu.moveDown(); // one past the end
        assertEquals(2, menu.getSelectedIndex());
    }

    @Test void moveUpDecrementsIndex() {
        Menu menu = new Menu(three());
        menu.moveDown();
        menu.moveUp();
        assertEquals(0, menu.getSelectedIndex());
    }

    @Test void moveUpIsClampedAtFirstOption() {
        Menu menu = new Menu(three());
        menu.moveUp(); menu.moveUp();
        assertEquals(0, menu.getSelectedIndex());
    }

    @Test void getSelectedReturnsCurrentOption() {
        Menu menu = new Menu(three());
        menu.moveDown();
        assertEquals("Beta", menu.getSelected().title());
        assertEquals("Detail B", menu.getSelected().detail());
    }

    @Test void setSelectedIndexJumpsDirectly() {
        Menu menu = new Menu(three());
        menu.setSelectedIndex(2);
        assertEquals(2, menu.getSelectedIndex());
        assertEquals("Gamma", menu.getSelected().title());
    }

    @Test void setSelectedIndexIgnoresNegative() {
        Menu menu = new Menu(three());
        menu.setSelectedIndex(-1);
        assertEquals(0, menu.getSelectedIndex());
    }

    @Test void setSelectedIndexIgnoresOutOfBounds() {
        Menu menu = new Menu(three());
        menu.setSelectedIndex(99);
        assertEquals(0, menu.getSelectedIndex());
    }

    @Test void sizeMatchesOptionCount() {
        assertEquals(3, new Menu(three()).size());
    }

    @Test void singleOptionMenuStaysAtZero() {
        Menu menu = new Menu(List.of(MenuOption.of("Only")));
        menu.moveDown(); menu.moveUp();
        assertEquals(0, menu.getSelectedIndex());
    }

    @Test void constructorRejectsEmptyList() {
        assertThrows(IllegalArgumentException.class, () -> new Menu(List.of()));
    }

    @Test void constructorRejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Menu(null));
    }
}