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


public class MainGUI extends JFrame {
	
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
	protected JLabel jLabelWelcome;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton_2;
	private JPanel panel;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton jButtonLogin;
	private JButton jButtonRegister;
	
	/**
	 * This is the default constructor
	 */
	public MainGUI() {
		super();
		
		// this.setSize(271, 295);
		this.setSize(495, 290);
		jLabelWelcome = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Welcome"));
		jLabelWelcome.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelWelcome.setForeground(Color.BLACK);
		jLabelWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		
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
				JFrame a = new FindRidesGUI();

				a.setVisible(true);
			}
		});
		
		jContentPane = new JPanel();
		jContentPane.setLayout(new GridLayout(5, 1, 0, 0));
		jContentPane.add(jLabelWelcome);
		jContentPane.add(jButtonQueryQueries);
		
		jButtonLogin = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Login")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new LoginGUI();
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonLogin);
		
		jButtonRegister = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Register")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new RegisterGUI();
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonRegister);
		jContentPane.add(panel);
		
		
		setContentPane(jContentPane);
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle"));
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}
	
	private void paintAgain() {
		jLabelWelcome.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Welcome"));
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QueryRides"));
		jButtonLogin.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Login"));
		jButtonRegister.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.Register"));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle"));
	}
	
}

