package Core.Definitions.Java.Benchmarks;

import Core.Entry;
import Core.Parser.Models.Class;
import Core.Parser.Models.File;
import Core.Parser.Models.Method;

import java.util.ArrayList;
import java.util.List;

public class Lines extends Core.Analyzer.Benchmarks.LinesBase {

    @Override
    public Entry[] run(File model) {
        List<Entry> entries = new ArrayList<>();
        for (Class c: model.Classes) {
            for (Method m : c.Methods){
                entries.add(analyzeMethod(m));
            }
        }
        return entries.toArray(new Entry[entries.size()]);
    }

    private Entry analyzeMethod(Method method){
        Entry entry = new Entry();
        entry.Name = method.FullName();
        entry.Type = this.Type();
        entry.Path = method.Path();
        entry.Value = method.Body().length;
        return entry;
    }
}
