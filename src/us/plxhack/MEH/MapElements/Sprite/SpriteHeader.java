package us.plxhack.MEH.MapElements.Sprite;

import org.zzl.minegaming.GBAUtils.BitConverter;
import org.zzl.minegaming.GBAUtils.DataStore;
import org.zzl.minegaming.GBAUtils.GBARom;
import org.zzl.minegaming.GBAUtils.ISaveable;
import us.plxhack.MEH.IO.Map;

import java.util.ArrayList;

public class SpriteHeader {

	public   byte bNumNPC;
	public   byte bNumExits;
	public   byte bNumTraps;
	public  byte bNumSigns;
	public  long pNPC;
	public  long pExits;
	public  long pTraps;
	public  long pSigns;
	private final int pData;
	private final GBARom rom;

	public SpriteHeader(GBARom rom){
		this(rom,rom.internalOffset);
	}	  

	public SpriteHeader(GBARom rom, int offset){
		pData = offset;
		this.rom = rom;
		rom.Seek(offset&0x1FFFFFF);
		bNumNPC=rom.readByte();
		bNumExits=rom.readByte();
		bNumTraps=rom.readByte();
		bNumSigns=rom.readByte();
		pNPC=rom.getPointer();
		pExits=rom.getPointer();
		pTraps=rom.getPointer();
		pSigns=rom.getPointer();
	}

	public void save() {
		rom.Seek(pData&0x1FFFFFF);
		rom.writeByte(bNumNPC);
		rom.writeByte(bNumExits);
		rom.writeByte(bNumTraps);
		rom.writeByte(bNumSigns);
		rom.writePointer((int)pNPC);
		rom.writePointer((int)pExits);
		rom.writePointer((int)pTraps);
		rom.writePointer((int)pSigns);
	}

    public static class SpritesSignManager implements ISaveable {

        public ArrayList<SpriteSign> mapSigns;
        private int internalOffset;
        private int originalSize;
        private Map loadedMap;
        private GBARom rom;

        public int getSpriteIndexAt(int x, int y) {
            int i = 0;
            for (SpriteSign s : mapSigns) {
                if (s.bX == x && s.bY == y) {
                    return i;
                }
                i++;
            }
            return -1;
        }

        public SpritesSignManager(GBARom rom, Map m, int offset, int count) {
            internalOffset = offset;
            this.rom = rom;
            this.loadedMap = m;
            rom.Seek(offset);
            mapSigns = new ArrayList<SpriteSign>();
            int i = 0;
            for (i = 0; i < count; i++) {
                mapSigns.add(new SpriteSign(rom));
            }
            originalSize = getSize();
        }

        public int getSize()
        {
            return mapSigns.size() * SpriteSign.getSize();
        }

        public void add(int x, int y)
        {
            mapSigns.add(new SpriteSign(rom, (byte)x,(byte)y));
        }

        public void remove(int x, int y)
        {
            mapSigns.remove(getSpriteIndexAt(x,y));
        }

        public void save() {
            rom.floodBytes(BitConverter.shortenPointer(internalOffset), rom.freeSpaceByte, originalSize);
            //TODO make this a setting, ie always repoint vs keep pointers
            if(originalSize < getSize()) {
                internalOffset = rom.findFreespace(DataStore.FreespaceStart, getSize());
                if(internalOffset < 0x08000000)
                    internalOffset += 0x08000000;
            }
            loadedMap.mapSprites.pSigns = internalOffset & 0x1FFFFFF;
            loadedMap.mapSprites.bNumSigns = (byte)mapSigns.size();
            rom.Seek(internalOffset);
            for (SpriteSign s : mapSigns)
                s.save();
        }
    }
}