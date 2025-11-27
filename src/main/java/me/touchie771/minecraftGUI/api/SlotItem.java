package me.touchie771.minecraftGUI.api;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Represents an item that can be placed in a specific slot of a Minecraft GUI menu.
 * This record encapsulates all the necessary information to display an item in an inventory.
 *
 * @param itemName The display name of the item (shown when hovering). If null and using customItemStack, keeps original name.
 * @param itemSlot The slot position where this item should be placed (0-53 for chest inventories)
 * @param material The Minecraft material type for this item. Required if customItemStack is null.
 * @param quantity The stack size of this item (1-64).
 * @param lore The lore lines for the item.
 * @param enchantments The enchantments to apply.
 * @param customItemStack A base ItemStack to use (e.g. for items with NBT).
 * @param customModelData The custom model data ID.
 * @param damage The damage/durability value.
 */
public record SlotItem(
    @Nullable Component itemName,
    short itemSlot,
    @Nullable Material material,
    int quantity,
    @Nullable List<Component> lore,
    @Nullable Map<Enchantment, Integer> enchantments,
    @Nullable ItemStack customItemStack,
    @Nullable Integer customModelData,
    @Nullable Integer damage
) {
    /**
     * Legacy constructor for backward compatibility.
     *
     * @param itemName The display name
     * @param itemSlot The slot
     * @param material The material
     * @param quantity The quantity
     */
    public SlotItem(@NotNull Component itemName, short itemSlot, @NotNull Material material, int quantity) {
        this(itemName, itemSlot, material, quantity, null, null, null, null, null);
    }
}