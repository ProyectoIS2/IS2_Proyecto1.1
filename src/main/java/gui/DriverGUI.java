package gui;

/**
 * @author Software Engineering teachers
 */


import javax.swing.*;

import businesslogic.BLFacade;
import domain.Driver;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.FlowLayout;


public class DriverGUI extends JFrame {
	
    private Driver driver;
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JButton jButtonCreateQuery = null;
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
	private JButton jButtonTakeMoney;
	private JButton jButtonLogOut;
	private JButton jButtonCheckReservations;
	private JButton jButtonCreateCar;
	private JButton jButtonCheckTrans;
	private JButton jButtonCheckOwnRides;
	private JButton jButtonCheckProfile;
	private JButton jButtonDeleteAccount;
	private JButton jButtonCehckComplaint;
	private JButton jButtonCheckRatings;
	private JButton jButtonCheckComplaint;
	private JButton jButtonRate;
	private JButton jButtonComplaint;
	
	/**
	 * This is the default constructor
	 */
	public DriverGUI(Driver d) {
		super();
		BLFacade facade = MainGUI.getBusinessLogic();

		driver=d;
		
		
		// this.setSize(271, 295);
		this.setSize(495, 290);
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("DriverAndTravelerGUI.Welcome")+" "+driver.getName());
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
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(rdbtnNewRadioButton_1);
		panel.add(rdbtnNewRadioButton_2);
		panel.add(rdbtnNewRadioButton);
		
		jButtonCreateQuery = new JButton();
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.CreateRide"));
		jButtonCreateQuery.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if(facade.getDriverCars(driver.getEmail()).size()!=0) {
					JFrame a = new CreateRideGUI(driver);
					a.setVisible(true);
				}else {
					jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.NoCarError"));
				}
			}
		});
		
		jButtonQueryQueries = new JButton();
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QueryRides"));
		jButtonQueryQueries.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
					JFrame a = new FindRidesGUI();
					a.setVisible(true);
			}
		});
		
		jContentPane = new JPanel();
		jContentPane.setLayout(new GridLayout(8, 2, 0, 0));
		jContentPane.add(jLabelSelectOption);
		jContentPane.add(jButtonCreateQuery);
		jContentPane.add(jButtonQueryQueries);
		
		jButtonTakeMoney = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.JButtonTakeMoney")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonTakeMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new TakeMoneyGUI(driver);
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonTakeMoney);
		
		jButtonCheckReservations = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.JButtonCheckReservations")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonCheckReservations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new CheckReservationsGUI(driver);
				a.setVisible(true);
			}
		});
		
		jButtonCreateCar = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonCreateCar")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonCreateCar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new CreateCarGUI(driver);
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonCreateCar);
		
		jButtonCheckOwnRides = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonCheckOwnRides")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonCheckOwnRides.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new CheckOwnRidesGUI(driver);
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonCheckOwnRides);
		jContentPane.add(jButtonCheckReservations);
		
		jButtonCheckTrans = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverAndTravelerGUI.jButtonCheckTrans")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonCheckTrans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new CheckTransactionsGUI(facade.getDriverTransactions(driver.getEmail()));
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonCheckTrans);
		
		jButtonDeleteAccount = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonDeleteAccount")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonDeleteAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				facade.deleteAccountDriver(driver.getEmail());
				dispose();
			}
		});
		jContentPane.add(jButtonDeleteAccount);
		
		
		
		jButtonCheckRatings = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonCheckRatings")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonCheckRatings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new CheckOwnRatingsGUI(driver.getRatings());
				a.setVisible(true);
			}
		});
		
		jButtonLogOut = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverAndTravelerGUI.JButtonLogOut")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		jContentPane.add(jButtonLogOut);
		jContentPane.add(jButtonCheckRatings);
		
		jButtonCheckComplaint = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonCheckComplaint")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonCheckComplaint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new CheckComplaintGUI(driver.getEmail());
				a.setVisible(true);
			}
		});
		
		jButtonRate = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonRate")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new DriverToRateGUI(d);
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonRate);
		jContentPane.add(jButtonCheckComplaint);
		jContentPane.add(panel);
		
		setContentPane(jContentPane);
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") + " - driver :"+driver.getName());
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}
	
	private void paintAgain() {
		jButtonCheckComplaint.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonCheckComplaint"));
		jButtonCheckRatings.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonCheckRatings"));
		jButtonDeleteAccount.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonDeleteAccount"));
		jButtonCreateCar.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonCreateCar"));
		jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverAndTravelerGUI.Welcome")+" "+driver.getName());
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QueryRides"));
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.CreateRide"));
		jButtonRate.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonRate"));
		jButtonTakeMoney.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.JButtonTakeMoney"));
		jButtonLogOut.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverAndTravelerGUI.JButtonLogOut"));
		jButtonCheckReservations.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.JButtonCheckReservations"));
		jButtonCheckTrans.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverAndTravelerGUI.jButtonCheckTrans"));
		jButtonCheckOwnRides.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.jButtonCheckOwnRides"));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle")+ " - driver :"+driver.getName());
	}
	
} // @jve:decl-index=0:visual-constraint="0,0"

