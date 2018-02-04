package Core.Parser.Models;

import java.util.List;

public class Class {
    public File Parent;
    public String Name;
    public List<Field> Fields;
    public List<Method> Methods;
    public String[] Comments;
    public String[] Body;

    public void add(Field field){
        field.Parent = this;
        Fields.add(field);
    }

    public void add(Method method){
        method.Parent = this;
        Methods.add(method);
    }
}
