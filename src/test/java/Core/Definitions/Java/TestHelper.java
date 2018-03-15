package Core.Definitions.Java;

import Core.Analyzer.Benchmarks.Types;
import Core.Definitions.SupportedLanguages;
import Core.Entry;
import Core.Exceptions.DefinitionNotFoundException;
import Core.Exceptions.NotSupportedException;
import Core.ProcessManager;
import Core.Report;

import java.util.concurrent.ThreadLocalRandom;

public class TestHelper {

    public static final String[] Body = {
            "    /* this",
            "       a test",
            "       block */",
            "package Core.Parser.Models;",
            "",
            "import java.util.ArrayList;",
            "import java.util.List;",
            "// this is a test",
            "public class Class implements IObject {",
            "    private IObject  _parent;",
            "    private String _name;",
            "    public String Type;",
            "    public List<Field> Fields = new ArrayList<>(test t",
            "",
            "                                                value v);",
            "    public List<Method> Methods = new ArrayList<>();",
            "    public List<Class> Classes = new ArrayList<>();",
            "    // test field comments",
            "    public String[] Comments;",
            "    private String[] Body;",
            "    // this is a test",
            "    public void add(Field field,",
            "                    val v){",
            "        field.Parent = this;",
            "        Fields.add(field);",
            "    }",
            "    public void add(Method method){",
            "        method.Parent = this;",
            "        Methods.add(method);",
            "    }",
            "    public static String[] parse(String line) {",
            "        _current = \"\";",
            "        _words = new ArrayList<>();",
            "        for (int i = 0; i < line.length(); i++) {",
            "            char c = line.charAt(i);",
            "            if (c == ' '){",
            "                _words.add(_current);",
            "                _current = \"\";",
            "                continue;",
            "            }",
            "            if (isDelimiter(c)){",
            "                _words.add(_current);",
            "                _words.add(\"\"+c);",
            "                _current = \"\";",
            "                continue;",
            "            }",
            "            _current += c;",
            "        }",
            "        consolidateWords();",
            "        return _words.toArray(new String[_words.size()]);",
            "    }",
            "}"};

    public static final Report Report;

    static {
        Core.Report report = null;
        try {
            report = new ProcessManager().process(Body, "testFile.java", SupportedLanguages.Java);
        } catch (DefinitionNotFoundException | NotSupportedException e) {
            e.printStackTrace();
        }
        Report = report;
    }

    public static String generateString(int length){
        String res = "";
        for (int i = 0; i < length;i++){
            res += (char) generateRandom(97, 122);
        }
        return res;
    }

    public static  <T> T getRandomValue(T[] array){
        return array[generateRandom(0, array.length-1)];
    }

    public static int generateRandom(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max+1);
    }


    public static Entry buildEntry(){
        String[] path = new String[4];
        for (int i = 0; i < 4;i++) path[i] = generateString(4);
        return new Entry(generateString(9),
                         path,
                         getRandomValue(Types.values()),
                         generateRandom(0, 100));
    }
}
