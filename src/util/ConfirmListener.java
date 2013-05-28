package util;

import javax.swing.JOptionPane;

public interface ConfirmListener {
	public static final int YES_OPTION = JOptionPane.YES_OPTION;
	public static final int NO_OPTION = JOptionPane.NO_OPTION;
	public static final int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;
	public void confirmOptionPerformed(int optionType);
}
