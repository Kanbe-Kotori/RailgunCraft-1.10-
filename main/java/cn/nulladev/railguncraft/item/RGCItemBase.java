package cn.nulladev.railguncraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cn.nulladev.railguncraft.core.RailgunCraft;
import cn.nulladev.railguncraft.item.crafting.RGCAdvRecipe;

public abstract class RGCItemBase extends Item {
	
	private RGCAdvRecipe recipe;
	
	public RGCItemBase(String name) {
        super();
        this.setUnlocalizedName(name);
        this.setCreativeTab(RailgunCraft.CreativeTabs);
        GameRegistry.registerItem(this, name);
	}

	public RGCItemBase setRecipe(ItemStack[][] stack) {
		this.recipe = new RGCAdvRecipe(stack, new ItemStack(this, 1));
		return this;
	}
	
	public RGCItemBase setRecipe(RGCAdvRecipe r) {
		this.recipe = r;
		return this;
	}
	
}
