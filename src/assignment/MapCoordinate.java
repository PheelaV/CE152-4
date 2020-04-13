package assignment;

public class MapCoordinate extends Coordinate implements  Comparable<MapCoordinate> {

    public MapCoordinate(double longitude, double latitude, double altitude){
        super(latitude, longitude, altitude);
    }

    public double distanceTo(MapCoordinate mapCoordinate){
        return  Math.sqrt(
                    Math.exp(this.latitude - mapCoordinate.latitude)
                  + Math.exp(this.longitude - mapCoordinate.longitude)

//                Altitude must not be taken into account and you can assume the Earth is
//                a smooth sphere for the purposes of this calculation.
//                  + Math.exp(this.altitude - mapCoordinate.altitude)
                );
    }

    @Override
    public int compareTo(MapCoordinate coordinate) {
        //altitude, then latitude, then longitude
        return this.latitude != coordinate.latitude
                    ? (int)(this.latitude - coordinate.latitude)
                : this.longitude != coordinate.longitude
                    ? (int)(this.longitude - coordinate.longitude)
                : this.altitude != coordinate.altitude
                    ? (int)(this.altitude - coordinate.altitude) :  hackToThrowException();
    }
    public int hackToThrowException(){
        throw new  IllegalArgumentException("The comparison was invalid");
    }

    @Override
    public boolean equals(Object obj) {
        var coordinate = (Coordinate)obj;
        return this.latitude == coordinate.latitude
            && this.longitude == coordinate.longitude
            && this.altitude == coordinate.altitude;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
