package FxmlControllers;

import Sql.DataBase;
import Sql.EntrySearch;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class EntryPoint {
    @FXML
    private TextField LoginEmail;
    @FXML
    private TextField LoginPassword;
    @FXML
    private Button LoginBtn;
    @FXML
    private TextField RegEmail;
    @FXML
    private TextField RegPassword;
    @FXML
    private TextField RegName;
    @FXML
    private Label hdnLabelLogin;
    @FXML
    private Label hdnLabelReg;

    @FXML
    public void onLoginPressed() throws IOException {
        Stage stage = (Stage) LoginBtn.getScene().getWindow();
        Parent parent;

        String email = LoginEmail.getText();
        String password = LoginPassword.getText();
        if (EntrySearch.exists(email, password)) {
            hdnLabelLogin.setVisible(false);
            if (EntrySearch.getID(email) == 1) {
                parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AdminInterface.fxml")));
            } else {
                parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UserInterface.fxml")));
            }
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } else {
            hdnLabelLogin.setVisible(true);
        }
    }

    @FXML
    public void onRegPressed() throws SQLException {
        String email = RegEmail.getText();
        String password = RegPassword.getText();
        String name = RegName.getText();
        if (!EntrySearch.exists(email)) {
            hdnLabelReg.setText("Successfully registered!");
            hdnLabelReg.setVisible(true);
            DataBase.addUser(name, email, password);
        } else {
            hdnLabelReg.setText("email already exists");
            hdnLabelReg.setVisible(true);
        }
    }


}

