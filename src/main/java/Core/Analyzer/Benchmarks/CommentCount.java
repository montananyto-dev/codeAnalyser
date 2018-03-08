package Core.Analyzer.Benchmarks;

import Core.Analyzer.IBenchmark;
import Core.Entry;
import Core.Parser.Models.Class;
import Core.Parser.Models.Field;
import Core.Parser.Models.File;
import Core.Parser.Models.Method;
import Core.Parser.Models.Object;

import java.util.ArrayList;
import java.util.List;

public class CommentCount implements IBenchmark {

    private int sum = 0;

    public Types Type() {
        return Types.CommentCount;
    }

    public Entry[] run(File model) {
        List<Entry> entries = new ArrayList<>();
        for (Class c : model.Classes){
            analyzeClass(c);
        }
        entries.add(new Entry(model, this.Type(), sum));
        return entries.toArray(new Entry[entries.size()]);
    }

    private void analyzeClass(Class model){
        countComments(model);
        for(Class c : model.Classes){
            analyzeClass(c);
        }
        for (Field f : model.Fields){
            countComments(f);
        }
        for (Method m : model.Methods){
            countComments(m);
        }
    }

    private void countComments(Object obj) {
        sum += obj.Comments().length;
    }
}
