package Core.Analyzer;

import Core.Analyzer.Benchmarks.*;
import Core.Parser.Models.File;
import Core.Report;

import java.util.List;

// interface which describes the default model for an analyzer.
//
public interface IAnalyzer {

    Report analyze(File model);
}
