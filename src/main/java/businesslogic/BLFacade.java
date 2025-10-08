package businesslogic;

import java.util.Date;
import dataAccess.RideInfo;
import java.util.List;

import domain.Ride;
import domain.Transaction;
import domain.Traveler;
import domain.Admin;
import domain.Alert;
import domain.Car;
import domain.Complaint;
import domain.Driver;
import domain.Reservation;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.UserAlreadyExistException;
import exceptions.UserDoesNotExistException;
import exceptions.AlertAlreadyExistsException;
import exceptions.CarAlreadyExistsException;
import exceptions.NotEnoughAvailableSeatsException;
import exceptions.NotEnoughMoneyException;
import exceptions.PasswordDoesNotMatchException;
import exceptions.ReservationAlreadyExistException;
import exceptions.RideAlreadyExistException;

import javax.jws.WebMethod;
import javax.jws.WebService;
 
/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BLFacade  {
	  
	/**
	 * This method returns all the cities where rides depart 
	 * @return collection of cities
	 */
	@WebMethod public List<String> getDepartCities();
	
	/**
	 * This method returns all the arrival destinations, from all rides that depart from a given city  
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	@WebMethod public List<String> getDestinationCities(String from);


	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @param nPlaces available seats
	 * @param driver to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today 
 	 * @throws RideAlreadyExistException if the same ride already exists for the driver
	 */
   @WebMethod
   public Ride createRide(RideInfo rideInfo) throws RideMustBeLaterThanTodayException, RideAlreadyExistException;
	
	
	/**
	 * This method retrieves the rides from two locations on a given date 
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @return collection of rides
	 */
	@WebMethod public List<Ride> getRides(String from, String to, Date date);
	
	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride 
	 * @param date of the month for which days with rides want to be retrieved 
	 * @return collection of rides
	 */
	@WebMethod public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);
	
	/**
	 * This method calls the data access to initialize the database with some events and questions.
	 * It is invoked only when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	@WebMethod public void initializeBD();
	
	@WebMethod
	public Driver createDriver(String email, String name, String password) throws UserAlreadyExistException;
	
	@WebMethod
	public Traveler createTraveler(String email, String name, String password)throws UserAlreadyExistException;
	
	@WebMethod
	public Reservation createReservation(int hm, Integer rideNumber, String travelerEmail) throws ReservationAlreadyExistException, NotEnoughAvailableSeatsException;
	
	@WebMethod
	public Driver getDriverByEmail(String email, String password) throws UserDoesNotExistException, PasswordDoesNotMatchException;
	
	@WebMethod
	public Traveler getTravelerByEmail(String email, String password) throws UserDoesNotExistException, PasswordDoesNotMatchException;
	
	@WebMethod
	public void pay(Reservation res) throws NotEnoughMoneyException;
	
	@WebMethod
	public void close();
	
	@WebMethod
	public List<Reservation> getDriverReservations(String email);
	
	@WebMethod
	public List<Reservation> getTravelerReservations(String email);
	
	@WebMethod
	public void takeMoneyDriver(String email, int hm) throws NotEnoughMoneyException;
	
	@WebMethod
	public void putMoneyTraveler(String email, int hm);
	
	@WebMethod
	public void updateReservation(Reservation res);
	
	@WebMethod
	public void cancelReservation (Reservation res);
	
	@WebMethod
	public void addCarToDriver(String driverEmail, String carPlate, int nPlaces, boolean dis) throws CarAlreadyExistsException;
	
	@WebMethod
	public List<Transaction> getTravelerTransactions(String email);
	
	@WebMethod
	public List<Transaction> getDriverTransactions(String email);
	
	@WebMethod
	public List<Car> getDriverCars(String email);
	
	@WebMethod
	public void removeRideDriver(Integer rideNumber, String email);
	
	@WebMethod
	public List<Ride> getDriverRides(String email);
	
	@WebMethod
	public void deleteAccountDriver(String email);
	
	@WebMethod
	public void deleteAccountTraveler(String email);
	
	@WebMethod
	public List<Complaint> getDriverComplaint(String email);
	
	@WebMethod
	public void createComplaintToDriver(String c, Reservation res);
	
	@WebMethod
	public void addRatingToDriver(String e, int z, Integer resCode);

	@WebMethod
	public void addRatingToTraveler(String e, int z, Integer resCode);
	
	@WebMethod
	public void createAlert(Traveler t, String jatorria, String helmuga) throws AlertAlreadyExistsException;
	
	@WebMethod
	public List<Ride> isRideBeenCreated(Alert alert);
	
	@WebMethod 
	public List<Alert> getTravelerAlert(String email);
	
	@WebMethod
	public void removeAlert(Integer z);
	
	@WebMethod
	public float getDriverRatings(String email);
    
	@WebMethod
	public float getTravelerRatings(String email);
	
	@WebMethod
	public Admin getAdminByEmail(String email, String password) throws UserDoesNotExistException, PasswordDoesNotMatchException;
	
	@WebMethod
	public List<Complaint> getAllComplaint();
	
	@WebMethod
	public void denyComplaint(Complaint co);
	
	@WebMethod
	public void acceptComplaint(Complaint co);
	
	@WebMethod
	public void denyComplaintAdmin(Complaint co);
	
	@WebMethod
	public void updateAlerts(String jatorria, String helburua);
}
