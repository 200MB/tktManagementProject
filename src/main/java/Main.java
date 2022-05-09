import Json.TktJson;
import Sql.Initialization;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.sql.SQLException;

public class Main extends Application {
    //todo:onClickEvent for column,seat page,balance fill
    public static void main(String[] args) throws SQLException, IOException {
        Initialization.startup();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EntryPoint.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.show();

    }

}