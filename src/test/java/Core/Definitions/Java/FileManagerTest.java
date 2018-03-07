package Core.Definitions.Java;

import Core.Analyzer.Benchmarks.Types;
import Core.Entry;
import Core.FileManager.CsvBuilder;
import Core.FileManager.Exceptions.FileNotValidException;
import Core.FileManager.FileManager;
import Core.Report;
import org.junit.Test;

public class FileManagerTest {

    @Test
    public void testCsvSerialization(){
        try {
            Report expected = new Report("test");
            int count = TestHelper.generateRandom(3,5);
            for (int i = 0; i < count;i++){
                expected.add(TestHelper.buildEntry());
            }
            Report result = CsvBuilder.read(CsvBuilder.build(expected));
            if (!expected.Name.equals(result.Name) || expected.Timestamp.getTime() != result.Timestamp.getTime() ||
                    expected.Entries.size() != result.Entries.size())
                assert false;
            else
                assert true;

        } catch (Exception e) {
            e.printStackTrace();
            assert true;
        }

    }


}
