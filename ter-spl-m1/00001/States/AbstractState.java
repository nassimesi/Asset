package States;

import java.util.Scanner;

import Maths.Calculator;


public abstract class AbstractState {

	public abstract void goNext(Calculator context);
	
	public abstract Object exec();
	
	protected Scanner sc = new Scanner(System.in);
}
