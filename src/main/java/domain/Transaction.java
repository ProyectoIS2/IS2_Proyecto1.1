package domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Transaction {
	@XmlID
	@Id 
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@GeneratedValue
	private Integer id;
	private int type;
	private double amount;
	private Driver d;
	private Traveler t;
	
	public Transaction () {
		super();
	}
	
	public Transaction (double amount, Driver d){
		this.amount=amount;
		this.d=d;
		this.type=1;
	}
	
	public Transaction (double amount, Traveler t){
		this.amount=amount;
		this.t=t;
		this.type=2;
	}
	
	public Transaction (double amount, Driver d, Traveler t){
		this.amount=amount;
		this.d=d;
		this.t=t;
		this.type=3;
	}
	
	public Transaction(double amount, Traveler t, Driver d) {
		this.amount=amount;
		this.d=d;
		this.t=t;
		this.type=4;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Driver getD() {
		return d;
	}

	public void setD(Driver d) {
		this.d = d;
	}

	public Traveler getT() {
		return t;
	}

	public void setT(Traveler t) {
		this.t = t;
	}
	
	public String toString() {
		String s = "";
		switch (type) {
		case 1: 
			s = this.id+";"+this.amount+";"+this.d.getEmail();
			break;
		case 2:
			s = this.id+";"+this.amount+";"+this.t.getEmail();
			break;
		case 3:
			s = this.id+";"+this.amount+";"+this.d.getEmail()+";"+this.t.getEmail();
			break;
		case 4:
			s = this.id+";"+this.amount+";"+this.t.getEmail()+";"+this.d.getEmail();
		}
		return s;
	}
}
