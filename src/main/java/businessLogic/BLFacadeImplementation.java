package businessLogic;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.jws.WebMethod;
import javax.jws.WebService;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.*;
import exceptions.*;

/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation  implements BLFacade {
	DataAccess dbManager;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		dbManager=new DataAccess();
		//dbManager.close();
	}
	
    public BLFacadeImplementation(DataAccess da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		ConfigXML c=ConfigXML.getInstance();
		
		dbManager=da;		
	}
    
    
    /**
     * {@inheritDoc}
     */
    @WebMethod public List<String> getDepartCities(){
    	dbManager.open();	
		
		 List<String> departLocations=dbManager.getDepartCities();		

		dbManager.close();
		
		return departLocations;
    	
    }
    /**
     * {@inheritDoc}
     */
	@WebMethod public List<String> getDestinationCities(String from){
		dbManager.open();	
		List<String> targetCities=dbManager.getArrivalCities(from);		
		dbManager.close();
		
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
   @WebMethod
   public Ride createRide( String from, String to, Date date,float price, String driverEmail, String carPlaces ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
	   
		dbManager.open();
		Ride ride=dbManager.createRide(from, to, date, price, driverEmail, carPlaces);		
		dbManager.close();
		return ride;
   };
	
   /**
    * {@inheritDoc}
    */
	@WebMethod 
	public List<Ride> getRides(String from, String to, Date date){
		dbManager.open();
		List<Ride>  rides=dbManager.getRides(from, to, date);
		dbManager.close();
		return rides;
	}

    
	/**
	 * {@inheritDoc}
	 */
	@WebMethod 
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		dbManager.open();
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		dbManager.close();
		return dates;
	}
	
	@WebMethod
	public void close() {
		DataAccess dB4oManager=new DataAccess();

		dB4oManager.close();

	}

	/**
	 * {@inheritDoc}
	 */
    @WebMethod	
	 public void initializeBD(){
    	dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}
    
    @WebMethod
    public Driver createDriver(String email, String name, String password) throws UserAlreadyExistException{
    	dbManager.open();
    	Driver d = dbManager.createDriver(email, name, password);
    	dbManager.close();
    	return d;
    }
    
    @WebMethod
    public Traveler createTraveler(String email, String name, String password)throws UserAlreadyExistException{
    	dbManager.open();
    	Traveler t = dbManager.createTraveler(email, name, password);
    	dbManager.close();
    	return t;
    }
    
    @WebMethod
    public Reservation createReservation(int hm, Integer rideNumber, String travelerEmail) throws ReservationAlreadyExistException, NotEnoughAvailableSeatsException{
    	dbManager.open();
    	Reservation res = dbManager.createReservation(hm, rideNumber, travelerEmail);
    	dbManager.close();
    	return res;
    }
    
    @WebMethod
    public Driver getDriverByEmail(String email, String password) throws UserDoesNotExistException, PasswordDoesNotMatchException{
    	dbManager.open();
    	Driver d = dbManager.getDriverByEmail(email, password);
    	dbManager.close();
    	return d;
    }
    
    @WebMethod
    public Traveler getTravelerByEmail(String email, String password) throws UserDoesNotExistException, PasswordDoesNotMatchException{
    	dbManager.open();
    	Traveler t = dbManager.getTravelerByEmail(email, password);
    	dbManager.close();
    	return t;
    }
    
    @WebMethod
    public void pay(Reservation res) throws NotEnoughMoneyException{
    	dbManager.open();
    	dbManager.pay(res);
    	dbManager.close();
    }
    
    @WebMethod
    public List<Reservation> getDriverReservations(String email){
    	dbManager.open();
    	List<Reservation> l = dbManager.getDriverReservations(email);
    	dbManager.close();
    	return l;
    }
	
    @WebMethod
	public List<Reservation> getTravelerReservations(String email){
		dbManager.open();
		List<Reservation> l = dbManager.getTravelerReservations(email);
    	dbManager.close();
    	return l;
	}
	
    @WebMethod
	public void takeMoneyDriver(String email, int hm) throws NotEnoughMoneyException{
		dbManager.open();
		dbManager.takeMoneyDriver(email, hm);
    	dbManager.close();
	}
	
    @WebMethod
	public void putMoneyTraveler(String email, int hm) {
		dbManager.open();
    	dbManager.putMoneyTraveler(email, hm);
    	dbManager.close();
	}
	
    @WebMethod
	public void updateReservation(Reservation res) {
		dbManager.open();
    	dbManager.updateReservation(res);
    	dbManager.close();
	}
	
    @WebMethod
	public void cancelReservation (Reservation res) {
		dbManager.open();
    	dbManager.cancelReservation(res);
    	dbManager.close();
	}
	
    @WebMethod
	public void addCarToDriver(String driverEmail, String carPlate, int nPlaces, boolean dis) throws CarAlreadyExistsException{
		dbManager.open();
		dbManager.addCarToDriver(driverEmail, carPlate, nPlaces, dis);
		dbManager.close();
	}
	
    @WebMethod
	public List<Transaction> getTravelerTransactions(String email){
		dbManager.open();
		List<Transaction> l=dbManager.getTravelerTransactions(email);
		dbManager.close();
		return l;
	}
	
    @WebMethod
	public List<Transaction> getDriverTransactions(String email){
		dbManager.open();
		List<Transaction> l=dbManager.getDriverTransactions(email);
		dbManager.close();
		return l;
	}
	
    @WebMethod
	public List<Car> getDriverCars(String email){
		dbManager.open();
		List<Car> l=dbManager.getDriverCars(email);
		dbManager.close();
		return l;
	}
	
    @WebMethod
	public void removeRideDriver(Integer rideNumber, String email) {
		dbManager.open();
		dbManager.removeRideDriver(rideNumber, email);
		dbManager.close();
	}
	
    @WebMethod
	public List<Ride> getDriverRides(String email){
		dbManager.open();
		List<Ride> l=dbManager.getDriverRides(email);
		dbManager.close();
		return l;
	}
    
    @WebMethod
    public void deleteAccountDriver(String email) {
    	dbManager.open();
    	dbManager.deleteAccountDriver(email);
    	dbManager.close();
    }
    
    @WebMethod
    public void deleteAccountTraveler(String email) {
    	dbManager.open();
    	dbManager.deleteAccountTraveler(email);
    	dbManager.close();
    }
    
    @WebMethod
    public List<Complaint> getDriverComplaint(String email){
    	dbManager.open();
    	List<Complaint> l = dbManager.getDriverComplaint(email);
    	dbManager.close();
    	return l;
    }
    
    @WebMethod
    public void createComplaintToDriver(String c, Reservation res) {
    	dbManager.open();
    	dbManager.createComplaintToDriver(c, res);
    	dbManager.close();
    }
    
    @WebMethod
    public void addRatingToTraveler(String e, int z, Integer resCode) {
    	dbManager.open();
    	dbManager.addRatingToTraveler(e, z, resCode);
    	dbManager.close();
    }
    
    @WebMethod
    public void addRatingToDriver(String e, int z, Integer resCode) {
    	dbManager.open();
    	dbManager.addRatingToDriver(e, z, resCode);
    	dbManager.close();
    }
    
    @WebMethod
    public void createAlert(Traveler t, String jatorria, String helmuga) throws AlertAlreadyExistsException{
    	dbManager.open();
    	dbManager.createAlert(t, jatorria, helmuga);
    	dbManager.close();
    }
    
    @WebMethod
    public List<Ride> isRideBeenCreated(Alert alert) {
    	dbManager.open();
    	List<Ride> r = dbManager.isRideBeenCreated(alert);
    	dbManager.close();
    	return r;
    }
    
    @WebMethod
    public List<Alert> getTravelerAlert(String email){
    	dbManager.open();
    	List<Alert> l = dbManager.getTravelerAlert(email);
    	dbManager.close();
    	return l;
    }
    
    @WebMethod
    public void removeAlert(Integer z) {
    	dbManager.open();
    	dbManager.removeAlert(z);
    	dbManager.close();
    }
    
    @WebMethod
    public float getDriverRatings(String email) {
    	dbManager.open();
    	float l = dbManager.getDriverRatings(email);
    	dbManager.close();
    	return l;
    }
    
    @WebMethod
    public float getTravelerRatings(String email) {
    	dbManager.open();
    	float l = dbManager.getTravelerRatings(email);
    	dbManager.close();
    	return l;
    }
    
    @WebMethod
    public Admin getAdminByEmail(String email, String password) throws UserDoesNotExistException, PasswordDoesNotMatchException{
    	dbManager.open();
    	Admin a = dbManager.getAdminByEmail(email, password);
    	dbManager.close();
    	return a;
    }
    
    @WebMethod
    public List<Complaint> getAllComplaint(){
    	dbManager.open();
    	List<Complaint> c = dbManager.getAllComplaint();
    	dbManager.close();
    	return c;
    }
    
    @WebMethod
    public void denyComplaint(Complaint co) {
    	dbManager.open();
    	dbManager.denyComplaint(co);
    	dbManager.close();
    }
    
    @WebMethod
    public void acceptComplaint(Complaint co) {
    	dbManager.open();
    	dbManager.acceptComplaint(co);
    	dbManager.close();
    }
    
    @WebMethod
    public void denyComplaintAdmin(Complaint co) {
    	dbManager.open();
    	dbManager.denyComplaintAdmin(co);
    	dbManager.close();
    }
    
    @WebMethod
    public void updateAlerts(String jatorria, String helburua) {
    	dbManager.open();
    	dbManager.updateAlerts(jatorria, helburua);
    	dbManager.close();
    }
}

