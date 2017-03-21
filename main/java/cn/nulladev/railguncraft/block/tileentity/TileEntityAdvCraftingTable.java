package cn.nulladev.railguncraft.block.tileentity;

import java.util.Arrays;
import java.util.List;

import cn.nulladev.railguncraft.item.crafting.RGCAdvRecipe;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.IElectricItem;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityAdvCraftingTable extends TileEntity implements ITickable, IInventory, IEnergyTile, IEnergySink, INetworkDataProvider, INetworkUpdateListener {
	
	private ItemStack stack[] = new ItemStack[27];
	
	public int currentBuffer = 0;
	public final int maxBuffer = 10000;
	
	private int loadState = 0;
	private boolean addedToEnet;

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList list = nbt.getTagList("Items", 10);
        this.stack = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound nbt1 = list.getCompoundTagAt(i);
            byte slot = nbt1.getByte("Slot");
            if (slot >= 0 && slot < this.stack.length) {
                this.stack[slot] = ItemStack.loadItemStackFromNBT(nbt1);
            }
        }
        this.currentBuffer = nbt.getInteger("currentBuffer");
    }
 
    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("currentBuffer", this.currentBuffer);
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.stack.length; i++) {
            if (this.stack[i] != null) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte("Slot", (byte)i);
                this.stack[i].writeToNBT(nbt1);
                list.appendTag(nbt1);
            }
        }
        nbt.setTag("Items", list);
		return super.writeToNBT(nbt);
    }
	
    @Override
	public final void invalidate() {
    	if (this.loadState != 3)
    		onUnloaded();
    	super.invalidate();
	}

    @Override
	public final void onChunkUnload() {
    	if (this.loadState != 3)
    		onUnloaded();
    	super.onChunkUnload();
	}

    @Override
	public void validate() {
    	super.validate();
    	if ((this.worldObj == null) || (this.pos == null))
    		throw new IllegalStateException("no world/pos");
    	
    	if ((this.loadState != 0) && (this.loadState != 3))
    		throw new IllegalStateException("invalid load state: " + this.loadState);
    	
    	this.loadState = 1;
	}

    protected void onLoaded() {
      if (this.loadState != 1)
    	  throw new IllegalStateException("invalid load state: " + this.loadState);
      
      if (!this.worldObj.isRemote)
          this.addedToEnet = (!MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this)));

      this.loadState = 2;
    }

	protected void onUnloaded() {
		if (this.loadState == 3)
			throw new IllegalStateException("invalid load state: " + this.loadState);
		
		if (this.addedToEnet)
			this.addedToEnet = MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

		this.loadState = 3;
    }

	@Override
	public void update() {
		
		/*if (this.worldObj.isRemote) {
			return;
		}*/
		if (this.loadState == 1)
			this.onLoaded();
		if (this.loadState != 2)
			return;
		
		ItemStack[][] s = {
				{stack[0], 	stack[1], 	stack[2], 	stack[3], 	stack[4]},
				{stack[5], 	stack[6],	stack[7], 	stack[8], 	stack[9]},
				{stack[10], stack[11], 	stack[12], 	stack[13], 	stack[14]},
				{stack[15], stack[16], 	stack[17], 	stack[18], 	stack[19]},
				{stack[20], stack[21], 	stack[22], 	stack[23], 	stack[24]},
		};
		
		boolean flag = false;
		RGCAdvRecipe toRecipe = null;
		for (RGCAdvRecipe r : RGCAdvRecipe.getAllRecipes()) {
			if (RGCAdvRecipe.isEqual(s, r)) {
				flag = true;
				toRecipe = r;
			}
		}
		
		if (flag) {
			ItemStack i = toRecipe.m_product.copy();
			if (stack[25] == null || !stack[25].equals(i))
				stack[25] = i;
		} else {
			stack[25] = null;
		}
	    		
	    this.worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
		this.markDirty();
	}

	@Override
	public String getName() {
		return "adv_crafting_table";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return stack.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return stack[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.stack[index] != null) {
			ItemStack stack;
			if (this.stack[index].stackSize <= count) {
				stack = this.stack[index];
				this.stack[index] = null;
				return stack;
			} else {
				stack = this.stack[index].splitStack(count);
				if (this.stack[index].stackSize == 0) {
					this.stack[index] = null;
				}
				return stack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = this.stack[index];
		setInventorySlotContents(index, null);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.stack[index] = stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 25) {
			return false;
		} else if (index == 26) {
			if (stack == null)
				return true;
			else
				return stack.getItem() instanceof IElectricItem;
		} else {
			return true;
		}
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		
	}

	@Override
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return true;
	}

	@Override
	public double getDemandedEnergy() {
		return currentBuffer >= maxBuffer? 0 : Math.max(maxBuffer - currentBuffer, 128);
	}

	@Override
	public int getSinkTier() {
		return 1;
	}

	@Override
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		double used = Math.min(maxBuffer - currentBuffer, amount);
		currentBuffer += used;
		return amount - used;
	}

	@Override
	public void onNetworkUpdate(String field) {}

	@Override
	public List<String> getNetworkedFields() {
		return Arrays.asList(new String[0]);
	}
	
	@Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

}
