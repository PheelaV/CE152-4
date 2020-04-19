package assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

public class EarthFrame extends JFrame {

    private EarthRenderer earthRenderer;
    private EarthFrameRenderListener eventListener;

    private final String title = "Earth Altitude Map";

    private ArrayList<OrderableMapCoordinate> selectedCoordinates = new ArrayList<>();

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
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

    // The logic is: what is the offset to normal sea level
    public void setInitialSeaLevel(int addedAltitude){
        this.earthRenderer.seaLevel(addedAltitude);
    }

    private void saveCoordinate(ArrayList<OrderableMapCoordinate> coordinates){

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

//    private double calculate

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
            //1 left
            //3 right
            var buttonClicked = e.getButton();
            if (buttonClicked== MouseEvent.BUTTON1) {
                var x = earthRenderer.getLastClickedX();
                var y = earthRenderer.getLastClickedY();
                var visibleAltitude = earthRenderer.getSelectedVisibleAltitude();
                var selectedCoordinate = earthRenderer.getLastSelectedCoordinate();
                String format = "%s| selected(%d, %d) long=%.2f lat=%.2f trueAlt=%.2f visibleAlt=%.2f";
                setTitle(String.format(format, title, x, y, selectedCoordinate.longitude, selectedCoordinate.latitude, selectedCoordinate.altitude, visibleAltitude));

                System.out.println(selectedCoordinate);

                var previouslySelectedCoordinateIndex = selectedCoordinates.size() - 1;
                if (previouslySelectedCoordinateIndex != -1){
                    var previouslySelectedCoordinate = selectedCoordinates.get(previouslySelectedCoordinateIndex);

                    System.out.println("Distance to previously selected coordinate = " + selectedCoordinate.distanceTo(previouslySelectedCoordinate) + " meters" );
                }

                selectedCoordinates.add(new OrderableMapCoordinate(selectedCoordinate, selectedCoordinates.size()));
                Collections.sort(selectedCoordinates);
            } else if (buttonClicked== MouseEvent.BUTTON3) {
                if (selectedCoordinates.size() == 0) return;

                int lastAddedIndex = -1;
                int maxCoordinateOrder = -1;
                for (int i = 0; i < selectedCoordinates.size(); i++){
                    var coordinateOrder = selectedCoordinates.get(i).getOrder();

                    if(coordinateOrder > maxCoordinateOrder){
                       lastAddedIndex = i;
                        maxCoordinateOrder = coordinateOrder;
                    }
                }
                var lastCoordinate = selectedCoordinates.get(lastAddedIndex);

                selectedCoordinates.remove(lastAddedIndex);
                System.out.println("Deleted: " + lastCoordinate);
            };

            // The mighty spec says:
//            Add a mouse listener to your graphical user interface (GUI).
//            A left click should:
//                • add the coordinates of your map + true elevation to a List<MapCoordinate> OR if you did
//                    not complete Exercise 3+4 add a random double with the current screen coordinates to the list
//                • Print the clicked coordinates to the command line
//                • Print the distance to the previously clicked point (if there was one) to the command line
//                • Sort the list
//            A right click should:
//                • Delete the last coordinate that was added to the list (not the last one in the list)
//                • Print the deleted coordinate
            // And so it will be
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
