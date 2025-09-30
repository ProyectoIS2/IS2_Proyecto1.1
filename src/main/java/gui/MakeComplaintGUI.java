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


public class MakeComplaintGUI extends JFrame {
	
    private Reservation r;
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JButton jButtonCreateQuery = null;
	private JButton jButtonQueryQueries = null;

    private static BLFacade appFacadeInterface;
	
	protected JLabel jLabelMakeReservation;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton jButtonCreate;
	private JLabel jLabelHm;
	private JTextField textFieldHm;
	private JLabel jLabelError;
	private JButton jButtonBack;
	
	/**
	 * This is the default constructor
	 */
	public MakeComplaintGUI(Reservation r) {
		super();
		this.r=r;
		
		// this.setSize(271, 295);
		this.setSize(495, 290);
		jLabelMakeReservation = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MakeComplaintGUI.jLabelMakeComplaint"));
		jLabelMakeReservation.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelMakeReservation.setForeground(Color.BLACK);
		jLabelMakeReservation.setHorizontalAlignment(SwingConstants.CENTER);
		
		BLFacade facade = MainGUI.getBusinessLogic();
		
		jContentPane = new JPanel();
		jContentPane.setLayout(new GridLayout(5, 2, 0, 0));
		jContentPane.add(jLabelMakeReservation);
		
		jButtonBack = new JButton(ResourceBundle.getBundle("Etiquetas").getString("LoginAndRegisterGUI.jButtonBack")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new CheckOwnReservationsGUI(r.getTraveler());
	        	a.setVisible(true);
				dispose();
			}
		});
		jContentPane.add(jButtonBack);
		
		jLabelHm = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MakeComplaintGUI.jLabelComplaint")); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(jLabelHm);
		jLabelHm.setHorizontalAlignment(SwingConstants.CENTER);
		
		textFieldHm = new JTextField();
		textFieldHm.setText(""); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(textFieldHm);
		textFieldHm.setColumns(10);
		
		
		setContentPane(jContentPane);
		
		jButtonCreate = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MakeReservationGUI.jButtonCreate")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String com = textFieldHm.getText();
				if (com == null || com.isEmpty()) {
		            jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("MakeComplaintGUI.jButtonError"));
		        }else {
		        	facade.createComplaintToDriver(com, r);
		        	JFrame a = new CheckOwnReservationsGUI(r.getTraveler());
		        	a.setVisible(true);
		        	dispose();
		        }
			}
		});		
		jLabelError = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MakeComplaintGUI.jLabelError")); //$NON-NLS-1$ //$NON-NLS-2$
		jContentPane.add(jLabelError);
		jLabelError.setHorizontalAlignment(SwingConstants.CENTER);
		jContentPane.add(jButtonCreate);
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("MakeComplaintGUI.MainTitle"));
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}
} // @jve:decl-index=0:visual-constraint="0,0"

