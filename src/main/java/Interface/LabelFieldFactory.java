package Interface;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

class LabelFieldFactory {

    public class LabelField {
        public final javafx.scene.control.Label Label;
        public final TextField Field;
        public final String Name;

        public LabelField(String name, Label label, TextField field) {
            Name = name;
            Label = label;
            Field = field;
        }
    }

    private GridPane _grid;
    private int _column;
    private int _row;
    private Color _color;

    public LabelFieldFactory(GridPane grid, int column, int row, Color color) {
        _grid = grid;
        _column = column;
        _row = row;
        _color = color;
    }

    public LabelField build(String text) {
        Label label = new Label(text);
        _grid.add(label, _column, _row);
        TextField field = new TextField();
        _grid.add(field, _column + 1, _row);
        _row++;
        label.setTextFill(_color);
        return new LabelField(text, label, field);
    }

}
