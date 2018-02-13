package Core.Parser.Models;

public interface IObject {
    IObject Parent();
    void Parent(IObject parent);
    String Name();
    void Name(String value);
    String[] Body();
    void Body(String[] body);
}
