package FxmlControllers;

import IO.JsonIO;
import Sql.DataBase;
import Sql.InfoCreds;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

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
    private Button outBtn;
    @FXML
    private Button ResetBtn;
    @FXML
    private TextField ResetHall;
    @FXML
    private Label ResetHallWarning;

    @FXML
    //here i do not check if other fields except id is filled since it can be edited.
    public void onUploadBtnPressed() throws IOException {
        UpladWarningLabel.setVisible(false);
        if (!JsonIO.movieExists(UploadID.getText()) && UploadID.getText().length() > 0) {
            JsonIO.addJson(UploadName.getText(), UploadImgPath.getText(),
                    UploadRating.getText(), UploadDesc.getText(), String.valueOf(UploadView.isSelected()), UploadID.getText());

        } else {
            UpladWarningLabel.setVisible(true);
        }
    }

    public void onRemoveBtnPressed() throws IOException {
        if (JsonIO.movieExists(RemoveID.getText()) && RemoveID.getText().length() > 0) {
            RemoveWarningLabel.setVisible(false);
            ArrayList<JSONObject> arr = JsonIO.getJsonList();
            arr.removeIf(json -> json.containsValue(RemoveID.getText()));
            JsonIO.updateJson(arr);
            System.out.println("DEBUG: REMOVED JSON");
        } else {
            RemoveWarningLabel.setVisible(true);
        }
    }

    public void onAddBtnPressed() throws SQLException {
        if (!DataBase.hallExists(HallName.getText()) && HallName.getText().length() > 0 && HallSeats.getText().length() > 0) {
            HallWarningLabel.setVisible(false);
            Connection con = DriverManager.getConnection(InfoCreds.HOST, InfoCreds.USER, InfoCreds.PASSWORD);
            JSONObject json = createFreshJson(HallSeats.getText().split("x")[0],
                    (HallSeats.getText().split("x")[1]));
            PreparedStatement pre = con.prepareStatement("INSERT INTO `tkt`.`moviehalls` (`name`, `seats`,`json`) VALUES (?,?,?);");
            pre.setString(1,HallName.getText());
            pre.setString(2,HallSeats.getText());
            pre.setString(3, json.toJSONString());
            pre.execute();
            System.out.println("DEBUG: ADDED ROW INTO MOVIEHALLS");
        } else {
            HallWarningLabel.setVisible(true);
        }

    }

    private JSONObject createFreshJson(String x, String y) {
        JSONObject json = new JSONObject();
        IntStream.range(1, (Integer.parseInt(x) * Integer.parseInt(y) + 1)).forEach(
                e -> json.put(String.valueOf(e), "true"));
        return json;
    }

    public void onEditBtnPressed() throws IOException {
        if (JsonIO.movieExists(EditID.getText())) {
            EditWarningLabel.setVisible(false);
            ArrayList<JSONObject> arr = JsonIO.getJsonList();
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
            JsonIO.updateJson(arr);
            System.out.println("DEBUG: EDITED JSON");
        } else {
            EditWarningLabel.setVisible(true);
        }
    }

    public void onPriceBtnPressed() throws SQLException {
        if (Arrays.stream(new String[]{PriceMovieID.getText(), PriceTime.getText(),
                PriceHallName.getText(), Price.getText()}).noneMatch(e -> e.length() == 0)) {
            PriceWarningLabel.setVisible(false);
            Connection con = DriverManager.getConnection(InfoCreds.HOST, InfoCreds.USER, InfoCreds.PASSWORD);
            con.prepareStatement("INSERT INTO `tkt`.`prices` (`idofmovie`,`nameofhall`,`price`,`time`) VALUES(\"%s\",\"%s\",\"%s\",\"%s\")"
                    .formatted(PriceMovieID.getText(), PriceHallName.getText(), Price.getText(), PriceTime.getText())).execute();
            System.out.println("DEBUG: ADDED PRICE TO MOVIE");
        } else {
            PriceWarningLabel.setVisible(true);
        }
    }

    public void onResetBtnPressed() throws SQLException {
        if (!DataBase.hallExists(ResetHall.getText())) {
            ResetHallWarning.setText("Hall not found/empty field");
            ResetHallWarning.setVisible(true);
            return;
        }
        ResetHallWarning.setText("Done!");
        ResetHallWarning.setVisible(true);
        String dimension = DataBase.getHallSeats(ResetHall.getText());
        String row = dimension.split("x")[0];
        String col = dimension.split("x")[1];
        DataBase.updateJsonSeat(createFreshJson(row, col), ResetHall.getText());
        System.out.printf("DEBUG: RESET %s%n", ResetHall.getText());
    }

    public void onNextIDBtnPressed() {
        UploadID.setText(JsonIO.getNextAvailableID());
    }

    public void onOutPressed() {
        new EntryPoint().openFxml("/EntryPoint.fxml", (Stage) outBtn.getScene().getWindow());
    }
}

