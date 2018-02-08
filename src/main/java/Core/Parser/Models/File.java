package Core.Parser.Models;

import Core.Definitions.SupportedLanguages;

import java.util.List;

public class File {
    public SupportedLanguages Type;
    public List<Class> Classes;
    public String[] Body;

    public void add(Class obj){
        obj.Parent = this;
        Classes.add(obj);
    }
}
