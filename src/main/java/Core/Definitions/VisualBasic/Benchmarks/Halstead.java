package Core.Definitions.Java.Benchmarks;

import Core.Definitions.Java.Parser;
import Core.Entry;
import Core.Entry;
import Core.Parser.Helper;
import Core.Parser.Models.Class;
import Core.Parser.Models.File;
import Core.Parser.Models.Field;
import Core.Parser.Models.Method;
import Core.Parser.Models.Object;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Halstead extends Core.Analyzer.Benchmarks.HalsteadBase {
    
    
    public String[] Operators = {"auto","extern","register","static","typedef","virtual",
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
    
    //Unsure if this is sufficient for Operands
    public String[] Operands = {"bool","byte","int","float",
        "char","double","long","short","signed","unsigned","void"};
    public ArrayList<String> CollectedOperators;
    public ArrayList<String> CollectedOperands;
    public int TotalOperators;
    public int TotalOperands;
    
    public double CalculatedProgramLength;
    public double Volume;
    public double Difficulty;
    public double Effort;
    public double TimeToCode;
    public double DeliveredBugs;
    
   
    @Override
    public Entry[] run(File model) {
        List<Entry>entries = new ArrayList<>();
        for(Class c : model.Classes)
        {
            LoopClass(c);
        }
        CalcOuts();
        Entry entry = new Entry(model, this.Type());
        entry.Name = "Calculated Program Length";
        entry.Value = (int) CalculatedProgramLength;
        entries.add(entry);
        entry.Name = "Volume";
        entry.Value = (int) Volume;
        entries.add(entry);
        entry.Name = "Difficulty";
        entry.Value = (int) Difficulty;
        entries.add(entry);
        entry.Name = "Effort";
        entry.Value = (int) Effort;
        entries.add(entry);
        entry.Name = "Time required to program";
        entry.Value = (int) TimeToCode;
        entries.add(entry);
        entry.Name = "Number of delivered bugs";
        entry.Value = (int) DeliveredBugs;
        entries.add(entry);
        
        
        return entries.toArray(new Entry[entries.size()]);
    }
    private void CalcOuts()
    {
        double Utors = CollectedOperators.size();//Unique operators
        double Uands = CollectedOperands.size();//Unique operands
        double Vocab = Utors + Uands;
        int ProgramLength = TotalOperators + TotalOperands;
        CalculatedProgramLength = Utors*(Math.log(Utors)/Math.log(2)) + Uands*(Math.log(Uands)/Math.log(2)) ;
        Volume = ProgramLength*(Math.log(Vocab)/Math.log(2));
        Difficulty = (Utors/2)*(TotalOperands/Uands);
        Effort = Difficulty*Volume;
        TimeToCode = Effort/18;
        DeliveredBugs = (Volume/3000);//Possibly change volume to Effort(^2/3)
    }
            
    private List<Entry> LoopClass(Class C)
    {   
        List<Entry>entries = new ArrayList<>();
            for (Class c : C.Classes){
                LoopClass(c);
            }
            for (Method m : C.Methods){
                entries.add(analyzeMethod(m));
            }
            for (Field f : C.Fields){
                entries.add(analyzeField(f));
            }
            
        return entries;
        
        
    }
    

    private Entry analyzeMethod(Method method){
        
        
        
        Entry entry = new Entry(method, this.Type());
        for (String line: method.Body())
        {
            String[] words = Parser.LineParser.parse(line);
            for (String word: words)
            {
                if (Helper.contains(Operators, word))
                {
                    TotalOperators++;
                    String[] CoOperators = new String[CollectedOperators.size()];
                    CoOperators = CollectedOperators.toArray(CoOperators);
                    if (!Helper.contains(CoOperators, word))
                    {
                        CollectedOperators.add(word);                        
                    }
                }
                else if (Helper.contains(Operands, word))
                {
                    TotalOperands++;;
                    String[] CoOperands = new String[CollectedOperands.size()];
                    CoOperands = CollectedOperands.toArray(CoOperands);
                    if (!Helper.contains(CoOperands, word))
                    {
                        CollectedOperands.add(word);                        
                    }
                }
                
            }
            
        }
        entry.Value = 0;
        return entry;
    }
    private Entry analyzeField(Field field){
       Entry entry = new Entry(field, this.Type());
        for (String line: field.Body())
        {
            String[] words = Parser.LineParser.parse(line);
            for (String word: words)
            {
                if (Helper.contains(Operators, word))
                {
                    TotalOperators++;
                    String[] CoOperators = new String[CollectedOperators.size()];
                    CoOperators = CollectedOperators.toArray(CoOperators);
                    if (!Helper.contains(CoOperators, word))
                    {
                        CollectedOperators.add(word);                        
                    }
                }
                else if (Helper.contains(Operands, word))
                {
                    TotalOperands++;;
                    String[] CoOperands = new String[CollectedOperands.size()];
                    CoOperands = CollectedOperands.toArray(CoOperands);
                    if (!Helper.contains(CoOperands, word))
                    {
                        CollectedOperands.add(word);                        
                    }
                }
                
            }
            
        }
        entry.Value = 0;
        return entry;
    }
}

