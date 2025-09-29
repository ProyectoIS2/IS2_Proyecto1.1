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


public class CreateAlertGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JButton jButtonCreateQuery = null;
	private JButton jButtonQueryQueries = null;

    private static BLFacade appFacadeInterface;
	
	protected JLabel jLabelLogin;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton jButtonCreate;
	private JLabel jLabelFrom;
	private JLabel jLabelTo;
	private JTextField textFieldEmail;
	private JLabel jLabelError;
	private JTextField passwordField;
	private JButton jButtonBack;
	
	/**
	 * This is the default constructor
	 */
	public CreateAlertGUI(Traveler t) {
		super();
		
		// this.setSize(271, 295);
		this.setSize(495, 290);
		jLabelLogin = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateAlertGUI.MainTitle"));
		jLabelLogin.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelLogin.setForeground(Color.BLACK);
		jLabelLogin.setHorizontalAlignment(SwingConstants.CENTER);
		
		BLFacade facade = MainGUI.getBusinessLogic();
		
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
		
		jLabelFrom = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateAlertGUI.jLabelFrom")); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(jLabelFrom);
		jLabelFrom.setHorizontalAlignment(SwingConstants.CENTER);
		
		textFieldEmail = new JTextField();
		textFieldEmail.setText(""); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(textFieldEmail);
		
		jLabelTo = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateAlertGUI.jLabelTo")); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelTo.setHorizontalAlignment(SwingConstants.CENTER);
		jContentPane.add(jLabelTo);
		
		passwordField = new JTextField();
		passwordField.setText(""); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(passwordField);
		
		setContentPane(jContentPane);
		
		jLabelError = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateAlertGUI.jLabelError")); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(jLabelError);
		jLabelError.setHorizontalAlignment(SwingConstants.CENTER);
		
		jButtonCreate = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CreateAlertGUI.jButtonCreate")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String jatorria = textFieldEmail.getText();
					String helburua = passwordField.getText();
					if(jatorria==null) {
						jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateAlertGUI.FromIsNull"));
					}else if(helburua==null) {
						jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateAlertGUI.ToIsNull"));
					}else {
						facade.createAlert(t, jatorria, helburua);
						dispose();
					}
				}catch(AlertAlreadyExistsException u) {
					jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateAlertGUI.jLabelError1"));
				}
			}
		});
		jContentPane.add(jButtonCreate);
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("CreateAlertGUI.MainTitle"));
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}
	
	private void paintAgain() {
		jLabelLogin.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.Login"));
		jLabelFrom.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jLabelEmail"));
		jLabelTo.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jLabelPassword"));
		jButtonCreate.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.BLogin"));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.MainTitle"));
	}
	
} // @jve:decl-index=0:visual-constraint="0,0"

