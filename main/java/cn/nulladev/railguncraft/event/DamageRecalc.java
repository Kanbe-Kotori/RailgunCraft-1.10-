package cn.nulladev.railguncraft.event;

import ic2.api.item.ElectricItem;
import cn.nulladev.railguncraft.entity.EMPShieldFinder;
import cn.nulladev.railguncraft.entity.EntityEMPShield;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DamageRecalc {
	
	float dmgVal = 1F;
	
	@SubscribeEvent
	public void onPlayerAttacked(LivingHurtEvent event) {
		
		if (!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) event.getEntity();
		
		if (player.worldObj.isRemote) {
			return;
		}
		
		if (!EMPShieldFinder.hasShield(player)) {
			return;
		}
		EntityEMPShield shield = EMPShieldFinder.getShield(player);
				
		//System.out.println("player " + player.getDisplayNameString() + " suffered " + event.ammount + event.source.damageType + " damage");
		
		if (event.getSource().damageType.equals("lightningBolt")) {
			event.setAmount(event.getAmount() / 10);
			ElectricItem.manager.charge(shield.originStack, 1000000D, 3, true, false);
			return;
		}
		
		if (switchDmgType(event.getSource().damageType)) {
			if (ElectricItem.manager.use(shield.originStack, 128D * event.getAmount(), player)) {
				event.setAmount(event.getAmount() * dmgVal);
			}
		}
		
	}
	
	private boolean switchDmgType(String type) {
		if (type.equals("explosion") || type.equals("explosion.player")) {
			dmgVal = 0.2F;
		} else if (type.equals("fallingBlock") || type.equals("anvil")) {
			dmgVal = 0.3F;
		} else if (type.equals("arrow") || type.equals("thrown") || type.equals("onFire")) {
			dmgVal = 0.4F;
		} else if (type.equals("inFire") || type.equals("lava")) {
			dmgVal = 0.5F;
		} else if (type.equals("mob") || type.equals("player") || type.equals("generic")) {
			dmgVal = 0.6F;
		} else if (type.equals("inWall") || type.equals("drown") || type.equals("outOfWorld")) {
			dmgVal = 0.7F;
		} else {
			return false;
		}
		
		return true;
	}
	
}
