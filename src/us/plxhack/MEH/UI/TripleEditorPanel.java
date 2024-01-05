package us.plxhack.MEH.UI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.zzl.minegaming.GBAUtils.DataStore;
import org.zzl.minegaming.GBAUtils.ROMManager;

import us.plxhack.MEH.IO.Block;
import us.plxhack.MEH.IO.Tileset;
import us.plxhack.MEH.IO.Render.BlockRenderer;

public class TripleEditorPanel extends JPanel {

    private final BlockRenderer blockRenderer = new BlockRenderer();
	private Block block;
	private int mouseX = 0;
	private int mouseY = 0;
	private final BlockEditor host;
 
	public TripleEditorPanel(BlockEditor hostess) {
		this.host = hostess;
		this.block = new Block(0, ROMManager.getActiveROM());
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				int x = e.getX() / 16;
				int y = e.getY() / 16;

				if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK) {
	                mouseX = e.getX() / 16;
	                mouseY = e.getY() / 16;
					int bufWidth = (Math.min(TilesetPickerPanel.bufferWidth, 4));
					int bufHeight = (Math.min(TilesetPickerPanel.bufferHeight, 2));
					for(int DrawX=0; DrawX < bufWidth; DrawX++)
						for(int DrawY=0; DrawY < bufHeight; DrawY++)
							block.setTile(DrawX + x, DrawY + y, TilesetPickerPanel.selectBuffer[DrawX][DrawY]);
					block.save();
					host.rerenderTiles();
				}
				else {
					TilesetPickerPanel.calculateSelectBox(e);
				}
                repaint();
			}

			public void mouseMoved(MouseEvent e) {
				if(block == null) return;
				mouseX = (e.getX()  / 16);
				mouseY = (e.getY() / 16);
				if(mouseX > 4)
					mouseX = 4;
				if(mouseY > 2)
					mouseY = 2;
				if(mouseX < 0)
					mouseX = 0;
				if(mouseY < 0)
					mouseY = 0;
				repaint();
			}
		});

		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e)
			{
				mousePressed(e);
			}
			public void mousePressed(MouseEvent e) {
				TileEditorPanel.tripleSelectMode = true;
				if(!DataStore.mehMetTripleTiles) {
					JOptionPane.showMessageDialog(null, "You have entered the triple-tile selection mode! Select a block in the pane on the left to be your third tile.\n\nIf you want to remove a third layer, select block 0.");
					DataStore.meetTripleTiles();
				}
			}
			public void mouseReleased(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
		});
	}

	public void setBlock(Block b) {
		block = b;
		repaint();
	}
	
	public Block getBlock()
	{
		return block;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int y = 0; y < 2; y++) {
			for (int x = 2; x < 4; x++) {
				if (block.getTile(x, y).getTileNumber() < DataStore.MainTSSize) {
					g.setColor(host.tpp.global.getPalette(BlockRenderer.currentTime)[block.getTile(x, y).getPaletteNum()].getIndex(0)); //Set default background color
					g.drawImage(host.tpp.global.getTile(block.getTile(x, y).getTileNumber(), block.getTile(x, y).getPaletteNum(), block.getTile(x, y).xFlip, block.getTile(x, y).yFlip, BlockRenderer.currentTime).getScaledInstance(10, 10, Image.SCALE_FAST), (x-2) * 10, y * 10, null);
				} else {
					g.setColor(host.tpp.local.getPalette(BlockRenderer.currentTime)[block.getTile(x, y).getPaletteNum()].getIndex(0)); //Set default background color
					g.drawImage(host.tpp.local.getTile(block.getTile(x, y).getTileNumber() - DataStore.MainTSSize, block.getTile(x, y).getPaletteNum(), block.getTile(x, y).xFlip, block.getTile(x, y).yFlip , BlockRenderer.currentTime).getScaledInstance(10, 10, Image.SCALE_FAST), (x-2) * 16, y * 16, null);
				}
			}
		}
	}

	public void reset() {
        Tileset globalTiles = null;
        Tileset localTiles = null;
		block = null;
	}
}