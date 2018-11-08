package cordova.plugin.nmea.model;

public class NmeaVtgModel {
    private long timestamp;
    private String nmea;

    public NmeaVtgModel()
	{		
	}
	
	public NmeaVtgModel(long timestamp, String nmea)
	{
		super();
		this.timestamp = timestamp;
		this.nmea = nmea;
    }
    
    public long getTimestamp() 
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp) 
	{
		this.timestamp = timestamp;
	}

	public String getNmea() 
	{
		return nmea;
	}

	public void setNmea(String nmea) 
	{
		this.nmea = nmea;
	}

	@Override
	public String toString() 
	{
		return "NmeaVtgModel [timestamp=" + timestamp + ", nmea=" + nmea + "]";
	}	
}