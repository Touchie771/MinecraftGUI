# MinecraftGUI

A lightweight Java library for creating GUI menus in Minecraft Paper plugins with a fluent builder pattern. This is a personal project by Touchie771, but it's open for anyone to use and contribute to.

## Features

- **Fluent Builder Pattern**: Create menus with an intuitive, chainable API
- **Type Safety**: Full compile-time validation and null safety with JetBrains annotations
- **Adventure API Integration**: Modern text component support for rich formatting
- **Immutable Design**: Menus are immutable once created, preventing accidental modifications
- **Comprehensive Validation**: Built-in validation for inventory sizes, slots, and item quantities

## Quick Start

### Adding to Your Project

Add this library as a dependency in your Paper plugin's `build.gradle`:

```gradle
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/Touchie771/MinecraftGUI")
        credentials {
            username = <your-username>
            password = <your-PAT>
        }
    }
}

dependencies {
    compileOnly("me.touchie771:minecraftgui:1.1.0")
}
```

### Basic Usage

```java
import me.touchie771.minecraftGUI.api.Menu;
import me.touchie771.minecraftGUI.api.SlotItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class ExamplePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Create a simple menu
        Menu menu = Menu.newBuilder()
            .size(27)
            .title(Component.text("Example Menu"))
            .items(
                new SlotItem(
                    Component.text("Diamond Sword"),
                    13,  // Center slot
                    Material.DIAMOND_SWORD,
                    1
                )
            )
            .build();
        
        // Open the menu for a player
        player.openInventory(menu.getInventory());
    }
}
```

### Advanced Usage

```java
// Create a menu with multiple items
Menu complexMenu = Menu.newBuilder()
    .size(54)
    .title(Component.text("Complex Menu"))
    .items(
        new SlotItem(Component.text("Shop"), 10, Material.CHEST, 1),
        new SlotItem(Component.text("Settings"), 13, Material.REDSTONE, 1),
        new SlotItem(Component.text("Info"), 16, Material.BOOK, 1)
    )
    .build();

// Modify menu after creation
complexMenu.addItems(
    new SlotItem(Component.text("New Item"), 22, Material.EMERALD, 1)
);

// Get item at specific slot (useful for click events)
SlotItem clickedItem = complexMenu.getItemAt(slot);
if (clickedItem != null) {
    // Handle click logic
}

// Remove items
complexMenu.removeItems(clickedItem);

// Clear entire menu
complexMenu.clear();
```

## API Reference

### Menu Class

The main class for creating and managing GUI menus.

#### Builder Methods

- `size(int size)` - Set inventory size (must be multiple of 9, max 54)
- `title(Component title)` - Set the menu title
- `items(SlotItem... items)` - Add initial items
- `build()` - Create the menu instance

#### Instance Methods

- `addItems(SlotItem... items)` - Add items to existing menu
- `removeItems(SlotItem... items)` - Remove items from menu
- `clear()` - Remove all items
- `getInventory()` - Get the Bukkit Inventory instance
- `getItems()` - Get unmodifiable set of all items
- `getItemAt(int slot)` - Get item at specific slot

### SlotItem Record

Represents an item in a menu slot.

**Parameters:**
- `Component itemName` - Display name of the item
- `int itemSlot` - Slot position (0-53 for chest inventories)
- `Material material` - Minecraft material type
- `int quantity` - Stack size (1-64)

## Requirements

- **Java**: 21 or higher
- **Minecraft**: 1.21+ (Paper server)
- **Gradle**: 7.0+ for building

## Building

```bash
./gradlew build
```

The built JAR will be located in `build/libs/`.

## Testing

```bash
./gradlew test
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

For issues and questions, please use the GitHub Issues page.