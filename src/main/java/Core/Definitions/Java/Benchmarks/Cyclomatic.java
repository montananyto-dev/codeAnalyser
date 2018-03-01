package Core.Definitions.Java.Benchmarks;

import Core.Definitions.Java.Parser;
import Core.Entry;
import Core.Parser.Helper;
import Core.Parser.Models.Class;
import Core.Parser.Models.File;
import Core.Parser.Models.Method;

import java.util.ArrayList;
import java.util.List;

public class Cyclomatic extends Core.Analyzer.Benchmarks.CyclomaticBase {

    private String[] _keywords = {"if", "while", "for", "case"};

    @Override
    public Entry[] run(File model) {
        List<Entry> entries = new ArrayList<>();
        for (Class c : model.Classes){
            entries.addAll(analyzeClass(c));
        }
        return entries.toArray(new Entry[entries.size()]);
    }

    private List<Entry> analyzeClass(Class model){
        List<Entry> entries = new ArrayList<>();
        for(Class c : model.Classes){
            entries.addAll(analyzeClass(c));
        }
        for (Method m : model.Methods){
            entries.add(analyzeMethod(m));
        }
        return entries;
    }

    private Entry analyzeMethod(Method method){
        Entry entry = new Entry(method, this.Type());
        entry.Value = 1;
        for (String line : method.Body()){
            String[] words = Parser.LineParser.parse(line);
            for (String word : words){
                if (Helper.contains(_keywords, word)) entry.Value ++;
            }
        }
        return entry;
    }

}
