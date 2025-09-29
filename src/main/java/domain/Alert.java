package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Alert implements Serializable{
	@XmlID
	@Id 
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@GeneratedValue
	private Integer alertCode;
	private String jatorria;
	private String helburua;
	private Traveler traveler;
	private boolean sortuta = false;
	
	

	public Alert (String j, String h, Traveler t) {
		this.jatorria=j;
		this.helburua=h;
		this.traveler=t;
	}
	
	public Alert() {
		super();
	}
	
	public boolean isSortuta() {
		return sortuta;
	}

	public void setSortuta(boolean sortuta) {
		this.sortuta = sortuta;
	}

	public Integer getAlertCode() {
		return alertCode;
	}

	public void setAlertCode(Integer alertCode) {
		this.alertCode = alertCode;
	}

	public String getJatorria() {
		return jatorria;
	}

	public void setJatorria(String jatorria) {
		this.jatorria = jatorria;
	}

	public String getHelburua() {
		return helburua;
	}

	public void setHelburua(String helburua) {
		this.helburua = helburua;
	}

	public Traveler getTraveler() {
		return traveler;
	}

	public void setTraveler(Traveler traveler) {
		this.traveler = traveler;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alert other = (Alert) obj;
		if (this.jatorria.equals(other.jatorria)&&this.helburua.equals(other.helburua))
			return true;
		return false;
	}
}
