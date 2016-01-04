package bowenowen.oakvilletransit.Model;

/**
 * Created by owenchen on 15-02-07.
 */
public class Stop {
    private int _stop_id;
    private String _stop_name;
    private double _stop_lat;
    private double _stop_long;
    private int paramList;

    public Stop (int id, String stopName, double stopLat, double stopLong)
    {
        _stop_id = id;
        _stop_name = stopName;
        _stop_lat = stopLat;
        _stop_long = stopLong;
        paramList = 4;
    }

    public Stop (String stopName, double stopLat, double stopLong)
    {
        _stop_name = stopName;
        _stop_lat = stopLat;
        _stop_long = stopLong;
        paramList = 4;
    }

    public Stop (int id, String stopName)
    {
        _stop_name = stopName;
        _stop_id = id;
        paramList = 4;
    }

    public Stop ( String stopName)
    {
        _stop_name = stopName;
        paramList = 4;
    }

    public int getId()
   {
        return _stop_id;
   }

    public String getName()
    {
        return _stop_name;
    }

    public double getLatitude()
    {
        return _stop_lat;
    }

    public double getLongitude()
    {
        return _stop_lat;
    }
}
