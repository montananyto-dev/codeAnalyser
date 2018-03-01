package Core.Definitions.VisualBasic;

import Core.Analyzer.Analyzer;
import Core.Analyzer.Benchmarks.LinesBase;
import Core.Analyzer.IAnalyzer;
import Core.Analyzer.IBenchmark;
import Core.Definitions.IDefinition;
import Core.Definitions.SupportedLanguages;
import Core.Definitions.VisualBasic.Benchmarks.Comments;
import Core.Definitions.VisualBasic.Benchmarks.Cyclomatic;
import Core.Definitions.VisualBasic.Benchmarks.Halstead;
import Core.Parser.IParser;


public class Definition implements IDefinition {

    @Override
    public SupportedLanguages Language() {
        return SupportedLanguages.VisualBasic;
    }

    @Override
    public IParser Parser() {
        return new Parser();
    }

    @Override
    public IAnalyzer Analyzer() {
        return new Analyzer(new IBenchmark[]{new Comments(),
                                             new Cyclomatic(),
                                             new Halstead(),
                                             new LinesBase()});
    }

    @Override
    public String FileSignature() {
        return ".vb";
    }
}
