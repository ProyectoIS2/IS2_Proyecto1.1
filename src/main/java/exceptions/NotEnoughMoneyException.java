package exceptions;

public class NotEnoughMoneyException extends Exception{
	public NotEnoughMoneyException()
	  {
	    super();
	  }
	  /**This exception is triggered if the question does not have enough money
	  *@param s String of the exception
	  */
	  public NotEnoughMoneyException(String s)
	  {
	    super(s);
	  }
}
