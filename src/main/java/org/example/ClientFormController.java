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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.nio.file.Files;

public class ClientFormController {

    @FXML
    private VBox cleintvbox;

    @FXML
    private TextField clientText;

    DataInputStream dataInputStream ;

    DataOutputStream dataOutputStream ;

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

                Platform.runLater(()->{
                    Image image = new Image(new ByteArrayInputStream(bytes));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    imageView.setPreserveRatio(true);

                    cleintvbox.getChildren().add(imageView);
                });

            }catch (Exception e ){
                e.printStackTrace();
            }
        }

    }

    @FXML
    void btnSendMessage(ActionEvent event) {

        String text = clientText.getText();
        try {
            dataOutputStream.writeUTF("text");
            dataOutputStream.writeUTF(text);
            dataOutputStream.flush();
            clientText.clear();

            Platform.runLater(()-> cleintvbox.getChildren().add(new Text(text)));

        }catch (Exception e ){
            e.printStackTrace();
        }


    }
    public void initialize (){

        new Thread(()->{

            try {

                Socket socket = new Socket("localhost" , 5100);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (true){

                    String s = dataInputStream.readUTF();
                    if ("text".equals(s)){
                        String s1 = dataInputStream.readUTF();

                        Platform.runLater(()->cleintvbox.getChildren().add(new Text(s1)));
                    } else if ("image".equals(s)) {
                        int i = dataInputStream.readInt();
                        byte[] bytes = new byte[i];
                        dataInputStream.readFully(bytes);

                        Platform.runLater(()->{
                            Image image = new Image(new ByteArrayInputStream(bytes));
                            ImageView imageView = new ImageView(image);
                            imageView.setFitWidth(100);
                            imageView.setFitHeight(100);
                            imageView.setPreserveRatio(true);

                            cleintvbox.getChildren().add(imageView);
                        });
                    }
                }

            }catch (Exception e ){
                e.printStackTrace();
            }



        }).start();



    }

}
