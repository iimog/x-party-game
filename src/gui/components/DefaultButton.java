package gui.components;

import java.awt.Color;

import javax.swing.JButton;

import start.X;

import gui.aterai.RoundedCornerButtonUI;

public class DefaultButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	public DefaultButton(){
		this("");
	}
	
	public DefaultButton(String text){
		super(text);
		setBackground(Color.WHITE);
		setForeground(Color.DARK_GRAY);
		setFocusPainted(false);
		setFont(X.BUTTON_FONT);
		setUI(new RoundedCornerButtonUI());
	}

}
