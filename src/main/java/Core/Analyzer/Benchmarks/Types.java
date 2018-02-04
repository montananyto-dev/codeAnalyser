package Core.Analyzer.Benchmarks;

// lists the types of benchmarks.
// using an enum so that the possible benchmarks are set and we can continually reference back here instead of repeatedly
//      parsing the benchmark names.
public enum Types {
    Comment,
    Cyclomatic,
    Halstead,
    Lines
}
