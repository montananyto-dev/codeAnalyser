package Interface.Controls;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LabelField {
    public final javafx.scene.control.Label Label;
    public final TextField Field;
    public final String Name;

    public LabelField(){
        Name = "";
        Label = new Label("test");
        Field = new TextField("");
    }

    public LabelField(String name){
        Name = name;
        Label = new Label(Name);
        Field = new TextField();
    }
}
