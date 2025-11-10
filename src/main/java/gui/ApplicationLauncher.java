package gui;

import java.awt.Color;
import java.net.URL;
import java.util.Locale;

import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import businesslogic.BLFacade;
import businesslogic.BLFacadeImplementation;
import businesslogic.BLFactory;
import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Driver;

public class ApplicationLauncher { 
	
	
	
	public static void main(String[] args) {

		ConfigXML c=ConfigXML.getInstance();
	
		System.out.println(c.getLocale());
		
		Locale.setDefault(new Locale(c.getLocale()));
		
		System.out.println("Locale: "+Locale.getDefault());
		
	    Driver driver=new Driver("driver3@gmail.com","Test Driver", "123");

		
		MainGUI a=new MainGUI();
		a.setVisible(true);


		try {
			BLFacade appFacadeInterface;
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			boolean isLocal = c.isBusinessLogicLocal();
			
			appFacadeInterface=new BLFactory().getBusinessLogicFactory(isLocal);
			MainGUI.setBussinessLogic(appFacadeInterface);

		}catch (Exception e) {
			a.jLabelWelcome.setText("Error: "+e.toString());
			a.jLabelWelcome.setForeground(Color.RED);	
			
			System.out.println("Error in ApplicationLauncher: "+e.toString());
		}
		//a.pack();


	}

	
}
