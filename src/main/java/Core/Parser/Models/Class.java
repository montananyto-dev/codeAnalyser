package Core.Parser.Models;

import java.util.ArrayList;
import java.util.List;

public class Class extends Object {
    public String Type;
    public List<Field> Fields = new ArrayList<>();
    public List<Method> Methods = new ArrayList<>();
    public List<Class> Classes = new ArrayList<>();

    public void add(Field field){
        field.Parent(this);
        Fields.add(field);
    }

    public void add(Method method){
        method.Parent(this);
        Methods.add(method);
    }

    public void add(Class cl){
        cl.Parent(this);
        Classes.add(cl);
    }

    public enum ContentTypes {
        Field,
        Method,
        Object,
        Comment
    }
}
