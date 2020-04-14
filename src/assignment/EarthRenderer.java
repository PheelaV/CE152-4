package assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EarthRenderer extends JComponent {
    private Earth earth;

    private final int xProjectionSize = 640;
    private final int yProjectionSize = 320;

    public ArrayList<ArrayList<MapCoordinate>> data;
    private int xDataSize;
    private int yDataSize;

    private int altitudeMin = -11000;
    private int altitudeMax = 8500;
    //beginfun
    private float zoom = 1;
    private int xZoomPanCompensation = 0;
    private int yZoomPanCompensation = 0;

    private double realXUnit;
    private double realYUnit;
    private int seaLevelRise = 0;

    private boolean mouseDragInProgress = false;
    private int xLastMouseDragStart = 0;
    private int yLastMouseDragStart = 0;
    private int xPan = 0;
    private int yPan = 0;
    //for centering
    private int xDefaultPan;
    //endfun

    public EarthRenderer(Earth earth){
        this.earth = earth;

        this.refreshData();

        var adaptor = new MapInputAdapter();
        this.addMouseListener(adaptor);
        this.addMouseWheelListener(adaptor);
        this.addMouseMotionListener(adaptor);
        this.addKeyListener(adaptor);
//
//        var focusable = this.isFocusable();
    }

    public void seaLevel(int addedAltitude){
        //this is utterly inefficient, better option is to affect just the values displayed
//        this.earth.seaLevel(addedAltitude);
//        this.refreshData();
        this.seaLevelRise += addedAltitude;
        this.altitudeMin += addedAltitude;
        this.altitudeMax += addedAltitude;

        //centering out the X axis on 0;
        xDefaultPan += this.xProjectionSize / 2;
    }

    private void refreshData() {
        this.data = new ArrayList<ArrayList<MapCoordinate>>();

        for (var xEntry: this.earth.getMapData().entrySet()){
            this.data.add(new ArrayList<>(xEntry.getValue().values()));
        }

        this.checkDataIntegrity();
        this.setRenderingUnits();
    }

    private void checkDataIntegrity() {
        var yAxisSize = data.get(0).size();

        for (var entry: this.data){
            if(entry.size() != yAxisSize){
                throw new IllegalArgumentException("EarthRenderer needs rectangularly shaped data");
            }
        }
    }

    @Override
    public int getHeight() {
        return this.yProjectionSize;
    }

    @Override
    public int getWidth(){
        return this.xProjectionSize;
    }

    @Override
    public void paintComponent(Graphics g) {
        var xVirtualProjectionSize = zoom * xProjectionSize;
        var yVirtualProjectionSize = zoom * yProjectionSize;
        var xVirtualPan = this.xPan * zoom;
        var yVirtualPan = this.yPan * zoom;
        for (var x = 0; x < xProjectionSize; x++){
            for (var y = 0; y < yProjectionSize; y++){
                //account for plane panning, limit value to the projectionConstraints
                var xSubModulo =  ((x + xVirtualPan) % xVirtualProjectionSize);
                var ySubModulo =  ((y - yVirtualPan) % yVirtualProjectionSize);
                //if it is negative, invert it with respect the projection size
                var xSubExact = realXUnit * (xSubModulo < 0 ? xVirtualProjectionSize + xSubModulo : xSubModulo);
                var ySubExact = realYUnit * (ySubModulo < 0 ? yVirtualProjectionSize + ySubModulo : ySubModulo);
                //round the value for array indexing,
                var xSubIndex = (int)Math.floor(xSubExact);
                var ySubIndex = (int)Math.floor(ySubExact);

                try {
                    var mapCoordinate = this.data.get(xSubIndex).get(ySubIndex);
                    var color = getColor(mapCoordinate.altitude + seaLevelRise);
                    g.setColor(color);
                } catch (Exception e){
                    System.out.println(x + " " + y);
                }
                // The data is ordered by longitude, latitude
                //  We are projecting an image that starts at left bottom from top left, that is why we invert y
                var yCoordinate = yProjectionSize - 1 - y;
                //centering out on 0;
//                var xCoordinate = (x + xProjectionSize / 2) % xProjectionSize;
                var xCoordinate = x;
                g.drawLine(xCoordinate,yCoordinate,xCoordinate,yCoordinate);
            }
        }
    }

    private void setRenderingUnits() {
        this.xDataSize = data.size();
        this.yDataSize = data.get(0).size();

        this.realXUnit = xDataSize/(double)(xProjectionSize * zoom);
        this.realYUnit = yDataSize/(double)(yProjectionSize * zoom);
    }

    private int waterLevel = 0;
    private int greenLevel = 300;
    private int yellowLevel = 1900;
    private int darkYellowLevel = 3500;
    private int whiteLevel = 8500;

    private Color getColor(double altitude) {
        if (altitude < waterLevel){
            var red = (int)ScaleValue(altitude, 0, 100,waterLevel);
            var green = (int)ScaleValue(altitude, 0, 175, waterLevel);
            var blue = (int)ScaleValue(altitude, 0, 255, waterLevel);//(int)((-1*altitude) / 11000 * 255);
            return new Color(red, green, blue);
        } else if (altitude < greenLevel) {
            var red = (int)ScaleValue(altitude, 78, 224, waterLevel, greenLevel);
            var green = (int)ScaleValue(altitude, 117, 218, waterLevel, greenLevel);
            var blue = (int)ScaleValue(altitude, 78, 172, waterLevel, greenLevel);
            return new Color(red, green, blue);
        } else if (altitude < yellowLevel) {
            var red = (int)ScaleValue(altitude, 224, 124, greenLevel, yellowLevel);
            var green = (int)ScaleValue(altitude, 218, 106, greenLevel, yellowLevel);
            var blue = (int)ScaleValue(altitude, 172, 76, greenLevel, yellowLevel);
            return new Color(red, green, blue);
        } else if (altitude < darkYellowLevel) {
            var red = (int)ScaleValue(altitude, 124, 190, yellowLevel, darkYellowLevel);
            var green = (int)ScaleValue(altitude, 106, 190, yellowLevel, darkYellowLevel);
            var blue = (int)ScaleValue(altitude, 76, 190, yellowLevel, darkYellowLevel);
            return new Color(red, green, blue);
        }else if (altitude < whiteLevel) {
            var red = (int)ScaleValue(altitude, 190, 252, darkYellowLevel, whiteLevel);
            var green = (int)ScaleValue(altitude, 190, 252, darkYellowLevel, whiteLevel);
            var blue = (int)ScaleValue(altitude, 190, 252, darkYellowLevel, whiteLevel);
            return new Color(red, green, blue);
        }

        else return Color.BLACK;
    }

    public double ScaleValue(double value, int min, int max, int maxScale)
    {
        return ScaleValue(value, min, max, altitudeMin, maxScale);
    }

    public double ScaleValue(double value, int min, int max, int minScale, int maxScale)
    {
        return min + (value - minScale) / (double)(maxScale - minScale) * (double)(max - min);
    }


    private double getWholeNumberLeftOver(double value){
        var d = String.valueOf(value);
        var part = d.split("\\.");

        var decimalPart = Double.parseDouble("0." + part[1]);

        return decimalPart;
    }

    private int scalePanUnits(double value){
        return (int)(value / zoom);
    }

    class MapInputAdapter extends MouseAdapter implements KeyListener {
        public MapInputAdapter() {
            super();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            var button = e.getButton();
            if (button == MouseEvent.BUTTON1){
                this.zoomIn();
            } else if (button == MouseEvent.BUTTON3){
                this.zoomOut();
            }
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            xLastMouseDragStart = 0;
            yLastMouseDragStart = 0;
            mouseDragInProgress = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            // SOURCE https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
            requestFocusInWindow();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            super.mouseWheelMoved(e);
            seaLevel(10 * e.getWheelRotation() * e.getScrollAmount());
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);

            int xMouseDragEnd = e.getX();
            int yMouseDragEnd = e.getY();

            if(mouseDragInProgress){
                xPan += scalePanUnits(xLastMouseDragStart - xMouseDragEnd);
                yPan += scalePanUnits(yLastMouseDragStart - yMouseDragEnd);
                repaint();
            }

            xLastMouseDragStart = xMouseDragEnd;
            yLastMouseDragStart = yMouseDragEnd;
            mouseDragInProgress = true;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            var charPressed = e.getKeyChar();

            if( charPressed == 'i'){
                zoomIn();
            } else if (charPressed == 'o'){
                zoomOut();
            } else if (charPressed == KeyEvent.VK_ESCAPE) {
                resetView();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

        private void zoomIn(){
            zoom *= 2;

            var xCurrentCompensation = (int)((xProjectionSize - (xProjectionSize/zoom)) / 2) - xZoomPanCompensation;
            var yCurrentCompensation = -(int)((yProjectionSize - (yProjectionSize/zoom)) / 2) - yZoomPanCompensation;
            xZoomPanCompensation += xCurrentCompensation;
            yZoomPanCompensation += yCurrentCompensation;
            xPan += xCurrentCompensation;
            yPan += yCurrentCompensation;

            setRenderingUnits();
            repaint();
        }

        private void zoomOut(){
            zoom /= 2;

            var xCurrentCompensation = -xZoomPanCompensation +(int)((xProjectionSize - (xProjectionSize/zoom)) / 2);
            var yCurrentCompensation = - (yZoomPanCompensation  + (int)((yProjectionSize - (yProjectionSize/zoom)) / 2));
            xZoomPanCompensation += xCurrentCompensation;
            yZoomPanCompensation += yCurrentCompensation;
            xPan += xCurrentCompensation;
            yPan += yCurrentCompensation;

            setRenderingUnits();
            repaint();
        }

        private void resetView(){
            zoom = 1;
            xPan = xDefaultPan;
            yPan = 0;
            xZoomPanCompensation = 0;
            yZoomPanCompensation = 0;

            setRenderingUnits();
            repaint();
        }
    }
}
