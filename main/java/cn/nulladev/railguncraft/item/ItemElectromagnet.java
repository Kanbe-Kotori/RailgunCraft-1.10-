package cn.nulladev.railguncraft.item;

public class ItemElectromagnet extends RGCItemBase {

	public ItemElectromagnet(boolean isAdv) {
		super("electromagnet" + (isAdv? "_adv" : ""));
	}

}
