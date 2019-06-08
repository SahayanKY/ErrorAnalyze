package quantity;

public class UncertainNumber {
	public UncertainNumber(double x, double dx) {
		this.estimatedValue = x;
		this.standardDeviation = dx;
	}
	protected double estimatedValue;
	protected double standardDeviation;

}
