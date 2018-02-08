package Core.Definitions;

import Core.Analyzer.IAnalyzer;
import Core.Parser.IParser;
//  interface which defines a Language Definition.
//  Definitions provide a mapping for language specific implementations of the IParser and Analyzer objects.
//  by utilizing this design pattern, we do not actually create any objects until they are actively used,
//      which means there is very little impact for having a large amount of definitions.
public interface IDefinition {
    // returns the value of the language which is supported by the definition.
    SupportedLanguages Language();
    // returns a new copy of the language specific parser.
    IParser Parser();
    // returns a new copy of the language specific Analyzer.
    IAnalyzer Analyzer();
}

