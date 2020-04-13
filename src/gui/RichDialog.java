package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import gui.components.DefaultButton;


public class RichDialog extends AnzeigeDialog {
	private static final long serialVersionUID = 1L;
	private URL resource;
	private DefaultButton okButton;
	
	public RichDialog(URL resource){
		this.resource = resource;
		initGUI();
	}

	private JPanel getMessageButtonPanel(){
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		okButton = new DefaultButton("OK");
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				instance.closeDialog();
			}
		});
		panel.add(okButton);
		return panel;
	}

	private void initGUI(){
		JPanel hauptbereich = new JPanel();
		hauptbereich.setOpaque(false);
		hauptbereich.setLayout(new BorderLayout());
		JEditorPane editorPane = new JEditorPane();
		if (resource != null) {
		    try {
		        editorPane.setPage(resource);
		    } catch (IOException e) {
		        System.err.println("Attempted to read a bad URL: " + resource);
		    }
		} else {
		    System.err.println("Couldn't find file");
		}
		JScrollPane editorScrollPane = new JScrollPane(editorPane);
		editorScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setPreferredSize(new Dimension(800, 500));
		editorScrollPane.setMinimumSize(new Dimension(10, 10));
		hauptbereich.add(editorScrollPane, BorderLayout.CENTER);
		JPanel panel = null;
		panel = getMessageButtonPanel();
		hauptbereich.add(panel, BorderLayout.SOUTH);
		dialogPane.add(hauptbereich);
		dialogPane.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
	}
	
	public boolean requestFocusForMainButton() {
		boolean reqFoc = false;
		if(okButton != null) {
			reqFoc = okButton.requestFocusInWindow();
		}
		return reqFoc;
	}
}

