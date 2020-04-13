package assignment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EarthRenderer extends JComponent {
    private Earth earth;

    private final int xProjection =1400;
    private final int yProjection = 898;

    private int altitudeMin = -11000;
    private int altitudeMax = 8500;
    //beginfun
    private int zoom = 1;
    //endfun
    private double realXUnit;
    private double realYUnit;

    public ArrayList<ArrayList<MapCoordinate>> data;
    private int xDataSize;
    private int yDataSize;


    public EarthRenderer(Earth earth){
        this.earth = earth;

        this.refreshData();
    }

    public void seaLevel(int addedAltitude){
//        for (int x = 0; x < data.size(); x++){
//            for (int y = 0; y < data.get(0).size(); y++){
//                var coordinate = data.get(x).get(y);
//                data.get(x).set(y, new MapCoordinate(coordinate.longitude, coordinate.latitude, coordinate.altitude));
//            }
//        }
        this.earth.seaLevel(addedAltitude);
        this.refreshData();
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
                throw new IllegalArgumentException("EarthRenderer needs rectangular data");
            }
        }
    }

    @Override
    public int getHeight() {
        return this.yProjection;
    }

    @Override
    public int getWidth(){
        return this.xProjection;
    }

    @Override
    public void paintComponent(Graphics g) {

        for (var x = 0; x < xProjection; x++){
            //yProjectionUni
            for (var y = 0; y < yProjection; y++){
                var xSubExact = realXUnit * x;
                var xSubIndex = (int)Math.floor(xSubExact);

                var ySubExact = realXUnit * y ;
                var ySubIndex = (int)Math.floor(ySubExact);


                try {
                    var mapCoordintate = this.data.get(xSubIndex).get(ySubIndex);
                    var color = getColor(mapCoordintate.altitude);
                    g.setColor(color);
                } catch (Exception e){
                    var a = 1;
                    var b = e;
                }


                // The data is ordered by longitude, latitude
                //  We are projecting an image that starts at left bottom from top left, that is why we invert y
                var yCoordinate = yProjection - 1 - y;

                //centering out on 0;
                var xCoordinate = (x + xProjection / 2) % xProjection;
                g.drawLine(xCoordinate,yCoordinate,xCoordinate,yCoordinate);

            }
        }
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

    public double ScaleValue(double value, int min, int max)
    {
        return ScaleValue(value, min, max, altitudeMin, altitudeMax);
    }

    public double ScaleValue(double value, int min, int max, int minScale, int maxScale)
    {
        return min + (value - minScale) / (double)(maxScale - minScale) * (double)(max - min);
    }

    private void setRenderingUnits() {
        this.xDataSize = data.size();
        this.yDataSize = data.get(0).size();

        this.realXUnit = xDataSize/(double)(xProjection * zoom);
        this.realYUnit = yDataSize/(double)(yProjection * zoom);
    }

    private double getWholeNumberLeftOver(double value){
        var d = String.valueOf(value);
        var part = d.split("\\.");

        var decimalPart = Double.parseDouble("0." + part[1]);

        return decimalPart;
    }
}
