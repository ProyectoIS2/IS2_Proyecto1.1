package gui;

/**
 * @author Software Engineering teachers
 */


import javax.swing.*;

import businesslogic.BLFacade;
import domain.*;
import exceptions.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class RegisterGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JButton jButtonCreateQuery = null;
	private JButton jButtonQueryQueries = null;

    private static BLFacade appFacadeInterface;
	
	protected JLabel jLabelRegister;
	private JRadioButton RadioButtonTraveler;
	private JRadioButton RadioButtonDriver;
	private JPanel panel;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton jButtonRegister;
	private JLabel jLabelEmail;
	private JLabel jLabelPassword;
	private JTextField textFieldEmail;
	private JLabel jLabelError;
	private JPasswordField passwordField;
	private JButton jButtonBack;
	private JLabel jLabelName;
	private JTextField textFieldName;
	private JLabel jLabelRPassword;
	private JPasswordField passwordFieldR;
	
	/**
	 * This is the default constructor
	 */
	public RegisterGUI() {
		super();
		
		// this.setSize(271, 295);
		this.setSize(495, 290);
		jLabelRegister = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Register"));
		jLabelRegister.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelRegister.setForeground(Color.BLACK);
		jLabelRegister.setHorizontalAlignment(SwingConstants.CENTER);
		
		BLFacade facade = MainGUI.getBusinessLogic();
		
		RadioButtonTraveler = new JRadioButton(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.Traveler"));
		RadioButtonTraveler.setSelected(true);
		buttonGroup.add(RadioButtonTraveler);
		
		RadioButtonDriver = new JRadioButton(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.Driver"));
		buttonGroup.add(RadioButtonDriver);
	
		panel = new JPanel();
		panel.add(RadioButtonTraveler);
		panel.add(RadioButtonDriver);
		
		jContentPane = new JPanel();
		jContentPane.setLayout(new GridLayout(7, 2, 0, 0));
		jContentPane.add(jLabelRegister);
		
		jButtonBack = new JButton(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jButtonBack")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		jContentPane.add(jButtonBack);
		
		jLabelName = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.jLabelName")); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelName.setHorizontalAlignment(SwingConstants.CENTER);
		jContentPane.add(jLabelName);
		
		textFieldName = new JTextField();
		textFieldName.setText(""); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(textFieldName);
		textFieldName.setColumns(10);
		
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
		
		jLabelRPassword = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.jLabelRPassword")); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelRPassword.setHorizontalAlignment(SwingConstants.CENTER);
		jContentPane.add(jLabelRPassword);
		
		passwordFieldR = new JPasswordField();
		passwordFieldR.setText(""); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(passwordFieldR);
		jContentPane.add(panel);
		
		
		setContentPane(jContentPane);
		
		jButtonRegister = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.BRegister")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(new String(passwordField.getPassword()).equals(new String(passwordFieldR.getPassword()))) {
						if(RadioButtonDriver.isSelected()) {
							Driver d = facade.createDriver(textFieldEmail.getText(), textFieldName.getText(), new String(passwordField.getPassword()));
							JFrame a = new DriverGUI(d);
							a.setVisible(true);
							dispose();
						}else {
							Traveler t = facade.createTraveler(textFieldEmail.getText(), textFieldName.getText(), new String(passwordField.getPassword()));
							JFrame a = new TravelerGUI(t);
							a.setVisible(true);
							dispose();
						}
					}else {
						jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.PasswordsNotMatch"));
					}
				}catch(UserAlreadyExistException u) {
					jLabelError.setText(u.getMessage());
				}
			}
		});
		jContentPane.add(jButtonRegister);
		
		jLabelError = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.jLabelError")); //$NON-NLS-1$ //$NON-NLS-2$
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
		jLabelRegister.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Register"));
		jLabelEmail.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jLabelEmail"));
		jLabelPassword.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jLabelPassword"));
		jButtonRegister.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.BRegister"));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.MainTitle"));
	}
	
} // @jve:decl-index=0:visual-constraint="0,0"

