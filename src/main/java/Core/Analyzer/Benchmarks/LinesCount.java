package Core.Analyzer.Benchmarks;

import Core.Analyzer.IBenchmark;
import Core.Entry;
import Core.Parser.Models.Class;
import Core.Parser.Models.File;
import Core.Parser.Models.Method;
import Core.Parser.Models.Object;

import java.util.ArrayList;
import java.util.List;

public class LinesCount implements IBenchmark {

    public Types Type() {
        return Types.LinesCount;
    }

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
        return new Entry(object, this.Type(), object.Body().length);
    }
}
