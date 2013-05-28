package gui.components;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import start.X;

public class JButtonIcon extends JButton {
	/**
	 * Serial Version UID generated by Eclipse
	 */
	private static final long serialVersionUID = -5447642185291377872L;

	ImageIcon icon;
	public JButtonIcon(String icon, String alternativ){
		super();
		try{
			this.icon = new ImageIcon(getToolkit().getImage(X.getMainDir()+icon));
			this.setIcon(this.icon);
			this.setOpaque(false);
		}
		catch(Exception e){
			this.setText(alternativ);
		}
	}
	@Override
	public Dimension getPreferredSize(){
		if(icon!=null){
			return new Dimension(icon.getIconWidth(),icon.getIconHeight());
		}
		else {
			return super.getPreferredSize();
		}
	}
}
