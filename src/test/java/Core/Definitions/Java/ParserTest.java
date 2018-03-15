package Core.Definitions.Java;

import Core.Definitions.SupportedLanguages;
import Core.Entry;
import Core.Exceptions.DefinitionNotFoundException;
import Core.Exceptions.NotSupportedException;
import Core.Parser.Models.File;
import Core.ProcessManager;
import Core.Report;
import com.sun.deploy.util.StringUtils;
import org.junit.Test;

import java.util.Arrays;

import static java.lang.System.out;

public class ParserTest {


    @Test
    public void testJavaParseFile(){
        Parser p = (Parser) new Definition().Parser();
        File model = p.parse(TestHelper.Body);
        assert (model.Classes.size() == 1 &&
                model.Classes.get(0).Methods.size() == 3 &&
                model.Classes.get(0).Fields.size() == 8);
    }

    @Test
    public void testJavaBenchmarks(){

        for(String s : TestHelper.Body){
            System.out.println(s);
        }
        ProcessManager manager = new ProcessManager();
        try {
            Report report = manager.process(TestHelper.Body, "file1.java", SupportedLanguages.Java);
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