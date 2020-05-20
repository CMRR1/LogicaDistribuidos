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
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.net.ServerSocket;
import negociodistribuidos.Asignacion;
import negociodistribuidos.Tutor;

/**
 *
 * @author migue
 */
public class LogicaDistribuidos {

    public static final String URL = "jdbc:mysql://localhost:3306/distribuidos?useTimezone=true&serverTimezone=UTC";
    public static final String user = "root";
    public static final String pass = "1235";
    Tutor tuto = null;

    //Sentencia que se le envía al manejador de la base de datos para ejecutar un SP
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException {
        try {
            System.out.println("Iniciando programa");
            ServerSocket server = null;
            Socket scCliente = null;
            int puertoCliente = 5060;
            System.out.println("Creando server socket");
            server = new ServerSocket(puertoCliente);
            DataInputStream in2;
            DataOutputStream out2;
            ObjectOutputStream objOut;

            while (true) {
                scCliente = server.accept();
                System.out.println("Conexion con pantallas");
                //ESTO ES LO QUE SE CONECTA CON LAS PANTALLAS
                in2 = new DataInputStream(scCliente.getInputStream());
                out2 = new DataOutputStream(scCliente.getOutputStream());
                objOut = new ObjectOutputStream(scCliente.getOutputStream());
                int opcion = in2.readInt();
                switch (opcion) {
                    case 0: {
                        String user = in2.readUTF();
                        String pass = in2.readUTF();
                        System.out.println(user);
                        System.out.println(pass);
                        System.out.println("Procederemos a validar los datos");
                        String hostBD = "127.0.0.1";
                        int puertoBD = 5000;
                        DataInputStream in;
                        DataOutputStream out;
                        ObjectInputStream objIn;
                        Socket sc = new Socket(hostBD, puertoBD);
                        System.out.println("Cree el socket para conectarse al servidor");
                        in = new DataInputStream(sc.getInputStream());
                        out = new DataOutputStream(sc.getOutputStream());
                        objIn = new ObjectInputStream(sc.getInputStream());
                        System.out.println("ESTOY ENVIANDO LA OPCION");
                        System.out.println(opcion);
                        out.writeInt(opcion);
                        System.out.println("ESTOY ENVIANDO LOS DATOS");
                        out.writeUTF(user);
                        out.writeUTF(pass);
                        System.out.println("ENVIE LOS DATOS");
                        boolean login = in.readBoolean();
                        System.out.println("RECIBI EL BOOLEANO");
                        System.out.println(login);
                        if (login) {
                            System.out.println("ENTRE AL IF");
                            Tutor tuto = (Tutor) objIn.readObject();
                            System.out.println("CREE AL TUTOR");
                            System.out.println(tuto.getNombre());
                            System.out.println(tuto.getUsuario());
                            out2.writeBoolean(login);
                            System.out.println("ENVIE EL LOGINN");
                            objOut.writeObject(tuto);
                            System.out.println("ENVIE AL TUTOR");
                            sc.close();
                        } else {
                            System.out.println("ESTOY ENN EL FALSE");
                            out2.writeBoolean(false);
                            sc.close();
                        }
                        break;
                    }
                    case 1:{
                        break;
                    }
                    case 2:{
                        //Se crea la conexion con el servidor y sus objetos de envio y lectura al servidor
                        System.out.println("ANDO EN LA OPCION DE BUSCAR ASIGNACIONES");
                        String hostBD = "127.0.0.1";
                        int puertoBD = 5000;
                        Socket sc = new Socket(hostBD, puertoBD);
                        System.out.println("CREE EL SOCKET A LA BD");
                        DataInputStream in = new DataInputStream(sc.getInputStream());
                        DataOutputStream out = new DataOutputStream(sc.getOutputStream());
                        ObjectInputStream objIn = new ObjectInputStream(sc.getInputStream());
                        System.out.println("CREE LOS OBJETOS DE MOVIMIENTOS DE DATOS");
                        //Envio la opcion al servidor
                        out.writeInt(opcion);
                        int id = in2.readInt();
                        out.writeInt(id);
                        int size = in.readInt();
                        int cont = size;
                        System.out.println("TAMANIO");
                        System.out.println(size);
                        if(cont != 0){
                          ArrayList<Asignacion> asig = new ArrayList<Asignacion>();

                          while(cont > 0){
                              Asignacion aux = (Asignacion) objIn.readObject();
                              asig.add(aux);
                              cont--;
                          } 
                          
                          //Se envia el numero de datos al cliente;
                            System.out.println(size);
                          out2.writeInt(size);
                            for (Asignacion asignacion : asig) {
                                objOut.writeObject(asignacion);
                            }
                        }
                        
                        //Se cierra el socket del servidor
                        System.out.println("Fin");
                        sc.close();
                        break;
                    }
                    case 3:{
                        
                        System.out.println("LOGICA DE REVISION");
                        String hostBD = "127.0.0.1";
                        int puertoBD = 5000;
                        Socket sc = new Socket(hostBD, puertoBD);
                        System.out.println("CREE EL SOCKET A LA BD");
                        DataInputStream in = new DataInputStream(sc.getInputStream());
                        DataOutputStream out = new DataOutputStream(sc.getOutputStream());
                        ObjectInputStream objIn = new ObjectInputStream(sc.getInputStream());
                        System.out.println("CREE LOS OBJETOS DE MOVIMIENTOS DE DATOS");
                        out.writeInt(opcion);
                        int id_asignacion = in2.readInt();
                        out.writeInt(id_asignacion);
                        boolean respuesta = in.readBoolean();
                        out2.writeBoolean(respuesta);
                        System.out.println("ENVIE LA PETICION DE REVISION");
                        sc.close();
                        break;
                        
                    }
                    
                    default:
                        break;

                }
                // Se cierra el socket del cliente;
                scCliente.close();

            }

//            final String host = "127.0.0.1";
//            final int puerto = 5050;
//            DataInputStream in;
//            DataOutputStream out;
//            ObjectInputStream inObj;
//            ArrayList<Tutor> tutores = new ArrayList<Tutor>();
//            Socket sc = new Socket(host, puerto);
//            in = new DataInputStream(sc.getInputStream());
//            out = new DataOutputStream(sc.getOutputStream());
//            inObj = new ObjectInputStream(sc.getInputStream());
//            out.writeUTF("Marica.");
//            int cont = in.readInt();
//            
//            while(cont>0){
//                System.out.println("Recepcion:");
//                Tutor aux = (Tutor) inObj.readObject();
//                System.out.println(aux);
//                System.out.println(aux.getNombre());
//                String nom = aux.getNombre();
//                String user = aux.getUsuario();
//                String pass = aux.getContrasenia();
//                int id = aux.getId();
//                System.out.println(nom);
//                System.out.println(user);
//                System.out.println(pass);
//                System.out.println(id);
//                System.out.println(aux);
//                tutores.add(aux);
//                System.out.println(tutores);
//                System.out.println("----------");
//                cont--;
//            }
//            //System.out.println(in.readUTF());
//            
//            sc.close();
        } catch (IOException ex) {
            Logger.getLogger(LogicaDistribuidos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
