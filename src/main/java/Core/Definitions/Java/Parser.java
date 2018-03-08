package Core.Definitions.Java;

import Core.Definitions.SupportedLanguages;
import Core.Parser.Helper;
import Core.Parser.Models.*;
import Core.Parser.Models.Class;

import java.util.Arrays;

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
    protected String[] setBlockScopeOpen() {
        return new String[]{"{"};
    }
    protected String[] setBlockScopeClose() {
        return new String[]{"}"};
    }
    protected String[] setParameterScopeOpens() {
        return new String[]{"("};
    }
    protected String[] setParameterScopeCloses() {
        return new String[]{")"};
    }
    protected SupportedLanguages setLanguageType() {
        return SupportedLanguages.Java;
    }
    protected Core.Parser.LineParser setLineParser() {
        return new LineParser(this);
    }

    protected Class buildClass(String[] currentLine) {
        Class cl = new Class();
        String[] cleanedLine = Helper.remove(currentLine, Modifiers);
        cl.Type = cleanedLine[0];
        cl.Name(cleanedLine[1]);
        cl.Comments(getComments());
        return cl;
    }

    protected Field buildField(String[] line) {
        Field field = new Field();
        field.Comments(getComments());
        line = LineParser.mergeGenerics(line);
        if (Helper.contains(Arrays.copyOfRange(Modifiers, 0, 2), line[0])) {
            field.Accessibility = line[0];
        }
        else{
            field.Accessibility = Modifiers[0];
        }
        line = Helper.remove(line, Modifiers);
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
        line = Helper.remove(line, Modifiers);
        method.Type = line[0];
        method.Name(line[1]);
        return method;
    }

    protected Parameter buildParameter(String[] line, int i) {
        Parameter param = new Parameter();
        param.Type = line[i];
        param.Name(line[i + 1]);
        return param;
    }

    protected Class.ContentTypes determineClassContent(String[] line){
        line = Helper.remove(line, Modifiers);
        String[] reduced = LineParser.eliminateGenerics(line);
        if (reduced[0].startsWith("//") || reduced[0].startsWith("/*")) return Class.ContentTypes.Comment;
        if (Helper.contains(reduced, Objects)) return Class.ContentTypes.Object;
        if (reduced.length < 3) return null;
        if (reduced[2].equals(";") || reduced[2].equals("=")) return Class.ContentTypes.Field;
        if (reduced[2].equals("(")) return Class.ContentTypes.Method;
        return null;
    }

    protected File.ContentTypes determineFileContent(String[] line){
        if (Helper.contains(Imports, line[0])) return File.ContentTypes.Import;
        if (Helper.contains(Package, line[0])) return File.ContentTypes.Package;
        if (Helper.contains(Objects, line)) return File.ContentTypes.Object;
        if (Helper.contains(Commentline, line[0]) || Helper.contains(CommentBlock, line[0])) return File.ContentTypes.Comment;
        return null;
    }

    protected String parseImport(String[] line){
        return line[1];
    }

    protected String parsePackage(String[] line){
        return line[1];
    }
}
