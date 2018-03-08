package Core.Analyzer.Benchmarks;

// lists the types of benchmarks.
// using an enum so that the possible benchmarks are set and we can continually reference back here instead of repeatedly
//      parsing the benchmark names.
public enum Types {
    CommentCount,
    Cyclomatic,
    Halstead,
    LinesCount,
    MethodCount,
    WordCount;

    public static Types find(String name){
        for (Types type : Types.values()) {
            if (type.name().equals(name)) return type;
        }
        return null;
    }
}
