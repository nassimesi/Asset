
public class EOperator extends AbstractState {

public void goNext(Calculator context){
    context.setState(new EOperand2());
};}