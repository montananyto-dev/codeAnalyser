package Core.Analyzer.Benchmarks;

import Core.Analyzer.IBenchmark;
import Core.Parser.Models.File;
import Core.Report;

public abstract class HalsteadBase implements IBenchmark {
    @Override
    public Types Type() {
        return Types.Halstead;
    }

    public abstract Report.Entry[] run(File model);
}
