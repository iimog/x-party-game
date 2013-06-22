package gui.aterai;

import java.awt.Dimension;
import java.awt.geom.Ellipse2D;

import javax.swing.Action;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;

public class RoundButton extends RoundedCornerButton {
	private static final long serialVersionUID = 1L;
	public RoundButton() {
        this(null, null);
    }
    public RoundButton(Icon icon) {
        this(null, icon);
    }
    public RoundButton(String text) {
        this(text, null);
    }
    public RoundButton(Action a) {
        this();
        setAction(a);
    }
    public RoundButton(String text, Icon icon) {
        setModel(new DefaultButtonModel());
        init(text, icon);
        setFocusPainted(false);
        initShape();
    }
    @Override public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.width = d.height = Math.max(d.width, d.height);
        return d;
    }
    @Override protected void initShape() {
        if(!getBounds().equals(base)) {
            base = getBounds();
            shape = new Ellipse2D.Float(0, 0, getWidth()-1, getHeight()-1);
            border = new Ellipse2D.Float(focusstroke, focusstroke,
                                         getWidth()-1-focusstroke*2,
                                         getHeight()-1-focusstroke*2);
        }
    }
}