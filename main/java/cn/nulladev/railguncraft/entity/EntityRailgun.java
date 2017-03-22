package cn.nulladev.railguncraft.entity;

import java.util.List;

import cn.nulladev.railguncraft.core.ConfigLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityRailgun extends EntityHasOwner implements IProjectile{
	
	protected static final float SPEED = 5.0F;
	protected static final float DAMAGE = 40.0F;
	
	private int tileX = -1;
    private int tileY = -1;
    private int tileZ = -1;
    private Block field_145785_f;
    protected boolean inGround;
    public int throwableShake;
    private int ticksInGround;
    protected int ticksInAir;
    protected float mGravity = 0.03F;
    protected float velocityDecreaseRate = 0.99F;
    public int age = 100;
    	
	public EntityRailgun(World world, EntityPlayer thrower, double px, double py, double pz) {
        super(world);
        
        this.setOwner(thrower);
        this.setSize(width, height);
        this.mGravity = 0;
        this.setLocationAndAngles(thrower.posX, thrower.posY + thrower.getEyeHeight(), thrower.posZ, thrower.rotationYaw, thrower.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
        this.posY -= 0.10000000149011612D;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
        this.setPosition(this.posX, this.posY, this.posZ);
        float f = 0.4F;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f;
        this.motionY = -MathHelper.sin((this.rotationPitch) / 180.0F * (float)Math.PI) * f;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
        
        this.resetLocationAndSpeed();
        this.setPosition(px, py, pz);
        this.setOrigPos(px, py, pz);       
        this.ignoreFrustumCheck = true;
        this.age = 100;
        this.mGravity = 0F;
	}
	
	public EntityRailgun(World world, EntityPlayer thrower) {
		this(world, thrower, thrower.posX + 2 * thrower.getLookVec().normalize().xCoord, thrower.posY + thrower.eyeHeight + 2 * thrower.getLookVec().normalize().yCoord, thrower.posZ + 2 * thrower.getLookVec().normalize().zCoord);
	}
	
	public EntityRailgun(World world) {
		super(world);
	}

	/**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    @Override
	@SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }
    
    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    @Override
	public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_) {
        float f2 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
        p_70186_1_ /= f2;
        p_70186_3_ /= f2;
        p_70186_5_ /= f2;
        p_70186_1_ += this.rand.nextGaussian() * 0.007499999832361937D * p_70186_8_;
        p_70186_3_ += this.rand.nextGaussian() * 0.007499999832361937D * p_70186_8_;
        p_70186_5_ += this.rand.nextGaussian() * 0.007499999832361937D * p_70186_8_;
        p_70186_1_ *= p_70186_7_;
        p_70186_3_ *= p_70186_7_;
        p_70186_5_ *= p_70186_7_;
        this.motionX = p_70186_1_;
        this.motionY = p_70186_3_;
        this.motionZ = p_70186_5_;
        float f3 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_5_ * p_70186_5_);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(p_70186_1_, p_70186_5_) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(p_70186_3_, f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    /** 设置Entity速度。 */
    @Override
	@SideOnly(Side.CLIENT)
    public void setVelocity(double vx, double vy, double vz) {
        this.motionX = vx;
        this.motionY = vy;
        this.motionZ = vz;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(vx * vx + vz * vz);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(vx, vz) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(vy, f) * 180.0D / Math.PI);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
	public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();

        if (this.throwableShake > 0) {
            --this.throwableShake;
        }

        if (!this.inGround) {
            ++this.ticksInAir;
        }

        Vec3d vec3 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec31 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
        vec3 = new Vec3d(this.posX, this.posY, this.posZ);
        vec31 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null) {
            vec31 = new Vec3d(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        if (!this.worldObj.isRemote) {
            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            EntityPlayer entitylivingbase = this.getOwner();

            for (int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity)list.get(j);

                if (entity1.canBeCollidedWith() && (entity1 != entitylivingbase || this.ticksInAir >= 5)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f, f, f);
                    RayTraceResult movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new RayTraceResult(entity);
            }
        }

        if (movingobjectposition != null) {
        	this.onImpact(movingobjectposition);
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(Math.atan2(this.motionY, f1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        float f2 = velocityDecreaseRate;
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                float f4 = 0.25F;
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f4, this.posY - this.motionY * f4, this.posZ - this.motionZ * f4, this.motionX, this.motionY, this.motionZ);
            }

            f2 = 0.8F;
        }

        //速度衰减
        this.motionX *= f2;
        this.motionY *= f2;
        this.motionZ *= f2;
        //因为重力下降
        this.motionY -= this.getGravityVelocity();
        this.setPosition(this.posX, this.posY, this.posZ);
        if (this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ <= 0.0064F)
        	this.setDead();
        if (this.ticksInAir >= this.age)
        	this.setDead();
    }
    
    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    @Deprecated
    protected float getGravityVelocity() {
        return mGravity;
    }

    @Override
	protected void entityInit() {
    	super.entityInit();
		this.dataManager.register(new DataParameter(74, DataSerializers.FLOAT), 0F);
		this.dataManager.register(new DataParameter(75, DataSerializers.FLOAT), 0F);
		this.dataManager.register(new DataParameter(76, DataSerializers.FLOAT), 0F);
	}
	
	public void setOrigPos(double x, double y, double z) {
        this.dataManager.set(new DataParameter(74, DataSerializers.FLOAT), Float.valueOf((float)x));
        this.dataManager.set(new DataParameter(75, DataSerializers.FLOAT), Float.valueOf((float)y));
        this.dataManager.set(new DataParameter(76, DataSerializers.FLOAT), Float.valueOf((float)z));
	}
	
	public Vec3d getOrigPos() {
		
		float x = Float.valueOf(this.dataManager.get(new DataParameter(74, DataSerializers.FLOAT)).toString());
		float y = Float.valueOf(this.dataManager.get(new DataParameter(75, DataSerializers.FLOAT)).toString());
		float z = Float.valueOf(this.dataManager.get(new DataParameter(76, DataSerializers.FLOAT)).toString());

		return new Vec3d(x, y, z);
	}
	
	protected void onImpact(RayTraceResult target) {
        if (target.entityHit != null) {
        	EntityPlayer thrower = this.getOwner();
    		target.entityHit.attackEntityFrom(new EntityDamageSource("lightningBolt", thrower).setProjectile().setDamageBypassesArmor(), DAMAGE);
        }
        if (!this.worldObj.isRemote) {
    		worldObj.createExplosion(this.getOwner(), this.posX, this.posY, this.posZ, 2.0F, ConfigLoader.canRailgunDestroyBlocks);
            this.setDead();
        }
    }
	
    // 重新设置位置和速度
	protected void resetLocationAndSpeed() {
		EntityPlayer thrower = this.getOwner();
        this.setLocationAndAngles(thrower.posX, thrower.posY + thrower.getEyeHeight(), thrower.posZ, thrower.rotationYaw, thrower.rotationPitch);
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI);
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI);
        this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, SPEED, 0.0F);
	}
	
	public void resetSpeed() {
		EntityPlayer thrower = this.getOwner();
		Vec3d d = new Vec3d(this.posX - thrower.posX, this.posY - thrower.getEyeHeight() - thrower.posY, this.posZ - thrower.posZ).normalize();
        this.motionX = d.xCoord;
        this.motionZ = d.zCoord;
        this.motionY = d.yCoord;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, SPEED, 0.0F);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);    	
		nbt.setShort("xTile", (short)this.tileX);
    	nbt.setShort("yTile", (short)this.tileY);
    	nbt.setShort("zTile", (short)this.tileZ);
    	nbt.setByte("inTile", (byte)Block.getIdFromBlock(field_145785_f));
    	nbt.setByte("shake", (byte)this.throwableShake);
    	nbt.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    }

    @Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
        this.tileX = nbt.getShort("xTile");
        this.tileY = nbt.getShort("yTile");
        this.tileZ = nbt.getShort("zTile");
        this.field_145785_f = Block.getBlockById(nbt.getByte("inTile") & 255);
        this.throwableShake = nbt.getByte("shake") & 255;
        this.inGround = nbt.getByte("inGround") == 1;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

}