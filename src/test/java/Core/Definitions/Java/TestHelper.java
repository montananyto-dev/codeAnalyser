package Core.Definitions.Java;

import Core.Analyzer.Benchmarks.Types;
import Core.Entry;

import java.util.concurrent.ThreadLocalRandom;

public class TestHelper {

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
