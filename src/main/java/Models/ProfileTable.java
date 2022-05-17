package Models;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class ProfileTable {
    private final StringProperty price;
    private final StringProperty hallname;
    private final StringProperty moviename;

    public ProfileTable(String price, String hallname, String moviename) {
        this.price = new SimpleStringProperty(price);
        this.hallname = new SimpleStringProperty(hallname);
        this.moviename = new SimpleStringProperty(moviename);
    }

    public String getPrice() {
        return price.get();
    }

    public StringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getHallname() {
        return hallname.get();
    }

    public StringProperty hallnameProperty() {
        return hallname;
    }

    public void setHallname(String hallname) {
        this.hallname.set(hallname);
    }

    public String getMoviename() {
        return moviename.get();
    }

    public StringProperty movienameProperty() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename.set(moviename);
    }
}
