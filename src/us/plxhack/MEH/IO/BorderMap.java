package us.plxhack.MEH.IO;

import org.zzl.minegaming.GBAUtils.BitConverter;
import org.zzl.minegaming.GBAUtils.GBARom;
import org.zzl.minegaming.GBAUtils.ISaveable;
import us.plxhack.MEH.IO.Map.Map;
import us.plxhack.MEH.IO.Map.MapData;

public class BorderMap implements ISaveable {

    private final MapData mapData;
	private final BorderTileData mapTileData;
	public boolean isEdited = false;

	public BorderMap(GBARom rom, Map m) {
        mapData = m.getMapData();
		mapTileData = new BorderTileData(rom, BitConverter.shortenPointer(mapData.borderTilePtr),mapData);
	}
	
	public MapData getMapData() {return mapData;}
	public BorderTileData getMapTileData() {return mapTileData;}
	public void save() {mapTileData.save();}
}