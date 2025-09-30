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


public class CheckOwnRidesGUI extends JFrame {
	private Driver driver;
	private static final long serialVersionUID = 1L;
	DefaultComboBoxModel<String> originLocations = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();
	private final JLabel jLabelEvents = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.Rides")); 
	private JLabel jLabelError = new JLabel("");

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private JButton jButtonDeny;
	
	private JScrollPane scrollPaneEvents = new JScrollPane();

	private JTable tableReservations= new JTable();

	private DefaultTableModel tableModelReservations;


	private String[] columnNamesRides = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("CheckTransactionGUI.From"), 
			ResourceBundle.getBundle("Etiquetas").getString("CheckTransactionGUI.To"), 
			ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.RideDate")
	};
	
	private void updateReservations(List<Ride> resList) {
		tableModelReservations = new DefaultTableModel(null, columnNamesRides) {
			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
		};
		if(resList.size()==0) {
			jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.NoRides"));
		}else {
			for (Ride res: resList){
				Vector<Object> row = new Vector<Object>();
				row.add(res.getFrom());
				row.add(res.getTo());
				row.add(res.getDate().toString());
				row.add(res); // ev object added in order to obtain it with tableModelEvents.getValueAt(i,3)
				tableModelReservations.addRow(row);		
			}
		}
		
		tableReservations.setModel(tableModelReservations);
	}


	public CheckOwnRidesGUI(Driver d)
	{
		driver=d;
		
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(700, 500));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnRidesGUI.maintitle"));
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
		
		List<Ride> resList = facade.getDriverRides(driver.getEmail());
		this.updateReservations(resList);
		
		jButtonDeny = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnRidesGUI.jButtonDeny")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonDeny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!(tableReservations.getSelectedRow()==-1)) {
					List<Ride> resList = facade.getDriverRides(driver.getEmail());
					tableReservations.setEnabled(false);
					facade.removeRideDriver(resList.get(tableReservations.getSelectedRow()).getRideNumber(), driver.getEmail());;
					resList = facade.getDriverRides(driver.getEmail());
					updateReservations(resList);
					tableReservations.setEnabled(true);
				}else {
					jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnRidesGUI.jLabelError1"));
				}
			}
		});
		jButtonDeny.setBounds(168, 345, 319, 44);
		getContentPane().add(jButtonDeny);

	}
}
