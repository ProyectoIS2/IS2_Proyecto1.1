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
public class Complaint {
	@XmlID
	@Id 
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@GeneratedValue
	private Integer id;
	private String complaint;
	private Reservation res;
	private boolean denied = false;
	
	public Complaint () {
		super();
	}
	
	public Complaint (String complaint, Reservation r){
		this.complaint=complaint;
		this.res=r;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComplaint() {
		return complaint;
	}

	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}

	public Reservation getRes() {
		return res;
	}

	public void setRes(Reservation res) {
		this.res = res;
	}

	public boolean isDenied() {
		return denied;
	}

	public void setDenied(boolean denied) {
		this.denied = denied;
	}
	
	public String getDriverEmail() {
		return this.res.getDriver().getEmail();
	}
	
	public String getTravelerEmail() {
		return this.res.getTraveler().getEmail();
	}
	
	public String getDriverName() {
		return this.res.getDriver().getName();
	}
	
	public String getTravelerName() {
		return this.res.getTraveler().getName();
	}
	
	public Ride getRide() {
		return this.res.getRide();
	}
}
