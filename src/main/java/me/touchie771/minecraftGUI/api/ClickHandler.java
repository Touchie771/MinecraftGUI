package me.touchie771.minecraftGUI.api;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.function.Consumer;

/**
 * Handles click events for menu slots with filtering and cancellation options.
 * 
 * <p>This class provides a way to register callbacks for inventory clicks with
 * fine-grained control over which click types are handled and whether the event
 * should be automatically cancelled.</p>
 * 
 * <p>Example usage:
 * <pre>{@code
 * ClickHandler handler = ClickHandler.newBuilder()
 *     .callback(event -> {
 *         Player player = (Player) event.getWhoClicked();
 *         player.sendMessage("You clicked the item!");
 *     })
 *     .filter(ClickType.LEFT, ClickType.RIGHT)
 *     .autoCancel(true)
 *     .build();
 * }</pre></p>
 */
public class ClickHandler {
    
    private final Consumer<InventoryClickEvent> callback;
    private final EnumSet<ClickType> allowedClickTypes;
    private final boolean autoCancel;
    
    /**
     * Creates a new ClickHandler with the specified configuration.
     * This constructor is package-private and should only be called by ClickHandlerBuilder.
     * 
     * @param builder the configured builder containing handler properties
     * @throws IllegalArgumentException if builder or callback is null
     */
    ClickHandler(@NotNull ClickHandlerBuilder builder) {
        if (builder.callback == null) {
            throw new IllegalArgumentException("Callback cannot be null");
        }
        this.callback = builder.callback;
        this.allowedClickTypes = builder.allowedClickTypes != null ? 
            EnumSet.copyOf(builder.allowedClickTypes) : EnumSet.allOf(ClickType.class);
        this.autoCancel = builder.autoCancel;
    }
    
    /**
     * Handles the given inventory click event if it matches the configured filters.
     *
     * @param event the inventory click event to handle
     */
    public void handleClick(@NotNull InventoryClickEvent event) {

        // Check if the click type is allowed
        if (!allowedClickTypes.contains(event.getClick())) {
            return;
        }
        
        // Auto-cancel if configured
        if (autoCancel) {
            event.setCancelled(true);
        }
        
        // Execute the callback
        try {
            callback.accept(event);
        } catch (Exception e) {
            // Log the exception but don't rethrow to avoid breaking other handlers
            System.err.println("Error in ClickHandler callback: " + e.getMessage());
        }

    }
    
    /**
     * Creates a new ClickHandlerBuilder instance for constructing click handlers.
     * This is the recommended way to create new ClickHandler instances.
     * 
     * @return a new ClickHandlerBuilder ready for configuration
     */
    @Contract(" -> new")
    public static @NotNull ClickHandlerBuilder newBuilder() {
        return new ClickHandlerBuilder();
    }
    
    /**
     * Builder class for creating ClickHandler instances with a fluent API.
     * 
     * <p>This builder allows you to configure all aspects of a click handler before creation:
     * the callback function, allowed click types, and auto-cancellation behavior.</p>
     */
    public static class ClickHandlerBuilder {
        
        private Consumer<InventoryClickEvent> callback;
        private EnumSet<ClickType> allowedClickTypes;
        private boolean autoCancel = true;
        
        /**
         * Sets the callback function to execute when a click event is handled.
         * 
         * @param callback the consumer that will receive the click event
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if callback is null
         */
        public ClickHandlerBuilder callback(@NotNull Consumer<InventoryClickEvent> callback) {
            this.callback = callback;
            return this;
        }
        
        /**
         * Sets which click types should be handled.
         * If not specified, all click types will be handled.
         * 
         * @param clickTypes the click types to handle
         * @return this builder instance for method chaining
         */
        public ClickHandlerBuilder filter(@NotNull ClickType... clickTypes) {
            if (clickTypes == null || clickTypes.length == 0) {
                this.allowedClickTypes = EnumSet.allOf(ClickType.class);
            } else {
                this.allowedClickTypes = EnumSet.copyOf(Arrays.asList(clickTypes));
            }
            return this;
        }
        
        /**
         * Sets whether the event should be automatically cancelled before executing the callback.
         * Default is true.
         * 
         * @param autoCancel true to auto-cancel events, false otherwise
         * @return this builder instance for method chaining
         */
        public ClickHandlerBuilder autoCancel(boolean autoCancel) {
            this.autoCancel = autoCancel;
            return this;
        }
        
        /**
         * Builds and returns a new ClickHandler instance with the configured properties.
         * 
         * @return a new ClickHandler instance
         * @throws IllegalArgumentException if callback is not set
         */
        public ClickHandler build() {
            return new ClickHandler(this);
        }
    }
}