package gui.components;

import java.awt.Color;

import start.X;

import gui.aterai.RoundedCornerButton;

public class DefaultButton extends RoundedCornerButton {
	private static final long serialVersionUID = 1L;
	
	public DefaultButton(){
		this("");
	}
	
	public DefaultButton(String text){
		super(text);
		setBackground(Color.DARK_GRAY);
		setForeground(Color.LIGHT_GRAY);
		setFocusPainted(false);
		setFont(X.buttonFont);
		setActionColor(Color.BLACK);
	}

}
