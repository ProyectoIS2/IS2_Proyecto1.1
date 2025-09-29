package exceptions;

public class PasswordDoesNotMatchException extends Exception{
	public PasswordDoesNotMatchException()
	  {
	    super();
	  }
	  /**This exception is triggered if the question does not match 
	  *@param s String of the exception
	  */
	  public PasswordDoesNotMatchException(String s)
	  {
	    super(s);
	  }
}
