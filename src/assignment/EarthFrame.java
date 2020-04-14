package assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EarthFrame extends JFrame {

    private EarthRenderer earthRenderer;
    private EarthFrameRenderListener eventListener;

    private final String title = "Earth Altitude Map";

    private ArrayList<MapCoordinate> selectedCoordinates = new ArrayList<>();

    public EarthFrame(Earth earth){
        this.earthRenderer = new EarthRenderer(earth);
        this.eventListener = new EarthFrameRenderListener();
        var earthFrameRenderListener = new EarthFrameRenderListener();
        this.earthRenderer.addMouseListener(earthFrameRenderListener);
        this.earthRenderer.addKeyListener(earthFrameRenderListener);
        this.earthRenderer.addMouseMotionListener(earthFrameRenderListener);
        this.earthRenderer.addMouseWheelListener(earthFrameRenderListener);
        this.setResizable(false);
        this.getContentPane().setPreferredSize(new Dimension(this.earthRenderer.getWidth(), this.earthRenderer.getHeight()));
        this.add(this.earthRenderer);
        this.pack();
        this.setTitle(title);
        this.setVisible(true);
        this.repaint();
    }

    class EarthFrameRenderListener extends MouseAdapter implements KeyListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            var x = earthRenderer.getLastClickedX();
            var y = earthRenderer.getLastClickedY();
            var visibleAltitude = earthRenderer.getSelectedVisibleAltitude();
            setTitle(title + "| selected (" +  x + "," + y + ")  visible altitude= " + visibleAltitude);

            selectedCoordinates.add(earthRenderer.getLastSelectedCoordinate());
            // print the clicked coordinates to cl
            // print the distance to last selected
            // sort the list
            // right click should delete the last added coordinate and print the deleted coordinate

            //method, that will write the list of coordinates to a file, start a new file each time the program is run,
            // I may overwrite the last file, but I shall not overwrite the existing coordinates when adding a new set

        }

        @Override
        public void mousePressed(MouseEvent e) { }

        @Override
        public void mouseReleased(MouseEvent e) { }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) { }

        @Override
        public void mouseDragged(MouseEvent e) {
            setTitle(title);
        }

        @Override
        public void mouseMoved(MouseEvent e) { }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            super.mouseWheelMoved(e);
            setTitle(title);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            var charPressed = e.getKeyChar();
            if( charPressed == 'i' || charPressed == 'o' || charPressed == KeyEvent.VK_ESCAPE){
                setTitle(title);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) { }

        @Override
        public void keyReleased(KeyEvent e) { }
    }
}
