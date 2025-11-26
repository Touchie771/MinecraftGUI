package me.touchie771.minecraftGUI.api.presets;

import me.touchie771.minecraftGUI.api.ClickHandler;
import me.touchie771.minecraftGUI.api.Menu;
import me.touchie771.minecraftGUI.api.SlotItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A preset for a paginated menu.
 * Supports navigation between pages of items.
 */
public class PaginationMenu {

    private final Menu menu;
    private final List<PageItem> items;
    private int currentPage = 0;
    
    // Standard layout: 6 rows (54 slots). 
    // Slots 0-44 for content.
    // Slot 45: Previous, Slot 53: Next.
    private static final int SIZE = 54;
    private static final int ITEMS_PER_PAGE = 45;
    private static final int PREV_SLOT = 45;
    private static final int NEXT_SLOT = 53;

    /**
     * Creates a new PaginationMenu.
     *
     * @param plugin The plugin instance
     * @param title  The menu title
     * @param items  The list of items to display across pages
     */
    public PaginationMenu(@NotNull Plugin plugin, @NotNull Component title, @NotNull List<PageItem> items) {
        this.items = new ArrayList<>(items);
        
        this.menu = Menu.newBuilder()
            .plugin(plugin)
            .size(SIZE)
            .title(title)
            .build();
            
        update();
    }

    /**
     * Opens the menu for the specified player.
     *
     * @param player The player to open the menu for
     */
    public void open(@NotNull Player player) {
        player.openInventory(menu.getInventory());
    }

    private void update() {
        menu.clear();
        menu.clearHandlers();
        
        int totalPages = (int) Math.ceil((double) items.size() / ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1;
        
        if (currentPage < 0) currentPage = 0;
        if (currentPage >= totalPages) currentPage = totalPages - 1;

        int startIndex = currentPage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, items.size());

        for (int i = startIndex; i < endIndex; i++) {
            PageItem item = items.get(i);
            int slot = i - startIndex;
            
            menu.addItems(new SlotItem(item.name(), (short) slot, item.material(), item.quantity()));
            
            if (item.action() != null) {
                menu.onClick(slot, ClickHandler.newBuilder()
                    .callback(item.action())
                    .autoCancel(true)
                    .build());
            }
        }

        if (currentPage > 0) {
            menu.addItems(new SlotItem(
                Component.text("Previous Page", NamedTextColor.YELLOW),
                (short) PREV_SLOT,
                Material.ARROW,
                1
            ));
            menu.onClick(PREV_SLOT, ClickHandler.newBuilder()
                .callback(e -> {
                    currentPage--;
                    update();
                })
                .autoCancel(true)
                .build());
        }

        if (currentPage < totalPages - 1) {
             menu.addItems(new SlotItem(
                Component.text("Next Page", NamedTextColor.YELLOW),
                (short) NEXT_SLOT,
                Material.ARROW,
                1
            ));
            menu.onClick(NEXT_SLOT, ClickHandler.newBuilder()
                .callback(e -> {
                    currentPage++;
                    update();
                })
                .autoCancel(true)
                .build());
        }
    }

    /**
     * Represents an item in a paginated menu.
     *
     * @param name     The display name
     * @param material The material
     * @param quantity The quantity
     * @param action   The action to perform when clicked (optional)
     */
    public record PageItem(@NotNull Component name, @NotNull Material material, int quantity, @Nullable Consumer<InventoryClickEvent> action) {}
}