package assignment;

import javax.swing.*;
import java.awt.*;

public class EarthFrame extends JFrame {
    private EarthRenderer earthRenderer;

    public EarthFrame(EarthRenderer earthRenderer){
        this.earthRenderer = earthRenderer;
        this.setResizable(false);
        this.getContentPane().setPreferredSize(new Dimension(earthRenderer.getWidth(), earthRenderer.getHeight()));
        this.add(this.earthRenderer);
        this.pack();
        this.setTitle("Earth Altitude Map");
        this.setVisible(true);
    }
}
