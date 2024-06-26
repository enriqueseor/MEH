package us.plxhack.MEH.IO.Tile;

import org.zzl.minegaming.GBAUtils.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Tileset {

	private GBARom rom;
	private GBAImage image;
	private BufferedImage[][] bi;
	private Palette[][] palettes; 
	private Palette[][] palettesFromROM;
	private static Tileset lastPrimary;
	public TilesetHeader tilesetHeader;
	public final int numBlocks;
	private HashMap<Integer,BufferedImage>[] renderedTiles;
	private HashMap<Integer,BufferedImage>[] customRenderedTiles;
	private final byte[] localTSLZHeader = new byte[] { 10, 80, 9, 0, 32, 0, 0 };
	private final byte[] globalTSLZHeader = new byte[] { 10, 80, 9, 0, 32, 0, 0 };
	public boolean modified = false;

	public Tileset(GBARom rom, int offset) {
		this.rom = rom;
		loadData(offset);
		numBlocks = 1024;
		renderTiles(offset);	
	}
	
	public void loadData(int offset)
	{
		tilesetHeader = new TilesetHeader(rom,offset);
	}
	
	public void renderGraphics() {
		int imageDataPtr = (int)tilesetHeader.pGFX;
		if(tilesetHeader.isPrimary)
			lastPrimary = this;
		int[] uncompressedData = null;
		if(tilesetHeader.bCompressed == 1)
			uncompressedData = Lz77.decompressLZ77(rom, imageDataPtr);
		if(uncompressedData == null) {
			GBARom backup = (GBARom) rom.clone(); //Backup in case repairs fail
			rom.writeBytes((int)tilesetHeader.pGFX, (tilesetHeader.isPrimary ? globalTSLZHeader : localTSLZHeader)); //Attempt to repair the LZ77 data
			uncompressedData = Lz77.decompressLZ77(rom, imageDataPtr);
			rom = (GBARom) backup.clone(); //TODO add dialog to allow repairs to be permanant
			if(uncompressedData == null) {
				uncompressedData = BitConverter.ToInts(rom.readBytes(imageDataPtr, (tilesetHeader.isPrimary ? 128*DataStore.MainTSHeight : 128*DataStore.LocalTSHeight) / 2)); //TODO: Hardcoded to FR tileset sizes
			}
		}
		
		renderedTiles = new HashMap[16*4];
		customRenderedTiles = new HashMap[16*4];
		
		for(int i = 0; i < 16 * 4; i++)
			renderedTiles[i] = new HashMap<>();
		for(int i = 0; i < 16*4; i++)
			customRenderedTiles[i] = new HashMap<>();
		image = new GBAImage(uncompressedData,palettes[0][0],new Point(128,(tilesetHeader.isPrimary ? DataStore.MainTSHeight : DataStore.LocalTSHeight)));
	}
	
	public void renderPalettes() {
		palettes = new Palette[4][16];
		bi = new BufferedImage[4][16];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 16; j++) {
				palettes[i][j] = new Palette(GBAImageType.c16, rom.readBytes(((int)tilesetHeader.pPalettes)+((32*j) + (i * 0x200)),32));
			}
		}
		palettesFromROM = palettes.clone();
	}
	
	public void renderTiles(int offset) {
		renderPalettes();
		renderGraphics();
	}
	
	public void startTileThreads() {
		for(int i = 0; i < (tilesetHeader.isPrimary ? DataStore.MainTSPalCount : 13); i++)
			new TileLoader(renderedTiles,i).start();
	}
	
	public BufferedImage getTileWithCustomPal(int tileNum, Palette palette, boolean xFlip, boolean yFlip, int time) {
		int x = ((tileNum) % (bi[time][0].getWidth() / 8)) * 8;
		int y = ((tileNum) / (bi[time][0].getWidth() / 8)) * 8;
		BufferedImage toSend =  image.getBufferedImageFromPal(palette).getSubimage(x, y, 8, 8);
		if(!xFlip && !yFlip)
			return toSend;
		if(xFlip)
			toSend = horizontalFlip(toSend);
		if(yFlip)
			toSend = verticalFlip(toSend);
		return toSend;
	}

	public BufferedImage getTile(int tileNum, int palette, boolean xFlip, boolean yFlip, int time) {
		if(palette < DataStore.MainTSPalCount) {
			if(renderedTiles[palette+(time * 16)].containsKey(tileNum)) {
				if(xFlip && yFlip)
					return verticalFlip(horizontalFlip(renderedTiles[palette+(time * 16)].get(tileNum)));
				else if(xFlip) {
					return horizontalFlip(renderedTiles[palette+(time * 16)].get(tileNum));
				} else if(yFlip) {
					return verticalFlip(renderedTiles[palette+(time * 16)].get(tileNum));
				}
				return renderedTiles[palette+(time * 16)].get(tileNum);
			}
		} else if(palette < 13) {
			if(customRenderedTiles[(palette-DataStore.MainTSPalCount)+(time * 16)].containsKey(tileNum)) {
				if(xFlip && yFlip)
					return verticalFlip(horizontalFlip(customRenderedTiles[(palette-DataStore.MainTSPalCount)+(time * 16)].get(tileNum)));
				else if(xFlip) {
					return horizontalFlip(customRenderedTiles[(palette-DataStore.MainTSPalCount)+(time * 16)].get(tileNum));
				} else if(yFlip) {
					return verticalFlip(customRenderedTiles[(palette-DataStore.MainTSPalCount)+(time * 16)].get(tileNum));
				}
				return customRenderedTiles[(palette-DataStore.MainTSPalCount)+(time * 16)].get(tileNum);
			}
		} else {
			return new BufferedImage(8,8,BufferedImage.TYPE_INT_ARGB);
		}
		
		int x = ((tileNum) % (128 / 8)) * 8;
		int y = ((tileNum) / (128 / 8)) * 8;
		BufferedImage toSend = new BufferedImage(8,8,BufferedImage.TYPE_INT_ARGB);
		try {
			toSend =  bi[time][palette].getSubimage(x, y, 8, 8);
		} catch(Exception ignored) {}
		if(palette < DataStore.MainTSPalCount || renderedTiles.length > DataStore.MainTSPalCount)
			renderedTiles[palette+(time * 16)].put(tileNum, toSend);
		else
			customRenderedTiles[(palette-DataStore.MainTSPalCount)+(time * 16)].put(tileNum, toSend);

		if(!xFlip && !yFlip)
			return toSend;
		if(xFlip)
			toSend = horizontalFlip(toSend);
		if(yFlip)
			toSend = verticalFlip(toSend);
		return toSend;
	}
	
	public Palette[] getPalette(int time)
	{
		return palettes[time];
	}
	
	public Palette[][] getROMPalette()
	{
		return palettesFromROM.clone(); //No touchy the real palette!
	}
	
	public void resetPalettes()
	{
		palettes = getROMPalette();
	}
	
	public void setPalette(Palette[] pal, int time)
	{
		palettes[time] = pal;
	}
	
	public void setPalette(Palette pal, int index, int time)
	{
		palettes[time][index] = pal;
	}
	
	public void rerenderTileSet(int palette, int time) {
			bi[time][palette] = image.getBufferedImageFromPal(palettes[time][palette]);
	}
	
	public void renderPalettedTiles() {
		for(int j = 0; j < 4; j++) {
			for (int i = 0; i < 16; i++) {
				bi[j][i] = image.getBufferedImageFromPal(palettes[j][i]);
			}
		}
		for(int j = 0; j < 4; j++)
			for(int i = 0; i < 16; i++)
				rerenderTileSet(i,j);
	}
	public void resetCustomTiles() {
		customRenderedTiles = new HashMap[16*4];
		for(int i = 0; i < 16*4; i++)
			customRenderedTiles[i] = new HashMap<Integer,BufferedImage>();
	}
	
    private BufferedImage horizontalFlip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(w, h, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();
        return dimg;
    }
 
    private BufferedImage verticalFlip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(w, h, img.getColorModel()
                .getTransparency());
        Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return dimg;
    }

	public BufferedImage getTileSet(int palette, int time)
	{
		return bi[time][palette];
	}
	
	public BufferedImage getIndexedTileSet(int palette, int time) {
		return image.getIndexedImage(palettes[time][palette], true);
	}
	
	public TilesetHeader getTilesetHeader()
	{
		return tilesetHeader;
	}

	public GBARom getROM()
	{
		return rom;
	}
	
	private class TileLoader extends Thread implements Runnable {
		HashMap<Integer,BufferedImage>[] buffer;
		int pal;
		public TileLoader(HashMap<Integer,BufferedImage>[] hash, int palette) {
			buffer = hash;
			pal = palette;
		}
		
		@Override
		public void run() {
			int k = (tilesetHeader.isPrimary ? DataStore.MainTSSize : DataStore.LocalTSSize);
			for (int i = 0; i < 1023; i++) {
				try {
					buffer[pal].put(i, getTile(i, pal, false, false, 0));
				} catch (Exception e) {
					System.out.println("An error occured while writing tile " + i + " with palette " + pal);
				}
			}
		}
	}

	public void save() {
		//Caused issues last time I tested it
		for(int j = 0; j < 1; j++) {
			for (int i = 0; i < (tilesetHeader.isPrimary ? DataStore.MainTSPalCount : 16); i++) {
				rom.Seek(((int) tilesetHeader.pPalettes) + (32 * i + (j * 0x200)));
				palettes[j][i].save(rom);
			}
		}
		tilesetHeader.save();
	}
}