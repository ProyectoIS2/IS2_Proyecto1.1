package gui;

import configuration.UtilDate;

import com.toedter.calendar.JCalendar;

import businesslogic.BLFacade;
import domain.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

import javax.swing.table.DefaultTableModel;


public class CheckReservationsGUI extends JFrame {
	private Driver driver;
	private static final long serialVersionUID = 1L;
	DefaultComboBoxModel<String> originLocations = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();
	private final JLabel jLabelEvents = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.Rides")); 
	private JLabel jLabelError = new JLabel("");

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private JButton jButtonDeny;
	private JButton jButtonAccept;
	
	private JScrollPane scrollPaneEvents = new JScrollPane();

	private JTable tableReservations= new JTable();

	private DefaultTableModel tableModelReservations;


	private String[] columnNamesRides = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("CheckReservationGUI.Traveler"), 
			ResourceBundle.getBundle("Etiquetas").getString("CheckReservationGUI.NPlaces"), 
			ResourceBundle.getBundle("Etiquetas").getString("CheckReservationGUI.Ride"),
			ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Ratings")
	};
	
	BLFacade facade = MainGUI.getBusinessLogic();
	
	private void updateReservations(List<Reservation> resList) {
		tableModelReservations = new DefaultTableModel(null, columnNamesRides) {
			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
		};
		if(resList.size()==0) {
			jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("ChechReservationsGUI.jLabelError"));
		}else {
			for (Reservation res: resList){
				Vector<Object> row = new Vector<Object>();
				row.add(res.getTraveler().getName());
				row.add(res.getHmTravelers());
				row.add(res.getRide().getFrom()+"-"+res.getRide().getTo());
				row.add(facade.getTravelerRatings(res.getTraveler().getEmail()));
				row.add(res); // ev object added in order to obtain it with tableModelEvents.getValueAt(i,3)
				tableModelReservations.addRow(row);		
			}
		}
		
		tableReservations.setModel(tableModelReservations);
	}


	public CheckReservationsGUI(Driver d)
	{
		driver=d;
		
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(700, 500));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("CheckReservationGUI.MainTitle"));
		jLabelEvents.setBounds(210, 62, 259, 16);
		this.getContentPane().add(jLabelEvents);
		jLabelEvents.setHorizontalAlignment(SwingConstants.CENTER);

		jButtonClose.setBounds(new Rectangle(274, 419, 130, 30));

		jButtonClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		BLFacade facade = MainGUI.getBusinessLogic();

		this.getContentPane().add(jButtonClose, null);

		scrollPaneEvents.setBounds(new Rectangle(139, 119, 398, 195));

		scrollPaneEvents.setViewportView(tableReservations);
		tableModelReservations = new DefaultTableModel(null, columnNamesRides);
		
		tableReservations.setModel(tableModelReservations);

		tableModelReservations.setDataVector(null, columnNamesRides);
		tableModelReservations.setColumnCount(5); // another column added to allocate ride objects

		tableReservations.getColumnModel().getColumn(0).setPreferredWidth(170);
		tableReservations.getColumnModel().getColumn(1).setPreferredWidth(30);
		tableReservations.getColumnModel().getColumn(1).setPreferredWidth(170);
		tableReservations.getColumnModel().getColumn(1).setPreferredWidth(30);

		tableReservations.getColumnModel().removeColumn(tableReservations.getColumnModel().getColumn(4)); // not shown in JTable

		this.getContentPane().add(scrollPaneEvents, null);

		jLabelError = new JLabel(""); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelError.setBounds(210, 77, 324, 30);
		getContentPane().add(jLabelError);
		jLabelError.setHorizontalAlignment(SwingConstants.CENTER);
		
		List<Reservation> resList = facade.getDriverReservations(driver.getEmail());
		this.updateReservations(resList);
		
		jButtonAccept = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CheckReservationsGUI.jButtonAccept")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!resList.get(tableReservations.getSelectedRow()).isAccepted()) {
						tableReservations.setEnabled(false);
						facade.updateReservation(resList.get(tableReservations.getSelectedRow()));
						List<Reservation> resList = facade.getDriverReservations(driver.getEmail());
						updateReservations(resList);
						tableReservations.setEnabled(true);
					}else {
						jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckReservationsGUI.jLabelError2"));
					}
				}catch(Exception ex) {
					
				}
			}
		});
		jButtonAccept.setBounds(210, 345, 100, 44);
		getContentPane().add(jButtonAccept);
		
		jButtonDeny = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CheckReservationsGUI.jButtonDeny")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonDeny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!(tableReservations.getSelectedRow()==-1)) {
						List<Reservation> resList = facade.getDriverReservations(driver.getEmail());
						if(!(resList.get(tableReservations.getSelectedRow()).isAccepted())) {
							tableReservations.setEnabled(false);
							facade.cancelReservation(resList.get(tableReservations.getSelectedRow()));
							resList = facade.getDriverReservations(driver.getEmail());
							updateReservations(resList);
							tableReservations.setEnabled(true);
						}else {
							jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckReservationsGUI.jLabelError2"));
						}
					}else {
						jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckReservationsGUI.jLabelError1"));
					}
				}catch(Exception exc) {
					
				}
			}
		});
		jButtonDeny.setBounds(369, 345, 100, 44);
		getContentPane().add(jButtonDeny);

	}
}
