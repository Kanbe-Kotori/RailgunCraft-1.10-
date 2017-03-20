package cn.nulladev.railguncraft.item.crafting;

import ic2.api.item.IC2Items;

import java.util.ArrayList;
import net.minecraft.item.ItemStack;

public class RGCAdvRecipe {
	
	private static ArrayList<RGCAdvRecipe> recipes = new ArrayList<RGCAdvRecipe>();
	
	public final ItemStack[][] m_reactants;
	public final ItemStack m_product;
	
	public RGCAdvRecipe(ItemStack[][] reactants, ItemStack product) {
		this.m_reactants = reactants;
		this.m_product = product;
		recipes.add(this);
	}
	
	public static ArrayList<RGCAdvRecipe> getAllRecipes() {
		return recipes;
	}
	
	
	public static boolean isEqual(ItemStack[][] reactants, RGCAdvRecipe r) {
		ItemStack[][] ri = r.m_reactants;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (reactants[i][j] == null && ri[i][j] == null) {
					continue;
				} else if (reactants[i][j] == null || ri[i][j] == null) {
					return false;
				} else {
					if (reactants[i][j].isItemEqual(ri[i][j])) {
						continue;
					} else if (reactants[i][j].getItem() == ri[i][j].getItem() && ri[i][j].getItem()== IC2Items.getItem("energy_crystal").getItem()) {
						continue;
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}

}
