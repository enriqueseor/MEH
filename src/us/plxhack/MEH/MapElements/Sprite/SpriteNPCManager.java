package us.plxhack.MEH.MapElements.Sprite;

import org.zzl.minegaming.GBAUtils.BitConverter;
import org.zzl.minegaming.GBAUtils.DataStore;
import org.zzl.minegaming.GBAUtils.GBARom;
import org.zzl.minegaming.GBAUtils.ISaveable;
import us.plxhack.MEH.IO.Map;

import java.util.ArrayList;

public class SpriteNPCManager implements ISaveable {

	public ArrayList<SpriteNPC> mapNPCs;
	private int internalOffset;
	private final GBARom rom;
	private final int originalSize;
	private final Map loadedMap;

	public int[] GetSpriteIndices() {
		int[] indices = new int[mapNPCs.size()];
		for (int i = 0; i < mapNPCs.size(); i++) {
			indices[i] = mapNPCs.get(i).bSpriteSet;
		}
		return indices;
	}

	public int getSpriteIndexAt(int x, int y) {
		for (int i = 0; i < mapNPCs.size(); i++) {
			if (mapNPCs.get(i).bX == x && mapNPCs.get(i).bY == y) {
				return i;
			}
		}
		return -1;
	}

	public SpriteNPCManager(GBARom rom, Map m, int offset, int count) {
		this.rom = rom;
		internalOffset = offset;
		loadedMap = m;
		rom.Seek(offset);
		mapNPCs = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			mapNPCs.add(new SpriteNPC(rom));
		}
		originalSize = getSize();
	}

	public int getSize()
	{
		return mapNPCs.size() * SpriteNPC.getSize();
	}
	
	public void add(int x, int y)
	{
		mapNPCs.add(new SpriteNPC(rom, (byte)x,(byte)y));
	}

	public void remove(int x, int y)
	{
		mapNPCs.remove(getSpriteIndexAt(x,y));
	}

	public void save() {
		rom.floodBytes(BitConverter.shortenPointer(internalOffset), rom.freeSpaceByte, originalSize);
		// TODO make this a setting, ie always repoint vs keep pointers
		int i = getSize();
		if (originalSize < getSize()) {
			internalOffset = rom.findFreespace(DataStore.FreespaceStart, getSize());
			if (internalOffset < 0x08000000)
				internalOffset += 0x08000000;
		}
		loadedMap.mapSprites.pNPC = internalOffset;
		loadedMap.mapSprites.bNumNPC = (byte) mapNPCs.size();
		rom.Seek(internalOffset);
		for (SpriteNPC n : mapNPCs)
			n.save();
	}
}