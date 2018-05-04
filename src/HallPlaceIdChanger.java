import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.*;

public class HallPlaceIdChanger {
    public static void main(String[] args) throws IOException, ParseException {
        //args=new String[]{"c:\\Temp\\JAVA\\test\\box2.json"};
        //args=new String[]{"gui"};
        Hall hall = new Hall();
        if (args.length == 1 && args[0].toLowerCase().equals("gui"))
            SwingUtilities.invokeLater(() -> new ChangerWindow());
        else if (args.length < 1) {
            System.out.println("Usege: java -jar HallPlaceIdChanger.jar <filename.json> [startId(def:1001)] [reserv(def:1000+)] [raws]\r\n" +
                    "filename.json: text file with JSON hall code\r\n" +
                    "startId: start id (1001 if empty)\r\n" +
                    "reserv: adding free ids for sector (1000+ if empty)\r\n" +
                    "raws: extracts one sector only for multi-sector halls\r\n\r\n" +
                    "Usage: java -jar HallPlaceIdChanger.jar gui\r\n" +
                    "ui: start GUI");
        } else if (!argsConsistsRows(args)) {
            switch (args.length) {
                case 1:
                    hall.parseJsonHall(readJsonFile(args[0]));
                    hall.changePlacesIds();
                    writeJsonFile(createOutputFileName(args[0]), hall.toJsonString());
                    break;
                case 2:
                    hall.parseJsonHall(readJsonFile(args[0]));
                    hall.changePlacesIds(Integer.parseInt(args[1]));
                    writeJsonFile(createOutputFileName(args[0]), hall.toJsonString());
                    break;
                case 3:
                    hall.parseJsonHall(readJsonFile(args[0]));
                    hall.changePlacesIds(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                    writeJsonFile(createOutputFileName(args[0]), hall.toJsonString());
                    break;
            }
            System.out.println(hall.toString());
        } else {
            switch (args.length) {
                case 2:
                    hall.parseJsonHall(readJsonFile(args[0]));
                    hall.changePlacesIds();
                    writeJsonFile(createOutputFileName(args[0]), hall.extractSector());
                    break;
                case 3:
                    hall.parseJsonHall(readJsonFile(args[0]));
                    hall.changePlacesIds(Integer.parseInt(args[1]));
                    writeJsonFile(createOutputFileName(args[0]), hall.toJsonString());
                    break;
                case 4:
                    hall.parseJsonHall(readJsonFile(args[0]));
                    hall.changePlacesIds(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                    writeJsonFile(createOutputFileName(args[0]), hall.toJsonString());
                    break;
            }
            System.out.println(hall.toString());
        }

    }

    private static boolean argsConsistsRows(String[] args) {
        for (String arg : args) {
            if (arg.toLowerCase().toLowerCase().equals("rows")) return true;
        }
        return false;
    }

    private static String readJsonFile(String filename) throws IOException {
        StringBuilder jsonString = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
        }
        return jsonString.toString();
    }

    private static void writeJsonFile(String filename, String jsonText) throws IOException {

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(jsonText);
            writer.flush();
        }
    }

    private static String createOutputFileName(String filename) {
        int dot = filename.lastIndexOf('.');
        String base = (dot == -1) ? filename : filename.substring(0, dot);
        String extension = (dot == -1) ? "" : filename.substring(dot + 1);
        return base + "_changed." + extension;
    }
}
