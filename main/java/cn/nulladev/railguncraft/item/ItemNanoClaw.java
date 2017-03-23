package cn.nulladev.railguncraft.item;

import java.util.List;
import java.util.Random;

import cn.nulladev.railguncraft.core.RGCUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemNanoClaw extends RGCItemBase implements IElectricItem {
	
	public ItemNanoClaw() {
		super("nano_claw");
		setMaxStackSize(1);
		setMaxDamage(27);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean par5) {
	    super.onUpdate(stack, world, entity, slot, (par5) && (isActive(stack)));
		NBTTagCompound nbt = RGCUtils.get_or_create_nbt(stack);
		
		if (!nbt.hasKey("dirty")) {
			nbt.setBoolean("dirty", false);
		}
		
		if (!isActive(stack)) {
			if (nbt.getBoolean("dirty")) {
				setActive(nbt, true);
				nbt.setBoolean("dirty", false);
			} else {
				return;
			}
	    }
		
	    if (slot < 9) {
	    	if (!ElectricItem.manager.use(stack, 4.0D, (EntityLivingBase)entity)) {
	    		setActive(stack.getTagCompound(), false);
	    	}
	    } else {
	    	if (!ElectricItem.manager.use(stack, 1.0D, (EntityLivingBase)entity)) {
	    		setActive(stack.getTagCompound(), false);
	    	}
	    }
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
		NBTTagCompound nbt = RGCUtils.get_or_create_nbt(stack);
	    if (isActive(stack)) {
	    	if (!ElectricItem.manager.canUse(stack, 400.0D)) {
		    	this.setActive(nbt, false);;
		    }
	    }

	    return false;
	  }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (world.isRemote)
			return new ActionResult(EnumActionResult.PASS, stack);

		NBTTagCompound nbt = RGCUtils.get_or_create_nbt(stack);

	    if (isActive(nbt)) {
	    	setActive(nbt, false);
	    } else if (ElectricItem.manager.canUse(stack, 16.0D)) {
	    	setActive(nbt, true);
	    }

    	return new ActionResult(EnumActionResult.SUCCESS, stack);
	    
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (!isActive(stack))
			return true;
		NBTTagCompound nbt = RGCUtils.get_or_create_nbt(stack);
		nbt.setBoolean("dirty", true);
		setActive(nbt, false);
    	return true;
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
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		if (slot != EntityEquipmentSlot.MAINHAND) {
			return super.getAttributeModifiers(slot, stack);
		}
		
	    int dmg = 2;

	    if ((ElectricItem.manager.canUse(stack, 400.0D)) && (isActive(stack))) {
	    	dmg = 6 + new Random().nextInt(13);
	    	//System.out.println("random ret");
	    }

	    Multimap ret = HashMultimap.create();

	    ret.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -0.8D, 0));
	    ret.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", dmg, 0));

	    return ret;
	  }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		if (isActive(stack)) {
			tooltip.add(I18n.translateToLocal("item.nano_claw.info"));
		}
    }

}
