package testPreparaiton;

import javax.swing.*;
import java.awt.*;

public class Drawing1 extends JComponent {
    public void paintComponent(Graphics graphics) {
        // Whats what we are doing below called?
        Graphics2D g = (Graphics2D) graphics;
        // add drawing code below
        g.setColor(Color.green);
        g.fillRect(100, 100, 200, 100);
    }
}
