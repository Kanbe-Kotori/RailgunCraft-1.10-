package cn.nulladev.railguncraft.core;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RGCUtils {
	
	public static NBTTagCompound get_or_create_nbt(ItemStack stack) {
		if (stack.getTagCompound() != null) {
			return stack.getTagCompound();
		} else {
			NBTTagCompound nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
			return nbt;
		}
	}

}
