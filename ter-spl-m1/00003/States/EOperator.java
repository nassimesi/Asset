package States;
import Maths.Calculator;

import Maths.Subtraction;


public class EOperator extends AbstractState {
	final String sub = Subtraction.OperationLabel;

	@Override
	public void goNext(Calculator context) {
		context.setState(new EOperand2());
	}

	@Override
	public Object exec() {

		String input = null;
		System.out.println("> Please select an Operation [x] : ");

		System.out.println("> ["+sub+"] for Substraction ");

		input = sc.nextLine();

		if (input.equals(sub)) {
			return new Subtraction();
		}

		return null;

	}

}
