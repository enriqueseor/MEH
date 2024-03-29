package us.plxhack.MEH.UI;

import org.zzl.minegaming.GBAUtils.*;

import us.plxhack.MEH.Globals.UISettings;
import us.plxhack.MEH.Globals.Version;
import us.plxhack.MEH.IO.BankLoader;
import us.plxhack.MEH.IO.Map;
import us.plxhack.MEH.IO.MapIO;
import us.plxhack.MEH.IO.Render.BlockRenderer;
import us.plxhack.MEH.IO.TilesetCache;
import us.plxhack.MEH.MapElements.WildDataCache;
import us.plxhack.MEH.MapElements.WildDataType;
import us.plxhack.MEH.Plugins.Plugin;
import us.plxhack.MEH.Plugins.PluginManager;
import us.plxhack.MEH.Structures.ConnectionType;
import us.plxhack.MEH.Structures.EditMode;
import us.plxhack.MEH.Structures.EventType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class MainGUI extends JFrame {

    public static  UISettings uiSettings;
	public int paneSize = 0;
	public static JLabel lblInfo;
    public static JLabel lblX;
    public static JLabel lblY;
	public static JTree mapBanks;
	private static int eventIndex;
	private static EventType eventType;
	public static int currentType = 0;
	public static int selectedTime = 0;
	private static int popupX;
	private static int popupY;
	private JTabbedPane editorTabs;
	private JSplitPane splitPane;
	public static JLabel lblWidth;
	public static JLabel lblBorderTilesPointer;
	public static JLabel lblBorderWidth;
	public static JLabel lblLocalTilesetPointer;
	public static JLabel lblMapTilesPointer;
	public static JLabel lblHeight;
	public static JLabel lblBorderHeight;
	public static JLabel lblGlobalTilesetPointer;
	public static JLabel lblEncounterPercent;

	public static JPanel eventScrollPanel;
	private JMenuItem mnOpen;
	private JMenuItem mnSave;
	private JMenuItem mnSaveMap;
	private JMenuItem mnSaveMapToPNG;
	private JMenuItem mnExit;
    private JMenu mnHelp;
    private JMenuItem mnAbout;
    private PermissionTilePanel permissionTilePanel;
	private JScrollPane movementScrollPane;
	private final JPanel connectionsTabPanel;
	private final JPanel connectionsInfoPanel;
	private final JScrollPane connectionsEditorScroll;
	private JLabel lblNewLabel;
	private JPanel panelWildEditor;
	private static JSpinner spnHeight;
	private static JSpinner spnWidth;
	private final JMenu mnAddCon;
	private final JMenuItem mntmLeftCon;
	private final JMenuBar mbMain;
	private final JMenuItem mntmRightCon;
	private final JMenuItem mntmUpCon;
	private final JMenuItem mntmDownCon;

	// Wild Data Editor
    private static JPanel pkEditorPanel;
	private static JSpinner pkMin1;
	private static JSpinner pkMax1;
	public static JComboBox pkName1;
	private static JSpinner pkNo1;
	private static JLabel lblpkmax;
	private static JLabel lblPkMn;
	private static JLabel lblpknum;
	private static JLabel lblpkchance;
	private static JLabel pkchance1;
	private static JSpinner pkMin2;
	private static JSpinner pkMax2;
	public static JComboBox pkName2;
	private static JSpinner pkNo2;
	private static JLabel pkchance2;
	private static JSpinner pkMin3;
	private static JSpinner pkMax3;
	public static JComboBox pkName3;
	private static JSpinner pkNo3;
	private static JLabel pkchance3;
	private static JSpinner pkMin4;
	private static JSpinner pkMax4;
	public static JComboBox pkName4;
	private static JSpinner pkNo4;
	private static JLabel pkchance4;
	private static JSpinner pkMin5;
	private static JSpinner pkMax5;
	public static JComboBox pkName5;
	private static JSpinner pkNo5;
	private static JLabel pkchance5;
	private static JPanel panelpk6_10;
	private static JSpinner pkMin6;
	private static JSpinner pkMax6;
	public static JComboBox pkName6;
	private static JSpinner pkNo6;
	private static JLabel pkchance6;
	private static JSpinner pkMin7;
	private static JSpinner pkMax7;
	public static JComboBox pkName7;
	private static JSpinner pkNo7;
	private static JLabel pkchance7;
	private static JSpinner pkMin8;
	private static JSpinner pkMax8;
	public static JComboBox pkName8;
	private static JSpinner pkNo8;
	private static JLabel pkchance8;
	private static JSpinner pkMin9;
	private static JSpinner pkMax9;
	public static JComboBox pkName9;
	private static JSpinner pkNo9;
	private static JLabel pkchance9;
	private static JSpinner pkMin10;
	private static JSpinner pkMax10;
	public static JComboBox pkName10;
	private static JSpinner pkNo10;
	private static JLabel pkchance10;
	private static JPanel panelpk11_12;
	private static JSpinner pkMin11;
	private static JSpinner pkMin12;
	private static JSpinner pkMax11;
	private static JSpinner pkMax12;
	public static JComboBox pkName11;
	public static JComboBox pkName12;
	private static JSpinner pkNo11;
	private static JSpinner pkNo12;
	private static JLabel pkchance11;
	private static JLabel pkchance12;
	private static JScrollPane scrollPaneWildEditor;
	private JLabel lblPkEnvironment;
	private JLabel lblPkDayTime;
	private static JComboBox pkTime;
	private JComboBox pkEnvironment;
	private static JButton btnEnableTimebasedPokemon;
	
	// Map Creation
	JPanel panelMapTilesContainer;
	JPanel splitterMapTiles;
	public static MapEditorPanel mapEditorPanel;
	public JScrollPane mapScrollPane;
	JScrollPane tilesetScrollPane;
	JPanel eventsPanel;

	private JPanel mimeEditorPanel;
	JPanel panelTilesContainer;
	JPanel wildPokemonPanel;
	JPanel panelPermissions;
	public static BorderEditorPanel borderTileEditor;
	public static EventEditorPanel eventEditorPanel;
	public static TileEditorPanel tileEditorPanel;
	public static ConnectionsEditorPanel connectionsEditorPanel;
	public static JLabel lblTileVal;
	static {lblTileVal = new JLabel();}
	public static JPopupMenu popupMenu;
	public DataStore dataStore;
	private JPanel editorPanel;

    public static JToolBar panelButtons;

    private static JSlider pkEncounter;

    JPanel mimePanel;// Mr. Mime 2 dirty 4 mii
    private JMenu mnPatches;
    private JMenuItem mntmDaynightPokemon;
    private JCheckBoxMenuItem mnEnableDebugging;

	void CreateBorderArea() {
		JPanel panelBorderTilesContainer = new JPanel();
		panelBorderTilesContainer.setPreferredSize(new Dimension(10, 70));
		panelMapTilesContainer.add(panelBorderTilesContainer, BorderLayout.NORTH);
		panelBorderTilesContainer.setLayout(new BorderLayout(0, 0));

		JPanel panelBorderTilesSplitter = new JPanel();
		panelBorderTilesSplitter.setBackground(SystemColor.controlShadow);
		panelBorderTilesSplitter.setPreferredSize(new Dimension(10, 1));
		panelBorderTilesContainer.add(panelBorderTilesSplitter, BorderLayout.SOUTH);
		// Set up tileset
		JPanel panelBorderTilesToAbsolute = new JPanel();
		panelBorderTilesContainer.add(panelBorderTilesToAbsolute, BorderLayout.CENTER);
		panelBorderTilesToAbsolute.setLayout(null);

		borderTileEditor = new BorderEditorPanel();
		borderTileEditor.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Border Tiles", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		borderTileEditor.setBounds(4, 0, 150, 65);
		panelBorderTilesToAbsolute.add(borderTileEditor);
	}

	void CreateEventsPanel() {
		eventsPanel = new JPanel();
		editorTabs.addTab("Events", null, eventsPanel, null);
		eventsPanel.setLayout(new BorderLayout(0, 0));
		JPanel splitm = new JPanel();
		splitm.setPreferredSize(new Dimension(4, 10));
		splitm.setMaximumSize(new Dimension(4, 32000));
		JPanel pmtc = new JPanel();

		pmtc.setLayout(new BorderLayout(0, 0));

		eventScrollPanel = new JPanel();
		eventScrollPanel.setPreferredSize(new Dimension(220, 10));
		JScrollPane selectedEventScroll = new JScrollPane(eventScrollPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		eventsPanel.add(selectedEventScroll, BorderLayout.EAST);
		eventScrollPanel.setLayout(new BorderLayout(0, 0));
		eventEditorPanel = new EventEditorPanel();

		popupMenu = new JPopupMenu();
		addPopup(eventEditorPanel, popupMenu);

		JMenuItem mntmEditScript = new JMenuItem("Edit Script");
		mntmEditScript.addActionListener(e -> {
            int offset = 0;
            switch (eventType) {
                case NPC:
                    offset = (int) MapIO.loadedMap.mapNPCManager.mapNPCs.get(eventIndex).pScript;
                    break;
                case SIGN:
                    offset = (int) MapIO.loadedMap.mapSignManager.mapSigns.get(eventIndex).pScript;
                    break;
                case TRIGGER:
                    offset = (int) MapIO.loadedMap.mapTriggerManager.mapTriggers.get(eventIndex).pScript;
                    break;
                default:
                    break;
            }

            if (offset != 0) {
                try {
                    String[] Params = new String[] { ROMManager.currentROM.input_filepath, Long.toHexString(offset) };
                    Process p = Runtime.getRuntime().exec(DataStore.mehSettingCallScriptEditor + " " + ROMManager.currentROM.input_filepath + ":" + Long.toHexString(offset));// ",Params);
                } catch (Exception e1) {
                    setStatus("Script failed to load, please check to see if " + DataStore.mehSettingCallScriptEditor + " exists");
                }
            }
        });
		popupMenu.add(mntmEditScript);

		JMenuItem mntmRemoveEvent = new JMenuItem("Remove Event");
		mntmRemoveEvent.addActionListener(e -> {
            if(MapIO.loadedMap.mapExitManager.getSpriteIndexAt(popupX, popupY) != -1) {
                int result = JOptionPane.showConfirmDialog(new JFrame(),"Removing warps is currently slightly buggy and may cause existing warps in the map to break.\nWe currently advise to just temporarily relocate warps while this is sorted out.\nAre you sure you want to remove this warp?","ZOMG R U CRAZY???", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION)
                    MapIO.loadedMap.mapExitManager.remove(popupX, popupY);
            }
            else if(MapIO.loadedMap.mapSignManager.getSpriteIndexAt(popupX, popupY) != -1) {
                MapIO.loadedMap.mapSignManager.remove(popupX, popupY);
            }
            else if(MapIO.loadedMap.mapNPCManager.getSpriteIndexAt(popupX, popupY) != -1) {
                MapIO.loadedMap.mapNPCManager.remove(popupX, popupY);
            }
            else if(MapIO.loadedMap.mapTriggerManager.getSpriteIndexAt(popupX, popupY) != -1) {
                MapIO.loadedMap.mapTriggerManager.remove(popupX, popupY);
            }

            eventEditorPanel.Redraw = true;
            eventEditorPanel.repaint();
        });
		popupMenu.add(mntmRemoveEvent);

		JMenu mnAdd = new JMenu("Add...");
		popupMenu.add(mnAdd);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Warp");
		mntmNewMenuItem_2.addActionListener(e -> {
            MapIO.loadedMap.mapExitManager.add(popupX, popupY);
            eventEditorPanel.Redraw = true;
            eventEditorPanel.repaint();
        });
		mnAdd.add(mntmNewMenuItem_2);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("NPC");
		mntmNewMenuItem_3.addActionListener(e -> {
            MapIO.loadedMap.mapNPCManager.add(popupX, popupY);
            eventEditorPanel.Redraw = true;
            eventEditorPanel.repaint();
        });
		mnAdd.add(mntmNewMenuItem_3);

		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Sign");
		mntmNewMenuItem_4.addActionListener(e -> {
            MapIO.loadedMap.mapSignManager.add(popupX, popupY);
            eventEditorPanel.Redraw = true;
            eventEditorPanel.repaint();
        });
		mnAdd.add(mntmNewMenuItem_4);

		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Trigger Script");
		mntmNewMenuItem_5.addActionListener(e -> {
            MapIO.loadedMap.mapTriggerManager.add(popupX, popupY);
            eventEditorPanel.Redraw = true;
            eventEditorPanel.repaint();
        });
		mnAdd.add(mntmNewMenuItem_5);
		eventEditorPanel.setLayout(null);

		JScrollPane eventScrollPane = new JScrollPane(eventEditorPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		eventsPanel.add(eventScrollPane, BorderLayout.CENTER);
		eventScrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		eventScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		eventScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
	}

	void CreateMapPanel() {
		editorPanel = new JPanel();
		editorTabs.addTab("Map", null, editorPanel, null);
		editorPanel.setLayout(new BorderLayout(0, 0));
		splitterMapTiles = new JPanel();
		splitterMapTiles.setPreferredSize(new Dimension(4, 10));
		splitterMapTiles.setMaximumSize(new Dimension(4, 32767));

		panelMapTilesContainer = new JPanel();
		panelMapTilesContainer.setLayout(new BorderLayout(0, 0));

		mapEditorPanel = new MapEditorPanel();
		mapEditorPanel.setLayout(null);

		mapScrollPane = new JScrollPane(mapEditorPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editorPanel.add(mapScrollPane, BorderLayout.CENTER);
		mapScrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		mapScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		mapScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

		panelTilesContainer = new JPanel();
		editorPanel.add(panelTilesContainer, BorderLayout.EAST);
		panelTilesContainer.setPreferredSize(new Dimension((TileEditorPanel.editorWidth + 1) * 16 + 13, 10));
		panelTilesContainer.setLayout(new BorderLayout(0, 0));
	}

	void CreateMenus() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnFile = new JMenu("File");
		mnOpen = new JMenuItem("Open ROM");
		mnOpen.addActionListener(e -> openROM());

		mnSave = new JMenuItem("Save");
		mnSave.setEnabled(false);
		mnSave.addActionListener(e -> MapIO.saveAll());
		mnSaveMap = new JMenuItem("Save Map");
		mnSaveMap.setEnabled(false);
		mnSaveMap.addActionListener(e -> MapIO.saveMap());
		mnSaveMapToPNG = new JMenuItem("Save Map to PNG");
		mnSaveMapToPNG.setEnabled(false);
		mnSaveMapToPNG.addActionListener(e -> {
            FileDialog fd = new FileDialog(new Frame(), "Locate Tileset", FileDialog.SAVE);
            fd.setFilenameFilter((dir, name) -> (name.toLowerCase().endsWith(".png")));
            fd.setFile(MapIO.currentBank + "-" + MapIO.selectedMap + "-" + BankLoader.mapNames.get((int)MapIO.loadedMap.mapHeader.bLabelID) + ".png");
            fd.setDirectory(System.getProperty("user.home"));
            fd.setVisible(true);
            String location = fd.getDirectory() + fd.getFile();
            if(fd.getDirectory().equals("null"))
                return;
            try {
                ImageIO.write((RenderedImage) Map.renderMap(MapIO.loadedMap, true), "png", new File(location));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
		mnExit = new JMenuItem("Exit");
		mnExit.addActionListener(e -> {
            PluginManager.unloadAllPlugins();
            System.exit(0);
        });
		JMenu mnSettings = new JMenu("Settings");
		JMenuItem mntmPreferences = new JMenuItem("Preferences...");
		mntmPreferences.addActionListener(e -> {
        });
		JMenu mnTools = new JMenu("Tools");
		mnPatches = new JMenu("Patches");
		mntmDaynightPokemon = new JMenuItem("Day/Night Pokemon");
		mntmDaynightPokemon.addActionListener(e -> {
            if(!ROMManager.getActiveROM().isDNPkmnPatchAdded)
                MapIO.patchDNPokemon();
        });
        mnHelp = new JMenu("Help");
        mnAbout = new JMenuItem("About");
        mnAbout.addActionListener(e -> {
            StringBuilder developerLines = new StringBuilder();
            for (int i = 0; i < Version.Contributors.length; i++)
                developerLines.append(Version.Contributors[i]).append('\n');
            JOptionPane.showMessageDialog(new JFrame(),
                    "MEH - Map Editor of Happiness v" + Version.RequestApplicationBuild() + "\n\nDevelopers:\n" + developerLines,
                    "About",
                    JOptionPane.PLAIN_MESSAGE);
        });

        menuBar.add(mnFile);
        mnFile.add(mnOpen);
        mnFile.add(mnSave);
        mnFile.addSeparator();
        mnFile.add(mnSaveMap);
        mnFile.addSeparator();
        mnFile.add(mnSaveMapToPNG);
        mnFile.addSeparator();
        mnFile.add(mnExit);
        menuBar.add(mnSettings);
        mnSettings.add(mntmPreferences);
        menuBar.add(mnTools);
        mnTools.add(mnPatches);
        mnPatches.add(mntmDaynightPokemon);
        
        mntmNewMenuItem = new JMenuItem("Triple-Layer Tiles (FR)");
        mntmNewMenuItem.addActionListener(e -> new TripleTilePatcher().setVisible(true));
        mnPatches.add(mntmNewMenuItem);
        
        mnEnableDebugging = new JCheckBoxMenuItem("Enable Debugging");
        mnEnableDebugging.addActionListener(e -> {
            MapIO.DEBUG = mnEnableDebugging.getState();
            if(MapIO.DEBUG) {
                panelButtons.add(btnImportMap);
                panelButtons.add(btnNewMap);
                validate();
            } else {
                try {
                    panelButtons.remove(btnImportMap);
                    panelButtons.remove(btnNewMap);
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        mnTools.add(mnEnableDebugging);
        menuBar.add(mnHelp);
        mnHelp.add(mnAbout);
	}

	TopRowButton btnNewMap;
	TopRowButton btnSaveROM;
	TopRowButton btnSaveMap;
    TopRowButton btnBlockEdit;
    TopRowButton btnDNPokePatcher;
	JComboBox mapBlendComboBox;
	TopRowButton btnImportMap;
	private JMenuItem mntmNewMenuItem;
	private static JTextField txtGlobalTileset;
	private static JTextField txtLocalTileset;
	
	void CreateButtons() {
		if(MapIO.DEBUG)
			System.out.println("Resource path:" + MainGUI.class.getResource("."));
		panelButtons = new JToolBar();
        panelButtons.setFloatable(false);
        panelButtons.setRollover(true);
		panelButtons.setPreferredSize(new Dimension(10, 38));
		panelButtons.setMinimumSize(new Dimension(10, 38));
		getContentPane().add(panelButtons, BorderLayout.NORTH);

		TopRowButton btnOpenROM = new TopRowButton("/resources/ROMopen.png","Open ROM for editing",true);

		btnOpenROM.addActionListener(arg0 -> openROM());
		panelButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panelButtons.add(btnOpenROM);

		btnSaveROM = new TopRowButton("/resources/ROMsave.png","Write changes to ROM file",false);
		btnSaveROM.addActionListener(e -> {
            try {
                MapIO.saveMap();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            MapIO.saveROM();
        });
		panelButtons.add(btnSaveROM);
		panelButtons.add(Box.createHorizontalStrut(2));
		panelButtons.add(new ButtonSeparator(32));
		panelButtons.add(Box.createHorizontalStrut(1));
		
		btnNewMap = new TopRowButton("/resources/newmap.png","New Map (Not implemented)",false);
		btnNewMap.addActionListener(arg0 -> {
            MapEditorPanel.renderPalette = !MapEditorPanel.renderPalette;
            mapEditorPanel.repaint();
        });
		
		btnSaveMap = new TopRowButton("/resources/mapsave.png","Save Map to VROM",false);
		btnSaveMap.addActionListener(e -> MapIO.saveMap());
		panelButtons.add(btnSaveMap);
		
		panelButtons.add(Box.createHorizontalStrut(2));
		panelButtons.add(new ButtonSeparator(32));
		panelButtons.add(Box.createHorizontalStrut(1));

		btnImportMap = new TopRowButton("","Import Map (Not implemented)",false);
		btnImportMap.addActionListener(e -> {
            MapEditorPanel.renderTileset = !MapEditorPanel.renderTileset;
            mapEditorPanel.repaint();
        });
		if(MapIO.DEBUG) {
			panelButtons.add(btnImportMap);
			panelButtons.add(btnNewMap);
		}
		
		btnBlockEdit = new TopRowButton("/resources/blockedit.png","Edit Blocks",false);
		btnBlockEdit.addActionListener(e -> {
            JFrame ownerframe = new JFrame();
            new BlockEditor(ownerframe, "Block Editor", Dialog.ModalityType.APPLICATION_MODAL).setVisible(true);
        });
		panelButtons.add(btnBlockEdit);
		
		btnDNPokePatcher = new TopRowButton("","D/N PKMN",false);
		btnDNPokePatcher.addActionListener(e -> {
            //testXorColors();
            new WizardPatcher().setVisible(true);
        });
		panelButtons.add(btnDNPokePatcher);
		
		mapBlendComboBox = new JComboBox();
		mapBlendComboBox.addItemListener(e -> {
            BlockRenderer.currentTime = mapBlendComboBox.getSelectedIndex();
            MapIO.blockRenderer.getGlobalTileset().renderGraphics();
            MapIO.blockRenderer.getLocalTileset().renderGraphics();
            //Refresh block picker
            tileEditorPanel.RerenderTiles(TileEditorPanel.imgBuffer, 0);
            tileEditorPanel.repaint();
            //Refresh Map Editor
            MapEditorPanel.Redraw = true;
            MainGUI.mapEditorPanel.repaint();
        });
		mapBlendComboBox.setModel(new DefaultComboBoxModel(new String[] {"Day", "Morning", "Evening", "Night"}));
		mapBlendComboBox.setPreferredSize(new Dimension(80, 20));
		mapBlendComboBox.setEnabled(false);
		panelButtons.add(mapBlendComboBox);
	}
	
	void enableMapOperationButtons() {
		btnSaveMap.setEnabled(true);
		btnBlockEdit.setEnabled(true);
		btnDNPokePatcher.setEnabled(true);
		mapBlendComboBox.setEnabled(true);
		mnSaveMap.setEnabled(true);
		mnSaveMapToPNG.setEnabled(true);
		
	}
	
	public class ButtonSeparator extends JSeparator {
		public ButtonSeparator (int height) {
			super(SwingConstants.VERTICAL);
			Dimension d = getPreferredSize();
		    d.height = height;
		    setPreferredSize(d);
		}
	}
	
	public class TopRowButton extends JButton {
		public TopRowButton (String iconPath, String toolTipText, boolean enableSwitch) {
			super("");
			setIcon(new ImageIcon(MainGUI.class.getResource(iconPath)));
			setToolTipText(toolTipText);
			setFocusPainted(false);
			setBorder(null);
			setBorderPainted(false);
			setPreferredSize(new Dimension(36, 36));
			setEnabled(enableSwitch);
		}
	}

	void CreateWildPokemonPanel() {
		wildPokemonPanel = new JPanel();
		editorTabs.addTab("Wild Pokemon", null, wildPokemonPanel, null);
		wildPokemonPanel.setLayout(new BorderLayout(0, 0));

		panelWildEditor = new JPanel();
		scrollPaneWildEditor = new JScrollPane(panelWildEditor);
		scrollPaneWildEditor.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPaneWildEditor.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneWildEditor.getVerticalScrollBar().setUnitIncrement(16);
		wildPokemonPanel.add(scrollPaneWildEditor, BorderLayout.CENTER);
		panelWildEditor.setLayout(null);
		panelWildEditor.setPreferredSize(new Dimension(panelWildEditor.getWidth(), 550));

		JPanel panelWildHeader = new JPanel();
		panelWildHeader.setBounds(12, 12, 745, 83);
		panelWildEditor.add(panelWildHeader);
		panelWildHeader.setLayout(null);

        lblPkEnvironment = new JLabel("Environment:");
        lblPkEnvironment.setBounds(0, 5, 93, 15);
		panelWildHeader.add(lblPkEnvironment);

        lblPkDayTime = new JLabel("Time of Day:");
        lblPkDayTime.setBounds(320, 5, 93, 15);
		panelWildHeader.add(lblPkDayTime);

		pkTime = new JComboBox();
		pkTime.setModel(new DefaultComboBoxModel(new String[] { "Morning", "Day", "Evening", "Night" }));
		pkTime.setSelectedIndex(0);
		pkTime.setBounds(409, 0, 112, 24);
		pkTime.addActionListener(e -> {
                selectedTime = pkTime.getSelectedIndex();
                loadWildPokemon();
                System.out.println(selectedTime);
        });
		panelWildHeader.add(pkTime);

		pkEnvironment = new JComboBox();
		pkEnvironment.addActionListener(e -> {
            currentType = pkEnvironment.getSelectedIndex();
            loadWildPokemon();
        });
		pkEnvironment.setModel(new DefaultComboBoxModel(new String[] { "Grass", "Water", "Rock Smash", "Fishing" }));
		pkEnvironment.setBounds(98, 0, 144, 24);
		panelWildHeader.add(pkEnvironment);

		JLabel lblNewLabel_3 = new JLabel("Total Encounter Rate:");
		lblNewLabel_3.setBounds(0, 55, 154, 15);
		panelWildHeader.add(lblNewLabel_3);

		pkEncounter = new JSlider();
		pkEncounter.addChangeListener(e -> {
            try {
                int percent = Math.round((pkEncounter.getValue() / 255.0f) * 100);
                lblEncounterPercent.setText(percent + "%");
                MapIO.wildData.aWildPokemon[currentType].bRatio = (byte) pkEncounter.getValue();
            }
            catch (Exception ex) {
                if(MapIO.DEBUG)
                    ex.printStackTrace();
            }
        });
		pkEncounter.setPaintTicks(true);
		pkEncounter.setMajorTickSpacing(64);
		pkEncounter.setValue(128);
		pkEncounter.setMaximum(255);
		pkEncounter.setBounds(158, 32, 200, 52);
		panelWildHeader.add(pkEncounter);

		lblEncounterPercent = new JLabel("50%");
		lblEncounterPercent.setBounds(370, 55, 70, 15);
		panelWildHeader.add(lblEncounterPercent);

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setDividerSize(0);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_1.setEnabled(false);
		splitPane_1.setBounds(409, 30, 99, 52);
		panelWildHeader.add(splitPane_1);

		JButton btnAddPokeData = new JButton("Add");
		btnAddPokeData.addActionListener(e -> {
            WildDataCache.createWildDataIfNotExists(MapIO.currentBank, MapIO.currentMap).addWildData(WildDataType.values()[currentType]);
            loadWildPokemon();
        });
		splitPane_1.setLeftComponent(btnAddPokeData);

		JButton btnRemovePokeData = new JButton("Remove");
		btnRemovePokeData.addActionListener(e -> {
            if (MapIO.wildData == null)
                return;
            MapIO.wildData.removeWildData(WildDataType.values()[currentType]);
            loadWildPokemon();
        });
		splitPane_1.setRightComponent(btnRemovePokeData);
		splitPane_1.setDividerLocation(25);
		
		btnEnableTimebasedPokemon = new JButton("Enable Time-Based Edits");
		btnEnableTimebasedPokemon.addActionListener(e -> {
            if(!ROMManager.getActiveROM().isDNPkmnPatchAdded) {
                int result = JOptionPane.showConfirmDialog(new JFrame(),"It appears that you haven't patched your ROM for time-based Pok�mon changes.\nWould you like to do that now?","Peer Pressure", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION) {
                    MapIO.patchDNPokemon();
                    return;
                }
            }
            int result = JOptionPane.showConfirmDialog(new JFrame(),"This option will permanantely convert your wild Pok�mon data to a new format unsupported by other map editors.\nAre you sure you want to continue?","No More A-Map 4 u", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION) {
                MapIO.wildData.aWildPokemon[currentType].convertToDN();
                loadWildPokemon();
            }
        });
		btnEnableTimebasedPokemon.setBounds(533, 0, 212, 25);
		panelWildHeader.add(btnEnableTimebasedPokemon);

		pkEditorPanel = new JPanel();
		pkEditorPanel.setBounds(12, 100, 544, 202);
		panelWildEditor.add(pkEditorPanel);
		pkEditorPanel.setLayout(null);

		JLabel lblpkmin = new JLabel("Min");
		lblpkmin.setBounds(22, 12, 50, 15);
		pkEditorPanel.add(lblpkmin);

		lblpkmax = new JLabel("Max");
		lblpkmax.setBounds(80, 12, 50, 15);
		pkEditorPanel.add(lblpkmax);

		lblPkMn = new JLabel("Pok�mon");
		lblPkMn.setHorizontalAlignment(SwingConstants.CENTER);
		lblPkMn.setBounds(147, 12, 159, 15);
		pkEditorPanel.add(lblPkMn);

		lblpknum = new JLabel("No.");
		lblpknum.setHorizontalAlignment(SwingConstants.CENTER);
		lblpknum.setBounds(306, 12, 50, 15);
		pkEditorPanel.add(lblpknum);

		lblpkchance = new JLabel("Chance");
		lblpkchance.setBounds(382, 12, 70, 15);
		pkEditorPanel.add(lblpkchance);

		pkMin1 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMin1.addChangeListener(e -> {
            try {
                int i = (Integer) pkMin1.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][0].bMinLV = (byte) i;
            }
            catch (Exception ex) {
                if(MapIO.DEBUG)
                    ex.printStackTrace();
            }
        });
		pkMin1.setBounds(12, 30, 60, 32);
		pkEditorPanel.add(pkMin1);

		pkMax1 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMax1.setBounds(75, 30, 60, 32);
		pkEditorPanel.add(pkMax1);

		JComboBox<String> pkName1 = new JComboBox<>();
		pkName1.addActionListener(e -> {
			pkNo1.setValue(pkName1.getSelectedIndex());

			if (MapIO.wildData != null
					&& MapIO.wildData.aWildPokemon != null
					&& currentType >= 0 && currentType < MapIO.wildData.aWildPokemon.length
					&& MapIO.wildData.aWildPokemon[currentType] != null
					&& selectedTime >= 0 && selectedTime < MapIO.wildData.aWildPokemon[currentType].aWildPokemon.length
					&& MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime] != null
					&& MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][0] != null) {
				try {
					MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][0].wNum = pkName1.getSelectedIndex();
				} catch (Exception ex) {
					ex.printStackTrace();
					if (MapIO.DEBUG)
						System.out.println("Error loading wild Pokémon data, data not found or nonexistent.");
				}
			} else {
				// Handle the case where some required objects are null
				System.out.println("Error: Some required objects are null.");
			}
		});
		pkName1.setBounds(141, 30, 165, 32);
		pkEditorPanel.add(pkName1);

		pkNo1 = new JSpinner(new SpinnerNumberModel(1, 1, DataStore.NumPokemon, 1));
		pkNo1.addChangeListener(e -> {
            int i = (Integer) pkNo1.getValue();
            pkName1.setSelectedIndex(i);
        });
		pkNo1.setBounds(306, 30, 72, 32);
		pkEditorPanel.add(pkNo1);

		pkchance1 = new JLabel("20%");
		pkchance1.setHorizontalAlignment(SwingConstants.LEFT);
		pkchance1.setBounds(395, 38, 125, 15);
		pkEditorPanel.add(pkchance1);

		pkMin2 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMin2.setBounds(12, 65, 60, 32);
		pkMin2.addChangeListener(e -> {
            try {
                int i = (Integer) pkMin2.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][1].bMinLV = (byte) i;
            }
            catch (Exception ex) {
                if(MapIO.DEBUG)
                    ex.printStackTrace();
            }
        });
		pkEditorPanel.add(pkMin2);

		pkMax2 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMax2.setBounds(75, 65, 60, 32);
		pkEditorPanel.add(pkMax2);

		pkName2 = new JComboBox();
		pkName2.addActionListener(e -> {
            pkNo2.setValue(pkName2.getSelectedIndex());
            try {
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][1].wNum = pkName2.getSelectedIndex();
            }
            catch (Exception ex) {
                if(MapIO.DEBUG)
                    ex.printStackTrace();
            }
        });

		pkName2.setBounds(141, 65, 165, 32);
		pkEditorPanel.add(pkName2);
		pkNo2 = new JSpinner(new SpinnerNumberModel(1, 1, DataStore.NumPokemon, 1));
		pkNo2.setBounds(306, 65, 72, 32);
		pkNo2.addChangeListener(e -> {
            int i = (Integer) pkNo2.getValue();
            pkName2.setSelectedIndex(i);
        });
		pkEditorPanel.add(pkNo2);

		pkchance2 = new JLabel("20%");
		pkchance2.setHorizontalAlignment(SwingConstants.LEFT);
		pkchance2.setBounds(395, 73, 125, 15);
		pkEditorPanel.add(pkchance2);

		pkMin3 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMin3.setBounds(12, 98, 60, 32);
		pkMin3.addChangeListener(e -> {
            try {
                int i = (Integer) pkMin3.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][2].bMinLV = (byte) i;
            }
            catch (Exception ex){
                if(MapIO.DEBUG)
                    ex.printStackTrace();
            }
        });
		pkEditorPanel.add(pkMin3);

		pkMax3 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMax3.setBounds(75, 98, 60, 32);
		pkEditorPanel.add(pkMax3);

		pkName3 = new JComboBox();
		pkName3.addActionListener(e -> {
            pkNo3.setValue(pkName3.getSelectedIndex());
            try {
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][2].wNum = pkName3.getSelectedIndex();
            }
            catch (Exception ex) {
                if(MapIO.DEBUG)
                    ex.printStackTrace();
            }
        });
		pkName3.setBounds(141, 98, 165, 32);
		pkEditorPanel.add(pkName3);

		pkNo3 = new JSpinner(new SpinnerNumberModel(1, 1, DataStore.NumPokemon, 1));
		pkNo3.setBounds(306, 98, 72, 32);
		pkNo3.addChangeListener(e -> {
            int i = (Integer) pkNo3.getValue();
            pkName3.setSelectedIndex(i);
        });
		pkEditorPanel.add(pkNo3);

		pkchance3 = new JLabel("10%");
		pkchance3.setHorizontalAlignment(SwingConstants.LEFT);
		pkchance3.setBounds(395, 106, 125, 15);
		pkEditorPanel.add(pkchance3);

		pkMin4 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMin4.setBounds(12, 133, 60, 32);
		pkMin4.addChangeListener(e -> {
            try {
                int i = (Integer) pkMin4.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][3].bMinLV = (byte) i;
            }
            catch (Exception ex) {
                if(MapIO.DEBUG)
                    ex.printStackTrace();
            }
        });
		pkEditorPanel.add(pkMin4);

		pkMax4 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMax4.setBounds(75, 133, 60, 32);
		pkEditorPanel.add(pkMax4);

		pkName4 = new JComboBox();
		pkName4.addActionListener(e -> {
            pkNo4.setValue(pkName4.getSelectedIndex());
            try {
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][3].wNum = pkName4.getSelectedIndex();
            }
            catch (Exception ex) {
                if(MapIO.DEBUG)
                    ex.printStackTrace();
            }
        });
		pkName4.setBounds(141, 133, 165, 32);
		pkEditorPanel.add(pkName4);

		pkNo4 = new JSpinner(new SpinnerNumberModel(1, 1, DataStore.NumPokemon, 1));
		pkNo4.setBounds(306, 133, 72, 32);
		pkNo4.addChangeListener(e -> {
            int i = (Integer) pkNo4.getValue();
            pkName4.setSelectedIndex(i);
        });
		pkEditorPanel.add(pkNo4);

		pkchance4 = new JLabel("10%");
		pkchance4.setHorizontalAlignment(SwingConstants.LEFT);
		pkchance4.setBounds(395, 141, 125, 15);
		pkEditorPanel.add(pkchance4);

		pkMin5 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMin5.setBounds(12, 168, 60, 32);
		pkMin5.addChangeListener(e -> {
            try {
                int i = (Integer) pkMin5.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][4].bMinLV = (byte) i;
            }
            catch (Exception ex) {
                if(MapIO.DEBUG)
                    ex.printStackTrace();
            }
        });
		pkEditorPanel.add(pkMin5);

		pkMax5 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMax5.setBounds(75, 168, 60, 32);
		pkEditorPanel.add(pkMax5);

		pkName5 = new JComboBox();
		pkName5.addActionListener(e -> {
            pkNo5.setValue(pkName5.getSelectedIndex());
            try {
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][4].wNum = pkName5.getSelectedIndex();
            }
            catch (Exception ex) {
                if(MapIO.DEBUG)
                    ex.printStackTrace();
            }
        });
		pkName5.setBounds(141, 168, 165, 32);
		pkEditorPanel.add(pkName5);

		pkNo5 = new JSpinner(new SpinnerNumberModel(1, 1, DataStore.NumPokemon, 1));
		pkNo5.setBounds(306, 168, 72, 32);
		pkNo5.addChangeListener(e -> {
            int i = (Integer) pkNo5.getValue();
            pkName5.setSelectedIndex(i);
        });
		pkEditorPanel.add(pkNo5);

		pkchance5 = new JLabel("10%");
		pkchance5.setHorizontalAlignment(SwingConstants.LEFT);
		pkchance5.setBounds(395, 176, 125, 15);
		pkEditorPanel.add(pkchance5);

		panelpk6_10 = new JPanel();
		panelpk6_10.setLayout(null);
		panelpk6_10.setBounds(12, 275, 544, 202);
		panelWildEditor.add(panelpk6_10);

		pkMin6 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMin6.setBounds(12, 30, 60, 32);
		pkMin6.addChangeListener(e -> {
            try {
                int i = (Integer) pkMin6.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][5].bMinLV = (byte) i;
            }
            catch (Exception ex) {
            }
        });
		panelpk6_10.add(pkMin6);

		pkMax6 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMax6.setBounds(75, 30, 60, 32);
		panelpk6_10.add(pkMax6);

		pkName6 = new JComboBox();
		pkName6.addActionListener(e -> {
            pkNo6.setValue(pkName6.getSelectedIndex());
            try {
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][5].wNum = pkName6.getSelectedIndex();
            } catch (Exception ex) {}
        });
		pkName6.setBounds(141, 30, 165, 32);
		panelpk6_10.add(pkName6);

		pkNo6 = new JSpinner(new SpinnerNumberModel(1, 1, DataStore.NumPokemon, 1));
		pkNo6.setBounds(306, 30, 72, 32);
		pkNo6.addChangeListener(e -> {
            int i = (Integer) pkNo6.getValue();
            pkName6.setSelectedIndex(i);
        });
		panelpk6_10.add(pkNo6);

		pkchance6 = new JLabel("10%");
		pkchance6.setHorizontalAlignment(SwingConstants.LEFT);
		pkchance6.setBounds(395, 38, 137, 15);
		panelpk6_10.add(pkchance6);

		pkMin7 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMin7.setBounds(12, 65, 60, 32);
		pkMin7.addChangeListener(e -> {
            try {
                int i = (Integer) pkMin7.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][6].bMinLV = (byte) i;
            }
            catch (Exception ex) {
            }
        });
		panelpk6_10.add(pkMin7);

		pkMax7 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMax7.setBounds(75, 65, 60, 32);
		panelpk6_10.add(pkMax7);

		pkName7 = new JComboBox();
		pkName7.addActionListener(e -> {
            pkNo7.setValue(pkName7.getSelectedIndex());
            try {
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][6].wNum = pkName7.getSelectedIndex();
            }
            catch (Exception ex) {
            }
        });
		pkName7.setBounds(141, 65, 165, 32);
		panelpk6_10.add(pkName7);

		pkNo7 = new JSpinner(new SpinnerNumberModel(1, 1, DataStore.NumPokemon, 1));
		pkNo7.setBounds(306, 65, 72, 32);
		pkNo7.addChangeListener(e -> {
            int i = (Integer) pkNo7.getValue();
            pkName7.setSelectedIndex(i);
        });
		panelpk6_10.add(pkNo7);

		pkchance7 = new JLabel("5%");
		pkchance7.setHorizontalAlignment(SwingConstants.LEFT);
		pkchance7.setBounds(395, 73, 137, 15);
		panelpk6_10.add(pkchance7);

		pkMin8 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMin8.setBounds(12, 98, 60, 32);
		pkMin8.addChangeListener(e -> {
            try {
                int i = (Integer) pkMin8.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][7].bMinLV = (byte) i;
            }
            catch (Exception ex) {
            }
        });
		panelpk6_10.add(pkMin8);

		pkMax8 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMax8.setBounds(75, 98, 60, 32);
		panelpk6_10.add(pkMax8);

		pkName8 = new JComboBox();
		pkName8.addActionListener(e -> {
            pkNo8.setValue(pkName8.getSelectedIndex());
            try {
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][7].wNum = pkName8.getSelectedIndex();
            } catch (Exception ex) {}
        });
		pkName8.setBounds(141, 98, 165, 32);
		panelpk6_10.add(pkName8);

		pkNo8 = new JSpinner(new SpinnerNumberModel(1, 1, DataStore.NumPokemon, 1));
		pkNo8.setBounds(306, 98, 72, 32);
		pkNo8.addChangeListener(e -> {
            int i = (Integer) pkNo8.getValue();
            pkName8.setSelectedIndex(i);
        });
		panelpk6_10.add(pkNo8);

		pkchance8 = new JLabel("5%");
		pkchance8.setHorizontalAlignment(SwingConstants.LEFT);
		pkchance8.setBounds(395, 106, 137, 15);
		panelpk6_10.add(pkchance8);

		pkMin9 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMin9.setBounds(12, 133, 60, 32);
		pkMin9.addChangeListener(e -> {
            try {
                int i = (Integer) pkMin9.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][8].bMinLV = (byte) i;
            }
            catch (Exception ex) {
            }
        });
		panelpk6_10.add(pkMin9);

		pkMax9 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMax9.setBounds(75, 133, 60, 32);
		panelpk6_10.add(pkMax9);

		pkName9 = new JComboBox();
		pkName9.addActionListener(e -> {
            pkNo9.setValue(pkName9.getSelectedIndex());
            try {
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][8].wNum = pkName9.getSelectedIndex();
            }
            catch (Exception ex) {
            }
        });
		pkName9.setBounds(141, 133, 165, 32);
		panelpk6_10.add(pkName9);

		pkNo9 = new JSpinner(new SpinnerNumberModel(1, 1, DataStore.NumPokemon, 1));
		pkNo9.setBounds(306, 133, 72, 32);
		pkNo9.addChangeListener(e -> {
            int i = (Integer) pkNo9.getValue();
            pkName9.setSelectedIndex(i);
        });
		panelpk6_10.add(pkNo9);

		pkchance9 = new JLabel("4%");
		pkchance9.setHorizontalAlignment(SwingConstants.LEFT);
		pkchance9.setBounds(395, 141, 137, 15);
		panelpk6_10.add(pkchance9);

		pkMin10 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMin10.setBounds(12, 168, 60, 32);
		pkMin10.addChangeListener(e -> {
            try {
                int i = (Integer) pkMin10.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][9].bMinLV = (byte) i;
            }
            catch (Exception ex) {
            }
        });
		panelpk6_10.add(pkMin10);

		pkMax10 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMax10.setBounds(75, 168, 60, 32);
		panelpk6_10.add(pkMax10);

		pkName10 = new JComboBox();
		pkName10.addActionListener(e -> {
            pkNo10.setValue(pkName10.getSelectedIndex());
            try {
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][9].wNum = pkName10.getSelectedIndex();
            }
            catch (Exception ex) {
            }
        });
		pkName10.setBounds(141, 168, 165, 32);
		panelpk6_10.add(pkName10);

		pkNo10 = new JSpinner(new SpinnerNumberModel(1, 1, DataStore.NumPokemon, 1));
		pkNo10.setBounds(306, 168, 72, 32);
		pkNo10.addChangeListener(e -> {
            int i = (Integer) pkNo10.getValue();
            pkName10.setSelectedIndex(i);
        });
		panelpk6_10.add(pkNo10);

		pkchance10 = new JLabel("4%");
		pkchance10.setHorizontalAlignment(SwingConstants.LEFT);
		pkchance10.setBounds(395, 176, 137, 15);
		panelpk6_10.add(pkchance10);

		panelpk11_12 = new JPanel();
		panelpk11_12.setBounds(12, 477, 544, 73);
		panelWildEditor.add(panelpk11_12);
		panelpk11_12.setLayout(null);

		pkMin11 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMin11.setBounds(12, 0, 60, 32);
		pkMin11.addChangeListener(e -> {
            try {
                int i = (Integer) pkMin11.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][10].bMinLV = (byte) i;
            }
            catch (Exception ex) {
            }
        });
		panelpk11_12.add(pkMin11);

		pkMin12 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMin12.setBounds(12, 35, 60, 32);
		pkMin12.addChangeListener(e -> {
            try {
                int i = (Integer) pkMin12.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][11].bMinLV = (byte) i;
            } catch (Exception ex) {}
        });
		panelpk11_12.add(pkMin12);

		pkMax11 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMax11.setBounds(75, 0, 60, 32);
		panelpk11_12.add(pkMax11);

		pkMax12 = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		pkMax12.setBounds(75, 35, 60, 32);
		panelpk11_12.add(pkMax12);

		pkName11 = new JComboBox();
		pkName11.setBounds(141, 0, 165, 32);
		pkName11.addActionListener(e -> {
            pkNo11.setValue(pkName11.getSelectedIndex());
            try {
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][10].wNum = pkName11.getSelectedIndex();
            } catch (Exception ex) {}
        });
		panelpk11_12.add(pkName11);

		pkName12 = new JComboBox();
		pkName12.setBounds(141, 35, 165, 32);
		pkName12.addActionListener(e -> {
            pkNo12.setValue(pkName12.getSelectedIndex());
            try {
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][11].wNum = pkName12.getSelectedIndex();
            }
            catch (Exception ex) {
            }
        });
		panelpk11_12.add(pkName12);

		pkNo11 = new JSpinner(new SpinnerNumberModel(1, 1, DataStore.NumPokemon, 1));
		pkNo11.setBounds(306, 0, 72, 32);
		pkNo11.addChangeListener(e -> {
            int i = (Integer) pkNo11.getValue();
            pkName11.setSelectedIndex(i);
        });
		panelpk11_12.add(pkNo11);

		pkNo12 = new JSpinner(new SpinnerNumberModel(1, 1, DataStore.NumPokemon, 1));
		pkNo12.setBounds(306, 35, 72, 32);
		pkNo12.addChangeListener(e -> {
            int i = (Integer) pkNo12.getValue();
            pkName12.setSelectedIndex(i);
        });
		panelpk11_12.add(pkNo12);

		pkMax1.addChangeListener(e -> {
            try {
                int i = (Integer) pkMax1.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][0].bMaxLV = (byte) i;
            } catch (Exception ex) {}
        });
		pkMax2.addChangeListener(e -> {
            try {
                int i = (Integer) pkMax2.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][1].bMaxLV = (byte) i;
            } catch (Exception ex) {}
        });
		pkMax3.addChangeListener(e -> {
            try {
                int i = (Integer) pkMax3.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][2].bMaxLV = (byte) i;
            } catch (Exception ex) {}
        });
		pkMax4.addChangeListener(e -> {
            try {
                int i = (Integer) pkMax4.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][3].bMaxLV = (byte) i;
            } catch (Exception ex) {}
        });
		pkMax5.addChangeListener(e -> {
            try {
                int i = (Integer) pkMax5.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][4].bMaxLV = (byte) i;
            }
            catch (Exception ex) {
            }
        });
		pkMax6.addChangeListener(e -> {
            try {
                int i = (Integer) pkMax6.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][5].bMaxLV = (byte) i;
            } catch (Exception ex) {}
        });
		pkMax7.addChangeListener(e -> {
            try {
                int i = (Integer) pkMax7.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][6].bMaxLV = (byte) i;
            } catch (Exception ex) {}
        });
		pkMax8.addChangeListener(e -> {
            try {
                int i = (Integer) pkMax8.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][7].bMaxLV = (byte) i;
            } catch (Exception ex) {}
        });
		pkMax9.addChangeListener(e -> {
            try {
                int i = (Integer) pkMax9.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][8].bMaxLV = (byte) i;
            } catch (Exception ex) {}
        });
		pkMax10.addChangeListener(e -> {
            try {
                int i = (Integer) pkMax10.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][9].bMaxLV = (byte) i;
            } catch (Exception ex) {}
        });
		pkMax11.addChangeListener(e -> {
            try {
                int i = (Integer) pkMax11.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][10].bMaxLV = (byte) i;
            } catch (Exception ex) {}
        });
		pkMax12.addChangeListener(e -> {
            try {
                int i = (Integer) pkMax12.getValue();
                MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][11].bMaxLV = (byte) i;
            } catch (Exception ex) {}
        });

		pkchance11 = new JLabel("1%");
		pkchance11.setHorizontalAlignment(SwingConstants.LEFT);
		pkchance11.setBounds(395, 8, 137, 15);
		panelpk11_12.add(pkchance11);

		pkchance12 = new JLabel("1%");
		pkchance12.setHorizontalAlignment(SwingConstants.LEFT);
		pkchance12.setBounds(395, 43, 70, 15);
		panelpk11_12.add(pkchance12);

		lblNewLabel = new JLabel("Hey there,\nFirst off I'd like to thank you for taking the time to make your way to this tab. It seems that you are very interested in editing Wild Pok�mon, because that is obviously the name of this tab. Unfortunately neither Shiny Quagsire nor interdpth have actually implemented this feature so we put this giant block of text here to tell you that this feature isn't implemented.");
		lblNewLabel.setPreferredSize(new Dimension(51215, 15));
		lblNewLabel.setMaximumSize(new Dimension(512, 15));
		
		pkEditorPanel.setVisible(false);
		panelpk6_10.setVisible(false);
		panelpk11_12.setVisible(false);
	}

	void CreateMimeTab() {
		mimePanel = new JPanel();
		editorTabs.addTab("Mime", null, mimePanel, null);
		mimePanel.setLayout(new BorderLayout(0, 0));
		mimeEditorPanel = new JPanel();
		mimePanel.add(mimeEditorPanel);
		mimeEditorPanel.setLayout(null);

		JLabel lblWelcome = new JLabel("<html><center>Welcome to the map mime!\n<br>\nHere we will mime out your map so you can like see it, but without actually physically seeing it and stuff.</center></html>");
		lblWelcome.setBounds(90, 12, 559, 63);
		mimeEditorPanel.add(lblWelcome);

		lblWidth = new JLabel("Width: ");
		lblWidth.setBounds(64, 107, 65, 15);
		mimeEditorPanel.add(lblWidth);

		lblHeight = new JLabel("Height: ");
		lblHeight.setBounds(64, 134, 65, 15);
		mimeEditorPanel.add(lblHeight);

		lblBorderHeight = new JLabel("Border Height: ");
		lblBorderHeight.setBounds(64, 178, 164, 15);
		mimeEditorPanel.add(lblBorderHeight);

		lblMapTilesPointer = new JLabel("Map Tiles Pointer: ");
		lblMapTilesPointer.setBounds(245, 107, 339, 15);
		mimeEditorPanel.add(lblMapTilesPointer);

		lblBorderTilesPointer = new JLabel("Border Tiles Pointer:");
		lblBorderTilesPointer.setBounds(245, 123, 339, 15);
		mimeEditorPanel.add(lblBorderTilesPointer);

		lblGlobalTilesetPointer = new JLabel("Global Tileset Pointer:");
		lblGlobalTilesetPointer.setBounds(245, 151, 164, 15);
		mimeEditorPanel.add(lblGlobalTilesetPointer);

		lblLocalTilesetPointer = new JLabel("Local  Tileset  Pointer:");
		lblLocalTilesetPointer.setBounds(245, 176, 164, 15);
		mimeEditorPanel.add(lblLocalTilesetPointer);

		lblBorderWidth = new JLabel("Border Width: ");
		lblBorderWidth.setBounds(64, 162, 157, 15);
		mimeEditorPanel.add(lblBorderWidth);
	}

	void CreateTabbedPanels() {
		getContentPane().add(splitPane, BorderLayout.CENTER);

		editorTabs = new JTabbedPane(SwingConstants.TOP);
		splitPane.setRightComponent(editorTabs);
		CreateMapPanel();
		CreateBorderArea();
		
		panelTilesContainer.add(panelMapTilesContainer, BorderLayout.NORTH);
		panelTilesContainer.add(splitterMapTiles, BorderLayout.WEST);

		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(e -> {
            System.out.println("" + tabbedPane.getSelectedIndex());
            if (tabbedPane.getSelectedIndex() == 0)
                MapEditorPanel.setMode(EditMode.TILES);
            else
                MapEditorPanel.setMode(EditMode.MOVEMENT);

            MapEditorPanel.Redraw = true;
            mapEditorPanel.repaint();
            borderTileEditor.repaint();
        });
		tabbedPane.setBorder(null);
		panelTilesContainer.add(tabbedPane, BorderLayout.CENTER);
		tileEditorPanel = new TileEditorPanel(true);

		tileEditorPanel.setPreferredSize(new Dimension((TileEditorPanel.editorWidth) * 16 + 16, ((DataStore.EngineVersion == 1 ? 0x200 + 0x56 : 0x200 + 0x300) / TileEditorPanel.editorWidth) * 16));
		tileEditorPanel.setSize(tileEditorPanel.getPreferredSize());
		tileEditorPanel.setLayout(null);

		tilesetScrollPane = new JScrollPane(tileEditorPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tilesetScrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		tilesetScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		tabbedPane.addTab("Tiles", null, tilesetScrollPane, null);

		permissionTilePanel = new PermissionTilePanel();
		permissionTilePanel.setLayout(null);
		movementScrollPane = new JScrollPane(permissionTilePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		movementScrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		movementScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		tabbedPane.addTab("Movement", null, movementScrollPane, null);

		CreateEventsPanel();
		CreateWildPokemonPanel();
		CreateMimeTab();
	}

	void CreateSplitPane() {
		splitPane = new JSplitPane();
		splitPane.setDividerSize(1);
		splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, e -> {
            if (((JSplitPane) e.getSource()).getDividerLocation() > 300)
                ((JSplitPane) e.getSource()).setDividerLocation(350);
            else if (((JSplitPane) e.getSource()).getDividerLocation() < 25)
                ((JSplitPane) e.getSource()).setDividerLocation(25);
            if (paneSize == 0) {
                paneSize = 250;
                ((JSplitPane) e.getSource()).setDividerLocation(paneSize);
            }
            else {
                paneSize = ((JSplitPane) e.getSource()).getDividerLocation();
}
        });
		splitPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				if (((JSplitPane) e.getSource()).getDividerLocation() > 300)
					((JSplitPane) e.getSource()).setDividerLocation(350);
				else if (((JSplitPane) e.getSource()).getDividerLocation() < 25)
					((JSplitPane) e.getSource()).setDividerLocation(25);
				else
					((JSplitPane) e.getSource()).setDividerLocation(paneSize);

				if (paneSize == 0) {
					paneSize = 250;
					((JSplitPane) e.getSource()).setDividerLocation(paneSize);
				}
				else {
					paneSize = ((JSplitPane) e.getSource()).getDividerLocation();
                }
			}
		});
		splitPane.setDividerLocation(0.2);
	}

	void CreateStatusBar() {
		JPanel statusPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) statusPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		statusPanel.setPreferredSize(new Dimension(10, 24));
		getContentPane().add(statusPanel, BorderLayout.SOUTH);

		lblInfo = new JLabel("No ROM loaded.");
        lblX = new JLabel("X: 0");
        lblY = new JLabel("Y: 0");
		statusPanel.add(lblInfo);
        statusPanel.add(lblX);
        statusPanel.add(lblY);
	}

	public void CreatePermissions() {
		JPanel splitm = new JPanel();
		splitm.setPreferredSize(new Dimension(4, 10));
		splitm.setMaximumSize(new Dimension(4, 320));
		JPanel pmtc = new JPanel();

		pmtc.setLayout(new BorderLayout(0, 0));
		panelPermissions = new JPanel();
		panelPermissions.setLayout(null);
	}

	public MainGUI() {
		setPreferredSize(new Dimension(800, 1800));
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// TODO: Are you *sure* you want to exit / Check for saved
				// changes
				PluginManager.unloadAllPlugins();
				System.exit(0);
			}
		});

        uiSettings = new UISettings();

		CreateMenus();
		CreateStatusBar();
		CreateButtons();
		CreateSplitPane();
		CreateTabbedPanels();

		BufferedImage mime = null;

		try {
			mime = ImageIO.read(MainGUI.class.getResourceAsStream("/resources/mime.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ImagePanel mimePic = new ImagePanel(mime);
		mimePic.setBounds(545, 71, 164, 164);
		mimeEditorPanel.add(mimePic);

		spnWidth = new JSpinner(new SpinnerNumberModel(1, 1, 9001, 1));
		spnWidth.addChangeListener(e -> {
            if (!MapIO.doneLoading)
                return;
            try {
                int x = (Integer) spnWidth.getValue();
                int y = (Integer) spnHeight.getValue();
                MapIO.loadedMap.getMapTileData().resize(x, y);
                MapEditorPanel.Redraw = true;
                eventEditorPanel.Redraw = true;
                connectionsEditorPanel.loadConnections(MapIO.loadedMap);
                mapEditorPanel.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
		spnWidth.setPreferredSize(new Dimension(40, 24));
		spnWidth.setBounds(130, 105, 50, 24);
		mimeEditorPanel.add(spnWidth);

		spnHeight = new JSpinner(new SpinnerNumberModel(1, 1, 9001, 1));
		spnHeight.setPreferredSize(new Dimension(40, 24));
		spnHeight.setBounds(130, 132, 50, 24);
		spnHeight.addChangeListener(e -> {
            if (!MapIO.doneLoading)
                return;
            try {
				int x = (Integer) spnWidth.getValue();
                int y = (Integer) spnHeight.getValue();
                MapIO.loadedMap.getMapTileData().resize(x, y);
                MapEditorPanel.Redraw = true;
                eventEditorPanel.Redraw = true;
                connectionsEditorPanel.loadConnections(MapIO.loadedMap);
                mapEditorPanel.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

		mimeEditorPanel.add(spnHeight);
		
		txtGlobalTileset = new JTextField();
		txtGlobalTileset.setBounds(409, 148, 102, 20);
		mimeEditorPanel.add(txtGlobalTileset);
		txtGlobalTileset.setColumns(10);
		
		txtLocalTileset = new JTextField();
		txtLocalTileset.setColumns(10);
		txtLocalTileset.setBounds(409, 175, 102, 20);
		mimeEditorPanel.add(txtLocalTileset);

		connectionsTabPanel = new JPanel();
		editorTabs.addTab("Connections", null, connectionsTabPanel, null);
		connectionsTabPanel.setLayout(new BorderLayout(0, 0));

		connectionsInfoPanel = new JPanel();
		connectionsInfoPanel.setPreferredSize(new Dimension(10, 24));
		connectionsTabPanel.add(connectionsInfoPanel, BorderLayout.NORTH);
		connectionsInfoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));

		mbMain = new JMenuBar();
		mbMain.setBorderPainted(false);
		connectionsInfoPanel.add(mbMain);

		mnAddCon = new JMenu("Add...");
		mbMain.add(mnAddCon);

		mntmLeftCon = new JMenuItem("Left Connection");
		mntmLeftCon.addActionListener(e -> {
            ConnectionAddGUI g = new ConnectionAddGUI(ConnectionType.LEFT);
            g.setVisible(true);
        });
		mnAddCon.add(mntmLeftCon);

		mntmRightCon = new JMenuItem("Right Connection");
		mntmRightCon.addActionListener(e -> {
            ConnectionAddGUI g = new ConnectionAddGUI(ConnectionType.RIGHT);
            g.setVisible(true);
        });
		mnAddCon.add(mntmRightCon);

		mntmUpCon = new JMenuItem("Up Connection");
		mntmUpCon.addActionListener(e -> {
            ConnectionAddGUI g = new ConnectionAddGUI(ConnectionType.UP);
            g.setVisible(true);
        });
		mnAddCon.add(mntmUpCon);

		mntmDownCon = new JMenuItem("Down Connection");
		mntmDownCon.addActionListener(e -> {
            ConnectionAddGUI g = new ConnectionAddGUI(ConnectionType.DOWN);
            g.setVisible(true);
        });
		mnAddCon.add(mntmDownCon);

		connectionsEditorPanel = new ConnectionsEditorPanel();
		connectionsEditorScroll = new JScrollPane(connectionsEditorPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		connectionsEditorScroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		connectionsEditorScroll.getVerticalScrollBar().setUnitIncrement(16);
		connectionsEditorScroll.getHorizontalScrollBar().setUnitIncrement(16);
		connectionsEditorScroll.addMouseWheelListener(e -> {
            if (e.isControlDown() || e.isAltDown()) {
                connectionsEditorPanel.scale += e.getWheelRotation() / 5d;
                if (connectionsEditorPanel.scale < 0.3)
                    connectionsEditorPanel.scale = 0.3;
                else if (connectionsEditorPanel.scale > 10)
                    connectionsEditorPanel.scale = 10;
                connectionsEditorPanel.RescaleImages(false);
                connectionsEditorPanel.repaint();
            }
            else {
                // Vertical scrolling
                Adjustable adj = connectionsEditorScroll.getVerticalScrollBar();
                int scroll = e.getUnitsToScroll() * adj.getBlockIncrement();
                adj.setValue(adj.getValue() + scroll);
            }
        });
		connectionsTabPanel.add(connectionsEditorScroll, BorderLayout.CENTER);

		CreatePermissions();
		JPanel mapPanelFrame = new JPanel();
		mapPanelFrame.setPreferredSize(new Dimension(242, 10));
		splitPane.setLeftComponent(mapPanelFrame);
		mapPanelFrame.setLayout(new BorderLayout(0, 0));

		mapBanks = new JTree();
		mapBanks.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
		        mapEditorPanel.reset();
				MapIO.loadMap();
		        enableMapOperationButtons();
			}
		});
		mapBanks.addTreeSelectionListener(e -> {
            try {
                Object node = e.getPath().getPath()[e.getPath().getPath().length - 1];
                if (node instanceof BankLoader.MapTreeNode){
                    MapIO.selectedBank = ((BankLoader.MapTreeNode)node).bank;
                    MapIO.selectedMap = ((BankLoader.MapTreeNode)node).map;
                }
            } catch (Exception ex) {ex.printStackTrace();}
        });
		mapBanks.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		mapBanks.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						TreePath selectionPath = mapBanks.getSelectionPath();
						if (selectionPath != null) {
							Object lastPathComponent = selectionPath.getLastPathComponent();
							if (mapBanks.getModel().getIndexOfChild(mapBanks.getModel().getRoot(), lastPathComponent) == -1) {
								mapEditorPanel.reset();
								MapIO.loadMap();
								enableMapOperationButtons();
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		mapBanks.setModel(new DefaultTreeModel(null));
		mapBanks.setCellRenderer(new MapTreeRenderer());
		
		JScrollPane mapPane = new JScrollPane(mapBanks, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mapPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		mapPanelFrame.add(mapPane);
	}

    public static void updateTree() {
        Object root = mapBanks.getModel().getRoot();
        Object type = mapBanks.getModel().getChild(root, 1);
        Object folder = mapBanks.getModel().getChild(type, MapIO.selectedBank);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)(mapBanks.getModel().getChild(folder, MapIO.selectedMap));
        TreePath path = new TreePath(node.getPath());
        mapBanks.setSelectionPath(path);
        mapBanks.scrollPathToVisible(path);
    }

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popupX = e.getX() / 16;
				popupY = e.getY() / 16;
				popup.show(e.getComponent(), (popupX * 16) + 8, (popupY * 16) + 8);
			}
		});
	}

	public static void reloadMimeLabels() {
		spnWidth.setValue((int) MapIO.loadedMap.getMapData().mapWidth);
		spnHeight.setValue((int) MapIO.loadedMap.getMapData().mapHeight);
		lblBorderWidth.setText("Border Width: " + MapIO.loadedMap.getMapData().borderWidth);
		lblBorderHeight.setText("Border Height: " + MapIO.loadedMap.getMapData().borderHeight);
		lblMapTilesPointer.setText("Map Tiles Pointer: " + BitConverter.toHexString(MapIO.loadedMap.getMapData().mapTilesPtr));
		lblBorderTilesPointer.setText("Border Tiles Pointer: " + BitConverter.toHexString(MapIO.loadedMap.getMapData().borderTilePtr));
		txtGlobalTileset.setText(BitConverter.toHexString(MapIO.loadedMap.getMapData().globalTileSetPtr));
		txtLocalTileset.setText(BitConverter.toHexString(MapIO.loadedMap.getMapData().localTileSetPtr));
	}

	public static void loadWildPokemon() {
		if (MapIO.wildData == null || MapIO.wildData.aWildPokemon == null) {
			pkEditorPanel.setVisible(false);
			panelpk6_10.setVisible(false);
			panelpk11_12.setVisible(false);
			return;
		}
		if(MapIO.wildData.aWildPokemon[currentType].bDNEnabled == 1) {
			pkTime.setEnabled(true);
			btnEnableTimebasedPokemon.setVisible(false);
		} else {
			pkTime.setEnabled(false);
			selectedTime = 0;
			btnEnableTimebasedPokemon.setVisible(true);
		}
		if(MapIO.DEBUG)
			System.out.println(selectedTime);

		if (MapIO.currentBank != -1 && MapIO.currentMap != -1 && MapIO.wildData.aWildPokemon[currentType].aWildPokemon != null) {
			pkEncounter.setValue(MapIO.wildData.aWildPokemon[currentType].bRatio);
			pkEditorPanel.setVisible(true);

			pkMin1.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][0].bMinLV);
			pkMax1.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][0].bMaxLV);
			pkName1.setSelectedIndex(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][0].wNum);
			pkName1.repaint();
			pkNo1.setValue(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][0].wNum);

			pkMin2.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][1].bMinLV);
			pkMax2.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][1].bMaxLV);
			pkName2.setSelectedIndex(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][1].wNum);
			pkName2.repaint();
			pkNo2.setValue(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][1].wNum);

			pkMin3.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][2].bMinLV);
			pkMax3.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][2].bMaxLV);
			pkName3.setSelectedIndex(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][2].wNum);
			pkName3.repaint();
			pkNo3.setValue(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][2].wNum);

			pkMin4.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][3].bMinLV);
			pkMax4.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][3].bMaxLV);
			pkName4.setSelectedIndex(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][3].wNum);
			pkName4.repaint();
			pkNo4.setValue(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][3].wNum);

			pkMin5.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][4].bMinLV);
			pkMax5.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][4].bMaxLV);
			pkName5.setSelectedIndex(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][4].wNum);
			pkName5.repaint();
			pkNo5.setValue(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][4].wNum);

			if (currentType == 0 || currentType == 3) {
				panelpk6_10.setVisible(true);

				pkMin6.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][5].bMinLV);
				pkMax6.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][5].bMaxLV);
				pkName6.setSelectedIndex(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][5].wNum);
				pkName6.repaint();
				pkNo6.setValue(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][5].wNum);

				pkMin7.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][6].bMinLV);
				pkMax7.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][6].bMaxLV);
				pkName7.setSelectedIndex(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][6].wNum);
				pkName7.repaint();
				pkNo7.setValue(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][6].wNum);

				pkMin8.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][7].bMinLV);
				pkMax8.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][7].bMaxLV);
				pkName8.setSelectedIndex(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][7].wNum);
				pkName8.repaint();
				pkNo8.setValue(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][7].wNum);

				pkMin9.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][8].bMinLV);
				pkMax9.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][8].bMaxLV);
				pkName9.setSelectedIndex(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][8].wNum);
				pkName9.repaint();
				pkNo9.setValue(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][8].wNum);

				pkMin10.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][9].bMinLV);
				pkMax10.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][9].bMaxLV);
				pkName10.setSelectedIndex(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][9].wNum);
				pkName10.repaint();
				pkNo10.setValue(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][9].wNum);

				if (currentType == 0) {
					panelpk11_12.setVisible(true);

					pkMin11.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][10].bMinLV);
					pkMax11.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][10].bMaxLV);
					pkName11.setSelectedIndex(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][10].wNum);
					pkName11.repaint();
					pkNo11.setValue(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][10].wNum);

					pkMin12.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][11].bMinLV);
					pkMax12.setValue((int) MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][11].bMaxLV);
					pkName12.setSelectedIndex(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][11].wNum);
					pkName12.repaint();
					pkNo12.setValue(MapIO.wildData.aWildPokemon[currentType].aWildPokemon[selectedTime][11].wNum);

					pkchance1.setText("20%");
					pkchance2.setText("20%");
					pkchance3.setText("10%");
					pkchance4.setText("10%");
					pkchance5.setText("10%");
					pkchance6.setText("10%");
					pkchance7.setText("5%");
					pkchance8.setText("5%");
					pkchance9.setText("4%");
					pkchance10.setText("4%");
					pkchance11.setText("1%");
					pkchance12.setText("1%");
				}
				else {
					pkchance1.setText("70%  (old rod)");
					pkchance2.setText("30%  (old rod)");
					pkchance3.setText("60%  (good rod)");
					pkchance4.setText("20%  (good rod)");
					pkchance5.setText("20%  (good rod)");
					pkchance6.setText("40%  (super rod)");
					pkchance7.setText("40%  (super rod)");
					pkchance8.setText("15%  (super rod)");
					pkchance9.setText("4%    (super rod)");
					pkchance10.setText("1%    (super rod)");
					panelpk11_12.setVisible(false);
				}
			}
			else {
				pkchance1.setText("60%");
				pkchance2.setText("30%");
				pkchance3.setText("5%");
				pkchance4.setText("4%");
				pkchance5.setText("1%");
				panelpk6_10.setVisible(false);
				panelpk11_12.setVisible(false);
			}
		}
		else {
			pkEditorPanel.setVisible(false);
			panelpk6_10.setVisible(false);
			panelpk11_12.setVisible(false);
		}
	}

	public void openROM() {
		int i = GBARom.loadRom();
		if (i == -2) {
			JOptionPane.showMessageDialog(null, "The ROM could not be opened.\n" +
					"It may be open in another program.", "Error opening ROM", JOptionPane.WARNING_MESSAGE);
		}		
		if (i < 0)
			return;
		String s = Plugin.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		// while(s.contains("/") || s.contains("\\"))
		if (MapIO.DEBUG)
			System.out.println(s);
		dataStore = new DataStore("MEH.ini", ROMManager.currentROM.getGameCode());
		MapIO.loadPokemonNames();

		//if (true) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(null);
		DefaultTreeModel mapTree = new DefaultTreeModel(root);
		DefaultMutableTreeNode byName = new DefaultMutableTreeNode("Maps by Name");
		DefaultMutableTreeNode byBank = new DefaultMutableTreeNode("Maps by Bank");
		root.add(byName);
		root.add(byBank);
		mapBanks.setRootVisible(false);
		mapBanks.setModel(mapTree);

        BankLoader.reset();
        TilesetCache.clearCache();
        mapEditorPanel.reset();
        borderTileEditor.reset();
        tileEditorPanel.reset();
        mapEditorPanel.repaint();
        borderTileEditor.repaint();
        tileEditorPanel.repaint();

		setStatus("Loading...");
		new BankLoader((int) DataStore.MapHeaders, ROMManager.getActiveROM(), lblInfo, mapBanks, byBank).start();
		new WildDataCache(ROMManager.getActiveROM()).start();
        mnSave.setEnabled(true);
		btnSaveROM.setEnabled(true);
        
		this.setTitle("Map Editor of Happiness | " + new File(ROMManager.getActiveROM().input_filepath).getName());
		PluginManager.fireROMLoad();
	}

	public static void showEventPopUp(int x, int y, EventType event, int index) {
		popupX = x / 16;
		popupY = y / 16;
		popupMenu.show(eventEditorPanel, x, y);
		eventType = event;
		eventIndex = index;
	}

    public static void setStatus(String s) {
        lblInfo.setText(s);
    }

    public static void setMouseCoordinates(int x, int y) {
        lblX.setText("X: " + x);
        lblY.setText("Y: " + y);
    }

	private static class __Tmp {
		private static void __tmp() {
			  javax.swing.JPanel __wbp_panel = new javax.swing.JPanel();
			  
			  javax.swing.JMenuItem menuItem = new javax.swing.JMenuItem("New menu item");
			  __wbp_panel.add(menuItem);
		}
	}

	public void testXorColors() {
		BufferedImage img = (BufferedImage)Map.renderMap(MapIO.loadedMap, true);
		Image orig = img.getScaledInstance(img.getWidth(), img.getHeight(), 0);
		GradientTest t = new GradientTest(orig);
		t.setVisible(true);
		
		Color night = new Color(42,0,168,80);
		Color evening = new Color(56,2,0,40);
		Color day = new Color(0,0,0,0);
		Color morning = new Color(255,86,59,20);
		
		int stepsPH = 6;
		
		Color[] nightN = Palette.gradient(night, night, 3*stepsPH);
		Color[] nightM = Palette.gradient(night, morning, 5*stepsPH);
		Color[] morningD = Palette.gradient(morning, day, 6*stepsPH);
		Color[] dayE = Palette.gradient(day, evening, 4*stepsPH);
		Color[] eveningN = Palette.gradient(evening, night, stepsPH);
		
		Color[] gradient = new Color[24*stepsPH];
		for(int i = 0; i < 24*stepsPH; i++)
			gradient[i] = night;


        System.arraycopy(nightN, 0, gradient, 0, nightN.length);
        System.arraycopy(nightM, 0, gradient, nightN.length, nightM.length);
		
		for(int i = 0; i < morningD.length; i++)
			gradient[i+nightN.length+nightM.length] = morningD[i];
		
		for(int i = 0; i < dayE.length; i++)
			gradient[i+nightN.length+nightM.length+morningD.length] = dayE[i];
		
		for(int i = 0; i < eveningN.length; i++)
			gradient[i+nightN.length+nightM.length+morningD.length+dayE.length] = eveningN[i];
		
		for(int i = 0; i < nightN.length; i++)
			gradient[i+nightN.length+nightM.length+morningD.length+dayE.length+eveningN.length] = nightN[i];
		
		for(int i = 0; i < (24*stepsPH); i++) {
			Graphics g = img.getGraphics();
			g.drawImage(orig, 0,0, null);
			g.setColor(gradient[i]);
			g.fillRect(0, 0, img.getWidth(), img.getHeight());
			try {
				ImageIO.write(img, "png", new File("cycle/time" + String.format("%03d", i) + ".png"));
			} catch (IOException e) {e.printStackTrace();}
		}
		
		BufferedImage b = new BufferedImage(gradient.length * 8, 8, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < 24*stepsPH; i++) {
			Graphics g = b.getGraphics();
			g.setColor(gradient[i]);
			g.fillRect(i*8, 0, 8, 8);
		}
		try {
			ImageIO.write(b, "png", new File("cycle/gradient.png"));
		} catch (IOException e) {e.printStackTrace();}
		System.out.println();
		for(Color c : gradient) {
			System.out.print("0x" + String.format("%02X", c.getAlpha()) + String.format("%02X", c.getRed()) + String.format("%02X", c.getGreen()) + String.format("%02X", c.getBlue()) + ", ");
		}
	}
}