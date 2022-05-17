package FxmlControllers;

import Models.ProfileTable;
import Sql.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class Profile implements Initializable {

    @FXML
    private Label userNameLb;
    @FXML
    private Label userBalanceLb;
    @FXML
    private TextField balanceInput;
    @FXML
    private Label refillTxt;
    @FXML
    private TableView<ProfileTable> table;
    @FXML
    private TableColumn<ProfileTable, String> nameCol;
    @FXML
    private TableColumn<ProfileTable, String> hallCol;
    @FXML
    private TableColumn<ProfileTable, String> priceCol;

    public static String userName;
    public static String userBalance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLb.setText("Name: " + userName);
        userBalanceLb.setText("Balance: " + userBalance);
        initializeTable(table);
    }

    private void initializeTable(TableView<ProfileTable> table) {
        ObservableList<ProfileTable> obList = FXCollections.observableArrayList();
        try {
            obList.addAll(getData(Objects.requireNonNull(DataBase.getTransactionHistory(UserInterface.id))));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        setCells();
        table.setItems(obList);
    }

    public void balanceUpdate() {
        String balance = balanceInput.getText();
        if (isLegal(balance)) {
            DataBase.updateBalance(UserInterface.id, balance);
            UserInterface.balance = balance;
            userBalanceLb.setText("Balance: " + balance);
            refillTxt.setText("Refilled!");
        }
        else{
            refillTxt.setText("Invalid input!");
        }
        refillTxt.setVisible(true);

    }

    private boolean isLegal(String s) {
        boolean allnull = s.chars().allMatch(e->e == '0');

        for (char a : s.toCharArray()) {
            if ((a < '0' || a > '9') || allnull && s.length() > 1) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<ProfileTable> getData(ResultSet set) throws SQLException {
        ArrayList<ProfileTable> arrList = new ArrayList<>();
        while (set.next()) {
            arrList.add(new ProfileTable(set.getString(1),
                    set.getString(2), set.getString(3)));
        }
        return arrList;
    }

    private void setCells() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("moviename"));
        hallCol.setCellValueFactory(new PropertyValueFactory<>("hallname"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void onBackBtnPressed() {
        new EntryPoint().openFxml("/UserInterface.fxml", (Stage) userNameLb.getScene().getWindow());
    }
}
