package FxmlControllers;

import Sql.DataBase;
import Sql.EntrySearch;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import javax.xml.crypto.Data;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SeatVision implements Initializable {
    @FXML
    private GridPane seatGrid;
    @FXML
    private javafx.scene.control.ScrollPane cartSP;
    @FXML
    private Button seatBuy;
    @FXML
    private Label total;
    @FXML
    private Label response;
    @FXML
    private Label balanceLabel;

    private TextArea txt = new TextArea();
    private int totalPrice = 0;
    private ArrayList<Integer> marks = new ArrayList<>();
    private ArrayList<String> buys = new ArrayList<>();
    private String balance = UserInterface.balance;
    public static String price;
    public static String dimension;
    public static String hallName;

    private int rows;
    private int cols;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setRowsAndCols();
        setStyles();
        setGridPane(seatGrid);
    }

    private void setRowsAndCols() {
        String[] arr = dimension.split("x");
        rows = Integer.parseInt(arr[0]);
        cols = Integer.parseInt(arr[1]);
        balanceLabel.setText("Balance:" + balance + "$");
    }

    private void setStyles() {
        txt.setStyle("-fx-background: black;");
        txt.setStyle("-fx-text-fill: blue;");
        cartSP.setStyle("-fx-background: black");
    }

    //sets up a GridPane and adds an eventListener to each stack added to a GridPane cell
    private void setGridPane(GridPane seatGrid) {
        seatGrid.setGridLinesVisible(false);
        for (int i = 0; i < cols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setMinWidth(10.0);
            colConst.setPrefWidth(120.0);
            seatGrid.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setMinHeight(10.0);
            rowConst.setPrefHeight(120.0);
            seatGrid.getRowConstraints().add(rowConst);
        }
        int count = 1;
        JSONObject table = DataBase.getJsonSeat(hallName);
        cartSP.setFitToWidth(true);
        cartSP.setFitToHeight(true);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Text text = new Text(String.valueOf(count));
                text.setScaleX(2);
                text.setScaleX(1);
                StackPane stack = new StackPane(text);
                stack.setStyle("-fx-background-color: #2596be;");
                stack.setId(String.valueOf(count));
                int finalCount = count;
                stack.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.PRIMARY && !stack.getStyle().equalsIgnoreCase("-fx-background-color: #4c4d6f;")) {
                        if (!marks.contains(Integer.parseInt(stack.getId()))) {
                            marks.add(finalCount);
                            totalPrice += Integer.parseInt(price);
                            buys.add("Added " + stack.getId());
                            txt.setText(buys.toString());
                            total.setText("Total:%d".formatted(totalPrice));
                            cartSP.setContent(txt);
                            stack.setStyle("-fx-background-color: gray;");
                        }

                    }
                    if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseButton.SECONDARY && !stack.getStyle().equalsIgnoreCase("-fx-background-color: #4c4d6f;")) {
                        if (totalPrice >= Integer.parseInt(price) &&
                                marks.contains(Integer.parseInt(stack.getId()))) {
                            marks.removeIf(e -> e.equals(finalCount));
                            totalPrice -= Integer.parseInt(price);
                            total.setText("Total:%d".formatted(totalPrice));
                            buys.removeIf(e -> e.equalsIgnoreCase("Added " + stack.getId()));
                            txt.setText(buys.toString());
                            stack.setStyle("-fx-background-color: #2596be;");
                        }


                    }
                });
                assert table != null;
                if (table.get(String.valueOf(count)).equals("false")) stack.setStyle("-fx-background-color: #4c4d6f;");
                seatGrid.add(stack, j, i);
                count++;
            }
        }

    }

    //Updates User balance,Records transaction, and stores information in Transaction history
    public void onSeatBuyBtnPressed() {
        if (Integer.parseInt(balance) >= totalPrice && marks.size() > 0) {
            occupySeats();
            DataBase.recordTransaction(UserInterface.id, price, hallName, MoviePage.name);
            balanceLabel.setText("Balance:" + (Integer.parseInt(balance) - totalPrice));
            balance = String.valueOf(Integer.parseInt(balance) - totalPrice); //in case user decides to buy more on same page
            UserInterface.balance = balance; //in case user goes back to userinterface and reopens the page again.
            DataBase.updateBalance(UserInterface.id, balance);
            response.setText("Accepted!");
            response.setStyle("-fx-text-fill:green");
        } else {
            response.setText("Declined!");
            response.setStyle("-fx-text-fill:red");
        }
        response.setVisible(true);

    }

    //updates database containing a Json which contains information about GridView
    private void occupySeats() {
        for (Node node : seatGrid.getChildren()) {
            if (node.getId() != null && marks.contains(Integer.parseInt(node.getId()))) {
                node.setStyle("-fx-background-color: #4c4d6f;");
                JSONObject table = DataBase.getJsonSeat(hallName);
                table.put(node.getId(), "false");
                DataBase.updateJsonSeat(table, hallName);
                System.out.println("DEBUG: OCCUPIED SEATS");
            }
        }
    }

    public void onBackBtnPressed() {
        new EntryPoint().openFxml("/MoviePage.fxml", (Stage) seatBuy.getScene().getWindow());
    }

}
