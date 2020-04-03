package States;
import Maths.Calculator;


import Maths.Multiplication;
import Maths.Subtraction;


public class EOperator extends AbstractState {


	final String sub = Subtraction.OperationLabel;
	final String mult = Multiplication.OperationLabel;

	@Override
	public void goNext(Calculator context) {
		context.setState(new EOperand2());
	}

	@Override
	public Object exec() {

		String input = null;
		System.out.println("> Please select an Operation [x] : ");


		System.out.println("> ["+sub+"] for Substraction ");

		System.out.println("> ["+mult+"] for Multiplication ");

		input = sc.nextLine();

		if (input.equals(sub)) {
			return new Subtraction();
		}

		if (input.equals(mult)) {
			return new Multiplication();
		}

		return null;

	}

}
