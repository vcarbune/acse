
public class PointRate{
	private double truePosRate;
	private double falsePosRate;

	public PointRate(double truePosRate, double falsePosRate) {
		this.truePosRate = truePosRate;
		this.falsePosRate = falsePosRate;
	}

	public double getTruePosRate() {
		return truePosRate;
	}
	public double getFalsePosRate() {
		return falsePosRate;
	}
}
