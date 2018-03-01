package Core.Definitions.Java.Benchmarks;
import Core.Parser.LineParser;

public class Halstead extends Core.Analyzer.Benchmarks.HalsteadBase {

    @Override
    protected String[] setOperators() {
        return new String[] {"auto","extern","register","static","typedef","virtual",
                             "virtual","mutable","inline",
                             "const","friend","volatile","transient","final","break",
                             "case", "continue","default", "do", "else","enum", "for",
                             "goto", "if", "new", "return", "asm", "operator",
                             "private", "protected", "public","sizeof", "struct",
                             "switch", "union", "while", "this", "namespace",
                             "using", "try", "catch","throw", "throws", "finally",
                             "strictfp", "instanceof", "interface", "extends",
                             "implements","abstract", "concrete", "const_cast",
                             "static_cast", "dynamic_cast","reinterpret_cast",
                             "typeid", "template", "explicit", "true", "false",
                             "typename",
                             "(","{","[","<","=","+","-","*","/",";",",","&","|",
                             "!", "!=", "%" ,"%=" ,"&", "&&" ,"||" ,"&=", "*", "*=", "+",
                             "++", "+=", ",", "-" ,"--" ,"-=","->" ,"." ,"...", "/=" ,":" ,
                             "::", "<" ,"<<" ,"<<=" ,"<=" , "==" ,">" ,">=" ,">>" ,">>>" ,
                             ">>=",">>>=" ,"?" ,"^" ,"^=" ,"|" ,"|=" ,"~" ,";" ,"=&" ,
                             "“ “" ,"‘" ,"‘" ,"#" ,"##" ,"~"};
    }

    @Override
    protected LineParser setLineParser() {
        return Core.Definitions.Java.Parser.LineParser;
    }
}
