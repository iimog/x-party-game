package gui.aterai;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class ShapeButton extends JButton {
	private static final long serialVersionUID = 1L;
	protected final Color fc = new Color(100,150,255,200);
    protected final Color ac = new Color(230,230,230);
    protected final Color rc = Color.ORANGE;
    protected Shape shape;
    public ShapeButton(Shape s) {
        shape = s;
        setModel(new DefaultButtonModel());
        init("Shape", new DummySizeIcon(s));
        setVerticalAlignment(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBackground(new Color(250, 250, 250));
    }
    private void paintFocusAndRollover(Graphics2D g2, Color color) {
        g2.setPaint(new GradientPaint(0, 0, color, getWidth()-1, getHeight()-1, color.brighter(), true));
        g2.fill(shape);
    }
    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(getModel().isArmed()) {
            g2.setColor(ac);
            g2.fill(shape);
        }else if(isRolloverEnabled() && getModel().isRollover()) {
            paintFocusAndRollover(g2, rc);
        }else if(hasFocus()) {
            paintFocusAndRollover(g2, fc);
        }else{
            g2.setColor(getBackground());
            g2.fill(shape);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setColor(getBackground());
        super.paintComponent(g2);
    }
    @Override protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.draw(shape);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
    @Override public boolean contains(int x, int y) {
        return shape.contains(x, y);
    }
    private static class DummySizeIcon implements Icon{
        private final Shape shape;
        public DummySizeIcon(Shape s) {
            shape = s;
        }
        @Override public int getIconWidth() {
            return shape.getBounds().width;
        }
        @Override public int getIconHeight() {
            return shape.getBounds().height;
        }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {}
    }
}
