package testPreparaiton;

import javax.swing.*;
import java.awt.*;

public class Drawing2 extends JComponent {
    public void paintComponent(Graphics g) { circles((Graphics2D) g); }
    private int rnd(int n) { return (int) (Math.random() * n); }

    private void circles(Graphics2D g) {
        for (int i = 0; i < 50; i++) {
            g.setColor(new Color(rnd(256), rnd(256), rnd(256)));
        int r = 5 + rnd(60);
        g.fillOval(rnd(600), rnd(600), r, r);
        }
    }
}
