package Core.Definitions.Java.Benchmarks;

import Core.Entry;
import Core.Parser.Models.Class;
import Core.Parser.Models.File;
import Core.Parser.Models.Method;
import Core.Parser.Models.Object;

import java.util.ArrayList;
import java.util.List;

public class Lines extends Core.Analyzer.Benchmarks.LinesBase {

    @Override
    public Entry[] run(File model) {
        List<Entry> entries = new ArrayList<>();
        entries.add(analyzeBody(model));
        for (Class c: model.Classes) {
            for (Method m : c.Methods){
                entries.add(analyzeBody(m));
            }
        }
        return entries.toArray(new Entry[entries.size()]);
    }

    private Entry analyzeBody(Object object){
        Entry entry = new Entry(object, this.Type());
        entry.Value = object.Body().length;
        return entry;
    }
}
