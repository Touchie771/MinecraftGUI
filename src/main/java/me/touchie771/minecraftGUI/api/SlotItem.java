package me.touchie771.minecraftGUI.api;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
 * @param itemFlags The item flags to apply (e.g., HIDE_ATTRIBUTES).
 */
public record SlotItem(
    @Nullable Component itemName,
    int itemSlot,
    @Nullable Material material,
    int quantity,
    @Nullable List<Component> lore,
    @Nullable Map<Enchantment, Integer> enchantments,
    @Nullable ItemStack customItemStack,
    @Nullable Integer customModelData,
    @Nullable Integer damage,
    @Nullable Set<ItemFlag> itemFlags
) {
    /**
     * Legacy constructor for backward compatibility.
     *
     * @param itemName The display name
     * @param itemSlot The slot
     * @param material The material
     * @param quantity The quantity
     */
    public SlotItem(@NotNull Component itemName, int itemSlot, @NotNull Material material, int quantity) {
        this(itemName, itemSlot, material, quantity, null, null, null, null, null, null);
    }

    /**
     * Creates a new builder for constructing SlotItem instances.
     *
     * @param itemSlot The slot position where this item should be placed
     * @return A new Builder instance
     */
    public static Builder builder(int itemSlot) {
        return new Builder(itemSlot);
    }

    /**
     * Builder class for creating SlotItem instances with a fluent API.
     */
    public static class Builder {
        private @Nullable Component itemName;
        private final int itemSlot;
        private @Nullable Material material;
        private int quantity = 1;
        private @Nullable List<Component> lore;
        private @Nullable Map<Enchantment, Integer> enchantments;
        private @Nullable ItemStack customItemStack;
        private @Nullable Integer customModelData;
        private @Nullable Integer damage;
        private @Nullable Set<ItemFlag> itemFlags;

        private Builder(int itemSlot) {
            this.itemSlot = itemSlot;
        }

        /**
         * Sets the display name of the item.
         *
         * @param itemName The display name component
         * @return This builder instance for chaining
         */
        public Builder itemName(@Nullable Component itemName) {
            this.itemName = itemName;
            return this;
        }

        /**
         * Sets the material type for this item.
         *
         * @param material The material type
         * @return This builder instance for chaining
         */
        public Builder material(@Nullable Material material) {
            this.material = material;
            return this;
        }

        /**
         * Sets the stack size of this item.
         *
         * @param quantity The stack size (1-64)
         * @return This builder instance for chaining
         */
        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * Sets the lore lines for the item.
         *
         * @param lore The list of lore components
         * @return This builder instance for chaining
         */
        public Builder lore(@Nullable List<Component> lore) {
            this.lore = lore;
            return this;
        }

        /**
         * Adds a single line of lore to the item.
         *
         * @param line The lore line to add
         * @return This builder instance for chaining
         */
        public Builder addLore(@NotNull Component line) {
            if (this.lore == null) {
                this.lore = new ArrayList<>();
            } else if (!(this.lore instanceof ArrayList)) {
                this.lore = new ArrayList<>(this.lore);
            }
            this.lore.add(line);
            return this;
        }

        /**
         * Adds multiple lines of lore to the item.
         *
         * @param lines The lore lines to add
         * @return This builder instance for chaining
         */
        public Builder addLore(@NotNull Component... lines) {
            if (this.lore == null) {
                this.lore = new ArrayList<>();
            } else if (!(this.lore instanceof ArrayList)) {
                this.lore = new ArrayList<>(this.lore);
            }
            this.lore.addAll(Arrays.asList(lines));
            return this;
        }

        /**
         * Sets the enchantments to apply to the item.
         *
         * @param enchantments The map of enchantments to levels
         * @return This builder instance for chaining
         */
        public Builder enchantments(@Nullable Map<Enchantment, Integer> enchantments) {
            this.enchantments = enchantments;
            return this;
        }

        /**
         * Adds an enchantment to the item.
         *
         * @param enchantment The enchantment to add
         * @param level The level of the enchantment
         * @return This builder instance for chaining
         */
        public Builder addEnchantment(@NotNull Enchantment enchantment, int level) {
            if (this.enchantments == null) {
                this.enchantments = new HashMap<>();
            } else if (!(this.enchantments instanceof HashMap)) {
                this.enchantments = new HashMap<>(this.enchantments);
            }
            this.enchantments.put(enchantment, level);
            return this;
        }

        /**
         * Sets a custom ItemStack to use as base (e.g., for items with NBT).
         *
         * @param customItemStack The custom ItemStack
         * @return This builder instance for chaining
         */
        public Builder customItemStack(@Nullable ItemStack customItemStack) {
            this.customItemStack = customItemStack;
            return this;
        }

        /**
         * Sets the custom model data ID.
         *
         * @param customModelData The custom model data ID
         * @return This builder instance for chaining
         */
        public Builder customModelData(@Nullable Integer customModelData) {
            this.customModelData = customModelData;
            return this;
        }

        /**
         * Sets the damage/durability value.
         *
         * @param damage The damage value
         * @return This builder instance for chaining
         */
        public Builder damage(@Nullable Integer damage) {
            this.damage = damage;
            return this;
        }

        /**
         * Sets the item flags to apply.
         *
         * @param itemFlags The set of item flags
         * @return This builder instance for chaining
         */
        public Builder itemFlags(@Nullable Set<ItemFlag> itemFlags) {
            this.itemFlags = itemFlags;
            return this;
        }

        /**
         * Adds an item flag to the item.
         *
         * @param flag The item flag to add
         * @return This builder instance for chaining
         */
        public Builder addItemFlag(@NotNull ItemFlag flag) {
            if (this.itemFlags == null) {
                this.itemFlags = new HashSet<>();
            } else if (!(this.itemFlags instanceof HashSet)) {
                this.itemFlags = new HashSet<>(this.itemFlags);
            }
            this.itemFlags.add(flag);
            return this;
        }

        /**
         * Adds multiple item flags to the item.
         *
         * @param flags The item flags to add
         * @return This builder instance for chaining
         */
        public Builder addItemFlags(@NotNull ItemFlag... flags) {
            if (this.itemFlags == null) {
                this.itemFlags = new HashSet<>();
            } else if (!(this.itemFlags instanceof HashSet)) {
                this.itemFlags = new HashSet<>(this.itemFlags);
            }
            this.itemFlags.addAll(Arrays.asList(flags));
            return this;
        }

        /**
         * Builds the SlotItem instance with the configured values.
         *
         * @return A new SlotItem instance
         */
        public SlotItem build() {
            return new SlotItem(
                itemName,
                itemSlot,
                material,
                quantity,
                lore != null ? List.copyOf(lore) : null,
                enchantments != null ? Map.copyOf(enchantments) : null,
                customItemStack,
                customModelData,
                damage,
                itemFlags != null ? Set.copyOf(itemFlags) : null
            );
        }
    }
}