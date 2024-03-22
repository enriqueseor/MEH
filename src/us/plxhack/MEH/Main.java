package us.plxhack.MEH;

import java.awt.Dimension;
import java.awt.Toolkit;

import us.plxhack.MEH.IO.BankLoader;
import us.plxhack.MEH.Plugins.PluginManager;
import us.plxhack.MEH.UI.MainGUI;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		try {
			setSystemProperties();
			setLookAndFeel();
			JFrame window = createMainWindow();
			loadPlugins();
			window.setVisible(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					null,
					"An error occurred: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private static void setSystemProperties() {
		System.setProperty("sun.java2d.opengl", "true");
		System.setProperty("sun.java2d.accthreshold", "0");
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");
	}

	private static void setLookAndFeel() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		if (System.getProperty("os.name").toLowerCase().contains("nix") ||
				System.getProperty("os.name").toLowerCase().contains("nux") ||
				System.getProperty("os.name").toLowerCase().contains("aix")) {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} else {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
	}

	private static JFrame createMainWindow() {
		JFrame window = new MainGUI();
		window.setSize(1280, 720);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(
				screenSize.width / 2 - window.getSize().width / 2,
				screenSize.height / 2 - window.getSize().height / 2);
		window.setTitle("Map Editor of Happiness | No ROM Loaded");
		window.setMinimumSize(new Dimension(640, 360));
		BankLoader.reset();
		return window;
	}

	private static void loadPlugins() {
		try {
			PluginManager.loadAllPlugins();
		} catch (Exception e) {
			System.err.println("Failed to load plugins: " + e.getMessage());
		}
	}
}