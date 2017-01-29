package co.za.rightit.geolocation.model;

public class FareQuery {
	private Location from;
	private Location to;
	
	public FareQuery() {
		this(null, null);
	}
	
	public FareQuery(Location from, Location to) {
		this.from = from;
		this.to= to;
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
	
}
