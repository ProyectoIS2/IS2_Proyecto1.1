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


public class RateDriverGUI extends JFrame {
	private Reservation r;
	private static final long serialVersionUID = 1L;
	
	private final JLabel jLabelEvents = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RateDriverGUI.maintitle")); 
	private JLabel jLabelError = new JLabel("");

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));

	private final ButtonGroup buttonGroup = new ButtonGroup();

	public RateDriverGUI(Reservation r)
	{
		this.r=r;
		
		JRadioButton radioButtonOne = new JRadioButton("1"); //$NON-NLS-1$ //$NON-NLS-2$
		buttonGroup.add(radioButtonOne);
		radioButtonOne.setBounds(60, 214, 103, 21);
		getContentPane().add(radioButtonOne);
		
		JRadioButton radioButtonTwo = new JRadioButton("2");
		buttonGroup.add(radioButtonTwo);
		radioButtonTwo.setBounds(165, 214, 103, 21);
		getContentPane().add(radioButtonTwo);
		
		JRadioButton radioButtonThree = new JRadioButton("3");
		buttonGroup.add(radioButtonThree);
		radioButtonThree.setBounds(271, 214, 103, 21);
		getContentPane().add(radioButtonThree);
		
		JRadioButton radioButtonFour = new JRadioButton("4");
		buttonGroup.add(radioButtonFour);
		radioButtonFour.setBounds(381, 214, 103, 21);
		getContentPane().add(radioButtonFour);
		
		JRadioButton radioButtonFive = new JRadioButton("5");
		buttonGroup.add(radioButtonFive);
		radioButtonFive.setBounds(495, 214, 103, 21);
		getContentPane().add(radioButtonFive);
		
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(700, 500));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("RateGUI.jButtonRate"));
		jLabelEvents.setBounds(210, 62, 259, 16);
		this.getContentPane().add(jLabelEvents);
		jLabelEvents.setHorizontalAlignment(SwingConstants.CENTER);

		jButtonClose.setBounds(new Rectangle(404, 423, 130, 30));

		jButtonClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFrame a = new CheckOwnReservationsGUI(r.getTraveler());
				a.setVisible(true);
				dispose();
			}
		});
		BLFacade facade = MainGUI.getBusinessLogic();

		this.getContentPane().add(jButtonClose, null);

		jLabelError = new JLabel(""); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelError.setBounds(210, 77, 324, 30);
		getContentPane().add(jLabelError);
		jLabelError.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton jButtonRate = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RateGUI.jButtonRate")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(radioButtonOne.isSelected()) {
					facade.addRatingToDriver(r.getDriver().getEmail(), 1, r.getReservationCode());
				}else if(radioButtonTwo.isSelected()) {
					facade.addRatingToDriver(r.getDriver().getEmail(), 2, r.getReservationCode());
				}else if(radioButtonThree.isSelected()) {
					facade.addRatingToDriver(r.getDriver().getEmail(), 3, r.getReservationCode());
				}else if(radioButtonFour.isSelected()) {
					facade.addRatingToDriver(r.getDriver().getEmail(), 4, r.getReservationCode());
				}else if(radioButtonFive.isSelected()) {
					facade.addRatingToDriver(r.getDriver().getEmail(), 5, r.getReservationCode());
				}else {
					jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("RateGUI.jLabelError"));
				}
				JFrame a = new CheckOwnReservationsGUI(r.getTraveler());
				a.setVisible(true);
				dispose();
			}
		});
		jButtonRate.setBounds(200, 425, 94, 26);
		getContentPane().add(jButtonRate);
	}
}
