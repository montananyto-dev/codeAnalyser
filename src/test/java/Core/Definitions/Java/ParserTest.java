package Core.Definitions.Java;

import Core.Parser.Models.Class;
import Core.Parser.Models.File;
import Core.Parser.Models.Parameter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static java.lang.System.out;

public class ParserTest {

    @Test
    public void TestDetermineClassObjectType(){
        String val = "Map<SupportedLanguages[], IDefinition> _definitions(test t, v";
        Parser p = new Parser();
        Class.ContentTypes expected = Class.ContentTypes.Method;
        Class.ContentTypes result = p.determineClassContent(LineParser.parse(val));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParameParser(){
        String[] body = {"public static void test(t1<g1, g2> v1, t2 v2);"};
        Parser p = new Parser();
        List<Parameter> params = p.parseParameters(body, 0);
        for (Parameter param: params){
            out.println(param.Type + ":" + param.Name);
        }
    }

    @Test
    public void testParseFile(){
        String[] body = {"package Core.Parser.Models;",
                         "",
                         "import java.util.ArrayList;",
                         "import java.util.List;",
                         "",
                         "public class Class implements IObject {",
                         "    private IObject  _parent;",
                         "    private String _name;",
                         "    public String Type;",
                         "    public List<Field> Fields = new ArrayList<>();",
                         "    public List<Method> Methods = new ArrayList<>();",
                         "    public List<Class> Classes = new ArrayList<>();",
                         "    public String[] Comments;",
                         "    private String[] _body;",
                         "",
                         "    public void add(Field field){",
                         "        field.Parent = this;",
                         "        Fields.add(field);",
                         "    }",
                         "",
                         "    public void add(Method method){",
                         "        method.Parent = this;",
                         "        Methods.add(method);",
                         "    }",
                         "}"};

        Parser p = new Parser();
        File model = p.parse(body);
        out.println(); // break on debugging to view the model. // holy shit it works.
    }
}