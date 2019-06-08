package quantity;

import java.util.StringJoiner;

/**
 * 一般に量を表現します。
 * それは物理量、数値、単位を含みます。
 * */
public class Quantity {
	/**
	 * Quantityインスタンスのメンバへのアクセスを許可するクラスを規定します。
	 * Quantityインスタンスのメンバへアクセスするクラスは内部にこのクラスを継承した
	 * クラスをもつ必要があります。さらに、このクラスに許可されているクラスに限られます。
	 * */
	public static class QuantityAccessor{

		//protected指定は子クラスを意識したもの。
		/**
		 * メンバへのアクセスをコントロールするインスタンスを生成します。
		 *
		 * @throws RuntimeException このクラスを継承したクラスがアクセスを認めないクラスであった場合。
		 *
		 * */
		protected QuantityAccessor() {
			//アクセスを許可するクラスかどうかを判断する。
			//TODO 条件式を追加
		}

		protected UncertainNumber getNumber(Quantity x) {
			return x.number;
		}

		protected Unit getUnit(Quantity x) {
			return x.unit;
		}
	}

	public Quantity(double x, double dx, String unitstr) {
		this.number = new UncertainNumber(x,dx);

		int m,kg,s,A,K,mol,cd;
		//TODO 単位計算クラスを作成する。

		this.unit = new Unit(2,1,0,0,0,0,0);
	}

	private UncertainNumber number;
	private Unit unit;

	public String toString() {
		double x = number.estimatedValue;
		double dx = number.standardDeviation;

		//TODO dx == 0の場合について考える

		//まず、dxの表示する値と最終桁を決める
		//dxをa*10^bに変える(1<=a<10,b:整数)
		int b_dx = calcExp(dx);
		double a_dx = calcDecimal(dx,b_dx);
		long _a_dx;

		if(a_dx < 3) {
			//dxの仮数の部分aが1.2***や2.4***のときは2桁表示させたい
			//3桁目を四捨五入する
			//もし、その四捨五入で仮数が3を超えた場合、1桁表示(3)にする。
			//1.54*10^-3 -> 15.4*10^-4 -> 15*10^-4
			//2.96*10^-3 -> 29.6*10^-4 -> 30*10^-4 -> 3*10^-3
			_a_dx = calcRound(10*a_dx);
			if(_a_dx == 30) {
				_a_dx = 3;
				//b_dx = b_dx;
			}else {
				//仮数を10倍している分減らす
				b_dx -= 1;
			}
		}else {
			//dxの仮数部分が3.2や8.4のように3以上であれば1桁表示にする
			_a_dx = calcRound(a_dx);
			//b_dx = b_dx;
		}

		//dxの表示する仮数は_a_dx
		//dxの最終桁はb_dx
		//xをdxの最終桁に合わせて四捨五入する
		long _a_x = calcRound(calcDecimal(x,b_dx));

		int b;
		//今x=_a_x*10^b_dxとなっていて、1<=_a_x<10ではない
		//最終的に表示するbはx=a*10^b
		if(_a_x != 0) {
			b = calcExp(_a_x)+b_dx;//calcExp(x)としてもよいが、四捨五入により桁が変わっている可能性を考慮
		}else {
			b = b_dx;
		}

		//後は、_a_xの1桁目を一の位に、後の桁から一の位までを小数点以下に、
		//それに続けて括弧の中に_a_dxを置き、
		//最後にe^bとすれば終わり

		//_a_xの各桁の数字:_a_x_chararray
		//		※数学的には_a_xの指数部を弄ることでも求まるが、計算誤差を考慮
		//_a_xの符号:x_plus
		boolean x_plus = (x>=0)?true:false;
		char[] _a_x_chararray = String.valueOf(Math.abs(_a_x)).toCharArray();

		//結果の文字列を構築
		StringBuilder stb = new StringBuilder();
		if(!x_plus) {
			//xが負数である場合
			stb.append("-");
		}
		stb.append(_a_x_chararray[0]);
		if(_a_x_chararray.length > 1) {
			//小数点以下まで続く場合
			stb.append(".");
			for(int i=1;i<_a_x_chararray.length;i++) {
				stb.append(_a_x_chararray[i]);
			}
		}
		stb.append("("+_a_dx+")");
		if(b != 0){
			stb.append("e"+b);
		}

		String numberStr = stb.toString();


		StringJoiner stj = new StringJoiner(".");
		if(unit.meterDimension != 0) {
			stj.add("m^{"+unit.meterDimension+"}");
		}
		if(unit.kilogramDimension != 0) {
			stj.add("kg^{"+unit.kilogramDimension+"}");
		}
		if(unit.secondDimension != 0) {
			stj.add("s^{"+unit.secondDimension+"}");
		}
		if(unit.ampereDimension != 0) {
			stj.add("A^{"+unit.ampereDimension+"}");
		}
		if(unit.kelvinDimension != 0) {
			stj.add("K^{"+unit.kelvinDimension+"}");
		}
		if(unit.moleDimension != 0) {
			stj.add("mol^{"+unit.moleDimension+"}");
		}
		if(unit.candelaDimension != 0) {
			stj.add("cd^{"+unit.candelaDimension+"}");
		}
		String unitStr = stj.toString();

		if(unitStr.equals("")) {
			return numberStr;
		}else {
			return numberStr+" "+unitStr;
		}
	}

	/**
	 * 指定された数値xを±a*10^b(1<=a<10,b:整数)にし、
	 * その際のbを返します。
	 *
	 * @param x 実数
	 * @return ±a*10^bにしたときのb
	 * */
	private int calcExp(double x) {
		double log10x = Math.log10(Math.abs(x));
		return (int)Math.floor(log10x);
	}

	/**
	 * 指定された数値xをa*10^bにしたときのaを返します。
	 *
	 * @param x 実数
	 * @param b 整数
	 * @return a*10^bにした時のa
	 * */
	private double calcDecimal(double x, int b) {
		return x/Math.pow(10, b);
	}


	/**
	 * 指定された数値xを次のルールに従って四捨五入し、その整数値を返します。
	 * <ol>
	 * 	<li>絶対値をとる。
	 * 	<li>絶対値の小数点第一位が0から4の場合は切り捨てを行う。
	 * 	<li>絶対値の小数点第一位が6から9の場合は切り上げを行う。
	 * 	<li>絶対値の小数点第一位が5の場合は、一の位が偶数になるように切り捨て、または切り上げを行う。
	 * 	<li>得られた整数に符号をつけ、結果とする。
	 * </ol>
	 *
	 * @return 四捨五入した結果の整数値
	 * */
	private long calcRound(double x) {
		boolean plus = (x>=0)? true:false;
		double absx = Math.abs(x);
		long n;

		//整数部の数字
		n = (long)absx;
		//小数点第一位の数字
		long dx = (long)(10*(absx -n));

		if(0 <= dx && dx <= 4) {
			//切り捨てを行う
			//特に何もしない
			//n = n;

		}else if(6 <= dx && dx <= 9){
			//切り上げを行う
			n = n+1;

		}else {
			//小数点第一位が5の場合

			//一の位を求める
			long n1 = n%10;
			if(n1%2 == 0) {
				//一の位が偶数の場合
				//切り捨て、つまり何もしない
				//n = n;
			}else {
				//一の位が奇数の場合
				//切り上げ
				n = n+1;
			}
		}

		if(!plus) {
			n = -n;
		}

		return n;
	}


	public String toString(String unitstr) {
		//TODO どうすんの
		return null;
	}
}
