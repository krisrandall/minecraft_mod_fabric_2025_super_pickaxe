package com.example;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.util.math.random.Random;

public class ClamShellWorldGen {
    
    public static void registerWorldGen() {
        // For 1.21.8, world generation has changed significantly
        // We'll implement a simpler approach for now
        // You can manually place clam shells or add them via data packs
    }
    
    // Simple feature class for placing clam shells
    public static class ClamShellFeature extends Feature<DefaultFeatureConfig> {
        public ClamShellFeature() {
            super(DefaultFeatureConfig.CODEC);
        }
        
        @Override
        public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
            StructureWorldAccess world = context.getWorld();
            BlockPos pos = context.getOrigin();
            Random random = context.getRandom();
            
            // Try to find a suitable ocean floor position
            BlockPos.Mutable mutable = pos.mutableCopy();
            
            // Search downward for ocean floor
            for (int y = pos.getY(); y > world.getBottomY(); y--) {
                mutable.setY(y);
                
                // Check if we found the ocean floor
                BlockState currentBlock = world.getBlockState(mutable);
                if (!currentBlock.isOf(Blocks.WATER) && 
                    !currentBlock.isOf(Blocks.SEAGRASS) &&
                    !currentBlock.isOf(Blocks.TALL_SEAGRASS)) {
                    
                    // Check if the block above is water
                    BlockPos abovePos = mutable.up();
                    if (world.getBlockState(abovePos).isOf(Blocks.WATER)) {
                        // Valid ocean floor found - place the clam
                        BlockState clamState = ExampleMod.CLAM_SHELL_BLOCK.getDefaultState()
                            .with(ClamShellBlock.FACING, Direction.fromHorizontal(random.nextInt(4)))
                            .with(ClamShellBlock.OPEN, false);
                        
                        world.setBlockState(abovePos, clamState, 3);
                        return true;
                    }
                    break;
                }
            }
            
            return false;
        }
    }
}