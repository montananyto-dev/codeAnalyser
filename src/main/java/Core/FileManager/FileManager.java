package Core.FileManager;

import Core.Analyzer.Benchmarks.Types;
import Core.Entry;
import Core.FileManager.Exceptions.FileNotValidException;
import Core.FileManager.Exceptions.OutputDirectoryNotSetException;
import Core.Report;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


// class to handle all File based interactions.
// should strive to implement all methods in a static way so that we can reference this object statically.
public class FileManager {

    public static String[] read(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        return lines.toArray(new String[lines.size()]);
    }

    public static void write(Report report, File file) throws OutputDirectoryNotSetException, IOException {
        if (!file.isDirectory()) throw new OutputDirectoryNotSetException();
        String[] csv = buildCsv(report);
        File out = new File(file.getAbsolutePath() + "\\" + report.Name + longToString(report.Timestamp.getTime()) + ".csv");
        out.createNewFile();
        FileWriter writer = new FileWriter(out);
        for (String line : csv){
            writer.write(line);
        }
        writer.close();
    }



    public static Report readCsv(File file) throws IOException, FileNotValidException {
        List<String> rawText = Files.readAllLines(file.toPath());
        return readCsv(rawText.toArray(new String[rawText.size()]));
    }

    public static Report readCsv(String[] rawText) throws FileNotValidException {
        if (rawText.length == 0) throw new FileNotValidException();
        Report result = decodeReport(rawText[0]);
        for (int i = 1; i < rawText.length; i++) {
            result.add(decodeEntry(rawText[i]));
        }
        return result;
    }

    public static String[] buildCsv(Report report){
        List<String> values = new ArrayList<>();
        values.add(csvEncode(report));
        for (Entry e : report.Entries){
            String val = "";
            val += e.Type.name() + ",";
            val += csvEncode(e.Name) + ",";
            val += csvEncode(e.Path) + ",";
            val += e.Value + "\n";
            values.add(val);
        }
        return values.toArray(new String[values.size()]);
    }

    public static Report decodeReport(String line){
        String[] values = decodeLine(line);
        Report result = new Report(values[0]);
        result.Timestamp = new Timestamp(Long.parseLong(values[1]));
        return result;
    }

    public static Entry decodeEntry(String line){
        Entry result = new Entry();
        String[] decoded = decodeLine(line);
        result.Type = Types.find(decoded[0]);
        result.Name = decoded[1];
        result.Path = csvDecodeArray(decoded[2]);
        result.Value = Integer.parseInt(decoded[3]);
        return result;

    }

    private static String[] decodeLine(String line){
        List<String> values = new ArrayList<>();
        String value = "";
        boolean insideQuotes = false;
        for (char c : line.toCharArray()){
            if (c == ',' && !insideQuotes){
                values.add(value);
                value = "";
                continue;
            }
            if (c == '"'){
                insideQuotes = !insideQuotes;
                continue;
            }
            value += c;
        }
        if (!value.equals("")) values.add(value);
        return values.toArray(new String[values.size()]);
    }

    private static String csvEncode(String string){
       return "\"" + string + "\"";
    }

    private static String csvEncode(String[] array){
        if (array.length == 0) return "";
        String value = "\"";
        for (String str : array){
            value += str + ",";
        }
        value = value.substring(0, value.length() - 2);
        value += "\"";
        return value;
    }

    private static String csvEncode(Report report){
        return report.Name + "," + report.Timestamp.getTime() + "\n";
    }

    private static String[] csvDecodeArray(String array){
        List<String> values = new ArrayList<>();
        String value = "";
        for (char c : array.toCharArray()){
            if (c == ',') {
                values.add(value);
                value = "";
            }
            value += c;
        }
        return values.toArray(new String[values.size()]);
    }

    private static String longToString(long val){
        return String.format("%020d", val);
    }
}
