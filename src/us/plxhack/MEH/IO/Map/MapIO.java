package us.plxhack.MEH.IO.Map;

import org.zzl.minegaming.GBAUtils.DataStore;
import org.zzl.minegaming.GBAUtils.ROMManager;

import us.plxhack.MEH.IO.BankLoader;
import us.plxhack.MEH.IO.BorderMap;
import us.plxhack.MEH.IO.Render.BlockRenderer;
import us.plxhack.MEH.IO.Tile.TilesetCache;
import us.plxhack.MEH.MapElements.WildData.WildData;
import us.plxhack.MEH.MapElements.WildData.WildDataCache;
import us.plxhack.MEH.Plugins.PluginManager;
import us.plxhack.MEH.UI.MainGUI;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;

public class MapIO {

	public static Map loadedMap;
	public static BorderMap borderMap;
	public static int selectedBank = 0;
	public static int selectedMap = 0;
	public static int currentBank = 0;
	public static int currentMap = 0;
	public static boolean doneLoading = false;
	public static WildData wildData;
	public static boolean DEBUG = false;
	public static BlockRenderer blockRenderer = new BlockRenderer();

	public static void loadMap(int bank, int map) {
		selectedMap = map;
		selectedBank = bank;
		loadMap();
	}

	public static void loadMap() {
		long offset = BankLoader.maps[selectedBank].get(selectedMap);
		loadMapFromPointer(offset, false);
        MainGUI.updateTree();
	}

	public static void loadMapFromPointer(long offs, boolean justPointer) {
		MainGUI.setStatus("Loading Map...");
		final long offset = offs;
		if (!justPointer) {
			currentBank = -1;
			currentMap = -1;
		}

		new Thread(() -> {
            doneLoading = false;
            if (loadedMap != null)
                TilesetCache.get(loadedMap.getMapData().globalTileSetPtr).resetCustomTiles();

            loadedMap = new Map(ROMManager.getActiveROM(), (int) (offset));
            currentBank = selectedBank;
            currentMap = selectedMap;
            TilesetCache.switchTileset(loadedMap);

            borderMap = new BorderMap(ROMManager.getActiveROM(), loadedMap);
            MainGUI.mapEditorPanel.setGlobalTileset(TilesetCache.get(loadedMap.getMapData().globalTileSetPtr));
            MainGUI.mapEditorPanel.setLocalTileset(TilesetCache.get(loadedMap.getMapData().localTileSetPtr));
            MainGUI.eventEditorPanel.setGlobalTileset(TilesetCache.get(loadedMap.getMapData().globalTileSetPtr));
            MainGUI.eventEditorPanel.setLocalTileset(TilesetCache.get(loadedMap.getMapData().localTileSetPtr));

            MainGUI.tileEditorPanel.setGlobalTileset(TilesetCache.get(loadedMap.getMapData().globalTileSetPtr));
            MainGUI.tileEditorPanel.setLocalTileset(TilesetCache.get(loadedMap.getMapData().localTileSetPtr));
            MainGUI.tileEditorPanel.DrawTileset();
            MainGUI.tileEditorPanel.repaint();

            MainGUI.mapEditorPanel.setMap(loadedMap);
            MainGUI.mapEditorPanel.DrawMap();
            MainGUI.mapEditorPanel.DrawMovementPerms();
            MainGUI.mapEditorPanel.repaint();

            MainGUI.eventEditorPanel.setMap(loadedMap);
            MainGUI.eventEditorPanel.Redraw = true;
            MainGUI.eventEditorPanel.DrawMap();
            MainGUI.eventEditorPanel.repaint();
            MainGUI.borderTileEditor.setGlobalTileset(TilesetCache.get(loadedMap.getMapData().globalTileSetPtr));
            MainGUI.borderTileEditor.setLocalTileset(TilesetCache.get(loadedMap.getMapData().localTileSetPtr));
            MainGUI.borderTileEditor.setMap(borderMap);
            MainGUI.borderTileEditor.repaint();
            MainGUI.connectionEditorPanel.loadConnections(loadedMap);
            MainGUI.connectionEditorPanel.repaint();
            try {
                wildData = (WildData) WildDataCache.getWildData(currentBank, currentMap).clone();
            } catch (Exception ignored) {}

            MainGUI.loadWildPokemon();
            MainGUI.mapEditorPanel.repaint();
            doneLoading = true;
            PluginManager.fireMapLoad(selectedBank, selectedMap);
        }).start();
        MainGUI.setStatus(MainGUI.mapBanks.getLastSelectedPathComponent().toString() + " loaded.");
	}

