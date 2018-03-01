package Core.Definitions.VisualBasic.Benchmarks;

import Core.Definitions.Java.Parser;
import Core.Entry;
import Core.Entry;
import Core.Parser.Helper;
import Core.Parser.LineParser;
import Core.Parser.Models.Class;
import Core.Parser.Models.File;
import Core.Parser.Models.Field;
import Core.Parser.Models.Method;
import Core.Parser.Models.Object;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Halstead extends Core.Analyzer.Benchmarks.HalsteadBase {

    @Override
    protected String[] setOperators() {
        return new String[0];
    }

    @Override
    protected LineParser setLineParser() {
        return null;
    }
}

