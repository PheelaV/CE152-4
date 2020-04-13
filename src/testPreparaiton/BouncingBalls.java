package testPreparaiton;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JComponent;

public class BouncingBalls extends JComponent {
    public static final int REPAINT_INTERVAL = 15;
    public static final int BALL_RADIUS = 60;
    public static final Color BOX_COLOR = Color.BLACK;
    public static final int BOX_MARGIN = 50;
    public static Ball[] ballArray = new Ball[12];

    public BouncingBalls() {
        Random r = new Random();
        for (int i = 0; i < ballArray.length; i++) {
            Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            int size = (int) ((1 + r.nextDouble()) * BALL_RADIUS);
            ballArray[i] = new Ball(size, c);
        }
    }

    public void paintComponent(Graphics g) {
        g.setColor(BOX_COLOR);
        g.drawRect(BOX_MARGIN, BOX_MARGIN, Ball.BOX_WIDTH, Ball.BOX_HEIGHT);
        for (Ball b : ballArray) {
            g.setColor(b.getColor());
            int r = b.getRadius();
            g.fillOval(BOX_MARGIN + (int) b.getX() - r, BOX_MARGIN + (int) b.getY() - r, 2 * r, 2 * r);
        }
    }
}