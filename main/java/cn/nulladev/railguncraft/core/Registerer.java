package cn.nulladev.railguncraft.core;

import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.oredict.OreDictionary;
import cn.nulladev.railguncraft.block.BlockAdvCraftingTable;
import cn.nulladev.railguncraft.client.renderer.RendererEMPShield;
import cn.nulladev.railguncraft.client.renderer.RendererRailgun;
import cn.nulladev.railguncraft.entity.EntityEMPShield;
import cn.nulladev.railguncraft.entity.EntityRailgun;
import cn.nulladev.railguncraft.event.DamageRecalc;
import cn.nulladev.railguncraft.item.ItemEMPShield;
import cn.nulladev.railguncraft.item.ItemElectromagnet;
import cn.nulladev.railguncraft.item.ItemNanoClaw;
import cn.nulladev.railguncraft.item.ItemRailgun;
import cn.nulladev.railguncraft.item.RGCItemBase;

public class Registerer {
	
	public static RGCItemBase electromagnet;
	public static RGCItemBase electromagnet_adv;
	public static RGCItemBase EMP_shield;
	public static RGCItemBase nano_claw;
	public static RGCItemBase railgun;
	public static RGCItemBase railgun_adv;
	
	public static Block adv_crafting_table;
	
	public static void initAll(Object mod) {
		registerItems();
		registerBlocks();
		registerEntities(mod);
		registerRecipes();
		registerEvents();
	}
	
	private static void registerItems() {
		electromagnet = new ItemElectromagnet(false);
		electromagnet_adv = new ItemElectromagnet(true);
		EMP_shield = new ItemEMPShield();
		nano_claw = new ItemNanoClaw();
		railgun = new ItemRailgun(false);
		railgun_adv = new ItemRailgun(true);
	}
	
	private static void registerBlocks() {
		adv_crafting_table = new BlockAdvCraftingTable();
	}
	
	private static void registerEntities(Object mod) {
		EntityRegistry.registerModEntity(EntityRailgun.class, "Railgun", 1, mod, 128, 1, true);
		RenderingRegistry.registerEntityRenderingHandler(EntityRailgun.class, new IRenderFactory() {
			public Render<EntityRailgun> createRenderFor(RenderManager manager) {
				return new RendererRailgun(manager);
			}
		});
		//RenderingRegistry.registerEntityRenderingHandler(EntityRailgun.class, new RendererRailgun());
		
		EntityRegistry.registerModEntity(EntityEMPShield.class, "EMPShield", 2, mod, 128, 1, true);
		RenderingRegistry.registerEntityRenderingHandler(EntityEMPShield.class, new IRenderFactory() {
			public Render<EntityEMPShield> createRenderFor(RenderManager manager) {
				return new RendererEMPShield(manager);
			}
		});
		//RenderingRegistry.registerEntityRenderingHandler(EntityEMPShield.class, new RendererEMPShield());
	}
	
	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(electromagnet, 0, new ModelResourceLocation("railguncraft:electromagnet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(electromagnet_adv, 0, new ModelResourceLocation("railguncraft:electromagnet_adv", "inventory"));
		ModelLoader.setCustomModelResourceLocation(railgun, 0, new ModelResourceLocation("railguncraft:railgun", "inventory"));
		ModelLoader.setCustomModelResourceLocation(railgun_adv, 0, new ModelResourceLocation("railguncraft:railgun_adv", "inventory"));

		
		ModelLoader.setCustomMeshDefinition(EMP_shield, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				String name = ItemNanoClaw.isActive(stack)? "EMP_shield_active" : "EMP_shield";
				return new ModelResourceLocation("railguncraft:" + name, "inventory");
			}
	    });
		ModelBakery.registerItemVariants(EMP_shield, new ModelResourceLocation("railguncraft:EMP_shield", "inventory"));
	    ModelBakery.registerItemVariants(EMP_shield, new ModelResourceLocation("railguncraft:EMP_shield_active", "inventory"));
		
