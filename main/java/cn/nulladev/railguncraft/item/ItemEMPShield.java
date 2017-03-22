package cn.nulladev.railguncraft.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cn.nulladev.railguncraft.core.RGCUtils;
import cn.nulladev.railguncraft.entity.EMPShieldFinder;
import cn.nulladev.railguncraft.entity.EntityEMPShield;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class ItemEMPShield extends RGCItemBase implements IElectricItem {
		
	public ItemEMPShield() {
		super("EMP_shield");
		setMaxStackSize(1);
		setMaxDamage(27);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean par5) {
	    super.onUpdate(stack, world, entity, slot, (par5) && (isActive(stack)));
	    if (!isActive(stack)) {
	    	if (EMPShieldFinder.hasShield(entity)) {
	    		EMPShieldFinder.getShield(entity).setDead();
	    		EMPShieldFinder.removeShield(entity);
	    	}
	    	return;
	    }
	    
	    if (entity.isDead) {
	    	setActive(stack.getTagCompound(), false);
	    	if (EMPShieldFinder.hasShield(entity)) {
	    		EMPShieldFinder.getShield(entity).setDead();
	    		EMPShieldFinder.removeShield(entity);
	    	}
	    	return;
	    }
	    
	    if (!ElectricItem.manager.use(stack, 4.0D, (EntityLivingBase)entity)) {
	    	setActive(stack.getTagCompound(), false);
	    	if (EMPShieldFinder.hasShield(entity)) {
	    		EMPShieldFinder.getShield(entity).setDead();
	    		EMPShieldFinder.removeShield(entity);
	    	}
	    	return;
	    }
	    
	    if(!EMPShieldFinder.hasShield(entity)) {
	    	NBTTagCompound nbt = stack.getTagCompound() != null? stack.getTagCompound() : new NBTTagCompound();
	    	setActive(nbt, true);
	    	EntityEMPShield shield = new EntityEMPShield(world, (EntityPlayer) entity, stack);
	    	EMPShieldFinder.addShield(entity, shield);
	    	entity.worldObj.spawnEntityInWorld(shield);
	    }
	    
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (world.isRemote)
			return new ActionResult(EnumActionResult.PASS, stack);

		NBTTagCompound nbt = RGCUtils.get_or_create_nbt(stack);
		if (!nbt.hasKey("active")) {
			setActive(nbt, false);
		}

	    if (isActive(nbt)) {
	    	setActive(nbt, false);
	    	if (EMPShieldFinder.hasShield(player)) {
	    		EMPShieldFinder.getShield(player).setDead();
	    		EMPShieldFinder.removeShield(player);
	    	}
	    } else if (ElectricItem.manager.canUse(stack, 16.0D)) {
	    	setActive(nbt, true);
	    	EntityEMPShield shield = new EntityEMPShield(world, player, stack);
	    	EMPShieldFinder.addShield(player, shield);
	    	player.worldObj.spawnEntityInWorld(shield);
	    }

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	    
	}

	public static boolean isActive(ItemStack stack) {
		NBTTagCompound nbt = RGCUtils.get_or_create_nbt(stack);
		return isActive(nbt);
	}

	public static boolean isActive(NBTTagCompound nbt) {
		if (!nbt.hasKey("active")) {
			nbt.setBoolean("active", false);
			return false;
		}
		return nbt.getBoolean("active");
	}
	
	public static void setActive(ItemStack stack, boolean active) {
		NBTTagCompound nbt = RGCUtils.get_or_create_nbt(stack);	
		setActive(nbt, active);
	}

	public static void setActive(NBTTagCompound nbt, boolean active) {
		nbt.setBoolean("active", active);
	}

	@Override
	public boolean canProvideEnergy(ItemStack stack) {
		return false;
	}

	@Override
	public double getMaxCharge(ItemStack stack) {
		return 1000000;
	}

	@Override
	public int getTier(ItemStack stack) {
		return 3;
	}

	@Override
	public double getTransferLimit(ItemStack stack) {
		return 1024;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {
	    return EnumRarity.UNCOMMON;
	}

}
