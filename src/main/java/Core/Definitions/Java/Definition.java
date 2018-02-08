package Core.Definitions.Java;

import Core.Analyzer.Analyzer;
import Core.Analyzer.IAnalyzer;
import Core.Analyzer.IBenchmark;
import Core.Definitions.IDefinition;
import Core.Definitions.Java.Benchmarks.Comment;
import Core.Definitions.Java.Benchmarks.Cyclomatic;
import Core.Definitions.Java.Benchmarks.Halstead;
import Core.Definitions.Java.Benchmarks.Lines;
import Core.Definitions.SupportedLanguages;
import Core.Parser.IParser;

public class Definition implements IDefinition {

    public SupportedLanguages Language() {
        return SupportedLanguages.Java;
    }

    public IParser Parser() {
        return new Parser();
    }

    public IAnalyzer Analyzer() {

        return new Analyzer(new IBenchmark[]{new Comment(),
                                             new Cyclomatic(),
                                             new Halstead(),
                                             new Lines()});
    }
}
