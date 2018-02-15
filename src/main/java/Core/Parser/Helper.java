package Core.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Helper {
    public static boolean contains(String[] list, String value){
        for (String str:list) {
            if (str.equals(value)) return true;
        }
        return false;
    }

    public static boolean contains(String[] list, String[] values){
        for (String val: values){
            if (contains(list, val)) return true;
        }
        return false;
    }

    public static String[] remove(String[] list, String value){
        List<String> str = new ArrayList<>(Arrays.asList(list));
        str.remove(value);
        return str.toArray(new String[str.size()]);
    }

    public static String[] remove(String[] list, String[] values){
        List<String> str = new ArrayList<>(Arrays.asList(list));
        str.removeAll(Arrays.asList(values));
        return str.toArray(new String[str.size()]);
    }

    public static int find(String[] list, String value){
        for (int i = 0; i < list.length; i++){
            if (list[i].equals(value)) return i;
        }
        return 0;
    }
}
