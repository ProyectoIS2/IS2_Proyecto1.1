package gui;

import javax.swing.*;
import java.awt.*;
import domain.Driver;
import exceptions.PasswordDoesNotMatchException;
import businesslogic.BLFacade;
import businesslogic.BLFactory;


public class DriverTable extends JFrame {

    private Driver driver;
    private JTable tabla;

    public DriverTable(Driver driver) {
        super(driver.getName() + "'s rides ");
        this.setBounds(100, 100, 700, 200);
        this.driver = driver;

        DriverAdapter adapt = new DriverAdapter(driver);

        tabla = new JTable(adapt);
        
        tabla.setPreferredScrollableViewportSize(new Dimension(500, 70));
        JScrollPane scrollPane = new JScrollPane(tabla);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

public static void main(String[] args) {
        
        boolean isLocal = true;
        BLFacade blFacade = new BLFactory().getBusinessLogicFactory(isLocal);
        
        String conductorEmail = "urtzi@email.com";
        String conductorPassword = "1234"; 

        System.out.println("Buscando al conductor por email: " + conductorEmail);
        
        Driver d = null;
        
        try {
            d = blFacade.getDriverByEmail(conductorEmail, conductorPassword);
            
        } catch (PasswordDoesNotMatchException e) {
            System.out.println("Error: La contraseña no coincide.");
            
        } catch (Exception e) {
            System.out.println("Error: No se encontró el conductor o la contraseña es incorrecta.");
        }

        if (d != null) {
            System.out.println("Conductor encontrado. Creando tabla...");
            DriverTable dt = new DriverTable(d);
            dt.setVisible(true);
        } else {
            System.out.println("Error: Conductor no encontrado (revisa email y contraseña).");
        }
    }
}