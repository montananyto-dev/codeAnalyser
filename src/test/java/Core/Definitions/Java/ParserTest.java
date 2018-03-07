package Core.Definitions.Java;

import Core.Definitions.SupportedLanguages;
import Core.Entry;
import Core.Exceptions.DefinitionNotFoundException;
import Core.Exceptions.NotSupportedException;
import Core.Parser.IParser;
import Core.Parser.Models.File;
import Core.ProcessManager;
import Core.Report;
import com.sun.deploy.util.StringUtils;
import org.junit.Test;

import java.util.Arrays;

import static java.lang.System.out;

public class ParserTest {
    private static String[] _body = {
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

    @Test
    public void testJavaParseFile(){
        IParser p = new Definition().Parser();
        File model = p.parse(_body);
        assert (model.Classes.size() == 1 &&
                model.Classes.get(0).Methods.size() == 3 &&
                model.Classes.get(0).Fields.size() == 8);
    }

    @Test
    public void testJavaBenchmarks(){

        for(String s : _body){
            System.out.println(s);
        }
        ProcessManager manager = new ProcessManager();
        try {
            Report report = manager.process(_body, "file1.java", SupportedLanguages.Java);
            for (Entry e : report.Entries) {
                out.println(StringUtils.join(Arrays.asList(e.Path), ".") + ":" + e.Name + ": " + e.Type.name() + ":" + e.Values);
            }
            assert true;
        } catch (DefinitionNotFoundException e) {
            e.printStackTrace();
        } catch (NotSupportedException e) {
            e.printStackTrace();
        }

    }

}