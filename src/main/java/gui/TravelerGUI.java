package gui;

/**
 * @author Software Engineering teachers
 */


import javax.swing.*;

import domain.*;
import businessLogic.BLFacade;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class TravelerGUI extends JFrame {
	
    private Traveler traveler;
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JButton jButtonQueryQueries = null;

    private static BLFacade appFacadeInterface;
	
	public static BLFacade getBusinessLogic(){
		return appFacadeInterface;
	}
	 
	public static void setBussinessLogic (BLFacade afi){
		appFacadeInterface=afi;
	}
	protected JLabel jLabelSelectOption;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton_2;
	private JPanel panel;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton jButtonPutMoney;
	private JButton jButtonLogOut;
	private JButton jButtonCheckReservations;
	private JButton jButtonCheckTrans;
	private JButton jButtonDeleteAccount;
	private JButton jButtonCheckOwnRatings;
	private JButton jButtonCheckAlerts;
	private JButton jButtonCreateAlert;
	
	/**
	 * This is the default constructor
	 */
	public TravelerGUI(Traveler t) {
		super();

		this.traveler=t;
		
		BLFacade facade = MainGUI.getBusinessLogic();
		// this.setSize(271, 295);
		this.setSize(495, 290);
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("DriverAndTravelerGUI.Welcome")+" "+traveler.getName());
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelSelectOption.setForeground(Color.BLACK);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		
		rdbtnNewRadioButton = new JRadioButton("English");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("en"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();				}
		});
		buttonGroup.add(rdbtnNewRadioButton);
		
		rdbtnNewRadioButton_1 = new JRadioButton("Euskara");
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("eus"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();				}
		});
		buttonGroup.add(rdbtnNewRadioButton_1);
		
		rdbtnNewRadioButton_2 = new JRadioButton("Castellano");
		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("es"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();
			}
		});
		buttonGroup.add(rdbtnNewRadioButton_2);
	
		panel = new JPanel();
		panel.add(rdbtnNewRadioButton_1);
		panel.add(rdbtnNewRadioButton_2);
		panel.add(rdbtnNewRadioButton);
		
		jButtonQueryQueries = new JButton();
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QueryRides"));
		jButtonQueryQueries.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new FindAndBookRidesGUI(traveler);
				a.setVisible(true);
			}
		});
		
		jContentPane = new JPanel();
		jContentPane.setLayout(new GridLayout(6, 2, 0, 0));
		jContentPane.add(jLabelSelectOption);
		jContentPane.add(jButtonQueryQueries);
		
		jButtonPutMoney = new JButton(ResourceBundle.getBundle("Etiquetas").getString("TravelerGUI.JButtonPutMoney")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonPutMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new PutMoneyGUI(traveler);
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonPutMoney);
		
		jButtonLogOut = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverAndTravelerGUI.JButtonLogOut")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		jButtonCheckReservations = new JButton(ResourceBundle.getBundle("Etiquetas").getString("TravelerGUI.JButtonCheckReservations")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonCheckReservations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new CheckOwnReservationsGUI(traveler);
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonCheckReservations);
		
		jButtonCheckTrans = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverAndTravelerGUI.jButtonCheckTrans")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonCheckTrans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new CheckTransactionsGUI(facade.getTravelerTransactions(traveler.getEmail()));
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonCheckTrans);
		
		jButtonDeleteAccount = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonDeleteAccount")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonDeleteAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				facade.deleteAccountTraveler(traveler.getEmail());
				dispose();
			}
		});
		jContentPane.add(jButtonDeleteAccount);
		
		jButtonCheckOwnRatings = new JButton(ResourceBundle.getBundle("Etiquetas").getString("TravelerGUI.jButtonCheckOwnRatings")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonCheckOwnRatings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new CheckOwnRatingsGUI(traveler.getRatings());
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonCheckOwnRatings);
		jContentPane.add(jButtonLogOut);
		
		jButtonCheckAlerts = new JButton(ResourceBundle.getBundle("Etiquetas").getString("TravelerGUI.jButtonCheckAlerts")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonCheckAlerts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new CheckMyAlertsGUI(t);
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonCheckAlerts);
		
		jButtonCreateAlert = new JButton(ResourceBundle.getBundle("Etiquetas").getString("TravelerGUI.jButtonCreateAlert")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonCreateAlert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new CreateAlertGUI(t);
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonCreateAlert);
		jContentPane.add(panel);
		
		
		setContentPane(jContentPane);
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") + " - traveler :"+ traveler.getName());
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}
	
	private void paintAgain() {
		jButtonDeleteAccount.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonDeleteAccount"));
		jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverAndTravelerGUI.Welcome")+" "+traveler.getName());
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QueryRides"));
		jButtonPutMoney.setText(ResourceBundle.getBundle("Etiquetas").getString("TravelerGUI.JButtonPutMoney"));
		jButtonLogOut.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverAndTravelerGUI.JButtonLogOut"));
		jButtonCheckReservations.setText(ResourceBundle.getBundle("Etiquetas").getString("TravelerGUI.JButtonCheckReservations"));
		jButtonCheckTrans.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverAndTravelerGUI.jButtonCheckTrans"));
		jButtonCheckAlerts.setText(ResourceBundle.getBundle("Etiquetas").getString("TravelerGUI.jButtonCheckAlerts"));
		jButtonCreateAlert.setText(ResourceBundle.getBundle("Etiquetas").getString("TravelerGUI.jButtonCreateAlert")); 
		jButtonCheckOwnRatings.setText(ResourceBundle.getBundle("Etiquetas").getString("TravelerGUI.jButtonCheckOwnRatings"));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle")+ " - traveler :"+traveler.getName());
	}
	
} // @jve:decl-index=0:visual-constraint="0,0"

