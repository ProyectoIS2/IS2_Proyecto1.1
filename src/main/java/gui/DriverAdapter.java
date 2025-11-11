package gui;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Date;

import domain.Driver;
import domain.Ride;


public class DriverAdapter extends AbstractTableModel {

    private Driver driver;
    private List<Ride> rides;
    private final String[] columnNames = {"from", "to", "date", "places", "price"};

    public DriverAdapter(Driver driver) {
        this.driver = driver;
        this.rides = driver.getRides(); 
    }

    @Override
    public int getRowCount() {
        return rides.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
            case 2: return Date.class;
            case 3: return Integer.class;
            case 4: return Double.class;
            default: return String.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        Ride ride = rides.get(rowIndex);
        switch (colIndex) {
            case 0: return ride.getFrom();
            case 1: return ride.getTo();
            case 2: return ride.getDate();
            case 3: return ride.getnPlaces();
            case 4: return ride.getPrice();
            default: return null;
        }
    }
}