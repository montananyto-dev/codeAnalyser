package Core.Definitions.Java;

import Core.Definitions.SupportedLanguages;
import Core.Entry;
import Core.Exceptions.DefinitionNotFoundException;
import Core.Exceptions.NotSupportedException;
import Core.ProcessManager;
import Core.Report;
import com.sun.deploy.util.StringUtils;
import org.junit.Test;

import java.util.Arrays;

import static java.lang.System.out;

public class TestMain {
    @Test
    public void testMe() {
        String javaFileName = "test.java";
        ProcessManager manager = new ProcessManager();
        try {
            Report report = manager.process(TestHelper.Body, javaFileName, SupportedLanguages.Java);
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
