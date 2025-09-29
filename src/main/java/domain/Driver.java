package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Driver implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlID
	@Id 
	private String email;
	private String password;
	private String name; 
	private int[] ratings = new int[5];
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Ride> rides=new Vector<Ride>();
	private float money;
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Reservation> reservations = new Vector <Reservation>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Car> cars = new Vector <Car>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Transaction> transactions = new Vector <Transaction>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Complaint> complaints=new Vector<Complaint>();

	public Driver() {
		super();
	}

	public Driver(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
		for(int i = 0; i<5; i++) {
			ratings[i]=0;
		}
	}
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int[] getRatings() {
		return ratings;
	}

	public void setRatings(int[] ratings) {
		this.ratings = ratings;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

	public String toString(){
		return email+";"+name+rides;
	}
	
	public List<Reservation> getReservations() {
		return reservations;
	}

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	public List<Complaint> getComplaints() {
		return complaints;
	}

	public void setComplaints(List<Complaint> complaints) {
		this.complaints = complaints;
	}

	/**
	 * This method creates a bet with a question, minimum bet ammount and percentual profit
	 * 
	 * @param question to be added to the event
	 * @param betMinimum of that question
	 * @return Bet
	 */
	public Ride addRide(String from, String to, Date date, float price, Car car)  {
        Ride ride=new Ride(from,to,date,price, this, car);
        rides.add(ride);
        return ride;
	}

	/**
	 * This method checks if the ride already exists for that driver
	 * 
	 * @param from the origin location 
	 * @param to the destination location 
	 * @param date the date of the ride 
	 * @return true if the ride exists and false in other case
	 */
	public boolean doesRideExists(String from, String to, Date date)  {	
		for (Ride r:rides)
			if ( (java.util.Objects.equals(r.getFrom(),from)) && (java.util.Objects.equals(r.getTo(),to)) && (java.util.Objects.equals(r.getDate(),date)) )
			 return true;
		
		return false;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Driver other = (Driver) obj;
		if (email != other.email)
			return false;
		return true;
	}

	public void removeRide(String from, String to, Date date) {
		int i =0;
		for(Ride r: this.rides) {
			if(r.getFrom().equals(from)&&r.getTo()==to&&r.getDate()==date) {
				this.rides.remove(i);
				break;
			}
			i++;
		}
	}
	
	public boolean takeMoney(float amount) {
		if(amount<this.getMoney()) {
			this.setMoney(this.getMoney()-amount);
			return true;
		}else {
			return false;
		}
	}
	
	public boolean addReservation (Reservation res){
		return reservations.add(res);
	}
	
	public void updateReservation(Integer resCode) {
		int ind = this.findRes(resCode);
		Reservation res = this.reservations.get(ind);
		this.reservations.remove(ind);
		res.setAccepted(true);
		this.reservations.add(res);
	}
	
	private int findRes(Integer resCode) {
		int kont = 0;
		for(Reservation res : this.reservations) {
			if(res.getReservationCode().equals(resCode)) {
				return kont;
			}
			kont ++;
		}
		return -1;
	}
	
	public void cancelReservation(Reservation res) {
		this.reservations.remove(this.findRes(res.getReservationCode()));
	}
	
	public boolean addCar(Car car) {
		return this.cars.add(car);
	}
	
	public boolean addTransaction(Transaction t) {
		return this.transactions.add(t);
	}
	
	public boolean hasCars() {
		return !this.cars.isEmpty();
	}
	
	public boolean addComplaint(Complaint c) {
		return this.complaints.add(c);
	}
	
	public void addRating(int z) {
		switch (z) {
		case 1: this.ratings[0]+=1; break;
		case 2: this.ratings[1]+=1; break;
		case 3: this.ratings[2]+=1; break;
		case 4: this.ratings[3]+=1; break;
		case 5: this.ratings[4]+=1; break;
		}	
	}
	
	public float calculateRating() {
		float r = 0;
		int kop = 0;
		for(int i=1; i<=5; i++) {
			r=r+i*ratings[i-1];
			kop+=ratings[i-1];
		}
		return r/kop;
	}
	
	public void removeComplaint(Complaint c) {
		complaints.remove(this.findCom(c.getId()));
	}
	
	private int findCom(Integer resCode) {
		int kont = 0;
		for(Complaint c : this.complaints) {
			if(c.getId().equals(resCode)) {
				return kont;
			}
			kont ++;
		}
		return -1;
	}
}
