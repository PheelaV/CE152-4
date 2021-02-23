package assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;

public class EarthRenderer extends JComponent {

    public ActionEvent coordinateSelected;
    public ActionEvent coordinateDeselected;

    private Earth earth;

    private final int xProjectionSize = 720;
    private final int yProjectionSize = 360;


    //WARNING: Chokes on weak hardware
//    private final int xProjectionSize = 1920;
//    private final int yProjectionSize = 1080;

    private final int waterLevel = 0;
    private final int greenLevel = 300;
    private final int yellowLevel = 1900;
    private final int darkYellowLevel = 3500;
    private final int whiteLevel = 8500;

    public ArrayList<ArrayList<MapCoordinate>> data;
    private int xDataSize;
    private int yDataSize;

    private int altitudeMin;
    private int altitudeMax;

    //beginfun
    private float zoom = 1;
    private int xZoomPanCompensation = 0;
    private int yZoomPanCompensation = 0;

    private float xVirtualProjectionSize;
    private float yVirtualProjectionSize;
    private float xVirtualPan;
    private float yVirtualPan;

    private double realXUnit;
    private double realYUnit;
    private int seaLevelRise = 0;

    private boolean mouseDragInProgress = false;
    private int xLastMouseDragStart = 0;
    private int yLastMouseDragStart = 0;
    private int xPan = 0;
    private int yPan = 0;
    private int xDefaultPan;
    //endfun

    private boolean clickIndicatorActive = false;
    private int xLastClicked = 0;
    private int yLastClicked = 0;
    private MapCoordinate lastCoordinateSelected;
    private double selectedVisibleAltitude;

    public EarthRenderer(Earth earth){
        this.earth = earth;
        this.init();
//        this.processeve
    }

    @Override
    public int getHeight() {
        return this.yProjectionSize;
    }

    @Override
    public int getWidth(){
        return this.xProjectionSize;
    }

    //subSample the data and calculate their RGB values
    //TODO: better solution would be to regenerate the data when zooming and rising sea level, but keep a cached version for panning
    //TODO: or cash the RGB values for the whole dataset and regenerate only when rising sea level, keep samplings as it is
    @Override
    public void paintComponent(Graphics g) {
        this.setProjectionVariables();

        for (int x = 0; x < xProjectionSize; x++){
            for (int y = 0; y < yProjectionSize; y++){
                // get the subSample
                MapCoordinate mapCoordinate = getProjectedMapCoordinate(x, y);
                //pick a scaled color with respect to altitude
                Color color = getColor(mapCoordinate.altitude + seaLevelRise);
                g.setColor(color);

                // Invert Y - projecting top left to bottom right data structure into a bottom left, top right plane
                int yInverted = yProjectionSize - 1 - y;
                g.drawLine(x,yInverted,x,yInverted);
            }
        }

        if(clickIndicatorActive){
            g.setColor(Color.green);
            g.drawLine(xLastClicked, yLastClicked, xLastClicked + 10, yLastClicked);
            g.drawLine(xLastClicked, yLastClicked, xLastClicked, yLastClicked + 10);
        }
    }

    private void setProjectionVariables(){
        this.xVirtualProjectionSize = this.zoom * this.xProjectionSize;
        this.yVirtualProjectionSize = this.zoom * this.yProjectionSize;
        this.xVirtualPan = this.xPan * zoom;
        this.yVirtualPan = this.yPan * zoom;
    }

    private MapCoordinate getProjectedMapCoordinate(int x, int y){
        //account for plane panning, limit value to the projectionConstraints
        float xSubModulo =  ((x + this.xVirtualPan) % this.xVirtualProjectionSize);
        float ySubModulo =  ((y - this.yVirtualPan) % this.yVirtualProjectionSize);

        //if it is negative, invert it with respect the projection size
        double xSubExact = this.realXUnit * (xSubModulo < 0 ? this.xVirtualProjectionSize + xSubModulo : xSubModulo);
        double ySubExact = this.realYUnit * (ySubModulo < 0 ? this.yVirtualProjectionSize + ySubModulo : ySubModulo);
        //round the value for array indexing,

        int xSubIndex = (int)Math.floor(xSubExact);
        int ySubIndex = (int)Math.floor(ySubExact);

        // get the subSample
        return this.data.get(xSubIndex).get(ySubIndex);
    }

