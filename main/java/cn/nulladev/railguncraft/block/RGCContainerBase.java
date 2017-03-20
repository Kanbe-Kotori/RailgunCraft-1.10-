package cn.nulladev.railguncraft.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cn.nulladev.railguncraft.core.RailgunCraft;

public abstract class RGCContainerBase extends BlockContainer {
	
	public RGCContainerBase(Material material, String name) {
        super(material);
        this.setUnlocalizedName(name);
        this.setCreativeTab(RailgunCraft.CreativeTabs);
        GameRegistry.registerBlock(this, name);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

}
