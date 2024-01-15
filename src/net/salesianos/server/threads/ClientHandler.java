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
                    for (ObjectOutputStream otherObjOutputStream : connectedObjOutputStreamList) {
                        if (otherObjOutputStream != this.clientObjOutStream) {
                            Message byeMessage = new Message();
                            byeMessage.setContent(username + " ha salido del chat");
                            byeMessage.setUsername("ServerOut");
                            otherObjOutputStream.writeObject(byeMessage);
                        }
                    }
                    break;
                } else if (msg.getContent().startsWith("msg:") ) {
                    msg.setContent(msg.getContent().substring(4, msg.getContent().length()));
                    for (ObjectOutputStream otherObjOutputStream : connectedObjOutputStreamList) {
                        if (otherObjOutputStream != this.clientObjOutStream) {
                            otherObjOutputStream.writeObject(msg);
                        }
                    }
                } else {
                    clientObjOutStream.writeUTF("No sabes escribir");
                }
            }

        } catch (EOFException eofException) {
            this.connectedObjOutputStreamList.remove(this.clientObjOutStream);
          } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
          }
    }

}
