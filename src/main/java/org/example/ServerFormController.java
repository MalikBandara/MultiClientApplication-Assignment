package org.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;



public class ServerFormController {


    @FXML
    private TextField serverText;

    @FXML
    private VBox serverVbox;

    DataOutputStream dataOutputStream ;


    DataInputStream dataInputStream;





    @FXML
    void btnSendImage(ActionEvent event) {

        File file = new FileChooser().showOpenDialog(null);

        if (file!=null){
            try {
                byte[] bytes = Files.readAllBytes(file.toPath());
                dataOutputStream.writeUTF("image");
                dataOutputStream.writeInt(bytes.length);
                dataOutputStream.write(bytes);
                dataOutputStream.flush();


            }catch (Exception e ){
                e.printStackTrace();
            }
        }

    }


    @FXML
    void btnSendMessage(ActionEvent event) {

        try {
            String text = serverText.getText();
            dataOutputStream.writeUTF("text");
            dataOutputStream.writeUTF(text);
            dataOutputStream.flush();
            serverText.clear();

        }catch (Exception e ){
            e.printStackTrace();
        }

    }

    public void initialize (){
        new Thread(()->{
            try {

                ServerSocket socket = new ServerSocket(5100);
                Socket accept = socket.accept();
                dataOutputStream = new DataOutputStream(accept.getOutputStream());
                dataInputStream = new DataInputStream(accept.getInputStream());

                while (true){
                    String s = dataInputStream.readUTF();
                    if ("text".equals(s)){
                        String s1 = dataInputStream.readUTF();
                        Platform.runLater(()-> serverVbox.getChildren().add(new Text(s1)));
                    } else if ("image".equals(s)) {
                        int i = dataInputStream.readInt();
                        byte[] bytes = new byte[i];
                        dataInputStream.readFully(bytes);

                        Platform.runLater(()->{
                            Image image= new Image(new ByteArrayInputStream(bytes));
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(100);
                            imageView.setFitWidth(100);
                            imageView.setPreserveRatio(true);

                            serverVbox.getChildren().add(imageView);
                        });
                    }
                }



            }catch (Exception e ){
                e.printStackTrace();
            }


        }).start();


    }

}
