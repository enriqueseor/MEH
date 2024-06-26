package us.plxhack.MEH.UI;

import org.zzl.minegaming.GBAUtils.ROMManager;
import us.plxhack.MEH.IO.BankLoader;
import us.plxhack.MEH.IO.Map.Map;

import javax.swing.*;
import java.awt.*;

public class PanelMapPreview extends JPanel {

	private static final long serialVersionUID = 3799250208011044425L;
	Image mapBuf;

    public PanelMapPreview()
	{
		super();
	}
	
	public void setMap(int bank, int map) {
		long offset=BankLoader.maps[bank].get(map);
		mapBuf = Map.renderMap(new Map(ROMManager.currentROM,(int)(offset)), true);
        int scale = 4;
        mapBuf = mapBuf.getScaledInstance(mapBuf.getWidth(this) / scale, mapBuf.getHeight(this) / scale, Image.SCALE_SMOOTH);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(mapBuf != null)
			g.drawImage(mapBuf,(this.getWidth() / 2) - (mapBuf.getWidth(this) / 2), (this.getHeight() / 2) - (mapBuf.getHeight(this) / 2),this);
	}
}