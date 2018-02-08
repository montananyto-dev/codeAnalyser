package Core.Analyzer;

import Core.Analyzer.Benchmarks.Types;
import Core.Parser.Models.File;
import Core.Report;

// Interface which describes a benchmark object.
// Benchmarks are used to describe tests against a file model.
// A benchmark should have a specific algorithm, and can return multiple report entries (for instance if it needs to run on multiple methods/ objects).
public interface IBenchmark {
    // records the type of benchmark. uses Benchmarks.Types enum.
    Types Type();
    // operation which runs on a File model, generates a list of report entries based on the result of the benchmark.
    Report.Entry[] run(File model);
}
