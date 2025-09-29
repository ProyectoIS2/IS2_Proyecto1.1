package exceptions;

public class ReservationAlreadyExistException extends Exception{
	private static final long serialVersionUID = 1L;
	 
	 public ReservationAlreadyExistException()
	  {
	    super();
	  }
	  /**This exception is triggered if the question already exists 
	  *@param s String of the exception
	  */
	  public ReservationAlreadyExistException(String s)
	  {
	    super(s);
	  }
}
