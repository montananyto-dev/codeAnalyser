package Core.Parser.Models;

import Core.Definitions.SupportedLanguages;

import java.util.ArrayList;
import java.util.List;

public class File extends Object {
    public SupportedLanguages Type;
    public List<Class> Classes = new ArrayList<>();
    public String Package ="";
    public List<String> ExternalLibraries = new ArrayList<>();

    public void add(Class obj){
        obj.Parent(this);
        Classes.add(obj);
    }
    @Override
    public String FullName() {
        return Package;
    }

    public enum ContentTypes {
        Package,
        Import,
        Object,
        Comment
    }
}
