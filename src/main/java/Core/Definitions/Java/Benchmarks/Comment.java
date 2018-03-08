package Core.Definitions.Java.Benchmarks;

import Core.Entry;
import Core.Parser.Models.File;
import Core.Parser.Models.IObject;
import Core.Parser.Models.Method;
import Core.Parser.Models.Field;

import java.util.ArrayList;
import java.util.List;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Comment extends Core.Analyzer.Benchmarks.CommentBase {

    @Override
    public Entry[] run(File model) {
 
        List<Entry> entries = new ArrayList<>();
        
        int sum = 0;
        
        for (Core.Parser.Models.Class c: model.Classes) {
            
            for (Method m : c.Methods){
                sum += model.Comments().length;
                entries.add(countComments(sum));
            }

            for (Field f : c.Fields){
                sum += model.Comments().length;
                entries.add(countComments(sum));
            }
            
        }
//        return entries.toArray(new Entry[entries.size()]);
            return new Entry[sum];
    }

    private Entry countComments(int sum){
        Entry entry = new Entry();
        entry.Value = sum;
        return entry;
    }
    
}
