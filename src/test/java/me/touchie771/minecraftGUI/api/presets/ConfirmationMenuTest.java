package me.touchie771.minecraftGUI.api.presets;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.touchie771.minecraftGUI.api.Menu;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ConfirmationMenuTest {

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
    void testConfirmationMenuCreation() {
        Menu menu = ConfirmationMenu.create(plugin, Component.text("Confirm?"), () -> {}, () -> {});
        
        assertNotNull(menu);
        assertEquals(27, menu.getInventory().getSize());
        
        // Check Items
        assertNotNull(menu.getInventory().getItem(11));
        assertEquals(Material.LIME_WOOL, menu.getInventory().getItem(11).getType());
        
        assertNotNull(menu.getInventory().getItem(15));
        assertEquals(Material.RED_WOOL, menu.getInventory().getItem(15).getType());
    }

    @Test
    void testConfirmClick() {
        AtomicBoolean confirmed = new AtomicBoolean(false);
        AtomicBoolean canceled = new AtomicBoolean(false);

        Menu menu = ConfirmationMenu.create(plugin, Component.text("Confirm?"), 
            () -> confirmed.set(true), 
            () -> canceled.set(true)
        );

        player.openInventory(menu.getInventory());
        
        // Simulate click on Confirm (slot 11)
        player.simulateInventoryClick(player.getOpenInventory(), 11);

        assertTrue(confirmed.get(), "Confirm runnable should be executed");
        assertFalse(canceled.get(), "Cancel runnable should NOT be executed");
    }
    
    @Test
    void testCancelClick() {
        AtomicBoolean confirmed = new AtomicBoolean(false);
        AtomicBoolean canceled = new AtomicBoolean(false);

        Menu menu = ConfirmationMenu.create(plugin, Component.text("Confirm?"), 
            () -> confirmed.set(true), 
            () -> canceled.set(true)
        );

        player.openInventory(menu.getInventory());
        
        // Simulate click on Cancel (slot 15)
        player.simulateInventoryClick(player.getOpenInventory(), 15);

        assertFalse(confirmed.get(), "Confirm runnable should NOT be executed");
        assertTrue(canceled.get(), "Cancel runnable should be executed");
    }
}