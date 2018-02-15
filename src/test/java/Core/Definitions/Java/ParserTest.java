package Core.Definitions.Java;

import Core.Entry;
import Core.Exceptions.DefinitionNotFoundException;
import Core.Exceptions.NotSupportedException;
import Core.Parser.Models.Class;
import Core.Parser.Models.File;
import Core.Parser.Models.Parameter;
import Core.ProcessManager;
import Core.Report;
import com.sun.deploy.util.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;

public class ParserTest {

    @Test
    public void TestDetermineClassObjectType(){
        String method = "public Map<SupportedLanguages[], IDefinition> _definitions(test t, val v)";
        String field = "public void test;";
        String comment = "// this is a comment";
        String obj = "public class test{}";
        Parser p = new Parser();
        assert (p.determineClassContent(LineParser.parse(obj)) == Class.ContentTypes.Object &&
                p.determineClassContent(LineParser.parse(field)) == Class.ContentTypes.Field &&
                p.determineClassContent(LineParser.parse(method)) == Class.ContentTypes.Method &&
                p.determineClassContent(LineParser.parse(comment)) == Class.ContentTypes.Comment);
    }

    @Test
    public void testParseFile(){
        String[] body = {"    /* this",
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
                         "                                                value v);",
                         "    public List<Method> Methods = new ArrayList<>();",
                         "    public List<Class> Classes = new ArrayList<>();",
                         "    // test field comments",
                         "    public String[] Comments;",
                         "    private String[] _body;",
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

        Parser p = new Parser();
        File model = p.parse(body);
        out.println(); // break on debugging to view the model. // holy shit it works.
    }

    @Test
    public void testLinesBenchmark(){
        ProcessManager manager = new ProcessManager();
        String[] body = {"package Core.Parser.Models;",
                         "",
                         "import java.util.ArrayList;",
                         "import java.util.List;",
                         "// this is a test",
                         "public class Class implements IObject {",
                         "    private IObject  _parent;",
                         "    private String _name;",
                         "    public String Type;",
                         "    public List<Field> Fields = new ArrayList<>(test t",
                         "                                                value v);",
                         "    public List<Method> Methods = new ArrayList<>();",
                         "    public List<Class> Classes = new ArrayList<>();",
                         "    // test field comments",
                         "    public String[] Comments;",
                         "    private String[] _body;",
                         "    // this is a test",
                         "    public void add(Field field,",
                         "                    val v){",
                         "        field.Parent = this;",
                         "        Fields.add(field);",
                         "    }",
                         "    /* this",
                         "       a test",
                         "       block */",
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

        try {
            Report report = manager.process(body, "file1.java");
            for (Entry e : report.Entries) {
                out.println(StringUtils.join(Arrays.asList(e.Path), ".") + ":" + e.Name + ": " + e.Type.name() + ":" + e.Value);
            }
            assert true;
        } catch (DefinitionNotFoundException e) {
            e.printStackTrace();
        } catch (NotSupportedException e) {
            e.printStackTrace();
        }

    }
}