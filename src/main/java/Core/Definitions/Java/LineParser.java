package Core.Definitions.Java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LineParser {
    private static List<String> _words;
    private static String _current;
    private static char[] _delimiters = {'(',')',
                                        '{','}',
                                        '[',']',
                                        '<','>',
                                        '=','+','-','*','/',
                                        ';',','};

    public LineParser(char[] delimiters){
        _delimiters = delimiters;
    }

    public static String[] parse(String line) {
        _current = "";
        _words = new ArrayList<>();
        boolean insideString = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"' && !_words.get(_words.size()-1).equals("/")){
                insideString = !insideString;
            }
            if (!insideString) {
                if (c == ' ') {
                    _words.add(_current);
                    _current = "";
                    continue;
                }
                if (isDelimiter(c)) {
                    _words.add(_current);
                    _words.add("" + c);
                    _current = "";
                    continue;
                }
            }
            _current += c;
        }
        if (!_current.equals("")){
            _words.add(_current);
        }
        consolidateWords();
        return _words.toArray(new String[_words.size()]);
    }

    public static String[] mergeScope(String[] line, String open, String close){
        List<String> result = new ArrayList<>();
        boolean isMerging = false;
        String value = "";
        for (String aLine : line) {
            if (aLine.equals(open)) {
                isMerging = true;
                value = "";
            }
            if (isMerging) {
                value += aLine;
            }
            else {
                result.add(aLine);
            }
            if (aLine.equals(close)) {
                isMerging = false;
                result.set(result.size()-1, result.get(result.size()-1)+value);
            }
        }
        return result.toArray(new String[result.size()]);
    }

    public static String[] eliminateGenerics(String[] line){
        List<String> result = new ArrayList<>();
        boolean skip = false;
        for (int i = 0; i < line.length; i++){
            if (line[i].equals("<")){
                skip = true;
            }
            if (!skip) result.add(line[i]);
            if (line[i].equals(">"))
                skip = false;
        }
        return result.toArray(new String[result.size()]);
    }

    public static String[] mergeGenerics(String[] line){
        return mergeScope(line, "<", ">");
    }

    private static void consolidateWords(){
        List<String> consolidated = new ArrayList<>(_words);
        consolidated.removeAll(Collections.singleton(""));
        consolidated.removeAll(Collections.singleton(null));
        consolidated = consolidatePair(consolidated, new String[]{"-","+","*","/"},
                                                     new String[]{"=", "/", "*"});
        consolidated = consolidatePair(consolidated, new String[]{"[", "{", "<"},
                                                     new String[]{"]", "}", ">"}, true);

        _words = consolidated;
    }

    private static List<String> consolidatePair(List<String> words, String[] starts, String[] ends){
        List<String> cons = new ArrayList<>();
        for (int i = 0; i < words.size(); i++){
            String val = words.get(i);
            if (contains(starts, val) && i+1 < words.size()){
                String next = words.get(i+1);
                if (contains(ends, next)){
                    cons.add(val+next);
                    i+=1;
                    continue;
                }
            }
            cons.add(val);
        }
        return cons;
    }

    private static List<String> consolidatePair(List<String> words, String[] starts, String[] ends, boolean mergeWithPervious){
        if (!mergeWithPervious) return consolidatePair(words, starts, ends);
        List<String> cons = new ArrayList<>();
        for (int i = 0; i < words.size(); i++){
            String val = words.get(i);
            if (contains(starts, val)){
                if (i+1 >= words.size()) {
                    cons.add(val);
                    continue;
                }
                String next = words.get(i+1);
                if (contains(ends, next)){
                    String prev = cons.get(cons.size()-1);
                    cons.set(cons.size()-1, prev + val + next);
                    i+=1;
                    continue;
                }
            }
            cons.add(val);
        }
        return cons;
    }

    private static boolean contains(String[] list, String value){
        for (String str:list) {
            if (str.equals(value)) return true;
        }
        return false;
    }


    private static boolean isDelimiter(char c){
        for(int i = 0; i < _delimiters.length; i++){
            if (_delimiters[i] == c) return true;
        }
        return false;
    }
}
