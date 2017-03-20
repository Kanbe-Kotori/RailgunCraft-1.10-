package cn.nulladev.railguncraft.client.gui;

import org.lwjgl.opengl.GL11;

import cn.nulladev.railguncraft.block.container.ContainerAdvCraftingTable;
import cn.nulladev.railguncraft.block.tileentity.TileEntityAdvCraftingTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GuiAdvCraftingTable extends GuiContainer {
	 
	protected ResourceLocation gui = new ResourceLocation("railguncraft","textures/gui/GuiAdvCraftingTable.png");
	protected int xSize = 176;
    protected int ySize = 192;
	private TileEntityAdvCraftingTable tileentity;
	
	public GuiAdvCraftingTable(InventoryPlayer player, TileEntityAdvCraftingTable t) {
		super(new ContainerAdvCraftingTable(player, t));
		this.tileentity = t;
	}

	@Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        super.drawGuiContainerForegroundLayer(par1, par2);
        this.fontRendererObj.drawString(I18n.translateToLocal("tile.adv_crafting_table.name"), 56, -7, 0x000000);
        this.fontRendererObj.drawString("", 0, 0, 0xFFFFFF);
        this.mc.renderEngine.bindTexture(gui);
        int length = 14 * tileentity.currentBuffer / tileentity.maxBuffer;
    	this.drawTexturedModalRect(117, 92 - length, 176, 14 - length, 14, length);
	}
	
    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(gui);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
    }
	
}