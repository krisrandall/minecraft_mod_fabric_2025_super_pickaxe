package com.example.client;

import com.example.ClamShellBlockEntity;
import com.example.ExampleMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register the screen for the clam shell
        HandledScreens.register(ExampleMod.CLAM_SHELL_SCREEN_HANDLER, ClamShellScreen::new);
    }
}