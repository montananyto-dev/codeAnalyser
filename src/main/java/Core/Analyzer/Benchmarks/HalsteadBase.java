package Core.Analyzer.Benchmarks;

import Core.Analyzer.IBenchmark;
import Core.Entry;
import Core.Parser.Helper;
import Core.Parser.LineParser;
import Core.Parser.Models.Class;
import Core.Parser.Models.File;
import Core.Parser.Models.Method;

import java.util.ArrayList;

public abstract class HalsteadBase implements IBenchmark {

    public Types Type() {
        return Types.Halstead;
    }

    private final String[] Operators = setOperators();
    private final LineParser Parser = setLineParser();
    protected abstract String[] setOperators();
    protected abstract LineParser setLineParser();

    private ArrayList<String> CollectedOperators = new ArrayList<>();
    private ArrayList<String> CollectedOperands = new ArrayList<>();
    private int TotalOperators;
    private int TotalOperands;

    private double _volume;
    private double _difficulty;
    private double _effort;
    private double _timeToCode;
    private double _deliveredBugs;

    public Entry[] run(File model) {
        for (Class c : model.Classes){
            analyzeClass(c);
        }
        calculateResult();
        Entry res = new Entry(model, Type());
        res.Values.put("Volume", _volume);
        res.Values.put("Difficulty", _difficulty);
        res.Values.put("Effort", _effort);
        res.Values.put("Time To Code", _timeToCode);
        res.Values.put("Delivered Bugs", _deliveredBugs);
        return new Entry[]{res};
    }

    private void analyzeClass(Class model){
        for(Class c : model.Classes){
            analyzeClass(c);
        }
        for (Method m : model.Methods){
            analyzeMethod(m);
        }
    }

    private void analyzeMethod(Method method){
        for (String line: method.Body())
        {
            String[] words = Parser.mergeGenerics(Parser.parse(line));
            for (String word: words)
            {
                if (Helper.contains(Operators, word))
                {
                    TotalOperators++;
                    if (!CollectedOperators.contains(word))
                    {
                        CollectedOperators.add(word);
                    }
                }
                else
                {
                    TotalOperands++;
                    if (!CollectedOperands.contains(word))
                    {
                        CollectedOperands.add(word);
                    }
                }

            }

        }
    }

    private void calculateResult() {
        int uTors = CollectedOperators.size();//Unique operators
        int uAnds = CollectedOperands.size();//Unique operands
        int vocabulary = uTors + uAnds;
        int length = TotalOperands + TotalOperators;
        double CalculatedLength = uTors*(Math.log(uTors)/Math.log(2)) + uAnds*(Math.log(uAnds)/Math.log(2)) ;
        _volume = CalculatedLength*(Math.log(vocabulary)/Math.log(2));
        _difficulty = (uTors/2)*(TotalOperands/uAnds);
        _effort = _difficulty*_volume;
        _timeToCode = _effort/18;
        _deliveredBugs = (_volume/3000);//Possibly change volume to Effort(^2/3)
    }
}
