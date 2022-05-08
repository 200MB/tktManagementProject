package FxmlControllers;

import Json.TktJson;
import Sql.DataBase;
import Sql.EntrySearch;
import Sql.InfoCreds;
import Sql.Initialization;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminInterface {
    @FXML
    private TextField UploadName;
    @FXML
    private TextField UploadImgPath;
    @FXML
    private TextField UploadRating;
    @FXML
    private TextArea UploadDesc;
    @FXML
    private RadioButton UploadView;
    @FXML
    private Button UploadBtn;
    @FXML
    private TextField UploadID;
    @FXML
    private Label UpladWarningLabel;
    @FXML
    private TextField HallName;
    @FXML
    private TextField HallSeats;
    @FXML
    private Button HallAddBtn;
    @FXML
    private TextField RemoveID;
    @FXML
    private Button RemoveBtn;
    @FXML
    private Label RemoveWarningLabel;
    @FXML
    private Label HallWarningLabel;
    @FXML
    private TextField EditID;
    @FXML
    private TextField EditName;
    @FXML
    private TextField EditRating;
    @FXML
    private TextField EditPath;
    @FXML
    private TextArea EditDesc;
    @FXML
    private Button EditBtn;
    @FXML
    private Label EditWarningLabel;
    @FXML
    private RadioButton EditView;
    @FXML
    private TextField PriceMovieID;
    @FXML
    private TextField PriceHallName;
    @FXML
    private TextField PriceTime;
    @FXML
    private TextField Price;
    @FXML
    private Button PriceBtn;
    @FXML
    private Label PriceWarningLabel;

    @FXML
    //here i do not check if other fields except id is filled since it can be edited.
    public void onUploadBtnPressed() throws IOException {
        if (!TktJson.movieExists(UploadID.getText()) && UploadID.getText().length() > 0) {
            UpladWarningLabel.setVisible(false);
            TktJson.addJson(UploadName.getText(), UploadImgPath.getText(),
                    UploadRating.getText(), UploadDesc.getText(), String.valueOf(UploadView.isSelected()), UploadID.getText());

        } else {
            UpladWarningLabel.setVisible(true);
        }
    }

    public void onRemoveBtnPressed() throws IOException {
        if (TktJson.movieExists(RemoveID.getText()) && RemoveID.getText().length() > 0) {
            RemoveWarningLabel.setVisible(false);
            ArrayList<JSONObject> arr = TktJson.getJsonList();
            arr.removeIf(json -> json.containsValue(RemoveID.getText()));
            TktJson.updateJson(arr);
            System.out.println("DEBUG: REMOVED JSON");
        } else {
            RemoveWarningLabel.setVisible(true);
        }
    }

    public void onAddBtnPressed() throws SQLException {
        if (!DataBase.hallExists(HallName.getText()) && HallName.getText().length() > 0 && HallSeats.getText().length() > 0) {
            HallWarningLabel.setVisible(false);
            Connection con = DriverManager.getConnection(InfoCreds.HOST, InfoCreds.USER, InfoCreds.PASSWORD);
            con.prepareStatement("INSERT INTO `tkt`.`moviehalls` (`name`, `seats`) VALUES ('%s', '%s');".formatted(HallName.getText(), HallSeats.getText())).execute();
            System.out.println("DEBUG: ADDED ROW INTO MOVIEHALLS");
        } else {
            HallWarningLabel.setVisible(true);
        }
    }

    public void onEditBtnPressed() throws IOException {
        if (TktJson.movieExists(EditID.getText())) {
            EditWarningLabel.setVisible(false);
            ArrayList<JSONObject> arr = TktJson.getJsonList();
            JSONObject json = arr.stream().filter(e -> e.containsValue(EditID.getText())).findFirst().get();
            //if statements has to be done manually
            //RADIOBUTTON ALWAYS CHANGES REGARDLESS SO KEEP IT SYNCED.
            if (EditName.getText().length() > 0) {
                json.put("Name", EditName.getText());
            }
            if (EditDesc.getText().length() > 0) {
                json.put("Description", EditDesc.getText());
            }
            if (EditPath.getText().length() > 0) {
                json.put("Url", EditPath.getText());

            }
            if (EditRating.getText().length() > 0) {
                json.put("Rating", EditRating.getText());
            }

            json.put("Show", String.valueOf(EditView.isSelected()));

            arr.remove(arr.stream().filter(e -> e.containsValue(EditID.getText())).findFirst().get());
            arr.add(json);
            TktJson.updateJson(arr);
            System.out.println("DEBUG: EDITED JSON");
        } else {
            EditWarningLabel.setVisible(true);
        }
    }

    public void onPriceBtnPressed() {
        if (Arrays.stream(new String[]{PriceMovieID.getText(), PriceTime.getText(),
                PriceHallName.getText(), Price.getText()}).noneMatch(e -> e.length() == 0)) {
            PriceWarningLabel.setVisible(false);

        } else {
            PriceWarningLabel.setVisible(true);
        }
    }
}

