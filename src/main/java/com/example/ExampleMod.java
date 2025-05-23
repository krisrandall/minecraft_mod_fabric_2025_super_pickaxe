package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
    public static final String MOD_ID = "modid";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // Define your super pickaxe (matching your existing resources)
    public static final Item SUPER_PICKAXE = register("super_pickaxe", Item::new, new Item.Settings());

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        LOGGER.info("Joschi & Kris's mod is working!");
        LOGGER.info("Next step: Add custom items!");
        
        // Add the item to the creative inventory (Tools tab)
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(SUPER_PICKAXE);
        });
        
        LOGGER.info("Super pickaxe registered!");
        LOGGER.info("{} has been initialized!", MOD_ID);
    }
    
    // Helper method to register items (based on Fabric docs for 1.21.5)
    public static Item register(String name, java.util.function.Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, name));
        // Create the item instance with the registry key
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        // Register the item
        return Registry.register(Registries.ITEM, itemKey, item);
    }
}