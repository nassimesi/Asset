package States;

import Maths.Calculator;

public class EOperand2 extends AbstractState{

	@Override
	public void goNext(Calculator context) {
		context.setState(new EOperator());
	}

	@Override
	public Object exec() {
		String input = null;
		System.out.print("> Please give number : ");
		input = sc.nextLine();
		
		return Integer.valueOf(input);

	}

}
