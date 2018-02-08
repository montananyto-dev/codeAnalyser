package Core;

import Core.Analyzer.Benchmarks.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// used to report the values of benchmark operations.
public class Report {

    // contains analyze list of benchmarks run on various objects.
    public List<Entry> Entries = new ArrayList<>();

    // Adds a single benchmark result to the report.
    public void add(String name, String[] path, Types type, int result){
        Entries.add(new Entry(name, path, type, result));
    }
    // Adds an array of entries to the report.
    public void add(Entry[] entries){
        Collections.addAll(Entries, entries);
    }
    // Represents the model for analyze report entry.
    public class Entry{
        public final String Name;
        public final String[] Path;
        public final Types Type;
        public final int Value;

        public Entry(String name, String[] path, Types type, int value){
            Name = name;
            Path = path;
            Type = type;
            Value = value;
        }
    }

}
