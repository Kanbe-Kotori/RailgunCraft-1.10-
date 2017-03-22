package cn.nulladev.railguncraft.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.world.World;

public class EntityHasOwner extends Entity implements IEntityOwnable {
		
	public EntityHasOwner(World world) {
        super(world);
    }

	@Override
	protected void entityInit() {
		this.dataManager.register(new DataParameter(73, DataSerializers.STRING), "");
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		setOwnerName(nbt.getString("OwnerName"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setString("OwnerName", this.getOwnerName());
	}
	
	public void setOwnerName(String name) {
        this.dataManager.set(new DataParameter(73, DataSerializers.STRING), name);;
    }
	
	public void setOwner(EntityPlayer player) {
		setOwnerName(player.getDisplayNameString());
    }
	
	public String getOwnerName() {
		return this.dataManager.get(new DataParameter(73, DataSerializers.STRING)).toString();
	}

	@Override
	public UUID getOwnerId() {
		return this.getOwner().getUniqueID();
	}

	@Override
	public EntityPlayer getOwner() {
		return this.worldObj.getPlayerEntityByName(this.getOwnerName());
	}

}