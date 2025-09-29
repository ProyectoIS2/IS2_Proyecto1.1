package domain;


import java.io.Serializable;
import java.util.ArrayList;
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
public class Traveler implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlID
	@Id 
	private String email;
	private String name;
	private float money;
	private String password;
	private int[] ratings = new int[5];
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Reservation> reservations = new Vector<Reservation>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Transaction> transactions = new Vector <Transaction>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Complaint> complaints=new Vector<Complaint>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Alert> alerts=new Vector<Alert>();
	
	public Traveler() {
		super();
	}
	
	public Traveler(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password=password;
		for(int i = 0; i<5; i++) {
			ratings[i]=0;
		}
	}
	
	public List<Alert> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
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
	
	public List<Complaint> getComplaints() {
		return complaints;
	}

	public void setComplaints(List<Complaint> complaints) {
		this.complaints = complaints;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public void putMoney(float amount) {
		this.setMoney(this.getMoney()+amount);
	}
	
	public Reservation makeReservation(Ride r, int hm) {
		Reservation res = new Reservation(hm, r, this);
		this.reservations.add(res);
		return res;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Traveler other = (Traveler) obj;
		if (email != other.email)
			return false;
		return true;
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
	
	public void updateReservation(Integer resCode) {
		int ind = this.findRes(resCode);
		Reservation res = this.reservations.get(ind);
		this.reservations.remove(ind);
		res.setAccepted(true);
		this.reservations.add(res);
	}
	
	public void cancelReservation(Reservation res) {
		this.reservations.remove(this.findRes(res.getReservationCode()));
	}
	
	public boolean addTransaction(Transaction t) {
		return this.transactions.add(t);
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
	
	public boolean addAlert(Alert a) {
		return this.getAlerts().add(a);
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
