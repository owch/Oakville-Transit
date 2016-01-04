package bowenowen.oakvilletransit.Model;

/**
 * Created by owenchen on 15-04-28.
 */
public class StopTimes {
    int stop_times_id;
    String trip_id;
    String stop_id;
    String departure_time;
    String arrival_time;

    public StopTimes(int stop_times_id, String trip_id, String stop_id, String departure_time, String arrival_time)
    {
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.stop_times_id = stop_times_id;
        this.trip_id = trip_id;
        this.stop_id = stop_id;
    }

    public StopTimes(String trip_id, String stop_id, String departure_time, String arrival_time)
    {
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.trip_id = trip_id;
        this.stop_id = stop_id;
    }

    public int getStopTimeId()
    {
        return stop_times_id;
    }

    public String getTripId()
    {
        return trip_id;
    }

    public String getDepartureTime()
    {
        return departure_time;
    }

    public String getStopId()
    {
        return stop_id;
    }

    public String getArrivalTime()
    {
        return arrival_time;
    }
}
