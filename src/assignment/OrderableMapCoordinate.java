package assignment;

public class OrderableMapCoordinate extends MapCoordinate {
    private int order;

    public OrderableMapCoordinate(double longitude, double latitude, double altitude, int order) {
        super(longitude, latitude, altitude);

        this.order = order;
    }

    public OrderableMapCoordinate(MapCoordinate mapCoordinate, int order){
        super(mapCoordinate.longitude, mapCoordinate.latitude, mapCoordinate.altitude);

        this.order = order;
    }

    public int getOrder(){
        return this.order;
    }
}
