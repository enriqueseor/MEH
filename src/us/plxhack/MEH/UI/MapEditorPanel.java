package us.plxhack.MEH.UI;

import org.zzl.minegaming.GBAUtils.BitConverter;
import org.zzl.minegaming.GBAUtils.DataStore;

import us.plxhack.MEH.IO.Map.Map;
import us.plxhack.MEH.IO.Map.MapIO;
import us.plxhack.MEH.IO.Tile.Tileset;
import us.plxhack.MEH.Structures.EditMode;
import us.plxhack.MEH.Structures.MapTile;

import javax.swing.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class MapEditorPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static MapEditorPanel instance = null;

	public static MapEditorPanel getInstance() {
		if (instance == null) {
			instance = new MapEditorPanel();
		}
		return instance;
	}
	
	public static class SelectRect extends Rectangle {
		int startX;
		int startY;
		int realWidth;
		int realHeight;
		
		public SelectRect(int i, int j, int k, int l) {
			super(i,j,k,l);
			startX = i;
			startY = j;
			realWidth = k;
			realHeight = l;
		}
	}

	private static final long serialVersionUID = -877213633894324075L;
	private Tileset globalTiles;
	private Tileset localTiles;
	public Map map;
	static Rectangle mouseTracker;
	public static boolean Redraw = true;
	public static boolean renderPalette = false;
	public static boolean renderTileset = false;
	public static MapTile[][] selectBuffer;
	public static int bufferWidth = 1;
	public static int bufferHeight = 1;
	public static SelectRect selectBox;
	public Color selectRectColor = MainGUI.uiSettings.cursorColor;
	private static EditMode currentMode = EditMode.TILES;
 
	public MapEditorPanel() {
		mouseTracker = new Rectangle(0,0,16,16);
		selectBox = new SelectRect(0,0,16,16);
		selectBuffer = new MapTile[1][1];
		selectBuffer[0][0] = new MapTile(0,0xC);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		if (map == null || !isInBounds(e.getX(),e.getY()))
			return;
		
		if(MapIO.DEBUG)
			System.out.println(e.getButton());

		int x = selectBox.x / 16;
		int y = selectBox.y / 16;
		
		if(e.getButton() == 1) {
			selectRectColor = MainGUI.uiSettings.markerColor;
			drawTiles(x, y);
			map.isEdited = true;
		}
		else if(e.getButton() == 3) {
			selectBox = new SelectRect(x * 16,y * 16,16,16);
			selectRectColor = MainGUI.uiSettings.cursorSelectColor;
			
			if(currentMode == EditMode.TILES) {
				MainGUI.tileEditorPanel.baseSelectedTile = map.getMapTileData().getTile(x, y).getID();
				MainGUI.lblTileVal.setText("Current Tile: 0x" + BitConverter.toHexString(MainGUI.tileEditorPanel.baseSelectedTile));
			}
			else if(currentMode == EditMode.MOVEMENT) {
				PermissionTilePanel.baseSelectedTile = map.getMapTileData().getTile(x, y).getMeta();
				MainGUI.lblTileVal.setText("Current Perm: 0x" + BitConverter.toHexString(MainGUI.tileEditorPanel.baseSelectedTile));
			}
		}
		MainGUI.setMouseCoordinates(mouseTracker.x / 16, mouseTracker.y / 16);
        repaint();
	}
	
	public void mouseReleased(MouseEvent e) {
		if (map == null)
            return;
		
		selectRectColor = MainGUI.uiSettings.cursorColor;
		
		if(e.getButton() == 3) {
			if(isInBounds(e.getX(),e.getY())) {
				calculateSelectBox(e,selectBox);
				//Fill the tile buffer
				selectBuffer = new MapTile[selectBox.width / 16][selectBox.height / 16];
				bufferWidth = selectBox.width / 16;
				bufferHeight = selectBox.height / 16;
				for(int x = 0; x < bufferWidth; x++)
					for(int y = 0; y < bufferHeight; y++)
						selectBuffer[x][y] = (MapTile)map.getMapTileData().getTile(selectBox.x / 16 + x, selectBox.y / 16 + y).clone();
			}
		}
        repaint();
	}

	public void mouseEntered(MouseEvent e) {
		if (map == null)
            return;
		
		if(isInBounds(mouseTracker.x,mouseTracker.y))
			MainGUI.setMouseCoordinates(mouseTracker.x / 16, mouseTracker.y / 16);
        repaint();
	}

	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {
		mouseTracker.x = e.getX();
		mouseTracker.y = e.getY();
		
		if(map == null || !isInBounds(mouseTracker.x,mouseTracker.y))
			return;
		
		int x = (mouseTracker.x / 16);
		int y = (mouseTracker.y / 16);
		
		if(MapIO.DEBUG)
			System.out.println(x + " " + y);

		if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK)  {
			drawTiles(x, y);
			moveSelectRect(e);
			map.isEdited = true;
		}
		else
			if(isInBounds(e.getX(),e.getY()))
				calculateSelectBox(e,selectBox);
		
		MainGUI.setMouseCoordinates(x, y);
        repaint();
	}

	public void mouseMoved(MouseEvent e) {
		if(map == null || !isInBounds(e.getX(),e.getY()))
			return;
		
		moveSelectRect(e);
		repaint();
	}

	public boolean isInBounds(int x, int y) {
        return !(x < 0 | x >= (map.getMapData().mapWidth * 16) | y < 0 | y >= (map.getMapData().mapHeight * 16));
    }
	
	public void moveSelectRect(MouseEvent e) {
		if(isInBounds(e.getX(),e.getY()))
		{
			mouseTracker.x = e.getX();
			mouseTracker.y = e.getY();
			MainGUI.setMouseCoordinates(mouseTracker.x / 16, mouseTracker.y / 16);
		}
        
        selectBox.x = ((mouseTracker.x / 16) * 16);
        selectBox.y = ((mouseTracker.y / 16) * 16);
        selectBox.startX = ((mouseTracker.x / 16) * 16);
        selectBox.startY = ((mouseTracker.y / 16) * 16);
        
        if(selectBox.realWidth + selectBox.x > (map.getMapData().mapWidth * 16 - 1))
        	selectBox.width = (int) ((map.getMapData().mapWidth * 16) - selectBox.x);
        else
        	selectBox.width = selectBox.realWidth;
        if(selectBox.realHeight + selectBox.y > (map.getMapData().mapHeight * 16 - 1))
        	selectBox.height = (int) ((map.getMapData().mapHeight * 16) - selectBox.y);
        else
        	selectBox.height = selectBox.realHeight;
	}
	
	public void drawTiles(int x, int y) {
		for(int DrawX=0; DrawX < bufferWidth; DrawX++) {
			for(int DrawY = 0; DrawY < bufferHeight; DrawY++) {
				if (selectBox.x + DrawX * 16L < (map.getMapData().mapWidth * 16 - 1) && selectBox.y + DrawY * 16L < (map.getMapData().mapHeight * 16 - 1)) {
				//Tiles multi-select will grab both the tiles and the meta, while movement editing will only select metas.
					if(currentMode == EditMode.TILES) {
						map.getMapTileData().getTile(selectBox.x/16 + DrawX, selectBox.y/16 + DrawY).SetID(selectBuffer[DrawX][DrawY].getID());
						if(selectBuffer[DrawX][DrawY].getMeta() >= 0)
							map.getMapTileData().getTile(selectBox.x/16 + DrawX, selectBox.y/16 + DrawY).SetMeta(selectBuffer[DrawX][DrawY].getMeta()); //TODO Allow for tile-only selection. Hotkeys?
						drawTile(selectBox.x/16 + DrawX,selectBox.y/16 + DrawY);
					}
					else if(currentMode == EditMode.MOVEMENT) {
						map.getMapTileData().getTile(selectBox.x/16+DrawX, selectBox.y/16+DrawY).SetMeta(selectBuffer[DrawX][DrawY].getMeta());
						drawTile(selectBox.x/16+DrawX,selectBox.y/16+DrawY);
					}
				}
			}
		}
	}

	public static void calculateSelectBox(MouseEvent e, SelectRect givenBox) {
		//Round the values to multiples of 16
		int x = (e.getX() / 16) * 16;
		int y = (e.getY() / 16) * 16;
		givenBox.startX = ((givenBox.startX / 16) * 16);
		givenBox.startY = ((givenBox.startY / 16) * 16);;
		
		//If our selection is negative, adjust it to be positive starting from the position the mouse was released
		if (givenBox.realWidth < 0)
			givenBox.x = x;
		else
			givenBox.x = givenBox.startX;
		if (givenBox.realHeight < 0)
			givenBox.y = y;
		else
			givenBox.y = givenBox.startY;
		
		givenBox.width = givenBox.realWidth = Math.abs(givenBox.realWidth) + 16;
		givenBox.height = givenBox.realHeight = Math.abs(givenBox.realHeight) + 16;
		
		//Minimum sizes
		if(givenBox.realWidth == 0)
			givenBox.width = givenBox.realWidth = 16;
		if(givenBox.realHeight == 0)
			givenBox.height = givenBox.realHeight = 16;
	}

	public void setGlobalTileset(Tileset global) {
		globalTiles = global;
		MapIO.blockRenderer.setGlobalTileset(global);
	}

	public void setLocalTileset(Tileset local) {
		localTiles = local;
		MapIO.blockRenderer.setLocalTileset(local);
	}

	public void setMap(Map m) {
		map = m;
		Dimension size = new Dimension();
		size.setSize((int) (m.getMapData().mapWidth + 1) * 16,(int) (m.getMapData().mapHeight + 1) * 16);
		setPreferredSize(size);
		this.setSize(size);
	}

	public static Graphics gcBuff;
	static  Image imgBuffer = null;
	static Image permImgBuffer = null;

	public void DrawMap() {
		imgBuffer = Map.renderMap(map, true);
	}
	
	public void DrawMovementPerms() {
		try {
			permImgBuffer = createImage((int) map.getMapData().mapWidth * 16,
					(int) map.getMapData().mapHeight * 16);
			for (int y = 0; y < map.getMapData().mapHeight; y++) {
				for (int x = 0; x < map.getMapData().mapWidth; x++) {
					drawTile(x,y,EditMode.MOVEMENT);
				}
			}
		}
		catch (Exception e) {
			if(MapIO.DEBUG)
				e.printStackTrace();
		}
	}
	
	void drawTile(int x, int y) {
		drawTile(x,y,currentMode);
	}
	
	void drawTile(int x, int y, EditMode m) {
		if(m == EditMode.TILES) {
			gcBuff = imgBuffer.getGraphics();
			int TileID=(map.getMapTileData().getTile(x, y).getID());
			int srcX=(TileID % TileEditorPanel.editorWidth) * 16;
			int srcY = (TileID / TileEditorPanel.editorWidth) * 16;
			gcBuff.drawImage(((BufferedImage)(TileEditorPanel.imgBuffer)).getSubimage(srcX, srcY, 16, 16), x * 16, y * 16, this);
		}
		else if(m == EditMode.MOVEMENT) {
			gcBuff = permImgBuffer.getGraphics();
			int TileMeta=(map.getMapTileData().getTile(x, y).getMeta());
			
			//Clear the rectangle since transparency can draw ontop of itself
			((Graphics2D)gcBuff).setBackground(new Color(255,255,255,0));
			gcBuff.clearRect(x * 16, y * 16, 16, 16);
			gcBuff.drawImage(((BufferedImage)(PermissionTilePanel.imgPermissions)).getSubimage(TileMeta*16, 0, 16, 16), x * 16, y * 16, this);
		}
		gcBuff.finalize();
        repaint();
	}
	
	public static Image getMapImage() {
		return imgBuffer;
	}

	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		if (globalTiles != null) {
			if(MapEditorPanel.Redraw==true) {
				try {
					DrawMap();
				}
				catch (Exception e) {
                    e.printStackTrace();
                }
				DrawMovementPerms();
				MapEditorPanel.Redraw=false;
			}
			if(currentMode != EditMode.TILES) {
				Graphics2D g2 = (Graphics2D)graphics;
				g2.drawImage(imgBuffer, 0, 0, this);
				AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, DataStore.mehPermissionTranslucency);
				g2.setComposite(ac);
				g2.drawImage(permImgBuffer, 0, 0, this);
			}
			else
				graphics.drawImage(imgBuffer, 0, 0, this);

			if(renderPalette) {
				int x = 0;
				for(int i = 0; i < 16; i++) {
					while(x < 16) {
						try {
							graphics.setColor(globalTiles.getPalette(MapIO.blockRenderer.currentTime)[i].getIndex(x));
							graphics.fillRect(x*8, i*8, 8, 8);
						}
						catch (Exception e) {
                            e.printStackTrace();
                        }
						x++;
					}
					x = 0;
				}

				x = 0;
				for(int i = 0; i < 16; i++) {
					while(x < 16) {
						try {
							graphics.setColor(localTiles.getPalette(MapIO.blockRenderer.currentTime)[i].getIndex(x));
							graphics.fillRect(128+x*8, i*8, 8, 8);
						}
						catch(Exception e) {
                            e.printStackTrace();
                        }
						x++;
					}
					x = 0;
				}
			}
			
			if(renderTileset) {
				graphics.drawImage(MainGUI.tileEditorPanel.RerenderTiles(TileEditorPanel.imgBuffer, 255),0,0,this);
			}
			graphics.setColor(selectRectColor);
			if (mouseTracker.width < 0)
				mouseTracker.x -= Math.abs(mouseTracker.width);
			if (mouseTracker.height < 0)
				mouseTracker.y -= Math.abs(mouseTracker.height);
			try {
				graphics.drawRect(selectBox.x,selectBox.y,selectBox.width-1,selectBox.height-1);
			}
            catch(Exception e) 
            {
                e.printStackTrace();
            }
		}
	}

	public void reset() {
		globalTiles = null;
		localTiles = null;
		map = null;
		mouseTracker.x = 0;
		mouseTracker.y = 0;
		MainGUI.setMouseCoordinates(0, 0);
	    
	    selectBox.x = 0;
	    selectBox.y = 0;
	    selectBox.startX = 0;
	    selectBox.startY = 0;
	    selectBox.width = 16;
	    selectBox.height = 16;
	    selectBox.realWidth = 16;
	    selectBox.realHeight = 16;
		repaint();
	}

	public static void setMode(EditMode tiles) {
		currentMode = tiles;
	}

	public static EditMode getMode() {
		return currentMode;
	}
}
