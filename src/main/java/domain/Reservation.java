package domain;

import java.io.*;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Reservation implements Serializable {
	@XmlID
	@Id 
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@GeneratedValue
	private Integer reservationCode;
	private Boolean accepted;
	private boolean payed = false;
	private int hmTravelers;
	private Ride ride;
	private Traveler traveler;
	private Driver driver;
	private float cost;
	private boolean ratedT;
	private boolean ratedD;
	private boolean complained = false;
	
	public Reservation() {
		super();
	}
	
	public Reservation(int hm, Ride r, Traveler t) {
		this.hmTravelers=hm;
		this.ride=r;
		this.traveler=t;
		this.driver=ride.getDriver();
		this.cost=this.ride.getPrice()*this.hmTravelers;
		this.accepted=false;
		this.ratedT=false;
		this.ratedD=false;
	}

	public Integer getReservationCode() {
		return reservationCode;
	}

	public boolean isComplained() {
		return complained;
	}

	public void setComplained(boolean complained) {
		this.complained = complained;
	}

	public void setRatedD(boolean ratedD) {
		this.ratedD = ratedD;
	}

	public void setReservationCode(Integer reservationCode) {
		this.reservationCode = reservationCode;
	}

	public boolean isRatedT() {
		return ratedT;
	}

	public void setRatedT(boolean ratedT) {
		this.ratedT = ratedT;
	}

	public Boolean getRatedD() {
		return ratedD;
	}

	public void setRatedD(Boolean ratedD) {
		this.ratedD = ratedD;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public boolean isPayed() {
		return payed;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}

	public void setTraveler(Traveler traveler) {
		this.traveler = traveler;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public int getHmTravelers() {
		return hmTravelers;
	}

	public void setHmTravelers(int hmTravelers) {
		this.hmTravelers = hmTravelers;
	}

	public Ride getRide() {
		return ride;
	}

	public Traveler getTraveler() {
		return traveler;
	}
	
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reservation other = (Reservation) obj;
		if (this.reservationCode != other.getReservationCode())
			return false;
		return true;
	}
	
	public void accept() {
		this.setAccepted(true);
	}
}
