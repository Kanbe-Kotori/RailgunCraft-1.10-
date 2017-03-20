package cn.nulladev.railguncraft.block.container;

import cn.nulladev.railguncraft.block.tileentity.TileEntityAdvCraftingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerAdvCraftingTable extends Container {
	
	private TileEntityAdvCraftingTable tileentity;
	
	private int lastBuffer = 0;
	
    public ContainerAdvCraftingTable(InventoryPlayer player, TileEntityAdvCraftingTable t) {
    	tileentity = t;
    	for (int i = 0; i < 25; i++)
    		this.addSlotToContainer(new Slot(t, i, 8 + 18 * (i % 5), 5 + 18 * (i / 5)));
    	
		this.addSlotToContainer(new SlotInvalid(t, 25, 134, 41));
		
		this.addSlotToContainer(new SlotCharge(t, 26, 134, 77, 1));
		
		for (int i = 0; i < 9; i++)
            this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 155));

        for (int i = 0; i < 27; i++)
        	this.addSlotToContainer(new Slot(player, 9 + i, 8 + 18 * (i % 9), 97 + 18 * (i / 9)));

    }

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
 
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			this.tileentity.currentBuffer = par2;
		}
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (IContainerListener i : this.listeners) {
            i.sendProgressBarUpdate(this, 0, this.tileentity.currentBuffer);
        }
		
		this.lastBuffer = this.tileentity.currentBuffer;
	}
	
	@Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		if (slotId != 25) {
			return super.slotClick(slotId, dragType, clickTypeIn, player);
		} else {
			if (this.tileentity.currentBuffer < 10000) {
				return null;
			}
			if (this.tileentity.getStackInSlot(25) == null) {
				return null;
			}
			
			player.inventory.addItemStackToInventory(this.tileentity.getStackInSlot(25));
			for (int i = 0; i<25; i++) {
				if (this.tileentity.getStackInSlot(i) == null) {
					continue;
				} else {
					if (this.tileentity.getStackInSlot(i).stackSize > 1) {
						ItemStack s = this.tileentity.getStackInSlot(i).copy();
						s.stackSize --;
						this.tileentity.setInventorySlotContents(i, s);
					} else {
						this.tileentity.setInventorySlotContents(i, null);
					}		
				}
			}
			this.tileentity.currentBuffer = 0;
			return null;
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int num) {
		ItemStack stack = null;
		Slot slot = this.inventorySlots.get(num);
		if (slot != null && slot.getHasStack()) {
			ItemStack var5 = slot.getStack();
			stack = var5.copy();
			if (num >= 0 && num <= 24 || num == 26) {
				if (!this.mergeItemStack(var5, 27, 54, false)) {
					return null;
				}
				slot.onSlotChange(var5, stack);
			}
			else if (num >= 27 && num < 54) {
				if (!this.mergeItemStack(var5, 54, 63, false)) {
					return null;
				}
			}
			else if (num >= 54 && num < 63) {
				if (!this.mergeItemStack(var5, 27, 54, false)) {
					return null;
				}
			} else {
				return null;
			}
			if (var5.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
			if (var5.stackSize == stack.stackSize) {
				return null;
			}
			slot.onPickupFromSlot(player, var5);
		}
		return stack;
	}

}
