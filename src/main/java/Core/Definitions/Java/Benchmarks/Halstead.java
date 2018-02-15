package Core.Definitions.Java.Benchmarks;

import Core.Entry;
import Core.Parser.Models.File;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Halstead extends Core.Analyzer.Benchmarks.HalsteadBase {
    @Override
    public Entry[] run(File model) {
        return new Entry[0];
    }
}
