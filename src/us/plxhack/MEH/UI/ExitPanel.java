package us.plxhack.MEH.UI;

import us.plxhack.MEH.IO.Map.MapIO;
import us.plxhack.MEH.MapElements.Sprite.SpriteExitManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ExitPanel extends JPanel {

	int myIndex;
	JSpinner spinner;
	JSpinner spinner_1;
	
	void Load(SpriteExitManager mgr, int index){
		spinner.setValue(mgr.mapExits.get(index).bBank);
		spinner_1.setValue(mgr.mapExits.get(index).bMap);
	}
    void Save(SpriteExitManager mgr){
    	mgr.mapExits.get(myIndex).bBank = ((Byte)spinner.getValue()).byteValue();
    	mgr.mapExits.get(myIndex).bMap = ((Integer)spinner_1.getValue()).byteValue();
    }
	/**
	 * Create the panel.
	 */
	public ExitPanel(SpriteExitManager mgr, int index) {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		myIndex=index;
		setBorder(new TitledBorder(null, "Exit", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLabel lblDestBank = new JLabel("Dest Bank:");
		add(lblDestBank);
		
		spinner = new JSpinner();
		spinner.setPreferredSize(new Dimension(100, 30));
		add(spinner);
		
		JLabel lblDestMap = new JLabel("Dest Map:");
		add(lblDestMap);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(e -> Save(MapIO.loadedMap.mapExitManager));
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setPreferredSize(new Dimension(0, 0));
		add(horizontalStrut);
		
		spinner_1 = new JSpinner();
		spinner_1.setPreferredSize(new Dimension(100, 30));
		add(spinner_1);
		add(btnSave);
        Load(mgr, index);
	}
}