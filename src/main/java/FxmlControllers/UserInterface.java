package FxmlControllers;

import IO.JsonIO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserInterface implements Initializable {
    @FXML
    private Button UserProfile;
    @FXML
    private Button outBtn;
    @FXML
    private ScrollPane UserSP;
    private final TilePane UserTilePane = new TilePane();

    public static String name;
    public static String balance;
    public static String id;

    private final int rows = 4;
    private final int cols = 3;

    private int count = 0;
    private final ArrayList<JSONObject> arr = JsonIO.getJsonListForUsers();

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

    //creates and styles Label,ImageView and adds it in Vbox. gives the Vbox same id as movieID.
    //on double click it opens MoviePage fxml
    private VBox convertToImage(int count) {

        Label name = new Label();
        name.setText(String.valueOf(arr.get(count).get("Name")));
        name.setTextFill(Paint.valueOf("Yellow"));
        name.setStyle("-fx-font-size:16; -fx-font-family:sansserif;");
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
            JSONObject obj = JsonIO.getJsonListForUsers().stream().filter(e -> e.get("id").equals(box.getId())).findFirst().get();
            MoviePage.id = (String) obj.get("id");
            MoviePage.name = (String) obj.get("Name");
            MoviePage.desc = (String) obj.get("Description");
            MoviePage.rating = (String) obj.get("Rating");
            MoviePage.url = (String) obj.get("Url");

            new EntryPoint().openFxml("/MoviePage.fxml", (Stage) box.getScene().getWindow());

        });
        return box;
    }

    public void onUserProfileBtnPressed() {
        Profile.userName = name;
        Profile.userBalance = balance;
        new EntryPoint().openFxml("/Profile.fxml", (Stage) outBtn.getScene().getWindow());
    }

    public void onOutPressed() {
        new EntryPoint().openFxml("/EntryPoint.fxml", (Stage) outBtn.getScene().getWindow());
    }
}
