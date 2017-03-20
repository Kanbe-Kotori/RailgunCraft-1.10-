package cn.nulladev.railguncraft.block.container;

import ic2.api.item.IElectricItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCharge extends Slot {

	public final int mEnergyTier;
	
	public SlotCharge(IInventory p_i1824_1_, int index, int posX, int posY, int tier) {
		super(p_i1824_1_, index, posX, posY);
		this.mEnergyTier = tier;
	}
	
	@Override
    public boolean isItemValid(ItemStack stack) {
    	if (stack == null)
    		return false;
    	if (stack.getItem() instanceof IElectricItem) {
    		IElectricItem item = (IElectricItem)(stack.getItem());
    		if (item.getTier(stack) >= mEnergyTier && item.canProvideEnergy(stack))
    			return true;
    		else
    			return false;
    	} else {
			return false;  
    	}
    }

}
