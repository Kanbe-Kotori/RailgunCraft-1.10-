package cn.nulladev.railguncraft.entity;

import java.util.HashMap;

import net.minecraft.entity.Entity;

public class EMPShieldFinder {

	private static final HashMap<Entity, EntityEMPShield> map = new HashMap<Entity, EntityEMPShield>();
	
	public static boolean hasShield(Entity entity) {
		return map.containsKey(entity);
	}
	
	public static EntityEMPShield getShield(Entity entity) {
		if (hasShield(entity)) {
			return map.get(entity);
		} else {
			return null;
		}
	}
	
	public static void removeShield(Entity entity) {
		if (hasShield(entity))
			map.remove(entity);
	}
	
	public static void addShield(Entity entity, EntityEMPShield shield) {
		removeShield(entity);
		map.put(entity, shield);
	}
	
}
