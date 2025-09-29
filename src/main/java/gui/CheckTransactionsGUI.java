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
import java.util.*;
import java.util.List;
import exceptions.*;

import javax.swing.table.DefaultTableModel;


public class CheckTransactionsGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	DefaultComboBoxModel<String> originLocations = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();
	private final JLabel jLabelEvents = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CheckTransactionsGUI.MainTitle")); 
	private JLabel jLabelError = new JLabel("");

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	
	private JScrollPane scrollPaneEvents = new JScrollPane();

	private JTable tableReservations= new JTable();

	private DefaultTableModel tableModelReservations;
	private List<Transaction> transactionList = new Vector<Transaction>();


	private String[] columnNamesRides = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("CheckTransactionGUI.From"), 
			ResourceBundle.getBundle("Etiquetas").getString("CheckTransactionGUI.To"), 
			ResourceBundle.getBundle("Etiquetas").getString("CheckTransactionGUI.Amount")
	};


	public CheckTransactionsGUI(List<Transaction> transactions)
	{
		transactionList.addAll(transactions);
		
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(700, 500));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("CheckTransactionsGUI.MainTitle"));
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
		
		jLabelError = new JLabel(""); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelError.setBounds(210, 77, 324, 30);
		getContentPane().add(jLabelError);
		jLabelError.setHorizontalAlignment(SwingConstants.CENTER);
		
		BLFacade facade = MainGUI.getBusinessLogic();

		this.getContentPane().add(jButtonClose, null);

		scrollPaneEvents.setBounds(new Rectangle(139, 119, 398, 195));

		scrollPaneEvents.setViewportView(tableReservations);
		
		tableModelReservations = new DefaultTableModel(null, columnNamesRides) {
			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
		};

		tableReservations.setModel(tableModelReservations);

		tableModelReservations.setDataVector(null, columnNamesRides);
		tableModelReservations.setColumnCount(4); // another column added to allocate ride objects

		tableReservations.getColumnModel().getColumn(0).setPreferredWidth(170);
		tableReservations.getColumnModel().getColumn(1).setPreferredWidth(30);
		tableReservations.getColumnModel().getColumn(1).setPreferredWidth(170);

		tableReservations.getColumnModel().removeColumn(tableReservations.getColumnModel().getColumn(3)); // not shown in JTable

		this.getContentPane().add(scrollPaneEvents, null);
		
		if(transactionList.size()==0) {
			jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CheckTransactionsGUI.jLabelError"));
		}else {
			for (Transaction tran: transactionList){
				Vector<Object> row = new Vector<Object>();
				switch (tran.getType()) {
				case 1:
					row.add(tran.getD().getName());
					row.add(ResourceBundle.getBundle("Etiquetas").getString("CheckTransactionsGUI.Bank"));
					break;
				case 2:
					row.add(ResourceBundle.getBundle("Etiquetas").getString("CheckTransactionsGUI.Bank"));
					row.add(tran.getT().getName());
					break;
				case 3:
					row.add(tran.getT().getName());
					row.add(tran.getD().getName());
					break;
				case 4:
					row.add(tran.getD().getName());
					row.add(tran.getT().getName());
				}
				row.add(tran.getAmount());
				tableModelReservations.addRow(row);		
			}
		}
	}
}
