/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicadistribuidos;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import negociodistribuidos.Tutor;

/**
 *
 * @author ceccy
 */
public class LogicaDistribuidos {

    public static final String URL = "jdbc:mysql://localhost:3306/distribuidos?useTimezone=true&serverTimezone=UTC";
    public static final String user = "root";
    public static final String pass = "root";

    //Sentencia que se le envía al manejador de la base de datos para ejecutar un SP
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException {
        try {
            final String host = "127.0.0.1";
            final int puerto = 5000;
            DataInputStream in;
            DataOutputStream out;
            ObjectInputStream inObj;
            ArrayList<Tutor> tutores = new ArrayList<Tutor>();
            Socket sc = new Socket(host, puerto);
            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());
            inObj = new ObjectInputStream(sc.getInputStream());
            out.writeUTF("Marica.");
            int cont = in.readInt();
            
            while(cont>0){
                System.out.println("Recepcion:");
                Tutor aux = (Tutor) inObj.readObject();
                System.out.println(aux);
                System.out.println(aux.getNombre());
                String nom = aux.getNombre();
                String user = aux.getUsuario();
                String pass = aux.getContrasenia();
                int id = aux.getId();
                System.out.println(nom);
                System.out.println(user);
                System.out.println(pass);
                System.out.println(id);
                System.out.println(aux);
                tutores.add(aux);
                System.out.println(tutores);
                System.out.println("----------");
                cont--;
            }
            //System.out.println(in.readUTF());
            
            sc.close();

        } catch (IOException ex) {
            Logger.getLogger(LogicaDistribuidos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
