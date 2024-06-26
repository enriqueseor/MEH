package us.plxhack.MEH.IO.Connection;

import org.zzl.minegaming.GBAUtils.BitConverter;
import org.zzl.minegaming.GBAUtils.DataStore;
import org.zzl.minegaming.GBAUtils.GBARom;
import us.plxhack.MEH.IO.Map.MapHeader;
import us.plxhack.MEH.IO.Map.MapIO;
import us.plxhack.MEH.Structures.ConnectionType;
import us.plxhack.MEH.UI.MainGUI;

import java.util.ArrayList;

public class ConnectionData {

	private int originalSize;
	private final GBARom rom;
	private final MapHeader mapHeader;
	public long pNumConnections;
	public long pData;
	public ArrayList<Connection> aConnections;
	
	public ConnectionData(GBARom rom, MapHeader mHeader) {
		this.rom = rom;
		mapHeader = mHeader;
		load();
	}
	
	public void load() {
		rom.Seek(BitConverter.shortenPointer(mapHeader.pConnect));
		pNumConnections = rom.getPointer(true);
		pData = rom.getPointer(true);
		aConnections = new ArrayList<>();
		rom.Seek(BitConverter.shortenPointer(pData));
		for(int i = 0; i < pNumConnections; i++) {
			aConnections.add(new Connection(rom));
		}
		originalSize = getConnectionDataSize();
	}
	
	public void save() {
		if(pData < 0x08000000)
			pData += 0x08000000;
		
		rom.Seek(BitConverter.shortenPointer(mapHeader.pConnect));
		rom.writePointer(pNumConnections);
		rom.writePointer(pData);
		
		rom.Seek(BitConverter.shortenPointer(pData));
		for(int i = 0; i < pNumConnections; i++) {
			aConnections.get(i).save();
		}
	}
	
	public int getConnectionDataSize()
	{
		return aConnections.size() * 12;
	}
	
	public void addConnection(ConnectionType c, byte bank, byte map) {
		pNumConnections++;
		aConnections.add(new Connection(rom, c,bank,map));
		rom.floodBytes(BitConverter.shortenPointer(pData), rom.freeSpaceByte, originalSize);
		if(originalSize < getConnectionDataSize()) {
			pData = rom.findFreespace(DataStore.FreespaceStart, getConnectionDataSize());
		}
		MainGUI.connectionEditorPanel.loadConnections(MapIO.loadedMap);
	}
}