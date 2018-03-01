package Core.Analyzer.Benchmarks;

import Core.Analyzer.IBenchmark;
import Core.Entry;
import Core.Parser.Helper;
import Core.Parser.LineParser;
import Core.Parser.Models.Class;
import Core.Parser.Models.File;
import Core.Parser.Models.Method;

import java.util.ArrayList;
import java.util.List;

public abstract class CyclomaticBase implements IBenchmark {

    public Types Type() {
        return Types.Cyclomatic;
    }

    private final String[] _keywords = setKeywords();
    private final LineParser _LineParser = setLineParser();

    protected abstract String[] setKeywords();
    protected abstract LineParser setLineParser();

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

        int sum = 1;
        for (String line : method.Body()){
            String[] words = _LineParser.parse(line);
            for (String word : words){
                if (Helper.contains(_keywords, word)) sum++;
            }
        }
        return new Entry(method, this.Type(), sum);
    }
}
