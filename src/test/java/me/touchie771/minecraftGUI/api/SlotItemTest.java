package me.touchie771.minecraftGUI.api;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlotItemTest {

    @Test
    void testSlotItemCreation() {
        Component name = Component.text("Test Item");
        SlotItem item = new SlotItem(name, (short) 0, Material.DIAMOND, 1);

        assertEquals(name, item.itemName());
        assertEquals(0, item.itemSlot());
        assertEquals(Material.DIAMOND, item.material());
        assertEquals(1, item.quantity());
    }
}