package Core.Definitions.Java.Benchmarks;

import Core.Definitions.Java.LineParser;
import Core.Entry;
import Core.Parser.Helper;
import Core.Parser.Models.Class;
import Core.Parser.Models.File;
import Core.Parser.Models.Method;
import Core.Parser.Models.Object;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Cyclomatic extends Core.Analyzer.Benchmarks.CyclomaticBase {

    private String[] _keywords = {"if", "while", "for", "case"};

    @Override
    public Entry[] run(File model) {
        List<Entry> entries = new ArrayList<>();
        for (Class c : model.Classes){
            for (Method m : c.Methods){
                entries.add(analyzeMethod(m));
            }
        }
        return entries.toArray(new Entry[entries.size()]);
    }

    private Entry analyzeMethod(Method method){
        Entry entry = new Entry(method, this.Type());
        entry.Value  = 0;
        for (String line : method.Body()){
            String[] words = LineParser.parse(line);
            for (String word : words){
                if (Helper.contains(_keywords, word)) entry.Value ++;
            }
        }
        return entry;
    }

}
