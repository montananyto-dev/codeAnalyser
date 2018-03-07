package Core.Definitions.Java.Benchmarks;

import Core.Definitions.Java.Definition;
import Core.Parser.LineParser;

public class Cyclomatic extends Core.Analyzer.Benchmarks.CyclomaticBase {


    @Override
    protected String[] setKeywords() {
        return new String[] {"if", "while", "for", "case"};
    }

    @Override
    protected LineParser setLineParser() {
        return new Core.Definitions.Java.LineParser();
    }
}
