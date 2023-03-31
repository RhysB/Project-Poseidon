package net.minecraft.server;

public class ContainerFurnace extends Container {

    private TileEntityFurnace a;
    private int b = 0;
    private int c = 0;
    private int h = 0;

    public ContainerFurnace(InventoryPlayer inventoryplayer, TileEntityFurnace tileentityfurnace) {
        this.a = tileentityfurnace;
        this.addSlot(new Slot(tileentityfurnace, 0, 56, 17));
        this.addSlot(new Slot(tileentityfurnace, 1, 56, 53));
        this.addSlot(new SlotResult2(inventoryplayer.d, tileentityfurnace, 2, 116, 35));

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventoryplayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventoryplayer, i, 8 + i * 18, 142));
        }
    }

    public void addListener(ICrafting iCrafting) {
        super.addListener(iCrafting);
        iCrafting.a(this, 0, this.a.cookTime);
        iCrafting.a(this, 1, this.a.burnTime);
        iCrafting.a(this, 2, this.a.ticksForCurrentFuel);
    }

    public void updateCraftingMatrix() {
        super.updateCraftingMatrix();

        for (int i = 0; i < this.listeners.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.listeners.get(i);

            if (this.b != this.a.cookTime) {
                icrafting.a(this, 0, this.a.cookTime);
            }

            if (this.c != this.a.burnTime) {
                icrafting.a(this, 1, this.a.burnTime);
            }

            if (this.h != this.a.ticksForCurrentFuel) {
                icrafting.a(this, 2, this.a.ticksForCurrentFuel);
            }
        }

        this.b = this.a.cookTime;
        this.c = this.a.burnTime;
        this.h = this.a.ticksForCurrentFuel;
    }

    public boolean canInteractWith(EntityHuman entityhuman) {
        return this.a.a_(entityhuman);
    }

    public ItemStack a(int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.e.get(i);

        if (slot != null && slot.b()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 2) {
                this.a(itemstack1, 3, 39, true);
            } else if (i >= 3 && i < 30) {
                this.a(itemstack1, 30, 39, false);
            } else if (i >= 30 && i < 39) {
                this.a(itemstack1, 3, 30, false);
            } else {
                this.a(itemstack1, 3, 39, false);
            }

            if (itemstack1.count == 0) {
                slot.c((ItemStack) null);
            } else {
                slot.c();
            }

            if (itemstack1.count == itemstack.count) {
                return null;
            }

            slot.a(itemstack1);
        }

        return itemstack;
    }
}
