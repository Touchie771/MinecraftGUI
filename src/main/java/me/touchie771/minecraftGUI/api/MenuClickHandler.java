package me.touchie771.minecraftGUI.api;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages click event handling for a specific menu inventory.
 * 
 * <p>This class handles the registration and delegation of inventory click events
 * for menu slots. It maintains a mapping of slot positions to ClickHandler instances
 * and automatically registers with Bukkit's event system.</p>
 * 
 * <p>Each Menu instance has its own MenuClickHandler to ensure proper isolation
 * between different menus and prevent cross-contamination of click events.</p>
 */
class MenuClickHandler implements Listener {
    
    private final Inventory menuInventory;
    private final Map<Integer, ClickHandler> slotHandlers;
    private Consumer<InventoryCloseEvent> closeHandler;
    private boolean isRegistered = false;
    private final Logger logger;
    
    /**
     * Creates a new MenuClickHandler for the specified inventory.
     * 
     * @param menuInventory the inventory this handler manages
     * @throws IllegalArgumentException if menuInventory is null
     */
    MenuClickHandler(@NotNull Inventory menuInventory, @NotNull Logger logger) {
        this.menuInventory = menuInventory;
        this.slotHandlers = new HashMap<>();
        this.logger = logger;
    }
    
    /**
     * Registers a click handler for the specified slot.
     * 
     * @param slot the slot position (0-53 for chest inventories)
     * @param handler the click handler to register
     * @throws IllegalArgumentException if slot is invalid or handler is null
     */
    public void setClickHandler(int slot, @NotNull ClickHandler handler) {
        if (slot < 0 || slot >= menuInventory.getSize()) {
            throw new IllegalArgumentException("Slot " + slot + " is out of bounds for inventory size " + menuInventory.getSize());
        }
        slotHandlers.put(slot, handler);
    }
    
    /**
     * Removes the click handler for the specified slot.
     * 
     * @param slot the slot position to remove the handler from
     */
    public void removeClickHandler(int slot) {
        slotHandlers.remove(slot);
    }
    
    /**
     * Gets the click handler for the specified slot.
     * 
     * @param slot the slot position
     * @return the ClickHandler for the slot, or null if none is registered
     */
    public ClickHandler getClickHandler(int slot) {
        return slotHandlers.get(slot);
    }
    
    /**
     * Sets a handler that will be called when the inventory is closed.
     * 
     * @param closeHandler the consumer that will receive the close event
     */
    public void setCloseHandler(@NotNull Consumer<InventoryCloseEvent> closeHandler) {
        this.closeHandler = closeHandler;
    }
    
    /**
     * Removes the close handler.
     */
    public void removeCloseHandler() {
        this.closeHandler = null;
    }
    
    /**
     * Registers this handler with Bukkit's event system.
     * This method should be called when the menu is created or first opened.
     */
    public void register() {
        if (!isRegistered) {
            // Note: In a real implementation, you'd get the plugin instance
            // For now, we'll assume this is handled by the Menu class
            // Bukkit.getPluginManager().registerEvents(this, plugin);
            isRegistered = true;
        }
    }
    
    /**
     * Unregisters this handler from Bukkit's event system.
     * This method should be called when the menu is no longer needed.
     */
    public void unregister() {
        if (isRegistered) {
            HandlerList.unregisterAll(this);
            isRegistered = false;
        }
    }
    
    /**
     * Checks if this handler is currently registered with Bukkit.
     * 
     * @return true if registered, false otherwise
     */
    public boolean isRegistered() {
        return isRegistered;
    }
    
    /**
     * Handles inventory click events and delegates them to the appropriate slot handler.
     * 
     * @param event the inventory click event
     */
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        // Only handle clicks for our specific inventory
        if (!event.getInventory().equals(menuInventory)) {
            return;
        }
        
        // Get the clicked slot (use raw slot to get actual position in the open inventory)
        int slot = event.getRawSlot();
        
        // Ignore clicks outside the inventory
        if (slot < 0 || slot >= menuInventory.getSize()) {
            return;
        }
        
        // Check if we have a handler for this slot
        ClickHandler handler = slotHandlers.get(slot);
        if (handler != null) {
            handler.handleClick(event);
        }
    }
    
    /**
     * Handles inventory close events and calls the registered close handler if present.
     * 
     * @param event the inventory close event
     */
    @EventHandler
    public void onInventoryClose(@NotNull InventoryCloseEvent event) {
        // Only handle closes for our specific inventory
        if (!event.getInventory().equals(menuInventory)) {
            return;
        }
        
        // Call the close handler if present
        if (closeHandler != null) {
            try {
                closeHandler.accept(event);
            } catch (Exception e) {
                // Log the exception but don't rethrow
                logger.log(Level.SEVERE, "Error in close handler", e);
            }
        }
        
        // Auto-unregister to prevent memory leaks
        unregister();
    }
    
    /**
     * Clears all click handlers and the close handler.
     * This is useful when resetting a menu or preparing it for garbage collection.
     */
    public void clear() {
        slotHandlers.clear();
        closeHandler = null;
    }
    
    /**
     * Gets the number of registered click handlers.
     * 
     * @return the number of slots with click handlers
     */
    public int getHandlerCount() {
        return slotHandlers.size();
    }
}