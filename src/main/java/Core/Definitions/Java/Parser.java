package Core.Definitions.Java;

import Core.Definitions.SupportedLanguages;
import Core.Parser.Helper;
import Core.Parser.IParser;
import Core.Parser.Models.*;
import Core.Parser.Models.Class;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser implements IParser {

    private String[] _modifiers = {"private", "public", "protected",
                                   "static", "transient", "volatile", "const",
                                   "abstract", "final", "default", "native", "strictfp", "synchronized"};
    private String[] _objects = {"class", "interface", "enum"};
    private String[] _inheritance = {"implements", "extends"};
    private String[] _imports = {"import"};
    private String[] _package = {"package"};
    private String[] _comment = {"//", "/*"};
    private String[] _scopeOpens = {"{", "("};
    private String[] _scopeCloses = {"}", ")"};

    private int _line = 0;
    private String[] _body;
    private int _scopeopen = 0;
    private int _scopeClose = 0;
    private int _scopeLevel = 0;

    private List<String> _commentBuffer;

    public static final LineParser LineParser = new LineParser();


    @Override
    public File parse(String[] rawText) {
        _line = 0;
        _body = rawText;
        _commentBuffer = new ArrayList<>();
        return parseFile();
        //stuff
    }

    private File parseFile(){
        File file = new File();
        file.Type = SupportedLanguages.Java;
        file.Body(_body);
        for (; _line < _body.length; _line++){
            String[] line = LineParser.parse(_body[_line]);
            if (line.length <= 0) continue;
            File.ContentTypes type = determineFileContent(line);
            if (type == null) continue;
            switch (type){
                case Import:
                    file.ExternalLibraries.add(parseImport(line));
                    break;
                case Package:
                    file.Package = parsePackage(line);
                    break;
                case Object:
                    file.add(parseClass(line));
                    break;
                case Comment:
                    _commentBuffer.addAll(Arrays.asList(parseComment(line[0])));
                    break;
            }
        }
        return file;
    }

    private Class parseClass(String[] currentLine) {
        _scopeLevel+=1;
        Class cl = new Class();
        String[] cleanedLine = Helper.remove(currentLine, _modifiers);
        cl.Type = cleanedLine[0];
        cl.Name(cleanedLine[1]);
        cl.Comments(getComments());
        List<String> body = new ArrayList<>();
        if (Helper.contains(cleanedLine, "{")){
            _scopeopen++;
        }
        for (_line += 1; _line < _body.length; _line++){
            String[] line = LineParser.parse(_body[_line]);
            if (line.length <= 0) continue;
            if (Helper.contains(line, "}")){
                _scopeClose++;
                if (_scopeopen == _scopeClose) break;
            }
            body.add(_body[_line]);
            Class.ContentTypes type = determineClassContent(line);
            if (type == null) {
                continue;
            }
            if (type == Class.ContentTypes.Comment){ _commentBuffer.addAll(Arrays.asList(parseComment(line[0]))); continue;}
            if (type == Class.ContentTypes.Method){ cl.add(parseMethod(line)); continue;}
            if (type == Class.ContentTypes.Object){ cl.add(parseClass(line)); continue;}
            if (type == Class.ContentTypes.Field) cl.add(parseField(line));
        }
        cl.Body(body.toArray(new String[body.size()]));
        _scopeLevel -=1;
        return cl;
    }

    private Field parseField(String[] line) {
        Field field = new Field();
        field.Comments(getComments());
        List<String> body = new ArrayList<>();
        line = LineParser.mergeGenerics(line);
        if (Helper.contains(Arrays.copyOfRange(_modifiers, 0, 2), line[0])) {
            field.Accessibility = line[0];
        }
        else{
            field.Accessibility = _modifiers[0];
        }
        line = Helper.remove(line, _modifiers);
        field.Type = line[0];
        field.Name(line[1]);
        for(; _line < _body.length; _line++){
            line = LineParser.parse(_body[_line]);
            body.add(_body[_line]);
            if (Helper.contains(line, ";"))break;
        }
        field.Body(body.toArray(new String[body.size()]));
        return field;
    }

    private Method parseMethod(String[] line) {
        Method method = new Method();
        method.Comments(getComments());
        line = Helper.remove(line, _modifiers);
        method.Type = line[0];
        method.Name(line[1]);
        method.add(parseParameters());
        List<String> body = new ArrayList<>();
        for (_line+=1;_line < _body.length; _line++){
            String[] current = LineParser.parse(_body[_line]);
            if (checkIterateScope(current)){
                if (checkScopeEnded()) break;
            }
            body.add(_body[_line]);
        }
        method.Body(body.toArray(new String[body.size()]));
        return method;
    }

    private Parameter[] parseParameters() {
        List<Parameter> params = new ArrayList<>();
        boolean closed = false;
        while (!closed){
            String[] line = LineParser.mergeGenerics(LineParser.parse(_body[_line]));
            for (int i = Helper.find(line, "("); i < line.length; i++) {
                if (line[i].equals(",")) continue;
                if (checkIterateScope(line[i])){
                    if (checkScopeEnded()){
                        closed = true;
                        break;
                    }
                    continue;
                }
                Parameter param = new Parameter();
                param.Type = line[i];
                param.Name(line[i + 1]);
                params.add(param);
                i += 1;
            }
            if (line[line.length-1].equals("{")) _scopeopen++;
            if (closed) break;
            _line++;
        }
        return params.toArray(new Parameter[params.size()]);
    }

    private boolean checkIterateScope(String word){
        if (Helper.contains(_scopeOpens, word)) {
            _scopeopen++;
            return true;
        }
        if (Helper.contains(_scopeCloses, word)) {
            _scopeClose++;
            return true;
        }
        return false;
    }

    private boolean checkIterateScope(String[] line){
        boolean res = false;
        for (String word: line){
            if (checkIterateScope(word)) res = true;
        }
        return res;
    }

    private boolean checkScopeEnded(){
        return _scopeopen - _scopeClose == _scopeLevel;
    }

    private Class.ContentTypes determineClassContent(String[] line){
        line = Helper.remove(line, _modifiers);
        String[] reduced = LineParser.eliminateGenerics(line);
        if (reduced[0].startsWith("//") || reduced[0].startsWith("/*")) return Class.ContentTypes.Comment;
        if (Helper.contains(reduced, _objects)) return Class.ContentTypes.Object;
        if (reduced.length < 3) return null;
        if (reduced[2].equals(";") || reduced[2].equals("=")) return Class.ContentTypes.Field;
        if (reduced[2].equals("(")) return Class.ContentTypes.Method;
        return null;
    }

    private File.ContentTypes determineFileContent(String[] line){
        if (Helper.contains(_imports, line[0])) return File.ContentTypes.Import;
        if (Helper.contains(_package, line[0])) return File.ContentTypes.Package;
        if (Helper.contains(_objects, line)) return File.ContentTypes.Object;
        if (Helper.contains(_comment, line[0])) return File.ContentTypes.Comment;
        return null;
    }

    private String[] parseComment(String commentChar){
        if (commentChar.equals("//")){
            return new String[]{_body[_line]};
        }
        String[] current;
        List<String> res = new ArrayList<>();
        do{
            res.add(_body[_line]);
            current = LineParser.parse(_body[_line]);
            _line++;
        }while (!Helper.contains(current, "*/"));
        return res.toArray(new String[res.size()]);
    }

    private String parseImport(String[] line){
        return line[1];
    }

    private String parsePackage(String[] line){
        return line[1];
    }

    private String[] getComments(){
        String[] com = _commentBuffer.toArray(new String[_commentBuffer.size()]);
        _commentBuffer.clear();
        return com;
    }

    public static class LineParser extends Core.Parser.LineParser {

        protected LineParser() {
            _delimiters = new char[] {'(',')', '{','}', '[',']', '<','>',
                                      '=','+','-','*','/',
                                      ';',',',
                                      '&','|','%'};
            _consolidationProfiles.add(new ConsolidationProfile(new String[]{"-","+","*","/", "&", "|", "%"},
                                                                new String[]{"=", "/", "*", "&", "|"},
                                                                false));
            _consolidationProfiles.add(new ConsolidationProfile(new String[]{"[", "{", "<"},
                                                                new String[]{"]", "}", ">"},
                                                                true));
        }
    }
}
