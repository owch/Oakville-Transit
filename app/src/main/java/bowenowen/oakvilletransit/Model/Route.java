package bowenowen.oakvilletransit.Model;

/**
 * Created by owenchen on 15-02-07.
 */
public class Route {
    private int _route_id;
    private String _route_long_name;
    private String _route_type;
    private String _route_number;
    private int paramList;

    public Route (int id, String routeName, String routeType, String routeNumber)
    {
        _route_id = id;
        _route_long_name = routeName;
        _route_type = routeType;
        _route_number = routeNumber;
        paramList = 3;
    }

    public Route (String routeName, String routeType, String routeNumber)
    {
        _route_long_name = routeName;
        _route_type = routeType;
        _route_number = routeNumber;
        paramList = 2;
    }

    public int getId()
    {
        return _route_id;
    }

    public String getName()
    {
        return _route_long_name;
    }

    public String getType()
    {
        return _route_type;
    }

    public String getNumber() {return  _route_number;}
}
