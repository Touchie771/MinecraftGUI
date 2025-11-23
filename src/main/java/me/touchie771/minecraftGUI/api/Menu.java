package me.touchie771.minecraftGUI.api;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Represents a Minecraft GUI menu that can be displayed to players.
 * This class provides a fluent builder pattern for creating inventories with custom items.
 * 
 * <p>Menus are immutable once created, but items can be added, removed, or cleared
 * using the provided methods. The underlying Bukkit inventory is automatically updated
 * when items are modified.</p>
 * 
 * <p>Example usage:
 * <pre>{@code
 * Menu menu = Menu.newBuilder()
 *     .size(27)
 *     .title(Component.text("My Menu"))
 *     .items(new SlotItem(Component.text("Diamond"), (short) 0, Material.DIAMOND, 1))
 *     .build();
 * }</pre></p>
 */
public class Menu {

    private final Inventory menu;
    private final HashSet<SlotItem> items;

    /**
     * Creates a new menu from the specified builder.
     * This constructor is package-private and should only be called by MenuBuilder.
     * 
     * @param builder the configured builder containing menu properties
     * @throws IllegalArgumentException if builder is null
     */
    public Menu(@NotNull MenuBuilder builder) {
        this.menu = Bukkit.createInventory(null, builder.inventorySize, builder.inventoryTitle);
        this.items = new HashSet<>(builder.items);

        for (SlotItem item : items) {
            ItemStack itemStack = new ItemStack(item.material(), item.quantity());
            itemStack.editMeta(meta -> meta.displayName(item.itemName()));
            menu.setItem(item.itemSlot(), itemStack);
        }
    }

    /**
     * Adds one or more items to this menu.
     * Each item must be placed in a unique slot that is not already occupied.
     * 
     * @param items the items to add to this menu
     * @throws IllegalArgumentException if an item is null or its slot is already occupied
     */
    public void addItems(SlotItem @NotNull ... items) {
        for (SlotItem item : items) {
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }
            if (item.itemSlot() < 0 || item.itemSlot() > 53) {
                throw new IllegalArgumentException("Item slot must be between 0 and 53");
            }
            if (item.quantity() < 1 || item.quantity() > 64) {
                throw new IllegalArgumentException("Item quantity must be between 1 and 64");
            }
            if (this.items.stream().anyMatch(existing -> existing.itemSlot() == item.itemSlot())) {
                throw new IllegalArgumentException("Slot " + item.itemSlot() + " is already occupied");
            }
        }
        this.items.addAll(List.of(items));

