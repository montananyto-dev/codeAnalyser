package Core;

import Core.Analyzer.Benchmarks.Types;
import Core.Parser.Models.Object;

import java.util.HashMap;
import java.util.Map;

// Represents the model for analyze report entry.
public class Entry {
    public String Name;
    public String[] Path;
    public Types Type;
    public Map<String, Number> Values = new HashMap<>();

    public Entry(){}

    public Entry(Object obj, Types type){
        Name = obj.FullName();
        Path = obj.Path();
        Type = type;
    }

    public Entry(Object obj, Types type, Number value){
        Name = obj.FullName();
        Path = obj.Path();
        Type = type;
        Values.put("value", value) ;
    }

    public Entry(Object obj, Types type, String valueName, Number value){
        Name = obj.FullName();
        Path = obj.Path();
        Type = type;
        Values.put(valueName, value);
    }

    public Entry(String name, String[] path, Types type, Number value){
        Name = name;
        Path = path;
        Type = type;
        Values.put("value", value);
    }

    public Entry(String name, String[] path, Types type, Map<String, Number> values){
        Name = name;
        Path = path;
        Type = type;
        Values = values;
    }
}
