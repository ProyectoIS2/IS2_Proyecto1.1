package exceptions;

public class NotEnoughAvailableSeatsException extends Exception{
	private static final long serialVersionUID = 1L;
	 
	 public NotEnoughAvailableSeatsException()
	  {
	    super();
	  }
	  /**This exception is triggered if the question does not have enough seats available
	  *@param s String of the exception
	  */
	  public NotEnoughAvailableSeatsException(String s)
	  {
	    super(s);
	  }
}
