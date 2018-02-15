package Core.Parser.Models;

public class Parameter implements IObject {

    public String Type;
    private IObject _parent;
    private String _name;

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
    public void Name(String name) {
        _name = name;
    }

    @Override
    public String[] Body() {
        return new String[0];
    }

    @Override
    public void Body(String[] body) {

    }

    @Override
    public String[] Comments() {
        return new String[0];
    }

    @Override
    public void Comments(String[] comments) {

    }
}
