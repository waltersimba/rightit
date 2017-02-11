package co.za.rightit.geolocation.model;

import org.joda.time.DateTime;

public class FareQuery {
	
	private Location from;
	private Location to;
	private DateTime departureTime;
	
	public FareQuery() {
		this(null, null);
	}
	
	public FareQuery(Location from, Location to) {
		this.from = from;
		this.to= to;
		this.departureTime = DateTime.now();
	}

	public Location getFrom() {
		return from;
	}

	public Location getTo() {
		return to;
	}

	public void setFrom(Location from) {
		this.from = from;
	}

	public void setTo(Location to) {
		this.to = to;
	}

	public DateTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(DateTime departureTime) {
		this.departureTime = departureTime;
	}

	@Override
	public String toString() {
		return "FareQuery [from=" + from + ", to=" + to + ", departureTime=" + departureTime + "]";
	}

	public boolean hasDepartureTime() {
		return departureTime != null;
	}
	
}
