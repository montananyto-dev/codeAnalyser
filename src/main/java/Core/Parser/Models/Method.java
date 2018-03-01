package Core.Parser.Models;

import java.util.ArrayList;
import java.util.List;

public class Method extends Object {
    public String Type;
    private List<Parameter> _parameters = new ArrayList<>();

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
    public String FullName() {
        String fullName = Name() + "(";
        for (Parameter p :_parameters) {
            fullName += p.Type + ", ";
        }
        if (fullName.endsWith(", ")){
            fullName = fullName.substring(0, fullName.length()-2);
        }
        return fullName +")";
    }
}
