package Core.Analyzer;

import Core.Analyzer.Benchmarks.CommentCount;
import Core.Analyzer.Benchmarks.LinesCount;
import Core.Analyzer.Benchmarks.MethodCount;
import Core.Analyzer.Benchmarks.WordCount;
import Core.Parser.Models.File;
import Core.Report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Default implementation of the Analyzer.
// To use, simply pass an array of benchmarks into the constructor.
public class Analyzer implements IAnalyzer {
    // list of the benchmarks to be run.
    public List<IBenchmark> Benchmarks = new ArrayList<>();

    public Analyzer(IBenchmark[] benchmarks){
        Benchmarks.add(new CommentCount());
        Benchmarks.add(new LinesCount());
        Benchmarks.add(new MethodCount());
        Benchmarks.add(new WordCount());
        Collections.addAll(Benchmarks, benchmarks);
    }

    // used to run benchmarks on a file.
    public Report analyze(File model) {
        Report report = new Report(model.Package);
        for (IBenchmark benchmark: Benchmarks) {
            report.add(benchmark.run(model));
        }
        return report;
    }
}
