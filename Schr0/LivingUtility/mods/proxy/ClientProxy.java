package Schr0.LivingUtility.mods.proxy;

import net.minecraft.server.MinecraftServer;
import Schr0.LivingUtility.mods.entity.EntityLivingChest;
import Schr0.LivingUtility.mods.entity.renderer.RenderLivingChest;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@Override
	public void registerClient()
	{
		// ModLoader.addRendererでのmap.putに相当
		// Entityのクラスと描画, モデルを結びつける
		RenderingRegistry.registerEntityRenderingHandler(EntityLivingChest.class, new RenderLivingChest());
	}
	
	@Override
	public MinecraftServer getServer()
	{
		return FMLClientHandler.instance().getServer();
	}
	
	@Override
	public void addMessage(String message)
	{
		FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(message);
	}
	
}
