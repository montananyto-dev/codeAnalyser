package Core.Definitions.Java;

import Core.Parser.Helper;
import Core.Parser.Models.*;
import Core.Parser.Models.Class;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser extends Core.Parser.Parser {

    protected String[] setModifiers() {
        return new String[]{"private", "public", "protected",
                            "static", "transient", "volatile", "const",
                            "abstract", "final", "default", "native", "strictfp", "synchronized"};
    }
    protected String[] setObjects() {
        return new String[]{"class", "interface", "enum"};
    }
    protected String[] setInheritance() {
        return new String[]{"implements", "extends"};
    }
    protected String[] setImports() {
        return new String[]{"import"};
    }
    protected String[] setPackage() {
        return new String[]{"package"};
    }
    protected String[] setCommentLine() {
        return new String[]{"//"};
    }
    protected String[] setCommentBlock() {
        return new String[]{"/*", "*/"};
    }
    protected String[] setScopeOpen() {
        return new String[]{"{", "("};
    }
    protected String[] setScopeClose() {
        return new String[]{"}", ")"};
    }

    protected Core.Parser.LineParser setLineParser() {
        return new LineParser();
    }

    protected void DetermineFileAction(File file, String[] line, File.ContentTypes type) {
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

    protected void determineClassAction(Class cl, String[] line, Class.ContentTypes type) {
        if (type == null) {
            return;
        }
        if (type == Class.ContentTypes.Comment){ _commentBuffer.addAll(Arrays.asList(parseComment(line[0])));
            return;
        }
        if (type == Class.ContentTypes.Method){ cl.add(parseMethod(line));
            return;
        }
        if (type == Class.ContentTypes.Object){ cl.add(parseClass(line));
            return;
        }
        if (type == Class.ContentTypes.Field) cl.add(parseField(line));
    }

    protected Class buildClass(String[] currentLine) {
        Class cl = new Class();
        String[] cleanedLine = Helper.remove(currentLine, _modifiers);
        cl.Type = cleanedLine[0];
        cl.Name(cleanedLine[1]);
        cl.Comments(getComments());
        if (Helper.contains(cleanedLine, "{")){
            _scopeopen++;
        }
        return cl;
    }

    protected Field buildField(String[] line) {
        Field field = new Field();
        field.Comments(getComments());
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
        return field;
    }

    protected boolean shouldStopParsingField(String[] line){
        return Helper.contains(line, ";");
    }

    protected Method buildMethod(String[] line) {
        Method method = new Method();
        method.Comments(getComments());
        line = Helper.remove(line, _modifiers);
        method.Type = line[0];
        method.Name(line[1]);
        method.add(parseParameters());
        return method;
    }

    private Parameter[] parseParameters() {
        List<Parameter> params = new ArrayList<>();
        boolean closed = false;
        while (!closed){
            String[] line = LineParser.mergeGenerics(_lineParser.parse(_body[_line]));
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

    protected Class.ContentTypes determineClassContent(String[] line){
        line = Helper.remove(line, _modifiers);
        String[] reduced = LineParser.eliminateGenerics(line);
        if (reduced[0].startsWith("//") || reduced[0].startsWith("/*")) return Class.ContentTypes.Comment;
        if (Helper.contains(reduced, _objects)) return Class.ContentTypes.Object;
        if (reduced.length < 3) return null;
        if (reduced[2].equals(";") || reduced[2].equals("=")) return Class.ContentTypes.Field;
        if (reduced[2].equals("(")) return Class.ContentTypes.Method;
        return null;
    }

    protected File.ContentTypes determineFileContent(String[] line){
        if (Helper.contains(_imports, line[0])) return File.ContentTypes.Import;
        if (Helper.contains(_package, line[0])) return File.ContentTypes.Package;
        if (Helper.contains(_objects, line)) return File.ContentTypes.Object;
        if (Helper.contains(_commentline, line[0]) || Helper.contains(_commentBlock, line[0])) return File.ContentTypes.Comment;
        return null;
    }

    private String parseImport(String[] line){
        return line[1];
    }

    private String parsePackage(String[] line){
        return line[1];
    }
}
