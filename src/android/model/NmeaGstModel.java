package cordova.plugin.nmea.model;

public class NmeaGstModel {
    private long timestamp;
    private String nmea;

    public NmeaGstModel()
	{		
	}
	
	public NmeaGstModel(long timestamp, String nmea)
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
		return "NmeaGstModel [timestamp=" + timestamp + ", nmea=" + nmea + "]";
	}	
}