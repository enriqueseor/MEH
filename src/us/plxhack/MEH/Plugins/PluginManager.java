package us.plxhack.MEH.Plugins;

import org.zzl.minegaming.GBAUtils.ROMManager;
import us.plxhack.MEH.UI.MainGUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Objects;

public class PluginManager {

	private static final ArrayList<Plugin> plugins = new ArrayList<>();

	public static void loadAllPlugins() throws Exception {
		File search = new File("plugins/");
		if (search.exists()){
			for(File file : Objects.requireNonNull(search.listFiles())) {
				URLClassLoader classLoader = URLClassLoader .newInstance(new URL[] { new URL("file:./plugins/" + file.getName()) });
				Class<?> clazz = classLoader.loadClass("org.zzl.minegaming.TestPlugin.Plugin");
				final Plugin plugin = (Plugin) clazz.newInstance();
				plugin.load();
				plugins.add(plugin);
				
				if(plugin.createButton) {
					JButton btnPlugin = getjButton(plugin);
					MainGUI.panelButtons.add(btnPlugin);
				}
			}
		}
	}

	private static JButton getjButton(Plugin plugin) {
		JButton btnPlugin = new JButton("");
		btnPlugin.setToolTipText(plugin.getToolTip());
		btnPlugin.addActionListener(e -> {
if(plugin.bLoadROM){
plugin.loadROM(ROMManager.currentROM);
}
plugin.execute();
});
		btnPlugin.setIcon(plugin.getButtonImage());
		btnPlugin.setFocusPainted(false);
		btnPlugin.setBorderPainted(false);
		btnPlugin.setPreferredSize(new Dimension(54, 48));
		return btnPlugin;
	}

	public static void unloadAllPlugins() {
		for(Plugin p : plugins)
			p.unload();
	}
	
	public static void fireROMLoad() {
		for(Plugin p : plugins)
			p.loadROM(ROMManager.currentROM);
	}
	
	public static void fireROMSave() {
		for(Plugin p : plugins)
			p.saveROM();
	}
	
	public static void fireMapLoad(int bank, int map) {
		for(Plugin p : plugins)
			p.loadMap(bank,map);
	}
	
	public static void fireMapSave(int bank, int map) {
		for(Plugin p : plugins)
			p.saveMap(bank,map);
	}
}