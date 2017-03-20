package cn.nulladev.railguncraft.client.gui;

import cn.nulladev.railguncraft.block.container.ContainerAdvCraftingTable;
import cn.nulladev.railguncraft.block.tileentity.TileEntityAdvCraftingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiElementLoader implements IGuiHandler {
	
    public static final int GUI_AdvCraftingTable = 100;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
        case GUI_AdvCraftingTable:
            return new ContainerAdvCraftingTable(player.inventory, (TileEntityAdvCraftingTable) player.worldObj.getTileEntity(new BlockPos(x, y, z)));
        default:
            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
        case GUI_AdvCraftingTable:
            return new GuiAdvCraftingTable(player.inventory, (TileEntityAdvCraftingTable) player.worldObj.getTileEntity(new BlockPos(x, y, z)));
        default:
            return null;
        }
    }
}
