package Core.Parser.Models;

import java.util.ArrayList;
import java.util.List;

public class Class implements IObject {
    private IObject  _parent;
    private String _name;
    public String Type;
    public List<Field> Fields = new ArrayList<>();
    public List<Method> Methods = new ArrayList<>();
    public List<Class> Classes = new ArrayList<>();
    public String[] Comments;
    private String[] _body;

    public void add(Field field){
        field.Parent = this;
        Fields.add(field);
    }

    public void add(Method method){
        method.Parent = this;
        Methods.add(method);
    }

    public void add(Class cl){
        cl.Parent(this);
        Classes.add(cl);
    }

    @Override
    public IObject Parent() {
        return _parent;
    }

    @Override
    public void Parent(IObject parent) {
        _parent = parent;
    }

    @Override
    public String Name() {
        return _name;
    }

    @Override
    public void Name(String value) {
        _name = value;
    }

    @Override
    public String[] Body() {
        return _body;
    }

    @Override
    public void Body(String[] body) {
        _body = body;
    }

    public enum ContentTypes {
        Field,
        Method,
        Object,
        Comment
    }
}
