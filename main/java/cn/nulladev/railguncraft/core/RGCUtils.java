package cn.nulladev.railguncraft.core;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
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
	
	public static boolean remove_a_stack(EntityPlayer player, ItemStack stack) {
		for (ItemStack s : player.inventory.mainInventory) {
			if (s != null && s.isItemEqual(stack)) {
				if (s.stackSize > stack.stackSize) {
					s.stackSize -= stack.stackSize;
					return true;
				} else if (s.stackSize == stack.stackSize) {
					player.inventory.deleteStack(s);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
    }
	
	public static boolean isCtrlKeyDown() {
        return Minecraft.IS_RUNNING_ON_MAC ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }

}
