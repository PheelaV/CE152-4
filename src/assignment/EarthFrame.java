package assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class EarthFrame extends JFrame {

    private EarthRenderer earthRenderer;
    private EarthFrameRenderListener eventListener;

    private final String title = "Earth Altitude Map";

    private ArrayList<MapCoordinate> selectedCoordinates = new ArrayList<>();

    private final String currentDirectory = System.getProperty("user.dir");

    private String logFile;

    public EarthFrame(Earth earth){
        this.earthRenderer = new EarthRenderer(earth);
        this.eventListener = new EarthFrameRenderListener();
        var earthFrameRenderListener = new EarthFrameRenderListener();
        this.earthRenderer.addMouseListener(earthFrameRenderListener);
        this.earthRenderer.addKeyListener(earthFrameRenderListener);
        this.earthRenderer.addMouseMotionListener(earthFrameRenderListener);
        this.earthRenderer.addMouseWheelListener(earthFrameRenderListener);

        var earthWindowsListener = new EarthWindowListener();
        this.addWindowListener(earthWindowsListener);

        this.setResizable(false);
        this.getContentPane().setPreferredSize(new Dimension(this.earthRenderer.getWidth(), this.earthRenderer.getHeight()));
        this.add(this.earthRenderer);
        this.pack();
        this.setTitle(title);
        this.setVisible(true);
        this.repaint();

        //SOURCE: https://stackoverflow.com/questions/3914404/how-to-get-current-moment-in-iso-8601-format-with-date-hour-and-minute/3914973
        var timezone = TimeZone.getTimeZone("UTC");
        var dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmssSSS'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        dateFormat.setTimeZone(timezone);
        var nowAsISO = dateFormat.format(new Date());
        this.logFile = "altitudeMapLog_" + nowAsISO + ".xyz";
    }

    private void saveCoordinate(ArrayList<MapCoordinate> coordinates){

        var logFilePath = Paths.get(currentDirectory, logFile);

        try {
            FileWriter myWriter = new FileWriter(logFilePath.toString(), true);

            for (var coordinate: coordinates){
                myWriter.write(coordinate.toString() + "\n");
            }

            myWriter.close();
        } catch (IOException e) {
            System.out.println("Failed to save log");
            e.printStackTrace();
        }
    }

    class EarthWindowListener implements  WindowListener {

        @Override
        public void windowOpened(WindowEvent e) { }

        @Override
        public void windowClosing(WindowEvent e) {
            saveCoordinate(selectedCoordinates);
        }

        @Override
        public void windowClosed(WindowEvent e) { }

        @Override
        public void windowIconified(WindowEvent e) { }

        @Override
        public void windowDeiconified(WindowEvent e) { }

        @Override
        public void windowActivated(WindowEvent e) { }

        @Override
        public void windowDeactivated(WindowEvent e) { }
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