	public static String[] pokemonNames;

	public static void loadPokemonNames() {
		pokemonNames = new String[DataStore.NumPokemon];
		ROMManager.currentROM.Seek(ROMManager.currentROM.getPointerAsInt(DataStore.SpeciesNames));
		for (int i = 0; i < DataStore.NumPokemon; i++) {
			pokemonNames[i] = ROMManager.currentROM.readPokeText();
			System.out.println(pokemonNames[i]);
		}
		addStringArray(MainGUI.pkName1, pokemonNames);
		addStringArray(MainGUI.pkName2, pokemonNames);
		addStringArray(MainGUI.pkName3, pokemonNames);
		addStringArray(MainGUI.pkName4, pokemonNames);
		addStringArray(MainGUI.pkName5, pokemonNames);
		addStringArray(MainGUI.pkName6, pokemonNames);
		addStringArray(MainGUI.pkName7, pokemonNames);
		addStringArray(MainGUI.pkName8, pokemonNames);
		addStringArray(MainGUI.pkName9, pokemonNames);
		addStringArray(MainGUI.pkName10, pokemonNames);
		addStringArray(MainGUI.pkName11, pokemonNames);
		addStringArray(MainGUI.pkName12, pokemonNames);
	}

	public static void openScript(int scriptOffset) {
		if (DataStore.mehSettingCallScriptEditor == null || DataStore.mehSettingCallScriptEditor.isEmpty()) {
			int reply = JOptionPane.showConfirmDialog(null, "It appears that you have no script editor registered with MEH. Would you like to search for one?", "You need teh Script Editorz!!!", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				FileDialog fd = new FileDialog(new Frame(), "Choose your script editor...", FileDialog.LOAD);
				fd.setFilenameFilter((dir, name) -> ((System.getProperty("os.name").toLowerCase().contains("win") ? name.toLowerCase().endsWith(".exe") : name.toLowerCase().endsWith(".*")) || name.toLowerCase().endsWith(".jar")));
				fd.setVisible(true);
				String location = fd.getDirectory() + fd.getFile();
				if (location.isEmpty())
					return;
                DataStore.mehSettingCallScriptEditor = location;
			}
		}
		try {
			Runtime r = Runtime.getRuntime();
			String s = (DataStore.mehSettingCallScriptEditor.toLowerCase().endsWith(".jar") ? "java -jar " : "") + DataStore.mehSettingCallScriptEditor + " \"" + ROMManager.currentROM.input_filepath.replace("\"", "") + "\" 0x" + String.format("%x", scriptOffset);
			r.exec(s);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "It seems that your script editor has gone missing. Look around for it and try it again. I'm sure it'll work eventually.");
		}
	}

	public static void addStringArray(JComboBox<String> wildPokemon, String[] strings) {
		if (wildPokemon != null) {
			wildPokemon.removeAllItems();
			for (String s : strings) {
				wildPokemon.addItem(s);
			}
			wildPokemon.repaint();
		} else {
			System.out.println("Error: The JComboBox instance is null. MapIO.class Line157");
		}
	}


	public static void repaintTileEditorPanel() {
		MainGUI.tileEditorPanel.repaint();
	}

	public static void saveMap() {
		MapIO.loadedMap.save();
		MapIO.borderMap.save();
		TilesetCache.get(MapIO.loadedMap.getMapData().globalTileSetPtr).save();
		TilesetCache.get(MapIO.loadedMap.getMapData().localTileSetPtr).save();
		MainGUI.connectionEditorPanel.save(); // Save surrounding maps
		WildDataCache.setWildData(currentBank, currentMap, wildData);
		PluginManager.fireMapSave(MapIO.currentBank, MapIO.currentMap);
	}
	
	public static void saveROM() {
		PluginManager.fireROMSave();
		
		WildDataCache.save();
		ROMManager.getActiveROM().commitChangesToROMFile();
	}
	
	public static void saveAll() {
		saveMap();
		saveROM();
	}
}