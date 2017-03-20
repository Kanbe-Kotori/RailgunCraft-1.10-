package cn.nulladev.railguncraft.item;

import cn.nulladev.railguncraft.entity.EntityRailgun;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRailgun extends RGCItemBase implements IElectricItem {
	
	public ItemRailgun() {
		super("railgun");
		setMaxStackSize(1);
		setMaxDamage(27);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

	@Override
	public boolean canProvideEnergy(ItemStack itemStack) {
		return false;
	}

	@Override
	public double getMaxCharge(ItemStack itemStack) {
		return 1000000;
	}

	@Override
	public int getTier(ItemStack itemStack) {
		return 3;
	}

	@Override
	public double getTransferLimit(ItemStack itemStack) {
		return 1024;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (world.isRemote) {
			return new ActionResult(EnumActionResult.PASS, stack);
		}
		
		if (!player.capabilities.isCreativeMode) {
			
			/*if (player.inventory.hasItemStack(itemStackIn)) {
				player.inventory.removeStackFromSlot(index)(Items.IRON_INGOT);
			} else {
				return new ActionResult(EnumActionResult.PASS, stack);
			}*/
			
			if (!ElectricItem.manager.use(stack, 100000, player)) {
				//player.inventory.addItemStackToInventory(IC2Items.getItem("coin").copy());
				return new ActionResult(EnumActionResult.FAIL, stack);
			}
			
		}
		
		player.worldObj.spawnEntityInWorld(new EntityRailgun(player.worldObj, player));
		player.openContainer.detectAndSendChanges();
		
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	    //return super.onItemRightClick(stack, world, player, hand);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {
	    return EnumRarity.EPIC;
	}

}