    private Color getColor(double altitude) {
        if (altitude < waterLevel){
            int red = (int)ScaleValue(altitude, 0, 100,waterLevel);
            int green = (int)ScaleValue(altitude, 0, 175, waterLevel);
            int blue = (int)ScaleValue(altitude, 0, 255, waterLevel);//(int)((-1*altitude) / 11000 * 255);
            return new Color(red, green, blue);
        } else if (altitude < greenLevel) {
            int red = (int)ScaleValue(altitude, 78, 224, waterLevel, greenLevel);
            int green = (int)ScaleValue(altitude, 117, 218, waterLevel, greenLevel);
            int blue = (int)ScaleValue(altitude, 78, 172, waterLevel, greenLevel);
            return new Color(red, green, blue);
        } else if (altitude < yellowLevel) {
            int red = (int)ScaleValue(altitude, 224, 124, greenLevel, yellowLevel);
            int green = (int)ScaleValue(altitude, 218, 106, greenLevel, yellowLevel);
            int blue = (int)ScaleValue(altitude, 172, 76, greenLevel, yellowLevel);
            return new Color(red, green, blue);
        } else if (altitude < darkYellowLevel) {
            int red = (int)ScaleValue(altitude, 124, 190, yellowLevel, darkYellowLevel);
            int green = (int)ScaleValue(altitude, 106, 190, yellowLevel, darkYellowLevel);
            int blue = (int)ScaleValue(altitude, 76, 190, yellowLevel, darkYellowLevel);
            return new Color(red, green, blue);
        }else if (altitude < whiteLevel) {
            int red = (int)ScaleValue(altitude, 190, 252, darkYellowLevel, whiteLevel);
            int green = (int)ScaleValue(altitude, 190, 252, darkYellowLevel, whiteLevel);
            int blue = (int)ScaleValue(altitude, 190, 252, darkYellowLevel, whiteLevel);
            return new Color(red, green, blue);
        }
        else return Color.PINK;
    }

    public double ScaleValue(double value, int min, int max, int maxScale)
    {
        return ScaleValue(value, min, max, altitudeMin, maxScale);
    }

    public double ScaleValue(double value, int min, int max, int minScale, int maxScale)
    {
        return min + (value - minScale) / (double)(maxScale - minScale) * (double)(max - min);
    }

    private void init(){
        this.refreshData();

        MapInputAdapter adaptor = new MapInputAdapter();
        this.addMouseListener(adaptor);
        this.addMouseWheelListener(adaptor);
        this.addMouseMotionListener(adaptor);
        this.addKeyListener(adaptor);

        //centering out the X axis on 0;
        this.xPan = xDefaultPan = this.xProjectionSize / 2;
    }

    public void seaLevel(int addedAltitude){
        //this is utterly inefficient, but it is part of the assignment spec,
        // better option is to affect just the values displayed
        // that is why the code is currently deactivated
//        this.earth.seaLevel(addedAltitude);
//        this.refreshData();
        this.seaLevelRise += addedAltitude;
        this.altitudeMin += addedAltitude;
        this.altitudeMax += addedAltitude;
    }

    private void refreshData() {
        this.data = new ArrayList<>();

        for (Map.Entry<Double, Map<Double, MapCoordinate>> xEntry: this.earth.getMapData().entrySet()){
            ArrayList<MapCoordinate> entryArray = new ArrayList<>();

            for (MapCoordinate value: xEntry.getValue().values()){
                if(this.altitudeMax < value.altitude){
                    this.altitudeMax = (int)Math.ceil(value.altitude);
                }

                if(this.altitudeMin > value.altitude){
                    this.altitudeMin = (int)Math.floor(value.altitude);
                }

                entryArray.add(value);
            }

            this.data.add(entryArray);
        }

        this.checkDataIntegrity();
        this.setRenderingUnits();
    }

    private void checkDataIntegrity() {
        int yAxisSize = data.get(0).size();

        for (ArrayList<MapCoordinate> entry: this.data){
            if(entry.size() != yAxisSize){
                throw new IllegalArgumentException("EarthRenderer needs rectangularly shaped data");
            }
        }
    }

