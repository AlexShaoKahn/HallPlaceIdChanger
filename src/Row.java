import java.util.ArrayList;
import java.util.List;

public class Row {
    private long rowId;
    private String number;
    private List<Place> places = new ArrayList<>();

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Place> getPlaces() {
        return places;
    }
}
