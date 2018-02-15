package Core.Parser.Models;

import Core.Definitions.SupportedLanguages;

import java.util.ArrayList;
import java.util.List;

public class File implements IObject {
    public SupportedLanguages Type;
    public List<Class> Classes = new ArrayList<>();
    public String Package;
    public List<String> ExternalLibraries = new ArrayList<>();

    private IObject _parent;
    private String _name;
    private String[] _body = {};
    private String[] _comments = {};

    public void add(Class obj){
        obj.Parent(this);
        Classes.add(obj);
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
    public void Name(String name) {
        _name = name;
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
