package States;
import Maths.Calculator;

import Maths.Addition;



public class EOperator extends AbstractState {

	final String add = Addition.OperationLabel;

	@Override
	public void goNext(Calculator context) {
		context.setState(new EOperand2());
	}

	@Override
	public Object exec() {

		String input = null;
		System.out.println("> Please select an Operation [x] : ");

		System.out.println("> ["+add+"] for Addition ");

		input = sc.nextLine();

		if(input.equals(add)) {
			return new Addition();
		}


		return null;

	}

}
