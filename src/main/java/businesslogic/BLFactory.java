package businesslogic;

import java.awt.Color;
import java.net.URL;

import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.*;
import gui.MainGUI;

public class BLFactory {

	public BLFactory() {
	
	}
	public BLFacade getBusinessLogicFactory(boolean isLocal) {
		
		try {
			ConfigXML c=ConfigXML.getInstance();
			BLFacade appFacadeInterface;
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			
			if (isLocal) {
			
				DataAccess da= new DataAccess();
				appFacadeInterface = new BLFacadeImplementation(da);
			}
			else { //If remote
				String serviceName= "http://"+c.getBusinessLogicNode() +":"+ c.getBusinessLogicPort()+"/ws/"+c.getBusinessLogicName()+"?wsdl";
				 
				URL url = new URL(serviceName);
		 
		        //1st argument refers to wsdl document above
				//2nd argument is service name, refer to wsdl document above
		        QName qname = new QName("http://businessLogic/", "BLFacadeImplementationService");
		 
		        Service service = Service.create(url, qname);

		        appFacadeInterface = service.getPort(BLFacade.class);
			} 
			return appFacadeInterface;
			
		}catch (Exception e) {
			System.out.println("Error in BLFactory: "+e.toString());
			return null;
		}
	}

}
