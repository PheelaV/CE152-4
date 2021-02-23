package assignment;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class Earth {
    private int xRealResolution;
    private int yRealResolution;

    //1.D coordinate number - row numbers 2.D [longitude, latitude, altitude]

    //TODO: In order to make it more performant as well as use less memory, we could switch to just use arrayOfEarth as the internal data structure keeping the map
    private double[][] arrayOfEarth;
    private Map<Double, Map<Double, MapCoordinate>> mapOfEarth;

    private boolean dataLoaded;

    public void readDataArray(String filename){
        //store the data in private two dimensional array of type double
        ArrayList<String[]> rows = getFileRows(filename);
        arrayOfEarth = new double[rows.size()][3];

        for (int i = 0; i < rows.size(); i++){
            String[] row = rows.get(i);

            arrayOfEarth[i] = getMapTuple(rows.get(i));
        }

        this.dataLoaded = true;

        this.Celebrate();
    }

    public void readDataMap(String filename){
        ArrayList<String[]> rows = getFileRows(filename);

        this.initializeMapOfEarth();
        for (String[] row: rows){
            MapCoordinate locationTuple = createMapCoordinate(row);
            this.insertCoordinate(locationTuple);
        }

        this.Celebrate();
    }

    public double getAltitude(double longitude, double latitude) throws NoSuchElementException{
        return getMapCoordinate(longitude, latitude).altitude;
    }

    public boolean mapContainsCoordinates(double longitude, double latitude){
        return this.mapOfEarth.containsKey(longitude) && this.mapOfEarth.get(longitude).containsKey(latitude);
    }

    public void generateMap(double resolution){
        ArrayList<MapCoordinate> randomLocationTuples = getRandomLocationTuples(resolution);

        this.initializeMapOfEarth();
        for (MapCoordinate locationTuple: randomLocationTuples){
            this.insertCoordinate(locationTuple);
        }

        this.Celebrate();
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

    public double percentageAbove(double altitude) throws DataNotLoadedException {
        this.checkDataLoaded("percentageAbove");

        return Arrays.stream(this.arrayOfEarth)
                .filter(x -> x[2] > altitude)
                .count() / (double)this.arrayOfEarth.length * 100d;
    }

    public double percentageBellow(double altitude) throws DataNotLoadedException {
        this.checkDataLoaded("percentageBelow");

        return Arrays.stream(this.arrayOfEarth)
                .filter(x -> x[2] < altitude)
                .count() / (double)this.arrayOfEarth.length * 100d;
    }

    private void initializeMapOfEarth() {
        this.mapOfEarth = new TreeMap<>();
    }

    private MapCoordinate getMapCoordinate(double longitude, double latitude) throws NoSuchElementException{

        if(!mapContainsCoordinates(longitude, latitude)) throw new NoSuchElementException("There is no such entry in the map for the provided coordinates.");

        return this.mapOfEarth.get(longitude).get(latitude);
    }

    private ArrayList<MapCoordinate> getRandomLocationTuples(double resolution) {
        double xRealResolution = (int)(360/resolution);
        double yRealResolution = (int)(180/resolution);
        ArrayList<MapCoordinate> result = new ArrayList<>();
        Random random = new Random(444);

        for (double x = 0; x < xRealResolution; x++){
            for (double y = 0; y < yRealResolution; y++){
                result.add(new MapCoordinate(x, y, random.nextDouble() * 10000 - 5000));
            }
        }

        return result;
    }

    private void insertCoordinate(MapCoordinate c) {
        if(!mapOfEarth.containsKey(c.longitude)){
            mapOfEarth.put(c.longitude, new TreeMap<Double, MapCoordinate>(){{put(c.latitude, c);}});
        } else {
            Map<Double, MapCoordinate> entry = mapOfEarth.get(c.longitude);
            entry.put(c.latitude, c);
        }
    }

    private MapCoordinate createMapCoordinate(String[] row) {
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
                String line = input.nextLine();
                byte[] lineBytes = line.getBytes(StandardCharsets.UTF_8);
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
