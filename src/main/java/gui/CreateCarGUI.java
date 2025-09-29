package gui;

import java.text.DateFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.toedter.calendar.JCalendar;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Car;
import domain.Driver;
import domain.Ride;
import exceptions.CarAlreadyExistsException;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class CreateCarGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	
	private Driver driver;
	private JTextField fieldPlate=new JTextField();
	private JTextField fieldPlaces=new JTextField();
	
	private JLabel jLabelPlate = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateCarGUI.Plate"));
	private JLabel jLabelPlaces = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateCarGUI.Places")); 
	private Calendar calendarAct = null;
	private Calendar calendarAnt = null;

	private JScrollPane scrollPaneEvents = new JScrollPane();

	JRadioButton radioButtonDisabled =  new JRadioButton();
	private JButton jButtonCreate = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CreateCarGUI.CreateCar"));
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private JLabel jLabelError = new JLabel();
	private JComboBox carBox = new JComboBox();
	private DefaultComboBoxModel<Car> carInfo;
	
	private List<Date> datesWithEventsCurrentMonth;


	public CreateCarGUI(Driver driver) {

		this.driver=driver;
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(604, 370));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("CreateCarGUI.CreateCar"));

		jLabelPlate.setBounds(new Rectangle(6, 101, 92, 20));
		scrollPaneEvents.setBounds(new Rectangle(25, 44, 346, 116));

		jButtonCreate.setBounds(new Rectangle(100, 263, 130, 30));

		jButtonCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonCreate_actionPerformed(e);
			}
		});
		jButtonClose.setBounds(new Rectangle(275, 263, 130, 30));
		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClose_actionPerformed(e);
			}
		});

		jLabelError.setBounds(new Rectangle(6, 191, 320, 20));
		jLabelError.setForeground(Color.red);
		this.getContentPane().add(jLabelError, null);

		this.getContentPane().add(jButtonClose, null);
		this.getContentPane().add(jButtonCreate, null);
		this.getContentPane().add(jLabelPlate, null);

		BLFacade facade = MainGUI.getBusinessLogic();
		
		jLabelPlaces.setBounds(301, 103, 61, 16);
		getContentPane().add(jLabelPlaces);
		
		
		fieldPlate.setBounds(100, 99, 130, 26);
		getContentPane().add(fieldPlate);
		fieldPlate.setColumns(10);
		
		
		fieldPlaces.setBounds(396, 99, 123, 26);
		getContentPane().add(fieldPlaces);
		fieldPlaces.setColumns(10);
		
		radioButtonDisabled = new JRadioButton(ResourceBundle.getBundle("Etiquetas").getString("CreateCarGUI.radioButtonDisabled")); //$NON-NLS-1$ //$NON-NLS-2$
		radioButtonDisabled.setBounds(100, 164, 320, 21);
		getContentPane().add(radioButtonDisabled);
		
		carInfo = new DefaultComboBoxModel<Car>();
		for(Car c : driver.getCars()) {
			carInfo.addElement(c);
		}
		
	}	 
	private void jButtonCreate_actionPerformed(ActionEvent e) {
		jLabelError.setText("");
		String error=field_Errors();
		boolean dis = false;
		if(radioButtonDisabled.isSelected()) dis = true;
		if (error!=null) 
			jLabelError.setText(error);
		else
			try {
				BLFacade facade = MainGUI.getBusinessLogic();
				facade.addCarToDriver(driver.getEmail(), fieldPlate.getText(), Integer.parseInt(fieldPlaces.getText()), dis);
				jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateCarGUI.CreatedCar"));
				
			} catch (CarAlreadyExistsException e1) {
				// TODO Auto-generated catch block
				jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateCarGUI.CarAlreadyExistsException"));
			} 

		}
	

	private void jButtonClose_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
	private String field_Errors() {
		
		try {
			if ((fieldPlate.getText().length()==0) || (fieldPlaces.getText().length()==0))
				return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorQuery");
			else {
				int places = Integer.parseInt(fieldPlaces.getText());
				if (places <= 0) 
					return ResourceBundle.getBundle("Etiquetas").getString("CreateCarGUI.PlacesMustBeGreaterThan0");
				else 
					return null;

			}
		} catch (java.lang.NumberFormatException e1) {

			return  ResourceBundle.getBundle("Etiquetas").getString("CreateCarGUI.ErrorNumber");		
		} catch (Exception e1) {

			e1.printStackTrace();
			return null;

		}
	}
}
