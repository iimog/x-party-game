package games.tron;

import gui.AnzeigeDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TronDetailsDialog extends AnzeigeDialog {
	private static final long serialVersionUID = 1L;
	private Tron tron;
	private JPanel hauptbereichPanel;
	private JPanel buttonPanel;
	private JButton okButton;
	private TronSpielfeld spielfeld;
	private JPanel infoPanel;
	
	public TronDetailsDialog(Tron tron){
		this.tron = tron;
		initGUI();
		okButton.requestFocus();
	}
	
	private void initGUI(){
		addHauptbereich();
		addButtons();
	}

	private void addHauptbereich() {
		hauptbereichPanel = new JPanel();
		hauptbereichPanel.setLayout(new BorderLayout());
		spielfeld = new TronSpielfeld(tron.colums, tron.rows, tron.feldGroesse);
		if(tron.colums<=100 && tron.rows<=50)
			spielfeld.showGitter(true);
		for(int i=0; i<tron.spielerZahl; i++){
			spielfeld.faerbeFelder(tron.belegteFelder.get(i), tron.myPlayer[i].farbe);
		}
		for(int i=0; i<tron.spielerZahl; i++){
			try{
				Color col = tron.myPlayer[i].farbe;
				if(tron.kollision[i])
					col = Color.RED;
				spielfeld.faerbeFeld(tron.kopf[i], col);
			}
			catch(Exception e){
				spielfeld.faerbeFeld(tron.moveSchlangeRueckwaerts(tron.kopf[i], tron.aktuelleRichtung[i]), Color.RED);
			}
		}
		hauptbereichPanel.add(spielfeld, BorderLayout.CENTER);
		addInfos();
		dialogPane.add(hauptbereichPanel, BorderLayout.CENTER);
	}

	private void addInfos() {
		infoPanel = new JPanel();
		infoPanel.add(new JLabel("Belegter Bereich: "));
		int gesamt = tron.rows*tron.colums;
		int belegt = 0;
		for(int i=0; i<tron.spielerZahl; i++){
			belegt += tron.belegteFelder.get(i).size();
		}
		double proz = ((double)belegt/(double)gesamt)*1000;
		proz = (double)Math.round(proz)/10;
		String text = proz + "% (" + belegt + " von " + gesamt + " Feldern belegt)";
		infoPanel.add(new JLabel(text));
		hauptbereichPanel.add(infoPanel, BorderLayout.NORTH);
	}

	private void addButtons() {
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		dialogPane.add(buttonPanel, BorderLayout.SOUTH);
		okButton = new JButton("OK");
		buttonPanel.add(okButton);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				instance.closeDialog();
			}
		});
	}
}