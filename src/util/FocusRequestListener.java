package util;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * Helper Listener to reliably request focus for a particular component
 * Inspired by: https://tips4java.wordpress.com/2010/03/14/dialog-focus/
 */
public class FocusRequestListener implements AncestorListener {

	@Override
	public void ancestorAdded(AncestorEvent event) {
		event.getComponent().requestFocusInWindow();
	}

	@Override
	public void ancestorRemoved(AncestorEvent event) { }

	@Override
	public void ancestorMoved(AncestorEvent event) { }

}
