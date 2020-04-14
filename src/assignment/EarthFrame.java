package assignment;

import javax.swing.*;
import java.awt.*;

public class EarthFrame extends JFrame {
    private EarthRenderer earthRenderer;

    public EarthFrame(Earth earth){
        this.earthRenderer = new EarthRenderer(earth);
        this.setResizable(false);
        this.getContentPane().setPreferredSize(new Dimension(this.earthRenderer.getWidth(), this.earthRenderer.getHeight()));
        this.add(this.earthRenderer);
        this.pack();
        this.setTitle("Earth Altitude Map");
        this.setVisible(true);
        this.repaint();

//        var focusable = this.isFocusable();

    }
}
