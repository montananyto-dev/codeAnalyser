package Core.Analyzer.Benchmarks;

import Core.Analyzer.IBenchmark;
import Core.Entry;
import Core.Parser.Models.File;

public abstract class CyclomaticBase implements IBenchmark {
    @Override
    public Types Type() {
        return Types.Cyclomatic;
    }

    public abstract Entry[] run(File model);
}
