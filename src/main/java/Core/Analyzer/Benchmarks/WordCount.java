package Core.Analyzer.Benchmarks;

import Core.Analyzer.IBenchmark;
import Core.Entry;
import Core.Parser.Models.File;

import java.util.ArrayList;
import java.util.List;

public class WordCount implements IBenchmark {
    private int sum = 0;

    public Types Type() {
        return Types.WordCount;
    }

    public Entry[] run(File model) {
        List<Entry> entries = new ArrayList<>();
        analyzeFile(model);
        entries.add(new Entry(model, this.Type(), sum));
        return entries.toArray(new Entry[entries.size()]);
    }

    private void analyzeFile(File model){
        for (String str : model.Body()){
            sum += str.split("\\s+").length;
        }
    }
}
