package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
    public static final String MOD_ID = "modid";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // Custom super pickaxe class - trying to add mining speed
    public static class SuperPickaxeItem extends Item {
        public SuperPickaxeItem(Settings settings) {
            super(settings);
        }
        
        // Let's try the most common method name for mining speed
        @Override
        public float getMiningSpeed(net.minecraft.item.ItemStack stack, net.minecraft.block.BlockState state) {
            return 1000.0f; // Super fast - should break instantly
        }

        @Override
        public boolean isCorrectForDrops(net.minecraft.item.ItemStack stack, net.minecraft.block.BlockState state) {
            return true; // Can harvest ALL blocks
        }
        @Override
        public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
            return true; // Can be enchanted
        }

    }

    public static class Pearl extends Item {
        public Pearl(Settings settings) {
            super(settings);
        }
    }


    // Now use our custom class instead of basic Item
    public static final Item SUPER_PICKAXE = register("super_pickaxe", SuperPickaxeItem::new, 
        new Item.Settings()
            .maxDamage(156100) // 100x diamond durability (diamond = 1561)
    );

    public static final Item PEARL = register("pearl", Pearl::new,
        new Item.Settings()     
    );

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        LOGGER.info("Joschi & Kris's mod is working!");
        LOGGER.info("Next step: Add custom items!");
        
        // Add the item to the creative inventory (Tools tab)
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(SUPER_PICKAXE);
        });
        
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(PEARL);
        });
        
        LOGGER.info("Super pickaxe registered!");
        LOGGER.info("{} has been initialized!", MOD_ID);
    }
    
    // Helper method to register items (based on your working code)
    public static Item register(String name, java.util.function.Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, name));
        // Create the item instance with the registry key
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        // Register the item
        return Registry.register(Registries.ITEM, itemKey, item);
    }
}