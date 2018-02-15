package Core.Parser.Models;

public interface IObject {
    IObject Parent();
    void Parent(IObject parent);
    String Name();
    void Name(String name);
    default String FullName(){
        IObject parent = Parent();
        return (parent != null) ? parent.FullName() + "." + Name() : Name();
    }
    String[] Body();
    void Body(String[] body);
    String[] Comments();
    void Comments(String[] comments);
}
