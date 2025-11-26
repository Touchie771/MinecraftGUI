package me.touchie771.minecraftGUI.api;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    private ServerMock server;
    private Plugin plugin;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
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

        SlotItem item = new SlotItem(Component.text("Diamond"), (short) 0, Material.DIAMOND, 1);
        menu.addItems(item);

        assertTrue(menu.getItems().contains(item));
        assertNotNull(menu.getInventory().getItem(0));
        assertEquals(Material.DIAMOND, menu.getInventory().getItem(0).getType());
    }
}