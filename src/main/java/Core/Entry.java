package Core;

import Core.Analyzer.Benchmarks.Types;

// Represents the model for analyze report entry.
public class Entry {
    public String Name;
    public String[] Path;
    public Types Type;
    public int Value;

    public Entry(){}

    public Entry(String name, String[] path, Types type, int value){
        Name = name;
        Path = path;
        Type = type;
        Value = value;
    }
}
