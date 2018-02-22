package Core.Parser.Models;

public class Object implements IObject {

    private IObject _parent = null;
    private String[] _comments = new String[0];
    private String _name = "";
    private String[] _body = {};

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
    public String FullName() {
        return _name;
    }

    @Override
    public String[] Body() {
        return _body;
    }

    @Override
    public void Body(String[] body) {
        _body = body;
    }

    @Override
    public String[] Comments() {
        return _comments;
    }

    @Override
    public void Comments(String[] comments) {
        _comments = comments;
    }
}
