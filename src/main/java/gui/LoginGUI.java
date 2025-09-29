package gui;

/**
 * @author Software Engineering teachers
 */


import javax.swing.*;

import domain.*;
import exceptions.*;
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


public class LoginGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JButton jButtonCreateQuery = null;
	private JButton jButtonQueryQueries = null;

    private static BLFacade appFacadeInterface;
	
	protected JLabel jLabelLogin;
	private JRadioButton RadioButtonTraveler;
	private JRadioButton RadioButtonDriver;
	private JPanel panel;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton jButtonLogin;
	private JLabel jLabelEmail;
	private JLabel jLabelPassword;
	private JTextField textFieldEmail;
	private JLabel jLabelError;
	private JPasswordField passwordField;
	private JButton jButtonBack;
	private JRadioButton radioButtonAdmin;
	
	/**
	 * This is the default constructor
	 */
	public LoginGUI() {
		super();
		
		// this.setSize(271, 295);
		this.setSize(495, 290);
		jLabelLogin = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Login"));
		jLabelLogin.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelLogin.setForeground(Color.BLACK);
		jLabelLogin.setHorizontalAlignment(SwingConstants.CENTER);
		
		BLFacade facade = MainGUI.getBusinessLogic();
		
		RadioButtonTraveler = new JRadioButton(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.Traveler"));
		RadioButtonTraveler.setSelected(true);
		buttonGroup.add(RadioButtonTraveler);
		
		RadioButtonDriver = new JRadioButton(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.Driver"));
		buttonGroup.add(RadioButtonDriver);
		
		radioButtonAdmin = new JRadioButton("Admin"); //$NON-NLS-1$ //$NON-NLS-2$
		buttonGroup.add(radioButtonAdmin);

	
		panel = new JPanel();
		panel.add(RadioButtonTraveler);
		panel.add(RadioButtonDriver);
		panel.add(radioButtonAdmin);
		
		jContentPane = new JPanel();
		jContentPane.setLayout(new GridLayout(5, 2, 0, 0));
		jContentPane.add(jLabelLogin);
		
		jButtonBack = new JButton(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jButtonBack")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		jContentPane.add(jButtonBack);
		
		jLabelEmail = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jLabelEmail")); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(jLabelEmail);
		jLabelEmail.setHorizontalAlignment(SwingConstants.CENTER);
		
		textFieldEmail = new JTextField();
		textFieldEmail.setText(""); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(textFieldEmail);
		textFieldEmail.setColumns(10);
		
		jLabelPassword = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jLabelPassword")); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelPassword.setHorizontalAlignment(SwingConstants.CENTER);
		jContentPane.add(jLabelPassword);
		
		passwordField = new JPasswordField();
		passwordField.setText(""); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(passwordField);
		jContentPane.add(panel);
		
		
		
		
		setContentPane(jContentPane);
		
		jButtonLogin = new JButton(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.BLogin")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(RadioButtonDriver.isSelected()) {
						Driver d = facade.getDriverByEmail(textFieldEmail.getText(), new String(passwordField.getPassword()));
						JFrame a = new DriverGUI(d);
						a.setVisible(true);
						dispose();
					}else if (RadioButtonTraveler.isSelected()){
						Traveler t = facade.getTravelerByEmail(textFieldEmail.getText(), new String(passwordField.getPassword()));
						JFrame a = new TravelerGUI(t);
						a.setVisible(true);
						dispose();
					}else {
						Admin ad = facade.getAdminByEmail(textFieldEmail.getText(), new String(passwordField.getPassword()));
						JFrame a = new AdminGUI(ad);
						a.setVisible(true);
						dispose();
					}
				}catch(UserDoesNotExistException u) {
					jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.UserDoesNotExist"));
				}catch(PasswordDoesNotMatchException p) {
					jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.PasswordDoesNotMatch"));
				}
			}
		});
		jContentPane.add(jButtonLogin);
		
		jLabelError = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.jLabelError")); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(jLabelError);
		jLabelError.setHorizontalAlignment(SwingConstants.CENTER);
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.MainTitle"));
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}
	
	private void paintAgain() {
		jLabelLogin.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Login"));
		jLabelEmail.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jLabelEmail"));
		jLabelPassword.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jLabelPassword"));
		jButtonLogin.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.BLogin"));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.MainTitle"));
	}
	
} // @jve:decl-index=0:visual-constraint="0,0"

