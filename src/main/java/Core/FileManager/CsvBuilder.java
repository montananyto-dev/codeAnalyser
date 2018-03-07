package Core.FileManager;

import Core.Analyzer.Benchmarks.Types;
import Core.Entry;
import Core.FileManager.Exceptions.FileNotValidException;
import Core.Report;

import java.io.File;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvBuilder {
    public static Report read(File file) throws Exception {
        List<String> rawText = Files.readAllLines(file.toPath());
        return read(rawText.toArray(new String[rawText.size()]));
    }

    public static Report read(String[] rawText) throws Exception {
        if (rawText.length == 0) throw new FileNotValidException();
        Report result = decodeReport(rawText[0]);
        for (int i = 1; i < rawText.length; i++) {
            result.add(decodeEntry(rawText[i]));
        }
        return result;
    }

    public static String[] build(Report report){
        List<String> values = new ArrayList<>();
        values.add(encode(report));
        for (Entry e : report.Entries){
            values.add(encode(e));
        }
        return values.toArray(new String[values.size()]);
    }

    private static Report decodeReport(String line){
        String[] values = decodeLine(line);
        Report result = new Report(values[0]);
        result.Timestamp = new Timestamp(Long.parseLong(values[1]));
        return result;
    }

    private static Entry decodeEntry(String line) throws Exception {
        String[] decoded = decodeLine(line);
        return new Entry(decoded[1],
                         decodeArray(decoded[2]),
                         Types.find(decoded[0]),
                         decodeValues(decoded[3]));

    }

    private static String[] decodeArray(String array){
        List<String> values = new ArrayList<>();
        String value = "";
        for (char c : array.toCharArray()){
            if (c == ',') {
                values.add(value);
                value = "";
                continue;
            }
            value += c;
        }
        if (!value.equals("")) values.add(value);
        return values.toArray(new String[values.size()]);
    }

    private static Map<String, Number> decodeValues(String values) throws Exception {
        String[] kvps = decodeArray(values);
        if (kvps.length % 2 != 0) throw new Exception("UNEVEN Key Value Pairs");
        Map<String, Number> result = new HashMap<>();
        for (int i = 0; i < kvps.length; i+=2){
            result.put(kvps[i], decodeNumber(kvps[i+1]));
        }
        return result;
    }

    private static Number decodeNumber(String val){
        if (val.contains(".")) return Double.parseDouble(val);
        return Long.parseLong(val);
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
        for (int i = 0; i < values.size(); i++) {
            String val = values.get(i);
            values.set(i, val.replace("\n", "")) ;
        }
        return values.toArray(new String[values.size()]);
    }

    private static String encode(String string){
        return "\"" + string + "\"";
    }

    private static String encode(String[] array){
        if (array.length == 0) return "";
        String value = "\"";
        for (String str : array){
            value += str + ",";
        }
        value = value.substring(0, value.length() - 1);
        value += "\"";
        return value;
    }

    private static String encode(Report report){
        return report.Name + "," + report.Timestamp.getTime() + "\n";
    }

    private static String encode(Entry entry){
        String val = "";
        val += entry.Type.name() + ",";
        val += encode(entry.Name) + ",";
        val += encode(entry.Path) + ",";
        val += encode(entry.Values) + "\n";
        return val;
    }

    private static String encode(Map<String, Number> values){
        List<String> kvps = new ArrayList<>();
        for(String key : values.keySet()){
            kvps.add(key);
            kvps.add(values.get(key).toString());
        }
        return encode(kvps.toArray(new String[0]));
    }
}
