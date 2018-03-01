package Core.Parser;

import Core.Definitions.SupportedLanguages;
import Core.Parser.Models.Class;
import Core.Parser.Models.Field;
import Core.Parser.Models.File;
import Core.Parser.Models.Method;

import java.util.ArrayList;
import java.util.List;

public abstract class Parser implements IParser{
    protected final String[] _modifiers = setModifiers();
    protected final String[] _objects = setObjects();
    protected final String[] _inheritance = setInheritance();
    protected final String[] _imports = setImports();
    protected final String[] _package = setPackage();
    protected final String[] _commentline = setCommentLine();
    protected final String[] _commentBlock = setCommentBlock();
    protected final String[] _scopeOpens = setScopeOpen();
    protected final String[] _scopeCloses = setScopeClose();


    protected int _line = 0;
    protected String[] _body;
    protected int _scopeopen = 0;
    protected int _scopeClose = 0;
    protected int _scopeLevel = 0;


    protected abstract String[] setModifiers();
    protected abstract String[] setObjects();
    protected abstract String[] setInheritance();
    protected abstract String[] setImports();
    protected abstract String[] setPackage();
    protected abstract String[] setCommentLine();
    protected abstract String[] setCommentBlock();
    protected abstract String[] setScopeOpen();
    protected abstract String[] setScopeClose();

    protected List<String> _commentBuffer;

    protected final LineParser _lineParser = setLineParser();

    protected abstract Core.Parser.LineParser setLineParser();

    public File parse(String[] rawText) {
        _line = 0;
        _body = rawText;
        _commentBuffer = new ArrayList<>();
        return parseFile();
        //stuff
    }


    protected File parseFile(){
        File file = new File();
        file.Type = SupportedLanguages.Java;
        file.Body(_body);
        for (; _line < _body.length; _line++){
            String[] line = _lineParser.parse(_body[_line]);
            if (line.length <= 0) continue;
            File.ContentTypes type = determineFileContent(line);
            if (type == null) continue;
            DetermineFileAction(file, line, type);
        }
        return file;
    }

    protected abstract void DetermineFileAction(File file, String[] line, File.ContentTypes type);

    protected abstract File.ContentTypes determineFileContent(String[] line);

    protected Class parseClass(String[] currentLine) {
        _scopeLevel+=1;
        Class cl = buildClass(currentLine);
        List<String> body = new ArrayList<>();
        for (_line++; _line < _body.length; _line++){
            String[] line = _lineParser.parse(_body[_line]);
            if (line.length <= 0) continue;
            if (ScopeCheck(line))
                break;
            body.add(_body[_line]);
            Class.ContentTypes type = determineClassContent(line);
            determineClassAction(cl, line, type);
        }
        cl.Body(body.toArray(new String[body.size()]));
        _scopeLevel -=1;
        return cl;
    }

    protected abstract void determineClassAction(Class cl, String[] line, Class.ContentTypes type);

    protected abstract Class.ContentTypes determineClassContent(String[] line);

    protected abstract Class buildClass(String[] currentLine);

    protected Field parseField(String[] line) {
        Field field = buildField(line);
        List<String> body = new ArrayList<>();
        for(; _line < _body.length; _line++){
            line = _lineParser.parse(_body[_line]);
            body.add(_body[_line]);
            if (shouldStopParsingField(line))break;
        }
        field.Body(body.toArray(new String[body.size()]));
        return field;
    }

    protected abstract boolean shouldStopParsingField(String[] line);

    protected abstract Field buildField(String[] line);

    protected Method parseMethod(String[] line) {
        Method method = buildMethod(line);
        List<String> body = new ArrayList<>();
        for (_line+=1;_line < _body.length; _line++){
            String[] current = _lineParser.parse(_body[_line]);
            if (checkIterateScope(current)){
                if (checkScopeEnded()) break;
            }
            body.add(_body[_line]);
        }
        method.Body(body.toArray(new String[body.size()]));
        return method;
    }

    protected boolean checkScopeEnded() {
        return _scopeopen - _scopeClose == _scopeLevel;
    }

    protected abstract Method buildMethod(String[] line);

    protected boolean ScopeCheck(String[] line) {
        if (Helper.contains(line, "}")){
            _scopeClose++;
            if (_scopeopen - _scopeClose == _scopeLevel) return true;
        }
        return false;
    }

    protected boolean checkIterateScope(String word){
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

    protected boolean checkIterateScope(String[] line){
        boolean res = false;
        for (String word: line){
            if (checkIterateScope(word)) res = true;
        }
        return res;
    }

    protected String[] parseComment(String commentChar){
        if (Helper.contains(_commentline, commentChar)){
            int start = _body[_line].indexOf(commentChar);
            return new String[]{_body[_line].substring(start)};
        }
        String[] current;
        List<String> res = new ArrayList<>();
        do{
            res.add(_body[_line]);
            current = _lineParser.parse(_body[_line]);
            _line++;
        }while (!Helper.contains(current, _commentBlock[1]));
        return res.toArray(new String[res.size()]);
    }

    protected String[] getComments(){
        String[] com = _commentBuffer.toArray(new String[_commentBuffer.size()]);
        _commentBuffer.clear();
        return com;
    }
}