        for (SlotItem item : items) {
            ItemStack itemStack = new ItemStack(item.material(), item.quantity());
            itemStack.editMeta(meta -> meta.displayName(item.itemName()));
            this.menu.setItem(item.itemSlot(), itemStack);
        }
    }

    /**
     * Removes one or more items from this menu.
     * The specified items are removed from both the internal collection and the inventory.
     * 
     * @param items the items to remove from this menu
     */
    public void removeItems(SlotItem... items) {
        List.of(items).forEach(this.items::remove);

        for (SlotItem item : items) {
            this.menu.setItem(item.itemSlot(), null);
        }
    }

    /**
     * Removes all items from this menu.
     * Both the internal collection and the underlying inventory are cleared.
     */
    public void clear() {
        this.items.clear();
        this.menu.clear();
    }

    /**
     * Returns the underlying Bukkit inventory for this menu.
     * This can be used to open the inventory for players or other Bukkit operations.
     * 
     * @return the Bukkit inventory managed by this menu
     */
    public Inventory getInventory() {
        return this.menu;
    }

    /**
     * Returns an unmodifiable view of all items currently in this menu.
     * The returned collection cannot be modified - use addItems(), removeItems(), or clear() instead.
     *
     * @return an unmodifiable set of SlotItem instances in this menu
     */
    public HashSet<SlotItem> getItems() {
        return (HashSet<SlotItem>) Collections.unmodifiableSet(this.items);
    }

    /**
     * Returns the SlotItem at the specified slot position.
     * This is useful for handling inventory click events.
     * 
     * @param slot the slot position to check (0-53 for chest inventories)
     * @return the SlotItem at the specified slot, or null if the slot is empty
     */
    public SlotItem getItemAt(int slot) {
        return this.items.stream()
                .filter(item -> item.itemSlot() == slot)
                .findFirst()
                .orElse(null);
    }

    /**
     * Creates a new MenuBuilder instance for constructing menus.
     * This is the recommended way to create new Menu instances.
     * 
     * @return a new MenuBuilder ready for configuration
     */
    @Contract(" -> new")
    public static @NotNull MenuBuilder newBuilder() {
        return new MenuBuilder();
    }

    /**
     * Builder class for creating Menu instances with a fluent API.
     * 
     * <p>This builder allows you to configure all aspects of a menu before creation:
     * inventory size, title, and initial items. The builder validates all parameters
     * and prevents common errors like invalid inventory sizes or slot conflicts.</p>
     * 
     * <p>Example usage:
     * <pre>{@code
     * Menu menu = new MenuBuilder()
     *     .size(27)
     *     .title(Component.text("My Menu"))
     *     .items(new SlotItem(Component.text("Diamond"), (short) 0, Material.DIAMOND, 1))
     *     .build();
     * }</pre></p>
     */
    public static class MenuBuilder {

        private int inventorySize;
        private Component inventoryTitle;
        private final HashSet<SlotItem> items = new HashSet<>();

        /**
         * Sets the size of the inventory.
         * Must be a positive multiple of 9 and cannot exceed 54.
         * Valid sizes: 9, 18, 27, 36, 45, 54
         * 
         * @param size the inventory size in slots
         * @return this builder instance for method chaining
         */
        public MenuBuilder size(int size) {
            this.inventorySize = size;
            return this;
        }

        /**
         * Sets the title of the inventory.
         * The title is displayed at the top of the inventory when opened.
         * 
         * @param title the inventory title component
         * @return this builder instance for method chaining
         */
        public MenuBuilder title(Component title) {
            this.inventoryTitle = title;
            return this;
        }

        /**
         * Adds initial items to the menu.
         * Each item must be placed in a unique slot that is not already occupied.
         * 
         * @param items the items to add to this menu
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if an item is null or its slot is already occupied
         */
        public MenuBuilder items(SlotItem @NotNull ... items) {
            for (SlotItem item : items) {
                if (item == null) {
                    throw new IllegalArgumentException("Item cannot be null");
                }
                if (item.itemSlot() < 0 || item.itemSlot() > 53) {
                    throw new IllegalArgumentException("Item slot must be between 0 and 53");
                }
                if (item.quantity() < 1 || item.quantity() > 64) {
                    throw new IllegalArgumentException("Item quantity must be between 1 and 64");
                }
                if (this.items.stream().anyMatch(existing -> existing.itemSlot() == item.itemSlot())) {
                    throw new IllegalArgumentException("Slot " + item.itemSlot() + " is already occupied");
                }
            }
            this.items.addAll(List.of(items));
            return this;
        }

        /**
         * Builds and returns a new Menu instance with the configured properties.
         * This method validates all parameters before creating the menu.
         * 
         * @return a new Menu instance
         * @throws IllegalArgumentException if inventory size is invalid or title is null
         */
        public Menu build() {
            if (inventorySize <= 0) {
                throw new IllegalArgumentException("Inventory size must be positive");
            }
            if (inventorySize % 9 != 0) {
                throw new IllegalArgumentException("Inventory size must be a multiple of 9");
            }
            if (inventorySize > 54) {
                throw new IllegalArgumentException("Inventory size cannot exceed 54");
            }
            if (inventoryTitle == null) {
                throw new IllegalArgumentException("Inventory title cannot be null");
            }
            return new Menu(this);
        }
    }
}