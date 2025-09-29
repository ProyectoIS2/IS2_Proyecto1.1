package gui;

import businessLogic.BLFacade;
import configuration.UtilDate;

import com.toedter.calendar.JCalendar;
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


public class DriverToRateGUI extends JFrame {
	private Driver driver;
	private static final long serialVersionUID = 1L;
	DefaultComboBoxModel<String> originLocations = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();
	private final JLabel jLabelEvents = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.Rides")); 
	private JLabel jLabelError = new JLabel("");

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private JButton jButtonRate;
	
	private JScrollPane scrollPaneEvents = new JScrollPane();

	private JTable tableReservations= new JTable();

	private DefaultTableModel tableModelReservations;


	private String[] columnNamesRides = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("CheckReservationGUI.Traveler"), 
			ResourceBundle.getBundle("Etiquetas").getString("CheckReservationGUI.NPlaces"), 
			ResourceBundle.getBundle("Etiquetas").getString("CheckReservationGUI.Ride")
	};
	
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
				row.add(res); // ev object added in order to obtain it with tableModelEvents.getValueAt(i,3)
				tableModelReservations.addRow(row);		
			}
		}
		
		tableReservations.setModel(tableModelReservations);
	}


	public DriverToRateGUI(Driver d)
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
		tableModelReservations.setColumnCount(4); // another column added to allocate ride objects

		tableReservations.getColumnModel().getColumn(0).setPreferredWidth(170);
		tableReservations.getColumnModel().getColumn(1).setPreferredWidth(30);
		tableReservations.getColumnModel().getColumn(1).setPreferredWidth(170);

		tableReservations.getColumnModel().removeColumn(tableReservations.getColumnModel().getColumn(3)); // not shown in JTable

		this.getContentPane().add(scrollPaneEvents, null);

		jLabelError = new JLabel(""); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelError.setBounds(210, 77, 324, 30);
		getContentPane().add(jLabelError);
		jLabelError.setHorizontalAlignment(SwingConstants.CENTER);
		
		List<Reservation> resList = facade.getDriverReservations(driver.getEmail());
		this.updateReservations(resList);
		
		jButtonRate = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverToRateGUI.jButtonRate")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int selectedRow = tableReservations.getSelectedRow();
					if(!(selectedRow==-1)) {
						Reservation r = resList.get(selectedRow);
						if(r.isAccepted()) {
							if(!r.getRatedD()) {
								dispose();
								JFrame a = new RateTravelerGUI(r);
								a.setVisible(true);
							}else {
								jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jLabelError4"));
							}
						}else {
							jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jLabelError3"));
						}
					}else {
						jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckReservationsGUI.jLabelError1"));
					}
				}catch(Exception exc) {
					System.out.println("huebo");
				}
			}
		});
		jButtonRate.setBounds(289, 353, 100, 44);
		getContentPane().add(jButtonRate);

	}
}
