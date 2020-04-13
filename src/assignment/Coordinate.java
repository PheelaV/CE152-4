package assignment;

public class Coordinate {
    public final double latitude;
    public final double longitude;
    public final double altitude;

    public Coordinate(double latitude, double longitude, double altitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                longitude +
                "\t" + latitude +
                "\t" + altitude +
                '}';
    }
}
