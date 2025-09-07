package com.example;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ClamShellBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private static final int INVENTORY_SIZE = 1;
    
    public ClamShellBlockEntity(BlockPos pos, BlockState state) {
        super(ExampleMod.CLAM_SHELL_BLOCK_ENTITY, pos, state);
        // Initialize with a pearl when first created
        if (this.inventory.get(0).isEmpty()) {
            this.inventory.set(0, new ItemStack(ExampleMod.PEARL_ITEM, 1));
        }
    }
    
    @Override
    public Text getDisplayName() {
        return Text.translatable("block.modid.clam_shell");
    }
    
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ClamShellScreenHandler(syncId, playerInventory, this);
    }
    
    // Inventory implementation
    @Override
    public int size() {
        return INVENTORY_SIZE;
    }
    
    @Override
    public boolean isEmpty() {
        return this.inventory.stream().allMatch(ItemStack::isEmpty);
    }
    
    @Override
    public ItemStack getStack(int slot) {
        return slot >= 0 && slot < this.inventory.size() ? this.inventory.get(slot) : ItemStack.EMPTY;
    }
    
    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(this.inventory, slot, amount);
        if (!result.isEmpty()) {
            this.markDirty();
        }
        return result;
    }
    
    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }
    
    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.inventory.size()) {
            this.inventory.set(slot, stack);
            if (stack.getCount() > this.getMaxCountPerStack()) {
                stack.setCount(this.getMaxCountPerStack());
            }
            this.markDirty();
        }
    }
    
    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        }
        return player.squaredDistanceTo((double)this.pos.getX() + 0.5, 
                                        (double)this.pos.getY() + 0.5, 
                                        (double)this.pos.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public void clear() {
        this.inventory.clear();
        this.markDirty();
    }
    
    // NBT serialization
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
    }
    
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
    }
    
    // Drop contents when broken
    public void dropContents(World world, BlockPos pos) {
        Inventories.dropContents(world, pos, this.inventory);
    }
    
    // Custom ScreenHandler for 1-slot interface
    public static class ClamShellScreenHandler extends ScreenHandler {
        private final Inventory inventory;
        
        // Client constructor
        public ClamShellScreenHandler(int syncId, PlayerInventory playerInventory) {
            this(syncId, playerInventory, new SimpleInventory(1));
        }
        
        // Server constructor
        public ClamShellScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
            super(ExampleMod.CLAM_SHELL_SCREEN_HANDLER, syncId);
            this.inventory = inventory;
            inventory.onOpen(playerInventory.player);
            
            // Add clam shell slot (centered, single slot)
            this.addSlot(new Slot(inventory, 0, 80, 35));
            
            // Add player inventory slots
            int playerInvY = 84;
            // Player inventory
            for (int row = 0; row < 3; ++row) {
                for (int col = 0; col < 9; ++col) {
                    this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, playerInvY + row * 18));
                }
            }
            // Player hotbar
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col, 8 + col * 18, playerInvY + 58));
            }
        }
        
        @Override
        public boolean canUse(PlayerEntity player) {
            return this.inventory.canPlayerUse(player);
        }
        
        @Override
        public ItemStack transferSlot(PlayerEntity player, int index) {
            ItemStack newStack = ItemStack.EMPTY;
            Slot slot = this.slots.get(index);
            
            if (slot != null && slot.hasStack()) {
                ItemStack originalStack = slot.getStack();
                newStack = originalStack.copy();
                
                if (index < 1) {
                    // Transfer from clam to player inventory
                    if (!this.insertItem(originalStack, 1, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // Transfer from player inventory to clam
                    if (!this.insertItem(originalStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                
                if (originalStack.isEmpty()) {
                    slot.setStack(ItemStack.EMPTY);
                } else {
                    slot.markDirty();
                }
            }
            
            return newStack;
        }
        
        @Override
        public void close(PlayerEntity player) {
            super.close(player);
            this.inventory.onClose(player);
        }
    }
    
    // Helper class for client-side inventory
    public static class SimpleInventory implements Inventory {
        private final DefaultedList<ItemStack> items;
        
        public SimpleInventory(int size) {
            this.items = DefaultedList.ofSize(size, ItemStack.EMPTY);
        }
        
        @Override
        public int size() {
            return items.size();
        }
        
        @Override
        public boolean isEmpty() {
            return items.stream().allMatch(ItemStack::isEmpty);
        }
        
        @Override
        public ItemStack getStack(int slot) {
            return items.get(slot);
        }
        
        @Override
        public ItemStack removeStack(int slot, int amount) {
            return Inventories.splitStack(items, slot, amount);
        }
        
        @Override
        public ItemStack removeStack(int slot) {
            return Inventories.removeStack(items, slot);
        }
        
        @Override
        public void setStack(int slot, ItemStack stack) {
            items.set(slot, stack);
        }
        
        @Override
        public void markDirty() {}
        
        @Override
        public boolean canPlayerUse(PlayerEntity player) {
            return true;
        }
        
        @Override
        public void clear() {
            items.clear();
        }
    }
}