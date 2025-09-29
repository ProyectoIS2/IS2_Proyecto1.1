package exceptions;
public class RideAlreadyExistException extends Exception {
 private static final long serialVersionUID = 1L;
 
 public RideAlreadyExistException()
  {
    super();
  }
  /**This exception is triggered if the question already exists 
  *@param s String of the exception
  */
  public RideAlreadyExistException(String s)
  {
    super(s);
  }
}