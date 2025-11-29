package me.touchie771.minecraftGUI.api;

import be.seeseemelk.mockbukkit.MockBukkit;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    private Plugin plugin;

    @BeforeEach
    void setUp() {
        MockBukkit.mock();
        plugin = MockBukkit.createMockPlugin();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testMenuBuilderValidation() {
        // Test invalid size
        assertThrows(IllegalArgumentException.class, () -> 
            Menu.newBuilder().size(10).title(Component.text("Test")).plugin(plugin).build()
        );
        
        // Test missing title
        assertThrows(IllegalArgumentException.class, () -> 
            Menu.newBuilder().size(27).plugin(plugin).build()
        );
    }

    @Test
    void testMenuCreation() {
        Menu menu = Menu.newBuilder()
            .size(27)
            .title(Component.text("Test Menu"))
            .plugin(plugin)
            .build();

        assertNotNull(menu);
        assertEquals(27, menu.getInventory().getSize());
    }

    @Test
    void testAddItem() {
        Menu menu = Menu.newBuilder()
            .size(27)
            .title(Component.text("Test Menu"))
            .plugin(plugin)
            .build();

        SlotItem item = SlotItem.builder(0)
            .itemName(Component.text("Diamond"))
            .material(Material.DIAMOND)
            .build();
        menu.addItems(item);

        assertTrue(menu.getItems().contains(item));
        assertNotNull(menu.getInventory().getItem(0));
        assertEquals(Material.DIAMOND, Objects.requireNonNull(menu.getInventory().getItem(0)).getType());
    }

    @Test
    void testFillEmptyWith() {
        Menu menu = Menu.newBuilder()
            .size(9)
            .title(Component.text("Filler Test"))
            .plugin(plugin)
            .items(SlotItem.builder(4).material(Material.DIAMOND).build())
            .fillEmptyWith(Material.GRAY_STAINED_GLASS_PANE)
            .build();

        // Slot 4 should be Diamond
        assertEquals(Material.DIAMOND, Objects.requireNonNull(menu.getInventory().getItem(4)).getType());
        
        // Other slots should be filler
        assertEquals(Material.GRAY_STAINED_GLASS_PANE, Objects.requireNonNull(menu.getInventory().getItem(0)).getType());
        assertEquals(Material.GRAY_STAINED_GLASS_PANE, Objects.requireNonNull(menu.getInventory().getItem(8)).getType());
        
        // Total items should be 9
        assertEquals(9, menu.getItems().size());
    }

    @Test
    void testFillExcept() {
        Menu menu = Menu.newBuilder()
            .size(9)
            .title(Component.text("Fill Except Test"))
            .plugin(plugin)
            .fillExcept(Material.STONE, 0, 8)
            .build();

        // Slots 0 and 8 should be empty (null)
        assertNull(menu.getInventory().getItem(0));
        assertNull(menu.getInventory().getItem(8));
        
        // Middle slot should be filled
        assertEquals(Material.STONE, Objects.requireNonNull(menu.getInventory().getItem(4)).getType());
        
        // Total filled items should be 7
        assertEquals(7, menu.getItems().size());
    }

    @Test
    void testFillRange() {
        Menu menu = Menu.newBuilder()
            .size(9)
            .title(Component.text("Fill Range Test"))
            .plugin(plugin)
            .items(SlotItem.builder(4).material(Material.DIAMOND).build())
            .fillRange(3, 6, Material.DIRT)
            .build();

        // Range 3-6 (3, 4, 5). 4 is occupied by Diamond.
        // So 3 and 5 should be DIRT. 4 should be DIAMOND.
        
        assertNull(menu.getInventory().getItem(2)); // Outside range
        assertEquals(Material.DIRT, Objects.requireNonNull(menu.getInventory().getItem(3)).getType());
        assertEquals(Material.DIAMOND, Objects.requireNonNull(menu.getInventory().getItem(4)).getType());
        assertEquals(Material.DIRT, Objects.requireNonNull(menu.getInventory().getItem(5)).getType());
        assertNull(menu.getInventory().getItem(6)); // Outside range (end exclusive)
    }
}