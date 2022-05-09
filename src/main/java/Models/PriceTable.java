package Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class PriceTable {
    private final StringProperty hallname;
    private final StringProperty price;
    private final StringProperty time;

    public PriceTable(String hallname, String price, String time) {
        this.hallname = new SimpleStringProperty(hallname);
        this.price = new SimpleStringProperty(price);
        this.time = new SimpleStringProperty(time);
    }

    @Override
    public String toString() {
        return "PriceTable{" +
                "hallname=" + hallname +
                ", price=" + price +
                ", time=" + time +
                '}';
    }

    public String getHallname() {
        return hallname.get();
    }

    public StringProperty hallnameProperty() {
        return hallname;
    }

    public String getPrice() {
        return price.get();
    }

    public StringProperty priceProperty() {
        return price;
    }

    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }
}
