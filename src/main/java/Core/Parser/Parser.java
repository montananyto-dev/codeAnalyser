package Core.Parser;

import Core.Definitions.SupportedLanguages;
import Core.Parser.Models.*;
import Core.Parser.Models.Class;

import java.util.ArrayList;
import java.util.List;

public abstract class Parser implements IParser{
    protected final String[] Modifiers = setModifiers();
    protected final String[] Objects = setObjects();
    protected final String[] Inheritance = setInheritance();
    protected final String[] Imports = setImports();
    protected final String[] Package = setPackage();
    protected final String[] Commentline = setCommentLine();
    protected final String[] CommentBlock = setCommentBlock();
    protected final String[] BlockScopeOpens = setScopeOpen();
    protected final String[] BlockScopeCloses = setScopeClose();
    protected final String[] ParameterScopeOpens = setParameterScopeOpens();
    protected final String[] ParameterScopeCloses = setParameterScopeCloses();
    protected final SupportedLanguages Type = setLanguageType();


    protected int Line = 0;
    protected String[] Body;
    private int _blockScopeOpen = 0;
    private int _blockScopeClose = 0;
    private int _blockscopeLevel = 0;
    private int _parameterScopeOpen = 0;
    private int _parameterScopeClose = 0;


    protected abstract String[] setModifiers();
    protected abstract String[] setObjects();
    protected abstract String[] setInheritance();
    protected abstract String[] setImports();
    protected abstract String[] setPackage();
    protected abstract String[] setCommentLine();
    protected abstract String[] setCommentBlock();
    protected abstract String[] setScopeOpen();
    protected abstract String[] setScopeClose();
    protected abstract String[] setParameterScopeOpens();
    protected abstract String[] setParameterScopeCloses();
    protected abstract SupportedLanguages setLanguageType();

    protected List<String> _commentBuffer;

    protected final LineParser _lineParser = setLineParser();

    protected abstract Core.Parser.LineParser setLineParser();

    public File parse(String[] rawText) {
        Line = 0;
        Body = rawText;
        _commentBuffer = new ArrayList<>();
        return parseFile();
        //stuff
    }


    private File parseFile(){
        File file = new File();
        file.Type = Type;
        file.Body(Body);
        for (; Line < Body.length; Line++){
            String[] line = _lineParser.parse(Body[Line]);
            if (line.length <= 0) continue;
            File.ContentTypes type = determineFileContent(line);
            if (type == null) continue;
            determineFileAction(file, line, type);
        }
        return file;
    }

    protected abstract void determineFileAction(File file, String[] line, File.ContentTypes type);

    protected abstract File.ContentTypes determineFileContent(String[] line);

    protected Class parseClass(String[] currentLine) {
        _blockscopeLevel +=1;
        Class cl = buildClass(currentLine);
        List<String> body = new ArrayList<>();
        for (Line++; Line < Body.length; Line++){
            String[] line = _lineParser.parse(Body[Line]);
            if (line.length <= 0) continue;
            if (BlockScopeEnded()) break;
            body.add(Body[Line]);
            Class.ContentTypes type = determineClassContent(line);
            determineClassAction(cl, line, type);
        }
        cl.Body(body.toArray(new String[body.size()]));
        _blockscopeLevel -=1;
        return cl;
    }

    protected abstract void determineClassAction(Class cl, String[] line, Class.ContentTypes type);

    protected abstract Class.ContentTypes determineClassContent(String[] line);

    protected abstract Class buildClass(String[] currentLine);

    protected Field parseField(String[] line) {
        Field field = buildField(line);
        List<String> body = new ArrayList<>();
        if (shouldStopParsingField(line)){
            body.add(Body[Line]);
        }
        else {
            for (Line+=1; Line < Body.length; Line++) {
                body.add(Body[Line]);
                line = _lineParser.parse(Body[Line]);
                if (shouldStopParsingField(line)) break;
            }
        }
        field.Body(body.toArray(new String[body.size()]));
        return field;
    }

    protected abstract boolean shouldStopParsingField(String[] line);

    protected abstract Field buildField(String[] line);

    protected Method parseMethod(String[] line) {
        Method method = buildMethod(line);
        method.add(parseParameters(line));
        List<String> body = new ArrayList<>();
        for (Line +=1; Line < Body.length; Line++){
            String[] current = _lineParser.parse(Body[Line]);
            if (BlockScopeEnded()){break;}
            body.add(Body[Line]);
        }
        method.Body(body.toArray(new String[body.size()]));
        return method;
    }

    protected Parameter[] parseParameters(String[] line) {
        List<Parameter> params = new ArrayList<>();
        boolean closed = false;
        line = Core.Definitions.Java.LineParser.mergeGenerics(line);
        for (; Line < Body.length;){
            for (int i = Helper.find(line, "(")+1; i < line.length; i++) {
                if (line[i].equals(",")) continue;
                if (Helper.contains(ParameterScopeCloses, line[i])) {
                    if (ParameterScopeEnded()) {
                        closed = true;
                        break;
                    }
                }
                Parameter param = buildParameter(line, i);
                params.add(param);
                i += 1;
            }
            if (closed) break;
            Line++;
            line = Core.Definitions.Java.LineParser.mergeGenerics(_lineParser.parse(Body[Line]));
        }
        return params.toArray(new Parameter[params.size()]);
    }

    protected abstract Parameter buildParameter(String[] line, int i);
    protected abstract Method buildMethod(String[] line);

    protected boolean BlockScopeEnded() {
        return _blockScopeClose != 0 && _blockScopeOpen - _blockScopeClose == _blockscopeLevel;
    }

    protected boolean ParameterScopeEnded(){
        return _parameterScopeOpen == _parameterScopeClose;
    }

    protected void iterateScope(String word){
        if (Helper.contains(ParameterScopeOpens, word)){
            _parameterScopeOpen++;
            return;
        }
        if (Helper.contains(ParameterScopeCloses, word)){
            _parameterScopeClose++;
            return;
        }
        if (Helper.contains(BlockScopeOpens, word)){
            _blockScopeOpen++;
            return;
        }
        if (Helper.contains(BlockScopeCloses, word)) {
            _blockScopeClose++;
            return;
        }
    }

    protected String[] parseComment(String commentChar){
        if (Helper.contains(Commentline, commentChar)){
            int start = Body[Line].indexOf(commentChar);
            return new String[]{Body[Line].substring(start)};
        }
        String[] current;
        List<String> res = new ArrayList<>();
        do{
            res.add(Body[Line]);
            current = _lineParser.parse(Body[Line]);
            Line++;
        }while (!Helper.contains(current, CommentBlock[1]));
        Line--;
        return res.toArray(new String[res.size()]);
    }

    protected String[] getComments(){
        String[] com = _commentBuffer.toArray(new String[_commentBuffer.size()]);
        _commentBuffer.clear();
        return com;
    }
}
