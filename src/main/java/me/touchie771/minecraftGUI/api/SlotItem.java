package me.touchie771.minecraftGUI.api;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an item that can be placed in a specific slot of a Minecraft GUI menu.
 * This record encapsulates all the necessary information to display an item in an inventory.
 *
 * @param itemName The display name of the item (shown when hovering)
 * @param itemSlot The slot position where this item should be placed (0-53 for chest inventories)
 * @param material The Minecraft material type for this item
 * @param quantity The stack size of this item (1-64, must be valid for the material)
 */
public record SlotItem(@NotNull Component itemName, short itemSlot, @NotNull Material material, int quantity) {}