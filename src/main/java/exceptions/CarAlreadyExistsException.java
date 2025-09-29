package exceptions;

public class CarAlreadyExistsException extends Exception{
	public CarAlreadyExistsException()
	  {
	    super();
	  }
	  /**This exception is triggered if the question does not have enough seats available
	  *@param s String of the exception
	  */
	  public CarAlreadyExistsException(String s)
	  {
	    super(s);
	  }
}
