package Core.Analyzer.Benchmarks;

import Core.Analyzer.IBenchmark;
import Core.Entry;
import Core.Parser.Models.File;

public abstract class LinesBase implements IBenchmark {
    @Override
    public Types Type() {
        return Types.Lines;
    }

    public abstract Entry[] run(File model);
}
