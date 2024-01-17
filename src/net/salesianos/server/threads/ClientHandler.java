package net.salesianos.server.threads;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.salesianos.shared.models.Chat;
import net.salesianos.shared.models.Message;

public class ClientHandler extends Thread {

    private ObjectInputStream clientObjInStream;
    private ObjectOutputStream clientObjOutStream;
    private ArrayList<ObjectOutputStream> connectedObjOutputStreamList;
    private Chat chatLog;

    public ClientHandler(ObjectInputStream clientObjInStream, ObjectOutputStream clientObjOutStream,
            ArrayList<ObjectOutputStream> connectedObjOutputStreamList, Chat chatLog) {
        this.clientObjInStream = clientObjInStream;
        this.clientObjOutStream = clientObjOutStream;
        this.connectedObjOutputStreamList = connectedObjOutputStreamList;
        this.chatLog = chatLog;
    }

    @Override
    public void run() {

        String username = "";
        try {
            username = this.clientObjInStream.readUTF();

            for (Message message : chatLog.getAllMessages()) {
                this.clientObjOutStream.writeObject(message);
            }

            while (true) {
                Message msg = (Message) this.clientObjInStream.readObject();
                if (msg.getContent().startsWith("msg:")) {
                    Message newMessage = new Message();
                    String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    newMessage.setDate(currentTime);
                    newMessage.setUsername(username);
                    String mensajito = msg.getContent().substring(4);
                    newMessage.setContent(newMessage.getDate() + " <" + username + ">: " + mensajito);
                    chatLog.addMessage(newMessage);
                    for (ObjectOutputStream otherObjOutputStream : this.connectedObjOutputStreamList) {
                        if (otherObjOutputStream != this.clientObjOutStream) {
                            otherObjOutputStream.writeObject(newMessage);
                        }
                    }
                } else if (msg.getContent().startsWith("bye")) {
                    for (ObjectOutputStream otherObjOutputStream : this.connectedObjOutputStreamList) {
                        if (otherObjOutputStream != this.clientObjOutStream) {
                            Message byeMessage = new Message();
                            String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
                            byeMessage.setDate(currentTime);
                            byeMessage.setContent(username + " ha salido del chat");
                            byeMessage.setUsername("ServerOut");
                            otherObjOutputStream.writeObject(byeMessage);
                        }
                    }
                } else {
                    Message errorMessage = new Message();
                    errorMessage.setContent(
                            "Mensaje mal formateado, escriba msg: para escribir un mensaje o bye para salir");
                    errorMessage.setUsername("FormatError");
                    this.clientObjOutStream.writeObject(errorMessage);
                }

            }

        } catch (EOFException eofException) {
            this.connectedObjOutputStreamList.remove(this.clientObjOutStream);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
