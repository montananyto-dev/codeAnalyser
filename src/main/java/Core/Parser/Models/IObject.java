package Core.Parser.Models;

import java.util.Arrays;

public interface IObject {
    IObject Parent();
    void Parent(IObject parent);
    String Name();
    void Name(String name);
    String FullName();
    default String[] Path(){
        IObject parent = Parent();
        if (parent != null) {
            String[] parentPath = parent.Path();
            String[] path = Arrays.copyOf(parentPath, parentPath.length +1);
            path[parentPath.length] = parent.Name();
            return path;
        }
        return new String[0];
    }
    String[] Body();
    void Body(String[] body);
    String[] Comments();
    void Comments(String[] comments);
}