    private void setRenderingUnits() {
        this.xDataSize = data.size();
        this.yDataSize = data.get(0).size();
        this.realXUnit = xDataSize/(double)(xProjectionSize * zoom);
        this.realYUnit = yDataSize/(double)(yProjectionSize * zoom);
    }

    public int getLastClickedX() {
        return this.xLastClicked;
    }

    public int getLastClickedY() {
        return this.yLastClicked;
    }

    public MapCoordinate getLastSelectedCoordinate(){
        return this.lastCoordinateSelected;
    }

    public double getSelectedVisibleAltitude() {
        return selectedVisibleAltitude;
    }

    class MapInputAdapter extends MouseAdapter implements KeyListener {
        public MapInputAdapter() {
            super();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

//            var button = e.getButton();
//            if (button == MouseEvent.BUTTON1){
//                this.zoomIn();
//            } else if (button == MouseEvent.BUTTON3){
//                this.zoomOut();
//            }
//            clickIndicatorActive

            if (e.getButton() != MouseEvent.BUTTON1) return;

            xLastClicked = e.getX();
            yLastClicked = e.getY();
            setProjectionVariables();
            int yInverted = yProjectionSize - 1 - yLastClicked;
            MapCoordinate coordinate = getProjectedMapCoordinate(xLastClicked,  yInverted);
            lastCoordinateSelected = coordinate;
            selectedVisibleAltitude = coordinate.altitude + seaLevelRise;
            onCoordinateSelected();
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
            seaLevel(e.getWheelRotation() * e.getScrollAmount());
            onCoordinateDeselected();
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);

            int xMouseDragEnd = e.getX();
            int yMouseDragEnd = e.getY();

            if(mouseDragInProgress){
                xPan += (xLastMouseDragStart - xMouseDragEnd) / zoom;
                yPan += (yLastMouseDragStart - yMouseDragEnd) / zoom;
                repaint();
            }

            xLastMouseDragStart = xMouseDragEnd;
            yLastMouseDragStart = yMouseDragEnd;
            mouseDragInProgress = true;
            onCoordinateDeselected();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            char charPressed = e.getKeyChar();

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

        private void onCoordinateSelected(){
            clickIndicatorActive = true;
//            coordinateSelected.notifyAll();
        }

        private void onCoordinateDeselected(){
            clickIndicatorActive = false;
//            coordinateDeselected.notifyAll();
        }

        private void zoomIn(){
            zoom *= 2;

            int xCurrentCompensation = (int)((xProjectionSize - (xProjectionSize/zoom)) / 2) - xZoomPanCompensation;
            int yCurrentCompensation = -(int)((yProjectionSize - (yProjectionSize/zoom)) / 2) - yZoomPanCompensation;
            xZoomPanCompensation += xCurrentCompensation;
            yZoomPanCompensation += yCurrentCompensation;
            xPan += xCurrentCompensation;
            yPan += yCurrentCompensation;

            setRenderingUnits();
            clickIndicatorActive = false;
            repaint();
        }

        private void zoomOut(){
            zoom /= 2;

            int xCurrentCompensation = -xZoomPanCompensation +(int)((xProjectionSize - (xProjectionSize/zoom)) / 2);
            int yCurrentCompensation = - (yZoomPanCompensation  + (int)((yProjectionSize - (yProjectionSize/zoom)) / 2));
            xZoomPanCompensation += xCurrentCompensation;
            yZoomPanCompensation += yCurrentCompensation;
            xPan += xCurrentCompensation;
            yPan += yCurrentCompensation;

            setRenderingUnits();
            clickIndicatorActive = false;
            repaint();
        }

        private void resetView(){
            zoom = 1;
            xPan = xDefaultPan;
            yPan = 0;
            xZoomPanCompensation = 0;
            yZoomPanCompensation = 0;
            clickIndicatorActive = false;
            altitudeMin -= seaLevelRise;
            altitudeMax -= seaLevelRise;
            seaLevelRise = 0;
            setRenderingUnits();
            repaint();
        }
    }
}
