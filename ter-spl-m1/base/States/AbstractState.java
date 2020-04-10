
public abstract class AbstractState {

Scanner sc = new Scanner(System.in);

public abstract void goNext(Calculator context);
public abstract Object exec();}