package net.salesianos.server.threads;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import net.salesianos.shared.models.Message;

public class ClientHandler extends Thread {
    
    private ObjectInputStream clientObjInStream;
    private ObjectOutputStream clientObjOutStream;
    private ArrayList<ObjectOutputStream> connectedObjOutputStreamList;

    public ClientHandler(ObjectInputStream clientObjInStream, ObjectOutputStream clientObjOutStream, ArrayList<ObjectOutputStream> connectedObjOutputStreamList) {
    this.clientObjInStream = clientObjInStream;
    this.clientObjOutStream = clientObjOutStream;
    this.connectedObjOutputStreamList = connectedObjOutputStreamList;
  }
    @Override 
    public void run() {
        String username = "";
        try {

            username = this.clientObjInStream.readUTF();
            while (true) {
                Message msg = (Message) this.clientObjInStream.readObject();
                if (msg.getContent().startsWith("bye")) {
                    msg.setContent(null);
                    clientObjOutStream.writeUTF("Saliendo ");
                    this.connectedObjOutputStreamList.remove(this.clientObjOutStream);
                    this.clientObjOutStream.close();
                    break;
                } else if (msg.getContent().startsWith("msg:") ) {
                    msg.setContent(msg.getContent().substring(4, msg.getContent().length()));
                    System.out.println(username + ": " + msg.getContent());
                } else {
                    clientObjOutStream.writeUTF("No sabes escribir");
                }

                for (ObjectOutputStream otherObjOutputStream : connectedObjOutputStreamList) {
                    if (otherObjOutputStream != this.clientObjOutStream) {
                        otherObjOutputStream.writeObject(msg);
                    }
                }

            }

        } catch (EOFException eofException) {
            this.connectedObjOutputStreamList.remove(this.clientObjOutStream);
            System.out.println("CERRANDO CONEXIÓN CON " + username.toUpperCase());
          } catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
    }

}
