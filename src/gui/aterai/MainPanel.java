package gui.aterai;
//-*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
//http://terai.xrea.jp/Swing/RoundButton.html
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

class MainPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private final JButton button = new JButton("RoundedCornerButtonUI");
    public MainPanel() {
        super();
        add(new JButton("Default JButton"));
        button.setUI(new RoundedCornerButtonUI());
        add(button);
        add(new RoundedCornerButton("Rounded Corner Button"));
        add(new ShapeButton(makeStar(25,30,20)));
        add(new RoundButton("Round Button"));
        setPreferredSize(new Dimension(320, 200));
    }
    private Path2D.Double makeStar(int r1, int r2, int vc) {
        int or = Math.max(r1, r2);
        int ir = Math.min(r1, r2);
        double agl = 0.0;
        double add = 2*Math.PI/(vc*2);
        Path2D.Double p = new Path2D.Double();
        p.moveTo(or*1, or*0);
        for(int i=0;i<vc*2-1;i++) {
            agl+=add;
            int r = i%2==0?ir:or;
            p.lineTo(r*Math.cos(agl), r*Math.sin(agl));
        }
        p.closePath();
        AffineTransform at = AffineTransform.getRotateInstance(-Math.PI/2,or,0);
        return new Path2D.Double(p, at);
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGUI();
            }
        });
    }
    public static void createAndShowGUI() {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("RoundButton");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}