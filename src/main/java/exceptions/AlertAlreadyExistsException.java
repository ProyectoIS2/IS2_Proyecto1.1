package exceptions;

public class AlertAlreadyExistsException extends Exception{
	public AlertAlreadyExistsException()
	  {
	    super();
	  }
	  /**This exception is triggered if the question does not have enough seats available
	  *@param s String of the exception
	  */
	  public AlertAlreadyExistsException(String s)
	  {
	    super(s);
	  }
}
