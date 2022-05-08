package FxmlControllers;

import Json.TktJson;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.json.simple.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserInterface implements Initializable {
    @FXML
    private Button UserProfile;
    @FXML
    private ScrollPane UserSP;
    private TilePane UserTilePane = new TilePane();

    private final int rows = 4;
    private final int cols = 3;

    private int count = 0;
    private final ArrayList<JSONObject> arr = TktJson.getJsonListForUsers();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTilePane();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (count == arr.size()) break;
                UserTilePane.getChildren().add(convertToImage(count));
                count++;
            }
        }
        setScrollpane();

    }

    private void setTilePane() {
        UserTilePane.setPrefRows(rows);
        UserTilePane.setPrefColumns(cols);
        UserTilePane.setStyle("-fx-background-color:black");
        UserTilePane.setHgap(150);
        UserTilePane.setVgap(50);
        UserTilePane.getChildren().clear();
    }

    private void setScrollpane() {
        UserSP.setContent(UserTilePane);
        UserSP.setStyle("-fx-background: black; -fx-background-color: black");
        UserSP.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);    // Horizontal scroll bar
        UserSP.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
    }

    private VBox convertToImage(int count) {
        Label name = new Label();
        name.setText(String.valueOf(arr.get(count).get("Name")));
        name.setTextFill(Paint.valueOf("Yellow"));
        name.setStyle("-fx-font-size:15; -fx-font-family:sansserif;");
        ImageView view = new ImageView();
        view.setFitHeight(450);
        view.setFitWidth(320);
        view.setSmooth(true);
        view.setCache(true);
        Image image = new Image(String.valueOf(arr.get(count).get("Url")));
        view.setImage(image);
        VBox box = new VBox();
        box.getChildren().add(name);
        box.getChildren().add(view);
        box.setId(String.valueOf(arr.get(count).get("id")));

        box.setOnMouseClicked(mouseEvent -> {

        });
        return box;
    }
}
