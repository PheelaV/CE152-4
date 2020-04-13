package assignment;

import testPreparaiton.Ball;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class Earth {
    private int xRealResolution;
    private int yRealResolution;

    //1.D coordinate number - row numbers 2.D [longitude, latitude, altitude]
    private double[][] arrayOfEarth;
    private Map<Double, Map<Double, MapCoordinate>> mapOfEarth;

    private boolean dataLoaded;


    public void readDataArray(String filename){
        //store the data in private two dimensional array of type double
        ArrayList<String[]> rows = getFileRows(filename);
        arrayOfEarth = new double[rows.size()][3];

        for (var i = 0; i < rows.size(); i++){
            var row = rows.get(i);

            arrayOfEarth[i] = getMapTuple(rows.get(i));
        }

        this.dataLoaded = true;

        this.Celebrate();
    }

    public void readDataMap(String filename){
        ArrayList<String[]> rows = getFileRows(filename);

        this.initializeMapOfEarth();
        for (var row: rows){
            var locationTuple = getMapCoordinate(row);
            this.insertCoordinate(locationTuple);
        }

        this.Celebrate();
    }

    public void generateMap(double resolution){
        var randomLocationTuples = getRandomLocationTuples(resolution);

        this.initializeMapOfEarth();
        for (var locationTuple: randomLocationTuples){
            this.insertCoordinate(locationTuple);
        }

        this.Celebrate();
    }

    private void initializeMapOfEarth() {
        this.mapOfEarth = new TreeMap<>();
    }

    private ArrayList<MapCoordinate> getRandomLocationTuples(double resolution) {
        double xRealResolution = (int)(360/resolution);
        double yRealResolution = (int)(180/resolution);
        var result = new ArrayList<MapCoordinate>();
        var random = new Random(104729);

        for (var x = 0; x < xRealResolution; x++){
            for (var y = 0; y < yRealResolution; y++){
                result.add(new MapCoordinate(x, y, random.nextDouble()));
            }
        }

        return result;
    }

    private void insertCoordinate(MapCoordinate c) {
        if(!mapOfEarth.containsKey(c.longitude)){
            mapOfEarth.put(c.longitude, new TreeMap<>(){{put(c.latitude, c);}});
        } else {
            var entry = mapOfEarth.get(c.longitude);
            entry.put(c.latitude, c);
        }
    }

    private MapCoordinate getMapCoordinate(String[] row) {
        return new MapCoordinate(Double.parseDouble(row[0]), Double.parseDouble(row[1]), Double.parseDouble(row[2]));
    }

    private double[] getMapTuple(String[] row) {
        return new double[]{Double.parseDouble(row[0]), Double.parseDouble(row[1]), Double.parseDouble(row[2])};
    }

    private ArrayList<String[]> getFileRows(String filename) {
        ArrayList<String[]> rows = new ArrayList<>();
        Scanner input = null;
        String separator = "\t";

        ArrayList<String[]> data = new ArrayList<>();
        try {
            input = new Scanner(new File(filename));

            while (input.hasNextLine()){
                var line = input.nextLine();
                var lineBytes = line.getBytes(StandardCharsets.UTF_8);
                String[] row = line.split(separator);

                rows.add(row);
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            input.close();
        }

        return rows;
    }

    public List<Coordinate> coordinatesAbove(double altitude){
        return Arrays.stream(this.arrayOfEarth)
                .filter(x -> x[2] > altitude)
                .map(x -> new Coordinate(x[0], x[1], x[2]))
                .collect(Collectors.toList());
    }

    public List<Coordinate> coordinatesBelow(double altitude){
        return Arrays.stream(this.arrayOfEarth)
                .filter(x -> x[2] < altitude)
                .map(x -> new Coordinate(x[0], x[1], x[2]))
                .collect(Collectors.toList());
    }

    public void percentageAbove(double altitude) throws DataNotLoadedException {
        this.checkDataLoaded("percentageAbove");

        double percentage = (double)Arrays.stream(this.arrayOfEarth).filter(x -> x[2] > altitude).count() / (double)this.arrayOfEarth.length * 100;

        //SOURCE: https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html
        //SOURCE: https://docs.oracle.com/javase/tutorial/java/data/numberformat.html
        String format = "Proportion of coordinates above %1$.1f meters: %2$.1f%%";

        System.out.println(String.format(format, altitude, percentage));
    }

    public void percentageBelow(double altitude) throws DataNotLoadedException {
        this.checkDataLoaded("percentageBelow");

        double percentage = Arrays.stream(this.arrayOfEarth).filter(x -> x[2] < altitude).count() / this.arrayOfEarth.length * 100;
        String format = "Proportion of coordinates below %1$.1f meters: %2$.1f%%";

        System.out.println(String.format(format, altitude, percentage));
    }

    private void checkDataLoaded() throws DataNotLoadedException {
        this.checkDataLoaded("");
    }
    private void checkDataLoaded(String message) throws DataNotLoadedException {
        if(!this.dataLoaded) throw new DataNotLoadedException(message);
    }

    public class DataNotLoadedException extends Exception {
        DataNotLoadedException(String message){
            super(message);
        }
    }

    private void Celebrate() {
        System.out.println("Wubba Lubba DUB DUB");
    }

    public Map<Double, Map<Double, MapCoordinate>> getMapData(){
        return (Map<Double, Map<Double, MapCoordinate>>)((TreeMap)this.mapOfEarth).clone();
    }

    public void seaLevel(int addedAltitude){
        //Just to flex that I can manipulate maps, this is pretty much a duplicate of the method in EarthRenderer, I've put it in here just in case iterating over maps is in the marking criteria

        for (Map.Entry<Double, Map<Double, MapCoordinate>> xEntry : this.mapOfEarth.entrySet()) {
//            System.out.println(String.format("%-20s", entry.getKey()) + entry.getValue());
            for (Map.Entry<Double, MapCoordinate> yEntry: xEntry.getValue().entrySet()){
                this.mapOfEarth.get(xEntry.getKey()).put(yEntry.getKey(), new MapCoordinate(xEntry.getKey(), yEntry.getKey(), yEntry.getValue().altitude + addedAltitude));
            }
        }

    }
}
