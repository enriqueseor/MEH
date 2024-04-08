package us.plxhack.MEH.UI;

import org.zzl.minegaming.GBAUtils.BitConverter;
import us.plxhack.MEH.IO.MapIO;
import us.plxhack.MEH.MapElements.Sprite.SpriteHeader;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class SignPanel extends JPanel {

	private final JTextField textField;
	int myIndex;

	void Load(SpriteHeader.SpritesSignManager mgr, int index){
    	textField.setText(BitConverter.toHexString((int) mgr.mapSigns.get(index).pScript));
    }
    void Save(SpriteHeader.SpritesSignManager mgr){
    	mgr.mapSigns.get(myIndex).pScript = Integer.parseInt(textField.getText(), 16);
    }
	/**
	 * Create the panel.
	 */
	public SignPanel(SpriteHeader.SpritesSignManager mgr, int index) {
		myIndex=index;
		setBorder(new TitledBorder(null, "Sign", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(null);
		
		JLabel lblScriptPointer = new JLabel("<html>Script Pointer:   <B style=\"color: green\">$</B><html>");
		lblScriptPointer.setBounds(12, 27, 127, 15);
		add(lblScriptPointer);
		
		textField = new JTextField();
		textField.setBounds(128, 23, 83, 26);
		add(textField);
		textField.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(12, 119, 68, 25);
		btnSave.addActionListener(e -> Save(MapIO.loadedMap.mapSignManager));
		add(btnSave);
		
		JButton btnOpenScript = new JButton("Open Script");
		btnOpenScript.addActionListener(arg0 -> MapIO.openScript(Integer.parseInt(textField.getText(), 16)));
		btnOpenScript.setBounds(56, 54, 142, 25);
		add(btnOpenScript);
		Load(mgr, index);
	}
}