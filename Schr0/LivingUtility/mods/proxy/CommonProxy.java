package Schr0.LivingUtility.mods.proxy;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import cpw.mods.fml.common.FMLCommonHandler;

public class CommonProxy
{
	public void registerClient()
	{
		// none
	}
	
	public MinecraftServer getServer()
	{
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}
	
	public void addMessage(String message)
	{
		getServer().logInfo(StringUtils.stripControlCodes(message));
	}
}