package lk.ijse.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ServerInitializer extends Application {

    @Override
    public void start(Stage primarystage) throws Exception {

        Parent root= FXMLLoader.load(getClass().getResource("/view/SeverForm.fxml"));

        Scene scene = new Scene(root);
        primarystage.setScene(scene);
        primarystage.setTitle("Server");
        primarystage.centerOnScreen();
        primarystage.setResizable(false);
        primarystage.show();

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primarystage.getScene().getWindow());
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"))));
        stage.setTitle("chatRoom");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
