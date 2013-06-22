package gui.menu;

import games.Modus;
import gui.Anzeige;
import gui.components.DefaultButton;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import start.X;

public class ModusMenuPanel extends Anzeige {
	private static final long serialVersionUID = 1L;
	private static String myBackground = "media/ablauf/iceBG3.jpg";
	private JPanel soloPanel;
	private JButton soloButton;
	private JPanel duellPanel;
	private JButton duellButton;
	private JPanel anweisungPanel;
	private JPanel teamPanel;
	private JButton teamButton;
	private JPanel tripplePanel;
	private JButton trippleButton;
	private JPanel viererPanel;
	private JButton viererButton;
	private JLabel anweisungLabel;

	public ModusMenuPanel(){
		GridLayout myLayout = new GridLayout(6, 1);
		myLayout.setHgap(5);
		myLayout.setVgap(5);
		myLayout.setColumns(1);
		myLayout.setRows(6);
		this.setLayout(myLayout);
		this.setOpaque(false);
		{
			anweisungPanel = new JPanel();
			this.add(anweisungPanel);
			anweisungPanel.setOpaque(false);
			{
				anweisungLabel = new JLabel("Modus wählen...");
				anweisungPanel.add(anweisungLabel);
				Font grosserFont = X.buttonFont.deriveFont(X.buttonFont.getSize()*3.0f);
				anweisungLabel.setFont(grosserFont);
				anweisungLabel.setForeground(Color.WHITE);
			}
		}
		{
			soloPanel = new JPanel();
			add(soloPanel);
			soloPanel.setOpaque(false);
			{
				soloButton = new DefaultButton(Modus.SOLO.toString());
				soloPanel.add(soloButton);
				soloButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						soloButtonActionPerformed(evt);
					}
				});
			}
		}
		{
			duellPanel = new JPanel();
			add(duellPanel);
			duellPanel.setOpaque(false);
			{
				duellButton = new DefaultButton();
				duellPanel.add(duellButton);
				duellButton.setText(Modus.DUELL.toString());
				duellButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						duellButtonActionPerformed(evt);
					}
				});
			}
		}
		{
			teamPanel = new JPanel();
			add(teamPanel);
			teamPanel.setOpaque(false);
			{
				teamButton = new DefaultButton();
				teamPanel.add(teamButton);
				teamButton.setText(Modus.TEAM.toString());
				teamButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						teamButtonActionPerformed(evt);
					}
				});
			}
		}
		{
			tripplePanel = new JPanel();
			add(tripplePanel);
			tripplePanel.setOpaque(false);
			{
				trippleButton = new DefaultButton();
				tripplePanel.add(trippleButton);
				trippleButton.setText(Modus.TRIPPLE.toString());
				trippleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						trippleButtonActionPerformed(evt);
					}
				});
			}
		}
		{
			viererPanel = new JPanel();
			add(viererPanel);
			viererPanel.setOpaque(false);
			{
				viererButton = new DefaultButton();
				viererPanel.add(viererButton);
				viererButton.setText(Modus.VIERER.toString());
				viererButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						viererButtonActionPerformed(evt);
					}
				});
			}
		}
	}

	protected void viererButtonActionPerformed(ActionEvent evt) {
		//EasyDialog.showMessage("Der Vierspielermodus ist noch in Bearbeitung", 
		//		new Bildschirm("media/ablauf/baustelle.jpg"));
		instance.changeAnzeige(new StartMatch(Modus.VIERER));
	}
	protected void teamButtonActionPerformed(ActionEvent evt) {
		instance.changeAnzeige(new StartMatch(Modus.TEAM));
	}

	protected void trippleButtonActionPerformed(ActionEvent evt) {
		//EasyDialog.showMessage("Der Dreispielermodus ist noch in Bearbeitung", 
		//		new Bildschirm("media/ablauf/baustelle2.jpg"));
		instance.changeAnzeige(new StartMatch(Modus.TRIPPLE));
	}

	protected void soloButtonActionPerformed(ActionEvent evt) {
		//EasyDialog.showMessage("Der Einzelspielermodus befindet sich noch in Bearbeitung", 
		//		new Bildschirm("media/ablauf/baustelle3.jpg"));
		instance.changeAnzeige(new StartMatch(Modus.SOLO));
	}

	protected void duellButtonActionPerformed(ActionEvent evt) {
		instance.changeAnzeige(new StartMatch(Modus.DUELL));
	}

	@Override
	public void nowVisible(){
		instance.changeBackground(myBackground);
	}
}
