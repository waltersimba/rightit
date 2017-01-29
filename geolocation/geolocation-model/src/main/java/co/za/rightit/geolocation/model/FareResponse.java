package co.za.rightit.geolocation.model;

public class FareResponse {

	private String origin;
	private String destination;
	private Measurement distance;
	private Measurement duration;
	private Measurement cost;
	
	public FareResponse withOrigin(String origin) {
		this.origin = origin;
		return this;
	}
	
	public FareResponse withDestination(String destination) {
		this.destination = destination;
		return this;
	}
	
	public FareResponse withDistance(Measurement distance) {
		this.distance = distance;
		return this;
	}
	
	public FareResponse withDuration(Measurement duration) {
		this.duration = duration;
		return this;
	}
	
	public FareResponse withCost(Measurement cost) {
		this.cost = cost;
		return this;
	}
	
	public String getOrigin() {
		return origin;
	}

	public String getDestination() {
		return destination;
	}

	public Measurement getDistance() {
		return distance;
	}

	public Measurement getDuration() {
		return duration;
	}

	public Measurement getCost() {
		return cost;
	}
	
	@Override
	public String toString() {
		return "[origin=" + origin + ", destination=" + destination + ", distance=" + distance
				+ ", duration=" + duration + ", cost=" + cost + "]";
	}

	public static final class Measurement {
		
		private String text;
		private Object value;
		
		public Measurement() {
			this(null, null);
		}
		
		public Measurement(String text, Object value) {
			this.text = text;
			this.value = value;
		}

		public String getText() {
			return text;
		}

		public Object getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "[text=" + text + ", value=" + value + "]";
		}
		
	}
	
}
