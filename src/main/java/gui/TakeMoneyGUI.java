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


public class TakeMoneyGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private Driver driver;
	
	private JPanel jContentPane = null;
	private JButton jButtonCreateQuery = null;
	private JButton jButtonQueryQueries = null;

    private static BLFacade appFacadeInterface;
	
	protected JLabel jLabelPutMoney;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton jButtonTake;
	private JLabel jLabelExpireDate;
	private JLabel jLabelCVC;
	private JTextField textFieldExpireDate;
	private JLabel jLabelError;
	private JButton jButtonBack;
	private JLabel jLabelCardNumber;
	private JTextField textFieldCardNumber;
	private JLabel jLabelName;
	private JTextField jTextFieldCVC;
	private JTextField jTextFieldName;
	private JLabel jLabelMoneyAmount;
	private JTextField jTextFieldMoneyAmount;
	
	/**
	 * This is the default constructor
	 */
	public TakeMoneyGUI(Driver d) {
		super();
		BLFacade facade = MainGUI.getBusinessLogic();
		driver=d;
		Driver dr;
		try {
			dr = facade.getDriverByEmail(d.getEmail(), d.getPassword());
		}catch(Exception e) {
			dr = d;
		}
		
		// this.setSize(271, 295);
		this.setSize(495, 290);
		jLabelPutMoney = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("TakeMoneyGUI.MainTitle")+dr.getMoney()+" €");
		jLabelPutMoney.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelPutMoney.setForeground(Color.BLACK);
		jLabelPutMoney.setHorizontalAlignment(SwingConstants.CENTER);
		
		jContentPane = new JPanel();
		jContentPane.setLayout(new GridLayout(7, 2, 0, 0));
		jContentPane.add(jLabelPutMoney);
		
		jButtonBack = new JButton(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jButtonBack")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		jContentPane.add(jButtonBack);
		
		jLabelCardNumber = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("PutMoneyGUI.jLabelCardNumber")); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelCardNumber.setHorizontalAlignment(SwingConstants.CENTER);
		jContentPane.add(jLabelCardNumber);
		
		textFieldCardNumber = new JTextField();
		textFieldCardNumber.setText(""); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(textFieldCardNumber);
		textFieldCardNumber.setColumns(10);
		
		jLabelExpireDate = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("PutMoneyGUI.ExpireDate")); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(jLabelExpireDate);
		jLabelExpireDate.setHorizontalAlignment(SwingConstants.CENTER);
		
		textFieldExpireDate = new JTextField();
		textFieldExpireDate.setText(""); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(textFieldExpireDate);
		textFieldExpireDate.setColumns(10);
		
		jLabelCVC = new JLabel("CVC"); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelCVC.setHorizontalAlignment(SwingConstants.CENTER);
		jContentPane.add(jLabelCVC);
		
		jTextFieldCVC = new JTextField();
		jTextFieldCVC.setText(""); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(jTextFieldCVC);
		jTextFieldCVC.setColumns(10);
		
		jLabelName = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.jLabelName")); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelName.setHorizontalAlignment(SwingConstants.CENTER);
		jContentPane.add(jLabelName);
		
		jTextFieldName = new JTextField();
		jTextFieldName.setText(""); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(jTextFieldName);
		jTextFieldName.setColumns(10);
		
		
		setContentPane(jContentPane);
		
		jLabelMoneyAmount = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("PutMoneyGUI.jLabelMoneyAmount")); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelMoneyAmount.setHorizontalAlignment(SwingConstants.CENTER);
		jContentPane.add(jLabelMoneyAmount);
		
		jTextFieldMoneyAmount = new JTextField();
		jTextFieldMoneyAmount.setText(""); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(jTextFieldMoneyAmount);
		jTextFieldMoneyAmount.setColumns(10);
		
		jLabelError = new JLabel(""); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(jLabelError);
		jLabelError.setHorizontalAlignment(SwingConstants.CENTER);
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("TakeMoneyGUI.MainTitle"));
		
		jButtonTake = new JButton(ResourceBundle.getBundle("Etiquetas").getString("TakeMoneyGUI.TakeMoney")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonTake.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String kop = textFieldCardNumber.getText();
					int proba = Integer.parseInt(kop);
					kop = textFieldExpireDate.getText();
					proba = Integer.parseInt(kop);
					kop = jTextFieldCVC.getText();
					proba = Integer.parseInt(kop);
					kop = jTextFieldMoneyAmount.getText();
					if (kop == null || kop.isEmpty()) {
			            jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("TakeMoneyGUI.jButtonError2"));
			        }else {
			        	int hm = Integer.parseInt(kop);
			        	if(hm>0) {
			            	facade.takeMoneyDriver(driver.getEmail(), hm);
				            dispose();
			            }else {
			            	jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("TakeMoneyGUI.jButtonError2"));
			            }
			        }
				}catch (NumberFormatException error) {
		        	jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("MakeReservationGUI.jButtonError"));
				}catch(NotEnoughMoneyException error) {
					jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("TakeMoneyGUI.jButtonError"));
				}
			}
		});
		jContentPane.add(jButtonTake);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}
	
	private void paintAgain() {
		jLabelPutMoney.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Register"));
		jLabelExpireDate.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jLabelEmail"));
		jLabelCVC.setText(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jLabelPassword"));
		jButtonTake.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.BRegister"));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.MainTitle"));
	}
	
} // @jve:decl-index=0:visual-constraint="0,0"

