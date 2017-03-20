package cn.nulladev.railguncraft.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class RGCCreativeTabs extends CreativeTabs{

	public RGCCreativeTabs() {
		super("RailgunCraft");
	}

	@Override
	public Item getTabIconItem() {
		return Registerer.railgun;
	}

}
