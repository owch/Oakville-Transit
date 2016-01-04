package bowenowen.oakvilletransit.Model;

/**
 * Created by owenchen on 15-04-28.
 */
public class Trip {
    String route_number;
    String service_id;
    int trip_number;
    String block_id;

    public Trip(int trip_number, String route_number, String block_id, String service_id)
    {
        this.route_number = route_number;
        this.service_id = service_id;
        this.trip_number = trip_number;
        this.block_id = block_id;
    }

    public Trip(int trip_number, String route_number, String service_id)
    {
        this.route_number = route_number;
        this.service_id = service_id;
        this.trip_number = trip_number;
    }

    public int getTripNumber()
    {
        return trip_number;
    }

    public String getRouteNumber()
    {
        return route_number;
    }

    public String getBlockId()
    {
        return block_id;
    }

    public String getServiceId()
    {
        return service_id;
    }

}
