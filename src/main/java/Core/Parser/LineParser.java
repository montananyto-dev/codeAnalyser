package Core.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class LineParser {

    private Parser _parent;
    private char[] _delimiters = setDelimiters();

    protected abstract char[] setDelimiters();

    protected List<ConsolidationProfile> _consolidationProfiles = setConsolidationProfiles();

    protected abstract List<ConsolidationProfile> setConsolidationProfiles();

    public LineParser(){}

    public LineParser(Parser parent){
        _parent = parent;
    }

    public String[] parse(String line) {
        List<String> words = new ArrayList<>();
        String current = "";
        boolean insideString = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"' && !current.endsWith("\\")){
                insideString = !insideString;
            }
            if (!insideString) {
                if (c == ' ') {
                    words.add(current);
                    current = "";
                    continue;
                }
                if (isDelimiter(c)) {
                    words.add(current);
                    words.add("" + c);
                    current = "";
                    continue;
                }
            }
            current += c;
        }
        if (!current.equals("")){
            words.add(current);
        }
        words = consolidateWords(words);
        countScopeChanges(words);
        return words.toArray(new String[words.size()]);
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

    private List<String> consolidateWords(List<String> words){
        List<String> consolidated = new ArrayList<>(words);
        consolidated.removeAll(Collections.singleton(""));
        consolidated.removeAll(Collections.singleton(null));
        for (ConsolidationProfile profile : _consolidationProfiles){
            consolidated = consolidatePair(consolidated, profile.Start, profile.End, profile.MergeWithPrevious);
        }
        words = consolidated;
        return words;
    }

    private void countScopeChanges(List<String> words){
        if (_parent == null) return;
        for (String word : words){
            _parent.iterateScope(word);
        }
    }

    private static List<String> consolidatePair(List<String> words, String[] starts, String[] ends){
        List<String> cons = new ArrayList<>();
        for (int i = 0; i < words.size(); i++){
            String val = words.get(i);
            if (Helper.contains(starts, val) && i+1 < words.size()){
                String next = words.get(i+1);
                if (Helper.contains(ends, next)){
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
            if (Helper.contains(starts, val)){
                if (i+1 >= words.size()) {
                    cons.add(val);
                    continue;
                }
                String next = words.get(i+1);
                if (Helper.contains(ends, next)){
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

    private boolean isDelimiter(char c){
        for(int i = 0; i < _delimiters.length; i++){
            if (_delimiters[i] == c) return true;
        }
        return false;
    }

    protected class ConsolidationProfile{
        public final String[] Start;
        public final String[] End;
        public final boolean MergeWithPrevious;

        public ConsolidationProfile(String[] start, String[] end, boolean mergeWithPrevious){
            Start = start;
            End = end;
            MergeWithPrevious = mergeWithPrevious;
        }
    }
}
