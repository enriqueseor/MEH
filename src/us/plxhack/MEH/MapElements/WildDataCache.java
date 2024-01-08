package us.plxhack.MEH.MapElements;

import org.zzl.minegaming.GBAUtils.DataStore;
import org.zzl.minegaming.GBAUtils.GBARom;

import java.util.HashMap;

public class WildDataCache extends Thread implements Runnable {

	private static final HashMap<Integer,WildData> dataCache = new HashMap<>();
	private static GBARom rom;
	private static int initialNum;
	
	public WildDataCache(GBARom rom)
	{
		WildDataCache.rom = rom;
	}
	
	public static void gatherData() {
		long pData = DataStore.WildPokemon;
		int count = 0;
		while(true) {
			WildDataHeader header = new WildDataHeader(rom, (int)pData);
			if(header.bBank == (byte)0xFF && header.bMap == (byte)0xFF)
				break;
			WildData wildData = new WildData(rom,header);
			int num = (header.bBank & 0xFF) + ((header.bMap & 0xFF)<<8);
			dataCache.put(num,wildData);
			pData += (4 * 5);
			count++;
		}
		initialNum = count;
	}
	
	public static void save() {
		long pData = DataStore.WildPokemon;
		if (initialNum < dataCache.size()) {
			pData = rom.findFreespace(DataStore.FreespaceStart, WildDataHeader.getSize() * dataCache.size());
			rom.repoint((int) DataStore.WildPokemon, (int) pData, 14); //TODO: Maybe make this configurable?
			rom.floodBytes((int) DataStore.WildPokemon, (byte) 0xFF, initialNum * WildDataHeader.getSize()); //TODO Make configurable
		}
		for (WildData wildData : dataCache.values()) {
			if (wildData != null) { // Add a null check before calling save()
				wildData.save((int) pData);
				pData += (4 * 5);
			} else {
				System.err.println("Error: WildData is null while saving"); // Handle the null case
			}
		}
	}
	
	public static WildData getWildData(int bank, int map) {
		int num = (bank & 0xFF) + ((map & 0xFF)<<8);
		return dataCache.get(num);
	}
	
	public static void setWildData(int bank, int map, WildData wildData) {
		WildData data = getWildData(bank, map);
		if (wildData!= null) {
			data.aWildPokemon = wildData.aWildPokemon.clone();
			data.wildDataHeader = wildData.wildDataHeader;
		} else {
			System.err.println("Error: WildDataCache: WildData not found for bank: " + bank + ", map: " + map);
		}
	}
	
	public static WildData createWildDataIfNotExists(int bank, int map) {
		if(dataCache.containsKey((bank & 0xFF) + ((map & 0xFF)<<8)))
			return getWildData(bank,map);
		else {
			WildData wildData = new WildData(rom, bank, map);
			dataCache.put((bank & 0xFF) + ((map & 0xFF)<<8), wildData);
			return wildData;
		}
	}
	
	@Override
	public void run() {
		gatherData();
	}
}