package org.zzl.minegaming.MEH;

import java.util.HashMap;

import org.zzl.minegaming.GBAUtils.GBARom;

public class WildDataCache extends Thread implements Runnable
{
	private static HashMap<MapID,WildData> dataCache = new HashMap<MapID,WildData>();
	private static GBARom rom;
	
	public WildDataCache(GBARom rom)
	{
		this.rom = rom;
	}
	
	public static void gatherData()
	{
		long pData = DataStore.WildPokemon;
		while(true)
		{
			WildDataHeader h = new WildDataHeader(rom, (int)pData);
			if(h.bBank == (byte)0xFF && h.bMap == (byte)0xFF)
				break;
			
			WildData d = new WildData(rom,h);
			dataCache.put(new MapID((int)(h.bBank & 0xFF),(int)(h.bMap & 0xFF)),d);
			pData += (4 * 5);
		}
	}
	
	public static WildData getWildData(int bank, int map)
	{
		return dataCache.get(new MapID(bank,map));
	}
	
	public void run()
	{
		gatherData();
	}
}