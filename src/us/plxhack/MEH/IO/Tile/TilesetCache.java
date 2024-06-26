package us.plxhack.MEH.IO.Tile;

import org.zzl.minegaming.GBAUtils.DataStore;
import org.zzl.minegaming.GBAUtils.GBARom;
import org.zzl.minegaming.GBAUtils.ROMManager;
import us.plxhack.MEH.IO.Map.Map;
import us.plxhack.MEH.IO.Tile.Tileset;

import java.util.HashMap;

public class TilesetCache {

	private static HashMap<Integer, Tileset> cache = new HashMap<>();
	private GBARom rom;
	private TilesetCache(){}
	
	public static void contains(int offset) {}
	
	/**
	 * Pulls a tileset from the tileset cache. Create a new tileset if one is not cached.
	 * @param offset Tileset data offset
	 */
	public static Tileset get(int offset) {
        Tileset t;
        if(cache.containsKey(offset)) {
            t = cache.get(offset);
			if(t.modified) {
				t.loadData(offset);
				t.renderTiles(offset);
				t.modified = false;
			}
        }
		else {
            t = new Tileset(ROMManager.getActiveROM(), offset);
			cache.put(offset, t);
        }
        return t;
    }

	public static void clearCache()
	{
		cache = new HashMap<>();
	}

	public static void saveAllTilesets() {
		for(Tileset t : cache.values())
			t.save();
	}
	
	public static void switchTileset(Map loadedMap) {
		get(loadedMap.getMapData().globalTileSetPtr).resetPalettes();
		get(loadedMap.getMapData().localTileSetPtr).resetPalettes();
		for(int j = 1; j < 5; j++)
			for(int i = DataStore.MainTSPalCount-1; i < 13; i++)
				get(loadedMap.getMapData().globalTileSetPtr).getPalette(j-1)[i] = get(loadedMap.getMapData().localTileSetPtr).getROMPalette()[j-1][i];
		for(int j = 0; j < 4; j++)
			get(loadedMap.getMapData().localTileSetPtr).setPalette(get(loadedMap.getMapData().globalTileSetPtr).getPalette(j),j);
		get(loadedMap.getMapData().localTileSetPtr).renderPalettedTiles();
		get(loadedMap.getMapData().globalTileSetPtr).renderPalettedTiles();
		get(loadedMap.getMapData().localTileSetPtr).startTileThreads();
		get(loadedMap.getMapData().globalTileSetPtr).startTileThreads();
	}
}