package assignment;

public class MapCoordinate extends Coordinate implements  Comparable<MapCoordinate> {

    public MapCoordinate(double longitude, double latitude, double altitude){
        super(latitude, longitude, altitude);
    }

    public double distanceTo(MapCoordinate mapCoordinate){
        //The mighty spec says:
////                Altitude must not be taken into account and you can assume the Earth is
////                a smooth sphere for the purposes of this calculation.
////                  + Math.exp(this.altitude - mapCoordinate.altitude)

        //SOURCE: https://www.movable-type.co.uk/scripts/latlong.html
        int earthRadius = 6371000;
        double latRad = mapCoordinate.latitude * Math.PI / 180;
        double latRad2 = this.latitude * Math.PI / 180;
        double latDifRad = (this.latitude - mapCoordinate.latitude)  * Math.PI / 180;
        double lonDifRad = (this.longitude - mapCoordinate.longitude)  * Math.PI / 180;

        double sqHalfChordLen = Math.sin(latDifRad/2) * Math.sin(latDifRad/2) +
                    Math.cos(latRad) * Math.cos(latRad2) *
                    Math.sin(lonDifRad/2) * Math.sin(lonDifRad/2);
        double angDistRad = 2 * Math.atan2(Math.sqrt(sqHalfChordLen), Math.sqrt(1-sqHalfChordLen));

        return earthRadius * angDistRad;
    }

    @Override
    public int compareTo(MapCoordinate coordinate) {
        //altitude, then latitude, then longitude
        return this.latitude != coordinate.latitude
                    ? (int)(this.latitude - coordinate.latitude)
                : this.longitude != coordinate.longitude
                    ? (int)(this.longitude - coordinate.longitude)
                : this.altitude != coordinate.altitude
                    ? (int)(this.altitude - coordinate.altitude) :  0;
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
