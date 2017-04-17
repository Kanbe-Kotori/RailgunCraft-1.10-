package cn.nulladev.railguncraft.core;

import cn.nulladev.railguncraft.client.gui.GuiElementLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = RailgunCraft.MODID, name = RailgunCraft.MODNAME, version = RailgunCraft.VERSION, dependencies="required-after:IC2")
public class RailgunCraft {

	public static final String MODID = "railguncraft";
	public static final String MODNAME = "RailgunCraft";
    public static final String VERSION = "10.17.4.17";
    
	public static final RGCCreativeTabs CreativeTabs = new RGCCreativeTabs();

    @Instance("railguncraft")
    public static RailgunCraft instance = new RailgunCraft();
    
    @SidedProxy(clientSide = "cn.nulladev.railguncraft.core.ClientProxy",
            	serverSide = "cn.nulladev.railguncraft.core.CommonProxy")
    public static CommonProxy proxy;    

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	new ConfigLoader(event);
    	Registerer.initAll(this);
    	proxy.preInit(event);
    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiElementLoader());
    	proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.postInit(event);
    }

}

