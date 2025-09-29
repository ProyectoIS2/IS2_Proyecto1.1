package exceptions;

public class UserDoesNotExistException extends Exception{
	public UserDoesNotExistException()
	  {
	    super();
	  }
	  /**This exception is triggered if the question does not already exist
	  *@param s String of the exception
	  */
	  public UserDoesNotExistException(String s)
	  {
	    super(s);
	  }
}
