package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.User.User;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientFormController {
    public GridPane gridPane;
    public AnchorPane clientPanel;
    public ScrollPane ClientScrollPane;
    public VBox clientVbox;
    public TextField txtType;
    public AnchorPane emojiPane;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    String clientName="";

    private final String[] emojis = {
            "\uD83D\uDE00", // 😀
            "\uD83D\uDE01", // 😁
            "\uD83D\uDE02", // 😂
            "\uD83D\uDE11", // 😑

    };

    public static void setImage(byte[] bytes, String sender) {


    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        sendMsg(txtType.getText());
    }
    public void initialize() throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new Socket("localhost", 5000);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    System.out.println("Client connected");
                    SeverFormController.receiveMessage(clientName+" joined.");

                    while (socket.isConnected()){
                        String receivingMsg = dataInputStream.readUTF();
                       appentText(receivingMsg, ClientFormController.this.clientVbox);

                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

        this.clientVbox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                ClientScrollPane.setVvalue((Double) newValue);
            }
        });
        emoji();

    }
    public void emoji(){
        emojiPane.setVisible(true);
        int buttonIndex = 0;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 2; column++) {
                if (buttonIndex < emojis.length) {
                    String emoji = emojis[buttonIndex];
                    JFXButton emojiButton = makeEmojiButton(emoji);
                   gridPane.add(emojiButton, column, row);
                    buttonIndex++;
                } else {
                    break;
                }
            }
        }

    }

    private JFXButton makeEmojiButton(String emoji) {
        JFXButton button = new JFXButton(emoji);
        button.getStyleClass().add("emoji-button");
        button.setOnAction(this::emojiButtonAction);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setFillWidth(button, true);
        GridPane.setFillHeight(button, true);
        button.setStyle("-fx-font-size: 15; -fx-text-fill: black; -fx-background-color: #F0F0F0; -fx-border-radius: 50");
        return button;
    }

    private void emojiButtonAction(ActionEvent event) {
        emojiPane.setVisible(true);
        JFXButton button = (JFXButton) event.getSource();
        txtType.appendText(button.getText());
    }
    private void sendMsg(String msg) {
        if (!msg.isEmpty()){
            if (!msg.matches(".*\\.(png|jpe?g|gif)$")){

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 0, 10));

                Text text = new Text(msg);
                text.setStyle("-fx-font-size: 14");
                TextFlow textFlow = new TextFlow(text);


                textFlow.setStyle("-fx-background-color: #68fcdd; -fx-font-weight: bold; -fx-color: #ffffff; -fx-background-radius: 20px");
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                text.setFill(Color.color(1, 1, 1));

                hBox.getChildren().add(textFlow);

                HBox hBoxTime = new HBox();
                hBoxTime.setAlignment(Pos.CENTER_RIGHT);
                hBoxTime.setPadding(new Insets(0, 5, 5, 10));
                String stringTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                Text time = new Text(stringTime);
                time.setStyle("-fx-font-size: 8");

                hBoxTime.getChildren().add(time);

                clientVbox.getChildren().add(hBox);
               clientVbox.getChildren().add(hBoxTime);


                try {
                    dataOutputStream.writeUTF(clientName + "-" + msg);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

               txtType.clear();
            }
        }
    }
    public static void appentText(String msg, VBox vBox) throws IOException {

        String name = msg.split("-")[0];
        String msgFromServer = msg.split("-")[1];

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        HBox hBoxName = new HBox();
        hBoxName.setAlignment(Pos.CENTER_LEFT);
        Text textName = new Text(name);
        TextFlow textFlowName = new TextFlow(textName);
        hBoxName.getChildren().add(textFlowName);

        Text text = new Text(msgFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: #abb8c3; -fx-font-weight: bold; -fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5,10,5,10));
        text.setFill(Color.color(0,0,0));

        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBoxName);
                vBox.getChildren().add(hBox);
            }
        });

    }
    public void setClientName(String name) {
        clientName=name;
    }
    

    public void btnImageOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png",".jpg",".gif",".bmp","*.jpeg")
        );
        Stage stage = (Stage) clientPanel.getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String sendImage = file.toURI().toString();
            sendToUser(sendImage);
        }

    }

    private void sendToUser(String sendImage) {
        HBox hBoxName = new HBox();
        hBoxName.setAlignment(Pos.CENTER_RIGHT);
        Text textName = new Text("Me");
        TextFlow textFlowName = new TextFlow(textName);
        hBoxName.getChildren().add(textFlowName);

        Image image = new Image(sendImage);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5,5,5,10));
        hBox.getChildren().add(imageView);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        clientVbox.getChildren().add(hBoxName);
        clientVbox.getChildren().add(hBox);

        try {
            dataOutputStream.writeUTF(clientName + "-" + sendImage);
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void btnEmojionAction(ActionEvent actionEvent) {
    }

}




