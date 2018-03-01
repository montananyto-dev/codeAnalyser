package Core;

import Core.Analyzer.Benchmarks.Types;
import Core.Parser.Models.Object;

// Represents the model for analyze report entry.
public class Entry {
    public String Name;
    public String[] Path;
    public Types Type;
    public int Value;

    public Entry(){}

    public Entry(Object obj, Types type){
        Name = obj.FullName();
        Path = obj.Path();
        Type = type;
    }

    public Entry(String name, String[] path, Types type, int value){
        Name = name;
        Path = path;
        Type = type;
        Value = value;
    }
}
