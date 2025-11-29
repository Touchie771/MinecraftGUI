package me.touchie771.minecraftGUI.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SlotItemTest {

    @Test
    @DisplayName("Test builder with minimal configuration")
    void testBuilderMinimalConfiguration() {
        int slot = 0;
        SlotItem item = SlotItem.builder(slot)
            .material(Material.DIAMOND)
            .build();

        assertNull(item.itemName());
        assertEquals(slot, item.itemSlot());
        assertEquals(Material.DIAMOND, item.material());
        assertEquals(1, item.quantity()); // Default quantity
        assertNull(item.lore());
        assertNull(item.enchantments());
        assertNull(item.customItemStack());
        assertNull(item.customModelData());
        assertNull(item.damage());
    }

    @Test
    @DisplayName("Test builder with full configuration")
    void testBuilderFullConfiguration() {
        Component name = Component.text("Full Item", NamedTextColor.GOLD);
        List<Component> lore = List.of(
            Component.text("First line"),
            Component.text("Second line", NamedTextColor.RED)
        );
        Map<Enchantment, Integer> enchants = Map.of(
            Enchantment.UNBREAKING, 3,
            Enchantment.SHARPNESS, 5
        );
        ItemStack customStack = mock(ItemStack.class);

        SlotItem item = SlotItem.builder(10)
            .itemName(name)
            .material(Material.NETHERITE_SWORD)
            .quantity(1)
            .lore(lore)
            .enchantments(enchants)
            .customItemStack(customStack)
            .customModelData(1234)
            .damage(50)
            .build();

        assertEquals(name, item.itemName());
        assertEquals(10, item.itemSlot());
        assertEquals(Material.NETHERITE_SWORD, item.material());
        assertEquals(1, item.quantity());
        assertEquals(lore, item.lore());
        assertEquals(enchants, item.enchantments());
        assertEquals(customStack, item.customItemStack());
        assertEquals(1234, item.customModelData());
        assertEquals(50, item.damage());
    }

    @Test
    @DisplayName("Test builder with custom ItemStack only")
    void testBuilderCustomItemStackOnly() {
        ItemStack customStack = mock(ItemStack.class);
        
        SlotItem item = SlotItem.builder(5)
            .customItemStack(customStack)
            .quantity(64)
            .build();

        assertNull(item.itemName());
        assertEquals(5, item.itemSlot());
        assertNull(item.material());
        assertEquals(64, item.quantity());
        assertNull(item.lore());
        assertNull(item.enchantments());
        assertEquals(customStack, item.customItemStack());
        assertNull(item.customModelData());
        assertNull(item.damage());
    }

    @Test
    @DisplayName("Test addLore with single line")
    void testAddLoreSingleLine() {
        Component line = Component.text("A single lore line");
        
        SlotItem item = SlotItem.builder(0)
            .material(Material.STONE)
            .addLore(line)
            .build();

        assertNotNull(item.lore());
        assertEquals(1, item.lore().size());
        assertEquals(line, item.lore().getFirst());
    }

    @Test
    @DisplayName("Test addLore with multiple lines")
    void testAddLoreMultipleLines() {
        Component line1 = Component.text("First line");
        Component line2 = Component.text("Second line");
        Component line3 = Component.text("Third line");
        
        SlotItem item = SlotItem.builder(0)
            .material(Material.STONE)
            .addLore(line1, line2, line3)
            .build();

        assertNotNull(item.lore());
        assertEquals(3, item.lore().size());
        assertEquals(line1, item.lore().get(0));
        assertEquals(line2, item.lore().get(1));
        assertEquals(line3, item.lore().get(2));
    }

    @Test
    @DisplayName("Test addLore chaining with existing lore")
    void testAddLoreChainingWithExistingLore() {
        Component initialLore = Component.text("Initial lore");
        Component addedLine1 = Component.text("Added line 1");
        Component addedLine2 = Component.text("Added line 2");
        
        SlotItem item = SlotItem.builder(0)
            .material(Material.STONE)
            .lore(List.of(initialLore))
            .addLore(addedLine1)
            .addLore(addedLine2)
            .build();

        assertNotNull(item.lore());
        assertEquals(3, item.lore().size());
        assertEquals(initialLore, item.lore().get(0));
        assertEquals(addedLine1, item.lore().get(1));
        assertEquals(addedLine2, item.lore().get(2));
    }

    @Test
    @DisplayName("Test addLore without initial lore")
    void testAddLoreWithoutInitialLore() {
        Component line = Component.text("First lore line");
        
        SlotItem item = SlotItem.builder(0)
            .material(Material.STONE)
            .addLore(line)
            .build();

        assertNotNull(item.lore());
        assertEquals(1, item.lore().size());
        assertEquals(line, item.lore().getFirst());
    }

    @Test
    @DisplayName("Test builder method chaining")
    void testBuilderMethodChaining() {
        Component name = Component.text("Chained Item");
        
        SlotItem item = SlotItem.builder(15)
            .itemName(name)
            .material(Material.EMERALD)
            .quantity(32)
            .customModelData(5678)
            .build();

        assertEquals(name, item.itemName());
        assertEquals(15, item.itemSlot());
        assertEquals(Material.EMERALD, item.material());
        assertEquals(32, item.quantity());
        assertEquals(5678, item.customModelData());
        assertNull(item.lore());
        assertNull(item.enchantments());
        assertNull(item.customItemStack());
        assertNull(item.damage());
    }

    @Test
    @DisplayName("Test builder with null values")
    void testBuilderWithNullValues() {
        SlotItem item = SlotItem.builder(20)
            .itemName(null)
            .material(Material.AIR)
            .lore(null)
            .enchantments(null)
            .customItemStack(null)
            .customModelData(null)
            .damage(null)
            .build();

        assertNull(item.itemName());
        assertEquals(20, item.itemSlot());
        assertEquals(Material.AIR, item.material());
        assertEquals(1, item.quantity()); // Default quantity
        assertNull(item.lore());
        assertNull(item.enchantments());
        assertNull(item.customItemStack());
        assertNull(item.customModelData());
        assertNull(item.damage());
    }

    @Test
    @DisplayName("Test legacy constructor still works")
    void testLegacyConstructor() {
        Component name = Component.text("Legacy Item");
        SlotItem item = new SlotItem(name, 5, Material.GOLD_INGOT, 16);

        assertEquals(name, item.itemName());
        assertEquals(5, item.itemSlot());
        assertEquals(Material.GOLD_INGOT, item.material());
        assertEquals(16, item.quantity());
        assertNull(item.lore());
        assertNull(item.enchantments());
        assertNull(item.customItemStack());
        assertNull(item.customModelData());
        assertNull(item.damage());
    }

    @Test
    @DisplayName("Test builder produces immutable record")
    void testBuilderProducesImmutableRecord() {
        Component originalName = Component.text("Original");
        SlotItem item = SlotItem.builder(0)
            .itemName(originalName)
            .material(Material.DIAMOND)
            .addLore(Component.text("Line 1"))
            .build();

        // Records are immutable, so these should work as expected
        assertEquals(originalName, item.itemName());
        assertEquals(Material.DIAMOND, item.material());
        
        // Verify that the lore list is immutable if created from builder
        assertThrows(UnsupportedOperationException.class, () -> {
            if (item.lore() != null) {
                item.lore().add(Component.text("This should fail"));
            }
        });
    }

    @Test
    @DisplayName("Test builder with enchantments")
    void testBuilderWithEnchantments() {
        Map<Enchantment, Integer> enchants = Map.of(
            Enchantment.PROTECTION, 4,
            Enchantment.FEATHER_FALLING, 4
        );
        
        SlotItem item = SlotItem.builder(8)
            .material(Material.DIAMOND_CHESTPLATE)
            .enchantments(enchants)
            .build();

        assertEquals(enchants, item.enchantments());
        assertEquals(Material.DIAMOND_CHESTPLATE, item.material());
        assertEquals(8, item.itemSlot());
    }

    @Test
    @DisplayName("Test builder with damage value")
    void testBuilderWithDamageValue() {
        SlotItem item = SlotItem.builder(12)
            .material(Material.IRON_SWORD)
            .damage(100)
            .build();

        assertEquals(100, item.damage());
        assertEquals(Material.IRON_SWORD, item.material());
        assertEquals(12, item.itemSlot());
    }

    @Test
    @DisplayName("Test builder with item flags")
    void testBuilderWithItemFlags() {
        SlotItem item = SlotItem.builder(0)
            .material(Material.DIAMOND_SWORD)
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
            .build();

        assertNotNull(item.itemFlags());
        assertEquals(2, item.itemFlags().size());
        assertTrue(item.itemFlags().contains(ItemFlag.HIDE_ATTRIBUTES));
        assertTrue(item.itemFlags().contains(ItemFlag.HIDE_ENCHANTS));
    }

    @Test
    @DisplayName("Test addEnchantment convenience method")
    void testAddEnchantment() {
        SlotItem item = SlotItem.builder(0)
            .material(Material.DIAMOND_PICKAXE)
            .addEnchantment(Enchantment.EFFICIENCY, 5)
            .addEnchantment(Enchantment.UNBREAKING, 3)
            .build();

        assertNotNull(item.enchantments());
        assertEquals(2, item.enchantments().size());
        assertEquals(5, item.enchantments().get(Enchantment.EFFICIENCY));
        assertEquals(3, item.enchantments().get(Enchantment.UNBREAKING));
    }
}