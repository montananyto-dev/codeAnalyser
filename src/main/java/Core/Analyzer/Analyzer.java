package Core.Analyzer;

import Core.Parser.Models.File;
import Core.Report;

import java.util.Arrays;
import java.util.List;

// Default implementation of the Analyzer.
// To use, simply pass an array of benchmarks into the constructor.
public class Analyzer implements IAnalyzer {
    // list of the benchmarks to be run.
    public List<IBenchmark> Benchmarks;

    public Analyzer(IBenchmark[] benchmarks){
        Benchmarks = Arrays.asList(benchmarks);
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
