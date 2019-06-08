package operation;

import quantity.Quantity;
import quantity.Quantity.QuantityAccessor;

public class QuantityOperator {

	private static class OperatorQuantityAccessor extends QuantityAccessor{
	}

	private final static OperatorQuantityAccessor accessor = new OperatorQuantityAccessor();

	public static Quantity plus(Quantity x, Quantity y) {


		return null;
	}

}
