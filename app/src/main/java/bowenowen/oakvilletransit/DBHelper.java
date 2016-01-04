package bowenowen.oakvilletransit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Pair;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bowenowen.oakvilletransit.Model.Route;
import bowenowen.oakvilletransit.Model.Stop;
import bowenowen.oakvilletransit.Model.StopTimes;
import bowenowen.oakvilletransit.Model.Trip;

/**
 * Created by owenchen on 15-04-18.
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String TABLE_STOPS = "Stops";
    private static final String TABLE_ROUTES = "Routes";
    private static final String TABLE_FAVOURITES = "Favourites";
    private static final String TABLE_TRIPS = "Trips";
    private static final String TABLE_STOP_TIMES = "StopTimes";
    private static final String TABLE_MAIN_STOP = "MainStops";
    private static final String TABLE_CURRENT_ROUTE = "CurrentRoute";

    private static final String STOP_ID = "stop_id";
    private static final String STOP_NAME = "stop_name";
    private static final String STOP_LAT = "stop_lat";
    private static final String STOP_LON = "stop_lon";
    private static final String STOP_NUMBER = "stop_number";

    private static final String ROUTE_ID = "route_id";
    private static final String ROUTE_LONG_NAME = "route_long_name";
    private static final String ROUTE_TYPE = "route_type";
    private static final String ROUTE_NUMBER = "route_number";

    private static final String TRIP_ID = "trip_id";
    private static final String TRIP_NUMBER = "trip_number";
    private static final String SERVICE_ID = "service_id";

    private static final String STOP_TIME_ID = "stop_time_id";
    private static final String ARRIVAL_TIME = "arrival_time";
    private static final String DEPARTURE_TIME = "departure_time";


    private static final String CREATE_TABLE_STOPS =  "CREATE TABLE IF NOT EXISTS " + TABLE_STOPS
            + "(" + STOP_ID + " INTEGER PRIMARY KEY," + STOP_NAME + " TEXT," + STOP_NUMBER + " INTEGER," + STOP_LAT + " FLOAT," + STOP_LON + " FLOAT" + ")";
    private static final String CREATE_TABLE_ROUTES = "CREATE TABLE IF NOT EXISTS " + TABLE_ROUTES
            + "(" + ROUTE_ID + " INTEGER PRIMARY KEY," + ROUTE_LONG_NAME + " TEXT," + ROUTE_TYPE + " TEXT," + ROUTE_NUMBER + " TEXT" + ")";
    private static final String CREATE_TABLE_FAVOURITES = "CREATE TABLE IF NOT EXISTS " + TABLE_FAVOURITES
            + "(" + STOP_ID + " INTEGER PRIMARY KEY," + STOP_NAME + " TEXT," + STOP_NUMBER + " INTEGER," + STOP_LAT + " FLOAT," + STOP_LON + " FLOAT" + ")";
    private static final String CREATE_TABLE_TRIPS = "CREATE TABLE IF NOT EXISTS " + TABLE_TRIPS
            + "(" + TRIP_ID + " INTEGER PRIMARY KEY," + TRIP_NUMBER + " TEXT," + SERVICE_ID + " TEXT," + ROUTE_NUMBER + " TEXT" + ")";
    private static final String CREATE_TABLE_MAIN_STOPS = "CREATE TABLE IF NOT EXISTS " + TABLE_MAIN_STOP
            + "(" + STOP_ID + " INTEGER PRIMARY KEY," + STOP_NUMBER + " TEXT," +  SERVICE_ID + " TEXT," + ARRIVAL_TIME + " TEXT," +  TRIP_NUMBER + " TEXT" + ")";
    private static final String CREATE_TABLE_STOP_TIMES = "CREATE TABLE IF NOT EXISTS " + TABLE_STOP_TIMES
            + "(" + STOP_TIME_ID + " INTEGER PRIMARY KEY," + TRIP_NUMBER + " INTEGER," + STOP_NUMBER + " TEXT," + ARRIVAL_TIME + " TEXT," + DEPARTURE_TIME + " TEXT" + ")";
    private static final String CREATE_TABLE_CURRENT_ROUTE = "CREATE TABLE IF NOT EXISTS " + TABLE_CURRENT_ROUTE
            + "(" + ROUTE_ID + " INTEGER PRIMARY KEY," + ROUTE_NUMBER + " TEXT" + ")";


    private static final String DATABASE_NAME = "transit.sqlite";
    private int DATABASE_VERSION;

        private static String DB_PATH = "/data/data/bowenowen.oakvilletransit/databases/";
    private SQLiteDatabase myDataBase;

    private final Context myContext;



    public DBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
        this.DATABASE_VERSION = version;
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STOPS);
        db.execSQL(CREATE_TABLE_ROUTES);
        db.execSQL(CREATE_TABLE_FAVOURITES);
        db.execSQL(CREATE_TABLE_STOP_TIMES);
        db.execSQL(CREATE_TABLE_TRIPS);
        db.execSQL(CREATE_TABLE_CURRENT_ROUTE);
        db.execSQL(CREATE_TABLE_MAIN_STOPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOP_TIMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT_ROUTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN_STOP);
        onCreate(db);
    }

    public void insertStop(Stop stop, String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STOP_NAME, stop.getName());
        values.put(STOP_LAT, stop.getLatitude());
        values.put(STOP_LON, stop.getLongitude());
        values.put(STOP_NUMBER, stop.getId());

        if(table.toLowerCase().equals("favourites"))
        {
            db.insert(TABLE_FAVOURITES, null, values);
        }
        else if(table.toLowerCase().equals("stops"))
        {
            db.insert(TABLE_STOPS, null, values);
        }
    }

    public void insertMainStop(String stopId, String arrivalTime, String tripNumber)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ARRIVAL_TIME,arrivalTime);
        values.put(STOP_NUMBER, stopId);
        values.put(TRIP_NUMBER, tripNumber);
        values.put(SERVICE_ID, getServiceId(tripNumber));


        db.insert(TABLE_MAIN_STOP, null, values);
    }

    public void insertRoute(Route route)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ROUTE_LONG_NAME, route.getName());
        values.put(ROUTE_TYPE, route.getType());
        values.put(ROUTE_NUMBER, route.getNumber());

        db.insert(TABLE_ROUTES, null, values);
    }

    public void insertCurrentRoute(String route_name)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ROUTE_NUMBER, route_name);


        db.insert(TABLE_CURRENT_ROUTE, null, values);
    }

    public void insertTrip(Trip trip)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRIP_NUMBER, trip.getTripNumber());
        values.put(ROUTE_NUMBER,trip.getRouteNumber());
        values.put(SERVICE_ID, trip.getServiceId());

        db.insert(TABLE_TRIPS, null, values);
    }

    public void insertStopTime(StopTimes stopTimes)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ARRIVAL_TIME,stopTimes.getArrivalTime());
        values.put(DEPARTURE_TIME,stopTimes.getDepartureTime());
        values.put(STOP_NUMBER,stopTimes.getStopId());
        values.put(TRIP_NUMBER, stopTimes.getTripId());

        db.insert(TABLE_STOP_TIMES, null, values);
    }

    public Stop getStop(String stop_number, String table)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "";

        if(table.toLowerCase().equals("favourites"))
        {
            selectQuery = "SELECT  * FROM " + TABLE_FAVOURITES + " WHERE "
                    + STOP_NUMBER + " = " + stop_number;
        }
        else if(table.toLowerCase().equals("stops"))
        {
            selectQuery = "SELECT  * FROM " + TABLE_STOPS + " WHERE "
                    + STOP_NUMBER + " = " + stop_number;
        }
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(STOP_NAME));
            Double lat = cursor.getDouble(cursor.getColumnIndex(STOP_LAT));
            Double lon = cursor.getDouble(cursor.getColumnIndex(STOP_LON));
            int number = cursor.getInt(cursor.getColumnIndex(STOP_NUMBER));

            Stop stop = new Stop(number, name, lat, lon);

            return stop;
        }
        else
        {
            return null;
        }
    }

    public String getRouteName(String route_number)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ROUTES + " WHERE "
            + ROUTE_NUMBER + " = " + route_number;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();


        String name = cursor.getString(cursor.getColumnIndex(ROUTE_LONG_NAME));

        return name;
    }

    public String getRouteWithTrip(String trip_number)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TRIPS + " WHERE "
                + TRIP_NUMBER + " = " + trip_number;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        String routeNumber = cursor.getString(cursor.getColumnIndex(ROUTE_NUMBER));

        return routeNumber;
    }

    public String getCurrentRoute()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CURRENT_ROUTE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount() == 0)
        {
            return null;
        }

        if (cursor != null)
            cursor.moveToFirst();

        return  cursor.getString(cursor.getColumnIndex(ROUTE_NUMBER));
    }

    public List<Stop> queryStop(String query)
    {
        List<Stop> stops = new ArrayList<Stop>();
        String selectQuery = "";
        selectQuery = "SELECT * FROM " + TABLE_STOPS + " WHERE " + STOP_NAME + " LIKE '%" + query + "%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int stop_number;
        String stop_name;
        Double stop_lat;
        Double stop_lon;

        if(cursor.moveToFirst())
        {
            do
            {
                stop_number = cursor.getInt(cursor.getColumnIndex(STOP_NUMBER));
                stop_name = cursor.getString(cursor.getColumnIndex(STOP_NAME));
                stop_lat = cursor.getDouble(cursor.getColumnIndex(STOP_LAT));
                stop_lon = cursor.getDouble(cursor.getColumnIndex(STOP_LON));

                stops.add(new Stop(stop_number, stop_name, stop_lat, stop_lon));
            }while (cursor.moveToNext());
        }
        return stops;
    }

    public List<StopTimes> getAllStopTime(String stopId)
    {
        List<StopTimes> stopTimes = new ArrayList<StopTimes>();
        String selectQuery = "SELECT * FROM " + TABLE_STOP_TIMES + " WHERE "
                + STOP_NUMBER + " = " + stopId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int trip_number;
        String arrival_time;
        String departure_time;

        if(cursor.moveToFirst())
        {
            do
            {
                arrival_time = cursor.getString(cursor.getColumnIndex(ARRIVAL_TIME));
                departure_time = cursor.getString(cursor.getColumnIndex(DEPARTURE_TIME));
                trip_number = cursor.getInt(cursor.getColumnIndex(TRIP_NUMBER));

                stopTimes.add(new StopTimes(Integer.toString(trip_number), stopId, departure_time, arrival_time));
            }while (cursor.moveToNext());
        }
        return stopTimes;
    }

    public List<Pair<Pair<String, String>, Pair<String, String>>> getStopTime(String stopNumber)
        {
        List<Pair<Pair<String, String>, Pair<String, String>>> stopTimes = new ArrayList<Pair<Pair<String, String>, Pair<String, String>>>();
        String selectQuery = "";

        String service_id = getDate();

        selectQuery = "SELECT * FROM " + TABLE_MAIN_STOP + " WHERE "
            + STOP_NUMBER + " = " + stopNumber + " AND " + SERVICE_ID + " = \"" + service_id + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String arrival_time;
        String trip_number;

        Time now = new Time();
        now.setToNow();

        Date rightNow = convertStringToDate(Integer.toString(now.hour) + ":" + Integer.toString(now.minute)+ ":" +  Integer.toString(now.second));
        Long curDifference;

        if(cursor.getCount() == 0)
        {
            return null;
        }
        if(cursor.moveToFirst())
        {
            do
            {
                arrival_time = cursor.getString(cursor.getColumnIndex(ARRIVAL_TIME));
                trip_number = cursor.getString(cursor.getColumnIndex(TRIP_NUMBER));
                service_id = cursor.getString(cursor.getColumnIndex(SERVICE_ID));

                    curDifference = minuteDifference(convertStringToDate(arrival_time), rightNow);

                    if(curDifference>=0) {
                        stopTimes.add(Pair.create(Pair.create(arrival_time, service_id), Pair.create(stopNumber, trip_number)));
                    }
            }while (cursor.moveToNext());
        }
        return stopTimes;
    }

    private String getServiceId(String trip_number)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TRIPS + " WHERE "
                + TRIP_NUMBER + " = " + trip_number;

        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor != null)
            cursor.moveToFirst();

        String serviceId = cursor.getString(cursor.getColumnIndex(SERVICE_ID));

        return serviceId;
    }

    public Trip getTrip(String trip_number)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TRIPS + " WHERE "
                + TRIP_NUMBER + " = " + trip_number;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        int id = cursor.getInt(cursor.getColumnIndex(TRIP_ID));
        String routeNumber = cursor.getString(cursor.getColumnIndex(ROUTE_NUMBER));
        String serviceId = cursor.getString(cursor.getColumnIndex(SERVICE_ID));

        Trip trip = new Trip(Integer.parseInt(trip_number), routeNumber, serviceId);

        return trip;
    }

    public Trip getTripWithRoute(String route_number)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TRIPS + " WHERE "
                + ROUTE_NUMBER + " = " + route_number;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        String trip_number = cursor.getString(cursor.getColumnIndex(TRIP_NUMBER));
        String serviceId = cursor.getString(cursor.getColumnIndex(SERVICE_ID));

        Trip trip = new Trip(Integer.parseInt(trip_number), route_number, serviceId);

        return trip;
    }

    public List<Trip> getTripList(String route_number)
    {
        List<Trip> tripList = new ArrayList<Trip>();
        String selectQuery = "";

        selectQuery = "SELECT * FROM " + TABLE_TRIPS + " WHERE "
            + ROUTE_NUMBER + " = " + route_number;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String trip_number;
        String service_id;

        if(cursor.moveToFirst())
        {
            do
            {
                trip_number = cursor.getString(cursor.getColumnIndex(TRIP_NUMBER));
                service_id = cursor.getString(cursor.getColumnIndex(SERVICE_ID));

                Trip trip = new Trip(Integer.parseInt(trip_number), route_number, service_id);
                tripList.add(trip);
            }while (cursor.moveToNext());
        }
        return tripList;
    }

    public List<Stop> getStopList(int tripNumber)
    {
        List<Stop> stopList = new ArrayList<Stop>();
        List<String> stopNumber = new ArrayList<String>();
        String selectQuery = "";

        selectQuery = "SELECT * FROM " + TABLE_STOP_TIMES + " WHERE "
                + TRIP_NUMBER + " = " + tripNumber;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if(cursor.moveToFirst())
        {
            do
            {
                stopNumber.add(cursor.getString(cursor.getColumnIndex(STOP_NUMBER)));
            }while (cursor.moveToNext());
        }
        //Get the stops
        for(String number : stopNumber)
        {
            stopList.add(getStop(number, "stops"));
        }

        return stopList;
    }

    public List<Stop> getAllStops(String table)
    {
        List<Stop> stops = new ArrayList<Stop>();
        String selectQuery = "";

        if(table.toLowerCase().equals("favourites"))
        {
            selectQuery = "SELECT * FROM " + TABLE_FAVOURITES;
        }
        else if(table.toLowerCase().equals("stops"))
        {
             selectQuery = "SELECT * FROM " + TABLE_STOPS;
        }


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int stop_number;
        String stop_name;
        Double stop_lat;
        Double stop_lon;

        if(cursor.moveToFirst())
        {
            do
            {
                stop_number = cursor.getInt(cursor.getColumnIndex(STOP_NUMBER));
                stop_name = cursor.getString(cursor.getColumnIndex(STOP_NAME));
                stop_lat = cursor.getDouble(cursor.getColumnIndex(STOP_LAT));
                stop_lon = cursor.getDouble(cursor.getColumnIndex(STOP_LON));

                stops.add(new Stop(stop_number, stop_name, stop_lat, stop_lon));
            }while (cursor.moveToNext());
        }
        return stops;
    }

    public  List<Route> getAllRoutes()
    {
        List<Route> routes = new ArrayList<Route>();
        String selectQuery = "SELECT  * FROM " + TABLE_ROUTES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int id;
        String name;
        String type;
        String number;

        if (cursor.moveToFirst())
        {
            do
            {
                id = cursor.getInt(cursor.getColumnIndex(ROUTE_ID));
                name = cursor.getString(cursor.getColumnIndex(ROUTE_LONG_NAME));
                type = cursor.getString(cursor.getColumnIndex(ROUTE_TYPE));
                number = cursor.getString(cursor.getColumnIndex(ROUTE_NUMBER));

                routes.add(new Route(id, name, type, number));
            }while (cursor.moveToNext());
        }
        return routes;
    }

    public List<String> getALLCurRoutes()
    {
        List<String> routes = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TABLE_CURRENT_ROUTE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String number;

        if (cursor.moveToFirst())
        {
            do
            {


                number = cursor.getString(cursor.getColumnIndex(ROUTE_NUMBER));

                routes.add(number);
            }while (cursor.moveToNext());
        }
        return routes;
    }

    public void deleteStop(Stop stop)
    {
        int id = stop.getId();

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_STOPS, STOP_ID + " =?", new String[] {String.valueOf(id)});
    }

    public void deleteFavourite(Stop stop)
    {
        int id = stop.getId();

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_FAVOURITES, STOP_NUMBER + "=" + id, null);
    }

    public void deleteRoute(Route route)
    {
        int id = route.getId();

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ROUTES, ROUTE_ID + " =?", new String[] {String.valueOf(id)});
    }

    public void deleteTrip(Trip trip)
    {
        int id = trip.getTripNumber();

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_TRIPS, TRIP_NUMBER + " =?", new String[] {String.valueOf(id)});
    }

    public void deleteStopTime(StopTimes stopTimes)
    {
        int id = stopTimes.getStopTimeId();

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_STOP_TIMES, STOP_TIME_ID + " =?", new String[] {String.valueOf(id)});
    }

    public void deleteCurrentRoute()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from CurrentRoute");
    }

    public void deleteTable(String table_name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + table_name);

    }

    public int getVersion()
    {
        return DATABASE_VERSION;
    }

    private Date convertStringToDate(String time)
    {
        Date date = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
        }

        return date;
    }

    private long minuteDifference(Date date, Date rightNow)
    {

        return getDateDiff(rightNow, date);
    }

    public static long getDateDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public String getDate()
    {
        Time now = new Time();
        now.setToNow();

            if(now.weekDay == 6)
            {
                return "01-Saturday";
            }
            else if(now.weekDay == 7)
            {
                return "01-Sunday";
            }
            else if(now.weekDay == 1 || now.weekDay == 2 || now.weekDay == 3 || now.weekDay == 4 || now.weekDay == 5)
            {
                return "01-Weekday";
            }
        return null;
    }

    public void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }



    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }
}
