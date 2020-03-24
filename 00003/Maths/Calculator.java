package Maths;

import States.AbstractState;
import States.EOperand1;



public class Calculator {

	protected Integer accumulator = 0; 
	protected Operation operator = null;
	protected AbstractState current = null;
	
	public Calculator() {
		current = new EOperand1();
	}

	public void run() {
		
		while(true) {
			accumulator = 0;
			Integer a = (Integer) current.exec();
			goNext();
			Operation op = (Operation) current.exec();
			goNext();
			Integer b = (Integer) current.exec();
			
			accumulator += op.compute(a, b);
			
			System.out.println("res : " + accumulator);
		}
		
	}
	
	public Operation getOperator() {
		return operator;
	}
	
	private void goNext() {
		current.goNext(this);
	}

	public void setState(AbstractState state) {
		current = state;
	}
	
	public void quitCalc() {
		System.exit(0);
	}
	
}
