package exceptions;
public class RideMustBeLaterThanTodayException extends Exception {
 private static final long serialVersionUID = 1L;
 
 public RideMustBeLaterThanTodayException()
  {
    super();
  }
  /**This exception is triggered if the event has already finished
  *@param s String of the exception
  */
  public RideMustBeLaterThanTodayException(String s)
  {
    super(s);
  }
}