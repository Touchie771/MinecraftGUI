package me.touchie771.minecraftGUI.api.presets;

import me.touchie771.minecraftGUI.api.ClickHandler;
import me.touchie771.minecraftGUI.api.Menu;
import me.touchie771.minecraftGUI.api.SlotItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * A preset menu for confirmation dialogs.
 * Provides a simple "Confirm" vs "Cancel" choice.
 */
public class ConfirmationMenu {

    private static final int SIZE = 27;
    private static final int CONFIRM_SLOT = 11;
    private static final int CANCEL_SLOT = 15;

    /**
     * Creates a generic confirmation menu.
     *
     * @param plugin     The plugin instance
     * @param title      The title of the menu
     * @param onConfirm  The action to run when confirmed
     * @param onCancel   The action to run when canceled
     * @return A configured Menu instance
     */
    public static Menu create(@NotNull Plugin plugin, @NotNull Component title, @NotNull Runnable onConfirm, @NotNull Runnable onCancel) {
        SlotItem confirmItem = SlotItem.builder(CONFIRM_SLOT)
            .itemName(Component.text("Confirm", NamedTextColor.GREEN))
            .material(Material.LIME_WOOL)
            .build();

        SlotItem cancelItem = SlotItem.builder(CANCEL_SLOT)
            .itemName(Component.text("Cancel", NamedTextColor.RED))
            .material(Material.RED_WOOL)
            .build();

        ClickHandler confirmHandler = ClickHandler.newBuilder()
            .callback(event -> {
                event.getWhoClicked().closeInventory();
                onConfirm.run();
            })
            .autoCancel(true)
            .build();

        ClickHandler cancelHandler = ClickHandler.newBuilder()
            .callback(event -> {
                event.getWhoClicked().closeInventory();
                onCancel.run();
            })
            .autoCancel(true)
            .build();

        return Menu.newBuilder()
            .plugin(plugin)
            .size(SIZE)
            .title(title)
            .items(confirmItem, cancelItem)
            .onClick(CONFIRM_SLOT, confirmHandler)
            .onClick(CANCEL_SLOT, cancelHandler)
            .build();
    }
}