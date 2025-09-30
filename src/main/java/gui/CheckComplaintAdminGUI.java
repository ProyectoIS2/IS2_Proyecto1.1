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


public class CheckComplaintAdminGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	DefaultComboBoxModel<String> originLocations = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();
	private final JLabel jLabelEvents = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CheckComplaintGUI.maintitle")); 
	private JLabel jLabelError = new JLabel("");

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	
	private JScrollPane scrollPaneEvents = new JScrollPane();

	private JTable tableReservations= new JTable();

	private DefaultTableModel tableModelReservations;


	private String[] columnNamesRides = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("CheckComplaintGUI.Who"), 
			ResourceBundle.getBundle("Etiquetas").getString("CheckComplaintGUI.What"), 
	};
	
	private void updateComplaint(List<Complaint> comList) {
		tableModelReservations = new DefaultTableModel(null, columnNamesRides) {
			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
		};
		if(comList.size()==0) {
			jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckComplaintGUI.NoComplaint"));
		}else {
			for (Complaint com: comList){
				Vector<Object> row = new Vector<Object>();
				row.add(com.getTravelerName());
				row.add(com.getRide().getFrom()+" - "+com.getRide().getTo());
				row.add(com); // ev object added in order to obtain it with tableModelEvents.getValueAt(i,3)
				tableModelReservations.addRow(row);		
			}
		}
		
		tableReservations.setModel(tableModelReservations);
	}


	public CheckComplaintAdminGUI()
	{
		
		BLFacade facade = MainGUI.getBusinessLogic();
		
		List<Complaint> comList = facade.getAllComplaint();
		
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(700, 500));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("CheckComplaintGUI.maintitle"));
		jLabelEvents.setBounds(210, 62, 259, 16);
		this.getContentPane().add(jLabelEvents);
		jLabelEvents.setHorizontalAlignment(SwingConstants.CENTER);

		jButtonClose.setBounds(new Rectangle(529, 423, 130, 30));

		jButtonClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		
		this.getContentPane().add(jButtonClose, null);

		scrollPaneEvents.setBounds(new Rectangle(24, 119, 652, 195));

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
		jLabelError.setBounds(91, 77, 488, 30);
		getContentPane().add(jLabelError);
		jLabelError.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel jLabelComplaint = new JLabel(""); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelComplaint.setBounds(24, 327, 652, 94);
		getContentPane().add(jLabelComplaint);
		
		JButton jButtonMoreInfo = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CheckComplaintGUI.jButtonMoreInfo")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonMoreInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Complaint com = facade.getAllComplaint().get(tableReservations.getSelectedRow());
				jLabelComplaint.setText(com.getComplaint());
			}
		});
		jButtonMoreInfo.setBounds(75, 425, 94, 26);
		getContentPane().add(jButtonMoreInfo);
		
		JButton jButtonAccept = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CheckReservationsGUI.jButtonAccept")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Complaint> comList = facade.getAllComplaint();;
				Complaint c = comList.get(tableReservations.getSelectedRow());
				facade.acceptComplaint(c);
				comList = facade.getAllComplaint();;
				updateComplaint(comList);
			}
		});
		jButtonAccept.setBounds(223, 426, 94, 25);
		getContentPane().add(jButtonAccept);
		
		JButton jButtonDeny = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CheckReservationsGUI.jButtonDeny")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonDeny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Complaint> comList = facade.getAllComplaint();
				Complaint c = comList.get(tableReservations.getSelectedRow());
				facade.denyComplaintAdmin(c);
			}
		});
		jButtonDeny.setBounds(368, 423, 101, 26);
		getContentPane().add(jButtonDeny);
		
		this.updateComplaint(comList);
	}
}
