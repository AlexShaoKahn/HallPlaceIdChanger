import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Hall {
    private String siteId;
    private String hallId;
    private String name;
    private List<Sector> sectors = new ArrayList<>();

    public void parseJsonHall(String jsonHall) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONArray jsonHallArray = (JSONArray) parser.parse(jsonHall);
        Iterator<JSONObject> hallIterator = jsonHallArray.iterator();
        JSONObject jsonHallObject = new JSONObject(hallIterator.next());
        siteId = (jsonHallObject.get("siteId").toString());
        hallId = (jsonHallObject.get("hallId").toString());
        name = ((String) jsonHallObject.get("name"));
        JSONArray jsonSectorsArray = (JSONArray) parser.parse(jsonHallObject.get("sectors").toString());
        Iterator<JSONObject> sectorsIterator = jsonSectorsArray.iterator();
        while (sectorsIterator.hasNext()) {
            JSONObject jsonSectorObject = new JSONObject(sectorsIterator.next());
            Sector sector = new Sector();
            sector.setSectorId(jsonSectorObject.get("sectorId").toString());
            sector.setName((String) jsonSectorObject.get("name"));
            sector.setX((Long) jsonSectorObject.get("x"));
            sector.setY((Long) jsonSectorObject.get("y"));
            sector.setHeight((Long) jsonSectorObject.get("height"));
            sector.setWidth((Long) jsonSectorObject.get("width"));
            JSONArray jsonRowsArray = (JSONArray) parser.parse(jsonSectorObject.get("rows").toString());
            Iterator<JSONObject> rowsIterator = jsonRowsArray.iterator();
            while (rowsIterator.hasNext()) {
                JSONObject jsonRowObject = new JSONObject(rowsIterator.next());
                Row row = new Row();
                row.setRowId((Long) jsonRowObject.get("rowId"));
//                row.setNumber(Long.toString((Long) jsonRowObject.get("number")));
                row.setNumber(jsonRowObject.get("number").toString());
                JSONArray jsonPlacesArray = (JSONArray) parser.parse(jsonRowObject.get("places").toString());
                Iterator<JSONObject> placesIterator = jsonPlacesArray.iterator();
                while (placesIterator.hasNext()) {
                    JSONObject jsonPlaceObject = new JSONObject(placesIterator.next());
                    Place place = new Place();
                    place.setPlaceId((Long) jsonPlaceObject.get("placeId"));
                    place.setNumber(jsonPlaceObject.get("number").toString());
                    place.setStatus(jsonPlaceObject.get("status").toString());
                    place.setX((Long) jsonPlaceObject.get("x"));
                    place.setY((Long) jsonPlaceObject.get("y"));
                    place.setHeight(jsonPlaceObject.get("height").toString());
                    place.setWidth(jsonPlaceObject.get("width").toString());
                    row.getPlaces().add(place);
                }
                sector.getRows().add(row);
            }
            sectors.add(sector);
        }
    }

    public void changePlacesIds() {
        changePlacesIds(1001);
    }

    public void changePlacesIds(int startId) {
        int placeId = startId;
        for (Sector sector : sectors) {
            for (Row row : sector.getRows()) {
                row.setRowId(placeId);
                for (Place place : row.getPlaces()) {
                    place.setPlaceId(placeId++);
                }
            }
            placeId = startId + placeId / 1000 * 1000 + 1000;
        }

    }

    public void changePlacesIds(int startId, int reserv) {
        int placeId = startId;
        for (Sector sector : sectors) {
            for (Row row : sector.getRows()) {
                for (Place place : row.getPlaces()) {
                    place.setPlaceId(placeId++);
                }
            }
            placeId += reserv;
        }
    }

    public String extractSector() {
        if (sectors.size() > 1) return "There is more than one sector in this hall!(" + sectors.size() + ")";
        else if (isFanSector(sectors.get(0))) return new StringBuilder()
                .append("{")
                .append(sectorCoords(0)).append(",")
                .append(jsonRows(0)).append("}")
                .toString();
        else return new StringBuilder()
                    .append("{")
                    .append(jsonRows(0))
                    .append("}")
                    .toString();
    }

    private boolean isFanSector(Sector sector) {
        for (Row row : sector.getRows()) {
            for (Place place : row.getPlaces()) {
                if (!place.getNumber().equals("-1")) return false;
            }
        }
        return true;
    }

    private String sectorCoords(int i) {
        Sector sector = sectors.get(i);
        return new StringBuilder()
                .append(addKey("x")).append(sector.getX()).append(",")
                .append(addKey("y")).append(sector.getY()).append(",")
                .append(addKey("height")).append(sector.getHeight()).append(",")
                .append(addKey("width")).append(sector.getWidth())
                .toString();
    }

    private String jsonRows(int i) {
        StringBuilder jsonRows = new StringBuilder();
        jsonRows.append(addKey("rows")).append("[");
        List<Row> rows = sectors.get(i).getRows();
        for (int j = 0; j < rows.size(); j++) {
            jsonRows.append("{");
            jsonRows
                    .append(addKey("rowId")).append(rows.get(j).getRowId()).append(",")
                    .append(addKey("number")).append(addStringValue(rows.get(j).getNumber())).append(",")
                    .append(jsonPlaces(i, j));
            jsonRows.append("}");
            if (j < rows.size() - 1) jsonRows.append(",");
        }
        jsonRows.append("]");
        return jsonRows.toString();
    }

    private String jsonPlaces(int i, int j) {
        StringBuilder jsonPlaces = new StringBuilder();
        jsonPlaces.append(addKey("places")).append("[");
        List<Place> places = sectors.get(i).getRows().get(j).getPlaces();
        for (int k = 0; k < places.size(); k++) {
            jsonPlaces.append("{");
            jsonPlaces
                    .append(addKey("placeId")).append(places.get(k).getPlaceId()).append(",")
                    .append(addKey("number")).append(addStringValue(places.get(k).getNumber())).append(",")
                    .append(addKey("status")).append(addStringValue(places.get(k).getStatus())).append(",")
                    .append(addKey("x")).append(places.get(k).getX()).append(",")
                    .append(addKey("y")).append(places.get(k).getY()).append(",")
                    .append(addKey("height")).append(addStringValue(places.get(k).getHeight())).append(",")
                    .append(addKey("width")).append(addStringValue(places.get(k).getWidth()));
            jsonPlaces.append("}");
            if (k < places.size() - 1) jsonPlaces.append(",");
        }
        jsonPlaces.append("]");
        return jsonPlaces.toString();
    }

    private String addKey(String key) {
        return new StringBuffer().append(addStringValue(key)).append(":").toString();
    }

    private String addStringValue(String value) {
        return new StringBuffer().append("\"").append(value).append("\"").toString();
    }

    public String getIDsRange() {
        return sectors.get(0).getRows().get(0).getPlaces().get(0).getPlaceId() + " - " +
                sectors.get(sectors.size() - 1).getRows().get(sectors.get(sectors.size() - 1).getRows().size() - 1).getPlaces().get(sectors.get(sectors.size() - 1).getRows().get(sectors.get(sectors.size() - 1).getRows().size() - 1).getPlaces().size() - 1).getPlaceId();
    }

    public String toJsonString() {
        StringBuilder jsonHall = new StringBuilder();
        jsonHall
                .append("[{")
                .append(addKey("siteId")).append(addStringValue(siteId)).append(",")
                .append(addKey("hallId")).append(addStringValue(hallId)).append(",")
                .append(addKey("name")).append(addStringValue(name)).append(",")
                .append(addKey("sectors")).append("[");
        for (int i = 0; i < sectors.size(); i++) {
            jsonHall
                    .append("{")
                    .append(addKey("sectorId")).append(addStringValue(sectors.get(i).getSectorId())).append(",")
                    .append(addKey("name")).append(addStringValue(sectors.get(i).getName())).append(",")
                    .append(sectorCoords(i)).append(",")
                    .append(jsonRows(i))
                    .append("}");
            if (i < sectors.size() - 1) jsonHall.append(",");
        }
        jsonHall
                .append("]")
                .append("}]");
        return jsonHall.toString();
    }

    @Override
    public String toString() {
        StringBuilder hallInfo = new StringBuilder();
        hallInfo
                .append("siteId ").append(siteId).append("\r\n")
                .append("hallId: ").append(hallId).append("\r\n")
                .append("name: ").append(name).append("\r\n")
                .append("sectors:\r\n");
        for (Sector sector : sectors) {
            hallInfo
                    .append("sectorId: ").append(sector.getSectorId()).append(", ")
                    .append("name: ").append(sector.getName()).append(", ")
                    .append("id: ").append(sector.getRows().get(0).getPlaces().get(0).getPlaceId())
                    .append(" - ").append(sector.getRows().get(sector.getRows().size() - 1).getPlaces().get(sector.getRows().get(sector.getRows().size() - 1).getPlaces().size() - 1).getPlaceId())
                    .append("\r\n");
        }
        return hallInfo.toString();
    }
}
