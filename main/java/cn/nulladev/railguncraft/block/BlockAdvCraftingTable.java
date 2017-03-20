package cn.nulladev.railguncraft.block;

import javax.annotation.Nullable;

import cn.nulladev.railguncraft.block.tileentity.TileEntityAdvCraftingTable;
import cn.nulladev.railguncraft.client.gui.GuiElementLoader;
import cn.nulladev.railguncraft.core.RailgunCraft;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockAdvCraftingTable extends RGCContainerBase {

	public BlockAdvCraftingTable() {
		super(Material.IRON, "adv_crafting_table");
		setTickRandomly(true);
	    setHardness(3.0F);
	    setSoundType(SoundType.METAL);
	    GameRegistry.registerTileEntity(TileEntityAdvCraftingTable.class, "TileEntityAdvCraftingTable");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityAdvCraftingTable();
	}
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		player.openGui(RailgunCraft.instance, GuiElementLoader.GUI_AdvCraftingTable, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
    }
	
}
