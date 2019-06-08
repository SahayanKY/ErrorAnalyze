package quantity;

/**
 * 単位を表現します。
 * */
public class Unit {
	public Unit(int meter, int kilogram, int second, int ampere, int kelvin, int mole, int candela) {
		this.meterDimension = meter;
		this.kilogramDimension = kilogram;
		this.secondDimension = second;
		this.ampereDimension = ampere;
		this.kelvinDimension = kelvin;
		this.moleDimension = mole;
		this.candelaDimension = candela;
	}
	protected int meterDimension;
	protected int kilogramDimension;
	protected int secondDimension;
	protected int ampereDimension;
	protected int kelvinDimension;
	protected int moleDimension;
	protected int candelaDimension;

}
