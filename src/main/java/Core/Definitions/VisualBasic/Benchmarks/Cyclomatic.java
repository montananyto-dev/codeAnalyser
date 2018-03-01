package Core.Definitions.VisualBasic.Benchmarks;

import Core.Parser.LineParser;

public class Cyclomatic extends Core.Analyzer.Benchmarks.CyclomaticBase {

    @Override
    protected String[] setKeywords() {
        return new String[0];
    }

    @Override
    protected LineParser setLineParser() {
        return null;
    }
}
