package cn.nulladev.railguncraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityEMPShield extends EntityHasOwner {
	
	public final ItemStack originStack;
	
	public EntityEMPShield(World world) {
		super(world);
		this.originStack = null;
	}
	
	public EntityEMPShield(World world, EntityPlayer owner, ItemStack stack) {
		super(world);
		this.setOwner(owner);
		this.posX = owner.posX;
		this.posY = owner.posY;
		this.posZ = owner.posZ;
		this.originStack = stack;
		this.rotationYaw = owner.rotationYaw;
	}
	
	@Override
    public void onUpdate() {
		super.onUpdate();
					
		EntityPlayer owner = this.getOwner();
		if (owner == null) {
			this.setDead();
			return;
		}
		
		if (this.posX == owner.posX && this.posY == owner.posY && this.posZ == owner.posZ)
			return;

		this.posX = owner.posX;
		this.posY = owner.posY;
		this.posZ = owner.posZ;
		this.rotationYaw = owner.rotationYaw;
		
	}

}
