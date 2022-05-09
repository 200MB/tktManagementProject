package FxmlControllers;

import Models.PriceTable;
import Sql.DataBase;
import com.mysql.cj.protocol.Resultset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MoviePage implements Initializable {
    @FXML
    private ImageView PageImage;
    @FXML
    private Label PageDescription;
    @FXML
    private Label PageName;
    @FXML
    private Label PageRating;
    @FXML
    private Button PageTableBtn;
    @FXML
    private TableView PageTable;
    @FXML
    private TableColumn hallColumn;
    @FXML
    private TableColumn priceColumn;
    @FXML
    private TableColumn timeColumn;

    public static String id;
    public static String name;
    public static String desc;
    public static String rating;
    public static String url;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PageName.setText(MoviePage.name);
        PageDescription.setText(MoviePage.desc);
        PageImage.setImage(new Image(MoviePage.url));
        PageRating.setText("Rating: " + MoviePage.rating);
        initializeTable(PageTable);
    }

    private void initializeTable(TableView table) {
        ObservableList<PriceTable> obList = FXCollections.observableArrayList();
        try {
            ResultSet set = DataBase.getPrices(id);
            obList.addAll(getData(set));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        setCells();
        table.setItems(obList);
    }

    private ArrayList<PriceTable> getData(ResultSet set) throws SQLException {
        ArrayList<PriceTable> arr = new ArrayList<>();
        while (set.next()) {
            String hall = set.getString(2);
            String price = set.getString(3);
            String time = set.getString(4);
            arr.add(new PriceTable(hall, price, time));
        }
        return arr;
    }

    private void setCells() {
        hallColumn.setCellValueFactory(new PropertyValueFactory<PriceTable, String>("hallname"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<PriceTable, String>("price"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<PriceTable, String>("time"));
    }
}

