package main;

import quantity.Quantity;

public class Main {
	//x,dx
	//-1.254, 0.295 -> -1.2(3)
	//-1.1, 43.2 -> 0(4)e1
	//-1.254e16, 1 -> -1.25400000000000000(10)e16
	//-1.254e17, 1 -> -1.254000000000000000(10)e17
	//-1.254e18, 1 -> --.9223372036854775808(10)e17

	public static void main(String args[]) {
		Quantity x = new Quantity(-1.254e19, 1, "");
		System.out.println(x);
	}
}
