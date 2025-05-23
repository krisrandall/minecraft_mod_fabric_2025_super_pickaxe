package com.example;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
    public static final String MOD_ID = "modid";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // Create our super pickaxe tool material using the new record constructor
    public static final ToolMaterial SUPER_TOOL_MATERIAL = new ToolMaterial(
        BlockTags.INCORRECT_FOR_NETHERITE_TOOL, // What blocks this tool can mine
        100000,    // Durability - 100x more than diamond
        80.0f,     // Mining speed - 10x faster than diamond
        50.0f,     // Attack damage - much stronger
        25,        // Enchantability - highly enchantable
        ItemTags.NETHERITE_TOOL_MATERIALS // Repair ingredient tag
    );

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        
        // Create the super pickaxe item inside the initialize method
        PickaxeItem superPickaxe = new PickaxeItem(
            SUPER_TOOL_MATERIAL, 
            1, // Attack damage bonus (total will be 51)
            -2.8f, // Attack speed (same as diamond pickaxe)
            new Item.Settings()
        );
        
        // Register our super pickaxe using the new Identifier.of method
        Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "super_pickaxe"),
            superPickaxe
        );
        
        LOGGER.info("Super Pickaxe registered!");
    }
}