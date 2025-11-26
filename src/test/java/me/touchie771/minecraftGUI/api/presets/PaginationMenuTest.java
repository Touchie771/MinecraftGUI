package me.touchie771.minecraftGUI.api.presets;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.touchie771.minecraftGUI.api.presets.PaginationMenu.PageItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaginationMenuTest {

    private ServerMock server;
    private Plugin plugin;
    private PlayerMock player;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.createMockPlugin();
        player = server.addPlayer();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testPaginationLogic() {
        // Create 50 items. Since page size is 45, we should have 2 pages.
        List<PageItem> items = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add(new PageItem(Component.text("Item " + i), Material.STONE, 1, null));
        }

        PaginationMenu menu = new PaginationMenu(plugin, Component.text("Pages"), items);
        menu.open(player);
        
        // Check Page 1
        assertNotNull(player.getOpenInventory().getTopInventory().getItem(0), "Item 0 should be present");
        assertNotNull(player.getOpenInventory().getTopInventory().getItem(44), "Item 44 should be present");
        
        // Check Next Button (Slot 53)
        assertNotNull(player.getOpenInventory().getTopInventory().getItem(53), "Next button should be present");
        assertEquals(Material.ARROW, player.getOpenInventory().getTopInventory().getItem(53).getType());
        
        // Check Prev Button (Slot 45) - Should be null on first page
        assertNull(player.getOpenInventory().getTopInventory().getItem(45), "Prev button should NOT be present on page 1");

        // Click Next Page
        player.simulateInventoryClick(player.getOpenInventory(), 53);
        
        // Check Page 2
        // Item 45 (index) should be at slot 0
        assertNotNull(player.getOpenInventory().getTopInventory().getItem(0), "Item 45 should be present on page 2");
        
        // Check Next Button - Should be null on last page
        assertNull(player.getOpenInventory().getTopInventory().getItem(53), "Next button should NOT be present on last page");
        
        // Check Prev Button - Should be present
        assertNotNull(player.getOpenInventory().getTopInventory().getItem(45), "Prev button should be present on page 2");
        
        // Click Prev Page
        player.simulateInventoryClick(player.getOpenInventory(), 45);
        
        // Should be back on Page 1
        assertNull(player.getOpenInventory().getTopInventory().getItem(45), "Prev button should NOT be present on page 1");
    }
}