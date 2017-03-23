package cn.nulladev.railguncraft.item;

import java.util.List;

import cn.nulladev.railguncraft.core.RGCUtils;
import cn.nulladev.railguncraft.entity.EntityRailgun;
import ic2.api.item.ElectricItem;
import ic2.api.item.IC2Items;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRailgun extends RGCItemBase implements IElectricItem {
	
	public final boolean isAdvanced;
	
	public ItemRailgun(boolean isAdv) {
		super(isAdv? "railgun_adv" : "railgun");
		this.isAdvanced = isAdv;
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
		return isAdvanced? 10000000 : 1000000;
	}

	@Override
	public int getTier(ItemStack itemStack) {
		return isAdvanced? 4 : 3;
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
			ItemStack coin = IC2Items.getItem("crafting", "coin").copy();;
			
			if (!RGCUtils.remove_a_stack(player, coin)) {
				return new ActionResult(EnumActionResult.PASS, stack);
			}
			
			if (!ElectricItem.manager.use(stack, isAdvanced? 100000 : 10000, player)) {
				player.inventory.addItemStackToInventory(coin);
				return new ActionResult(EnumActionResult.PASS, stack);
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
	    return isAdvanced? EnumRarity.RARE : EnumRarity.UNCOMMON;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		if (isAdvanced) {
			tooltip.add(I18n.translateToLocal("item.railgun_adv.info"));
		}
    }

}
