package me.touchie771.minecraftGUI.api;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SlotItemTest {

    @Test
    void testLegacyConstructor() {
        Component name = Component.text("Test Item");
        SlotItem item = new SlotItem(name, (short) 0, Material.DIAMOND, 1);

        assertEquals(name, item.itemName());
        assertEquals(0, item.itemSlot());
        assertEquals(Material.DIAMOND, item.material());
        assertEquals(1, item.quantity());
        assertNull(item.lore());
        assertNull(item.enchantments());
        assertNull(item.customItemStack());
    }

    @Test
    void testFullConstructor() {
        Component name = Component.text("Full Item");
        List<Component> lore = List.of(Component.text("Line 1"));
        Map<Enchantment, Integer> enchants = Map.of(Enchantment.UNBREAKING, 1);
        
        SlotItem item = new SlotItem(
            name,
            (short) 1,
            Material.GOLD_INGOT,
            5,
            lore,
            enchants,
            null,
            123,
            10
        );

        assertEquals(name, item.itemName());
        assertEquals(1, item.itemSlot());
        assertEquals(Material.GOLD_INGOT, item.material());
        assertEquals(5, item.quantity());
        assertEquals(lore, item.lore());
        assertEquals(enchants, item.enchantments());
        assertEquals(123, item.customModelData());
        assertEquals(10, item.damage());
    }

    @Test
    void testCustomItemStack() {
        ItemStack stack = mock(ItemStack.class);
        SlotItem item = new SlotItem(
            null,
            (short) 2,
            null,
            0,
            null,
            null,
            stack,
            null,
            null
        );

        assertEquals(stack, item.customItemStack());
        assertNull(item.material());
        assertNull(item.itemName());
    }
}