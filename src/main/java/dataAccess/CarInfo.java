package dataAccess;

public class CarInfo {
	String carPlate; 
	int nPlaces;
	boolean dis;
	public CarInfo(String carPlate, int nPlaces, boolean dis) {
		this.carPlate = carPlate;
		this.nPlaces = nPlaces;
		this.dis = dis;
	}
	
	public String getCarPlate() {
		return carPlate;
	}
	public int getnPlaces() {
		return nPlaces;
	}
	public boolean isDis() {
		return dis;
	}	
	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}	
	public void setnPlaces(int nPlaces) {
		this.nPlaces = nPlaces;
	}
	public void setDis(boolean dis) {
		this.dis = dis;
	}
}
