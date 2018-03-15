package Core.Definitions.Java;

import Core.Analyzer.Benchmarks.Types;
import Core.Entry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BenchmarkTest {

    @Test
    public void cyclomaticTest(){
        Entry result = null;
        for (Entry ent: TestHelper.Report.Entries){
            if (ent.Name.equals("parse(String)") && ent.Type == Types.Cyclomatic) {result = ent; break;}
        }
        int expected = 4;
        assertEquals(expected, result.Values.get("value"));

    }

    @Test
    public void linesCountTest(){
        Entry result = null;
        for (Entry ent : TestHelper.Report.Entries){
            if (ent.Name.equals("add(Field, val)") && ent.Type == Types.LinesCount){result = ent; break;}
        }
        int expected = 2;
        assertEquals(expected, result.Values.get("value"));

    }

    @Test
    public void methodsCountTest(){
        Entry result = null;
        for (Entry ent : TestHelper.Report.Entries){
            if (ent.Type == Types.MethodCount){ result = ent; break;}
        }
        int expected = 3;
        assertEquals(expected, result.Values.get("value"));
    }

    @Test
    public void wordsCountTest(){
       Entry result = null;
       for (Entry ent : TestHelper.Report.Entries){
           if (ent.Type == Types.WordCount) {result = ent; break;}
       }
       int expected = 191;
       assertEquals(expected, result.Values.get("value"));
    }

    @Test
    public void commentsTest(){
        Entry result = null;
        for (Entry ent : TestHelper.Report.Entries){
            if (ent.Type == Types.CommentCount) {result = ent; break;}
        }
        int expected = 6;
        assertEquals(expected, result.Values.get("value"));
    }
}
