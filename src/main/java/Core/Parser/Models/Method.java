package Core.Parser.Models;

import java.util.ArrayList;
import java.util.List;

public class Method implements IObject {
    public String Type;

    private IObject _parent;
    private String[] _comments;
    private String _name;
    private List<Parameter> _parameters = new ArrayList<>();
    private String[] _body = {};

    public void add(Parameter parameter){
        parameter.Parent(this);
        _parameters.add(parameter);
    }

    public void add(Parameter[] parameters){
        for (Parameter param: parameters) {
            param.Parent(this);
            _parameters.add(param);
        }
    }

    public Parameter[] Parameters(){
        return _parameters.toArray(new Parameter[_parameters.size()]);
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
