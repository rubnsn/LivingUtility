package Schr0.LivingUtility.mods.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import Schr0.LivingUtility.mods.LivingUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTabLUMaterial extends CreativeTabs
{
	public CreativeTabLUMaterial(String type)
	{
		super(type);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getTabIconItemIndex()
	{
		return LivingUtility.Item_LUMaterial.itemID;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel()
	{
		return "Living Utility Material";
	}
}