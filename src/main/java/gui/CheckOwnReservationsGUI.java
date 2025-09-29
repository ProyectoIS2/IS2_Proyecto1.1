package gui;

import businessLogic.BLFacade;
import configuration.UtilDate;

import com.toedter.calendar.JCalendar;
import domain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import exceptions.*;

import javax.swing.table.DefaultTableModel;

import java.time.LocalDate;


public class CheckOwnReservationsGUI extends JFrame {
	private Traveler traveler;
	private static final long serialVersionUID = 1L;
	DefaultComboBoxModel<String> originLocations = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();
	private final JLabel jLabelEvents = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.Rides")); 
	private JLabel jLabelError = new JLabel("");

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private JButton jButtonDeny;
	private JButton jButtonPay;
	
	private JScrollPane scrollPaneEvents = new JScrollPane();

	private JTable tableReservations= new JTable();

	private DefaultTableModel tableModelReservations;

	private BLFacade facade;

	private String[] columnNamesRides = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("CheckReservationGUI.Traveler"), 
			ResourceBundle.getBundle("Etiquetas").getString("CheckReservationGUI.NPlaces"), 
			ResourceBundle.getBundle("Etiquetas").getString("CheckReservationGUI.Ride"),
			ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationGUI.Status")
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
				row.add(res.isAccepted());
				row.add(res); // ev object added in order to obtain it with tableModelEvents.getValueAt(i,3)
				tableModelReservations.addRow(row);		
			}
		}
		tableReservations.setModel(tableModelReservations);
	}


	public CheckOwnReservationsGUI(Traveler t)
	{
		traveler=t;
		
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
		facade = MainGUI.getBusinessLogic();

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

		tableReservations.getColumnModel().removeColumn(tableReservations.getColumnModel().getColumn(3)); // not shown in JTable

		this.getContentPane().add(scrollPaneEvents, null);

		jLabelError = new JLabel(""); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelError.setBounds(210, 77, 324, 30);
		getContentPane().add(jLabelError);
		jLabelError.setHorizontalAlignment(SwingConstants.CENTER);
		
		List<Reservation> resList = facade.getTravelerReservations(traveler.getEmail());
		this.updateReservations(resList);
		
		jButtonPay = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jButtonPay")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonPay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!(tableReservations.getSelectedRow()==-1)) {
					try {
						List<Reservation>  resList=facade.getTravelerReservations(traveler.getEmail());
						Reservation r = resList.get(tableReservations.getSelectedRow());
						if(r.isAccepted()){
							if(!r.isPayed()) {
								facade.pay(resList.get(tableReservations.getSelectedRow()));
								resList = facade.getTravelerReservations(traveler.getEmail());
								updateReservations(resList);
							}else {
								jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jLabelError5"));
							}
						}else {
							jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jLabelError3"));
						}
					}catch(NotEnoughMoneyException error) {
						jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jLabelError2"));
					}
					
				}else {
					jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckReservationsGUI.jLabelError1"));
				}
			}
		});
		jButtonPay.setBounds(42, 345, 100, 44);
		getContentPane().add(jButtonPay);
		
		jButtonDeny = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CheckReservationsGUI.jButtonCancel")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonDeny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!(tableReservations.getSelectedRow()==-1)) {
					if(!(resList.get(tableReservations.getSelectedRow()).isAccepted())) {
						tableReservations.setEnabled(false);
						facade.cancelReservation(resList.get(tableReservations.getSelectedRow()));
						List<Reservation> resList = facade.getTravelerReservations(traveler.getEmail());
						updateReservations(resList);
						tableReservations.setEnabled(true);
					}else {
						jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckReservationsGUI.jLabelError2"));
					}
				}else {
					jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckReservationsGUI.jLabelError1"));
				}
			}
		});
		jButtonDeny.setBounds(167, 345, 100, 44);
		getContentPane().add(jButtonDeny);
		
		JButton jButtonConfirm = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jButtonConfirm")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reservation r = resList.get(tableReservations.getSelectedRow());
				if(!r.isAccepted()) {
					jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jLabelError3"));
				}else {
					if(r.getRide().getDate().compareTo(new Date())<0) {
						facade.cancelReservation(r);
						jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.Confirmed"));
					}else {
						jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jLabelError6"));
					}
				}
			}
		});
		jButtonConfirm.setBounds(290, 345, 100, 44);
		getContentPane().add(jButtonConfirm);
		
		JButton jButtonComplaint = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jButtonComplaint")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonComplaint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reservation r = resList.get(tableReservations.getSelectedRow());
				if(r.isAccepted()) {
					if(r.isComplained()) {
						jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jLabelError7"));
					}else {
						JFrame a = new MakeComplaintGUI(r);
						a.setVisible(true);
						dispose();
					}
				}else {
					jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jLabelError3"));
				}
			}
		});
		jButtonComplaint.setBounds(400, 345, 105, 44);
		getContentPane().add(jButtonComplaint);
		
		JButton jButtonRate = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RateGUI.jButtonRate"));
		jButtonRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reservation r = resList.get(tableReservations.getSelectedRow());
				if(r.isAccepted()) {
					if(!r.isRatedT()) {
						dispose();
						JFrame a = new RateDriverGUI(r);
						a.setVisible(true);
					}else {
						jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jLabelError4"));
					}
				}else {
					jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnReservationsGUI.jLabelError3"));
				}
			}
		});
		jButtonRate.setBounds(515, 345, 100, 44);
		getContentPane().add(jButtonRate);
		
	}
}