		ModelLoader.setCustomMeshDefinition(nano_claw, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				String name = ItemNanoClaw.isActive(stack)? "nano_claw_active" : "nano_claw";
				return new ModelResourceLocation("railguncraft:" + name, "inventory");
			}
	    });
	    ModelBakery.registerItemVariants(nano_claw, new ModelResourceLocation("railguncraft:nano_claw", "inventory"));
	    ModelBakery.registerItemVariants(nano_claw, new ModelResourceLocation("railguncraft:nano_claw_active", "inventory"));
		
        Item item_adv_crafting_table = Item.getItemFromBlock(adv_crafting_table);
        ModelLoader.setCustomModelResourceLocation(item_adv_crafting_table, 0, new ModelResourceLocation("railguncraft:adv_crafting_table", "inventory"));
	}
	
	private static void registerRecipes() {
		//------------electromagnet------------
		ItemStack irsh = IC2Items.getItem("crafting", "iron_shaft").copy();
		ItemStack cpcb = IC2Items.getItem("cable", "type:copper,insulation:1").copy();
		ItemStack[][] reactants_electromagnet = {
				{cpcb, cpcb, cpcb, cpcb, cpcb},
				{cpcb, cpcb, cpcb, cpcb, cpcb},
				{cpcb, cpcb, irsh, cpcb, cpcb},
				{cpcb, cpcb, cpcb, cpcb, cpcb},
				{cpcb, cpcb, cpcb, cpcb, cpcb}
		};
		electromagnet.setRecipe(reactants_electromagnet);
		//------------electromagnet end------------
		
		//------------electromagnet_adv------------
		ItemStack stsh = IC2Items.getItem("crafting", "steel_shaft").copy();
		ItemStack cpgd = IC2Items.getItem("cable", "type:gold,insulation:2").copy();
		ItemStack[][] reactants_electromagnet_adv = {
				{cpgd, cpgd, cpgd, cpgd, cpgd},
				{cpgd, cpgd, cpgd, cpgd, cpgd},
				{cpgd, cpgd, stsh, cpgd, cpgd},
				{cpgd, cpgd, cpgd, cpgd, cpgd},
				{cpgd, cpgd, cpgd, cpgd, cpgd}
		};
		electromagnet.setRecipe(reactants_electromagnet_adv);
		//------------electromagnet_adv end------------
		
		//------------railgun------------
		ItemStack encr = new ItemStack(IC2Items.getItem("energy_crystal").getItem(), 1, OreDictionary.WILDCARD_VALUE);
		ItemStack csir = IC2Items.getItem("casing", "iron").copy();
		ItemStack pllp = IC2Items.getItem("plate", "lapis").copy();
		ItemStack plob = IC2Items.getItem("plate", "obsidian").copy();
		ItemStack elmg = new ItemStack(electromagnet, 1);
		ItemStack advc = IC2Items.getItem("crafting", "advanced_circuit").copy();
		ItemStack cplt = IC2Items.getItem("crafting", "carbon_plate").copy();
		ItemStack stbt = new ItemStack(Item.getItemFromBlock(Blocks.STONE_BUTTON), 1);
		ItemStack[][] reactants_railgun = {
				{null, encr, csir, csir, csir},
				{pllp, pllp, elmg, advc, cplt},
				{pllp, plob, plob, plob, cplt},
				{null, null, null, stbt, cplt},
				{null, null, null, cplt, cplt}
		};
		railgun.setRecipe(reactants_railgun);
		//------------railgun end------------
		
		//------------railgun_adv------------
		ItemStack lpcr = new ItemStack(IC2Items.getItem("lapotron_crystal").getItem(), 1, OreDictionary.WILDCARD_VALUE);
		ItemStack adva = IC2Items.getItem("crafting", "alloy").copy();
		ItemStack pldl = IC2Items.getItem("plate", "dense_lapis").copy();
		ItemStack pldo = IC2Items.getItem("plate", "dense_obsidian").copy();
		ItemStack adve = new ItemStack(electromagnet_adv, 1);
		ItemStack rlgn = new ItemStack(railgun, 1);
		ItemStack plri = IC2Items.getItem("crafting", "iridium").copy();
		ItemStack[][] reactants_railgun_adv = {
				{null, lpcr, adva, adva, adva},
				{pldl, pldl, adve, rlgn, plri},
				{pldl, pldo, pldo, pldo, plri},
				{null, null, null, adva, plri},
				{null, null, null, plri, plri}
		};
		railgun_adv.setRecipe(reactants_railgun_adv);
		//------------railgun_adv end------------
		
		//------------nano_claw------------
		ItemStack[][] reactants_nano_claw = {
				{cplt, null, cplt, null, null},
				{null, cplt, null, cplt, null},
				{cplt, null, cplt, adva, cplt},
				{null, cplt, adva, cplt, adva},
				{null, null, cplt, adva, encr}
		};
		nano_claw.setRecipe(reactants_nano_claw);
		//------------nano_claw end------------
		
		//------------EMP_shield------------
		ItemStack cpci = IC2Items.getItem("cable", "type:copper,insulation:0").copy();
		ItemStack[][] reactants_EMP_shield = {
				{null, adva, elmg, adva, null},
				{adva, null, cpci, null, adva},
				{elmg, cpci, encr, cpci, elmg},
				{adva, null, cpci, null, adva},
				{null, adva, elmg, adva, null}
		};
		EMP_shield.setRecipe(reactants_EMP_shield);
		//------------EMP_shield end------------
		
	}
	
	private static void registerEvents() {
		MinecraftForge.EVENT_BUS.register(new DamageRecalc());
	}

}
