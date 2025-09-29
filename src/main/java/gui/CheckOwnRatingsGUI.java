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


public class CheckOwnRatingsGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	DefaultComboBoxModel<String> originLocations = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();
	private final JLabel jLabelEvents = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnRatingsGUI.maintitle")); 
	private JLabel jLabelError = new JLabel("");

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));

	public CheckOwnRatingsGUI(int[] l)
	{
		int[]lista = l;
		
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(700, 500));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnRatingsGUI.maintitle"));
		jLabelEvents.setBounds(210, 62, 259, 16);
		this.getContentPane().add(jLabelEvents);
		jLabelEvents.setHorizontalAlignment(SwingConstants.CENTER);

		jButtonClose.setBounds(new Rectangle(274, 384, 130, 30));
		jButtonClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		BLFacade facade = MainGUI.getBusinessLogic();

		this.getContentPane().add(jButtonClose, null);
		
		JLabel jLabelOne = new JLabel((String) null); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelOne.setBounds(100, 113, 73, 30);
		getContentPane().add(jLabelOne);
		jLabelOne.setText("1");
		
		JLabel jLabelTwo = new JLabel((String) null);
		jLabelTwo.setBounds(173, 113, 73, 30);
		getContentPane().add(jLabelTwo);
		jLabelTwo.setText("2");
		
		JLabel jLabelThree = new JLabel((String) null);
		jLabelThree.setBounds(246, 113, 73, 30);
		getContentPane().add(jLabelThree);
		jLabelThree.setText("3");
		
		JLabel jLabelFour = new JLabel((String) null);
		jLabelFour.setBounds(319, 113, 73, 30);
		getContentPane().add(jLabelFour);
		jLabelFour.setText("4");
		
		JLabel jLabelFive = new JLabel((String) null);
		jLabelFive.setBounds(392, 113, 73, 30);
		getContentPane().add(jLabelFive);
		jLabelFive.setText("5");
		
		JLabel one = new JLabel(Integer.toString(lista[0]));
		one.setBounds(100, 153, 73, 30);
		getContentPane().add(one);
		
		JLabel two = new JLabel(Integer.toString(lista[1]));
		two.setBounds(173, 153, 73, 30);
		getContentPane().add(two);
		
		JLabel three = new JLabel(Integer.toString(lista[2]));
		three.setBounds(246, 153, 73, 30);
		getContentPane().add(three);
		
		JLabel four = new JLabel(Integer.toString(lista[3]));
		four.setBounds(319, 153, 73, 30);
		getContentPane().add(four);
		
		JLabel five = new JLabel(Integer.toString(lista[4]));
		five.setBounds(392, 153, 73, 30);
		getContentPane().add(five);
		
		JLabel jLabelWhich = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnRatingsGUI.jLabelWhich")); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelWhich.setBounds(461, 113, 150, 30);
		getContentPane().add(jLabelWhich);
		
		JLabel jLabelHm = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CheckOwnRatingsGUI.jLabelHm")); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelHm.setBounds(461, 153, 150, 30);
		getContentPane().add(jLabelHm);
	}
}
