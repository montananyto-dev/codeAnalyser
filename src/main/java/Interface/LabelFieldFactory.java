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
    private int _columnIndex;
    private int _rowIndex;
    private Color _color;
    private int _colSpan;
    private int _rowSpan;

    public LabelFieldFactory(GridPane grid, int column, int row,int colSpan,int rowSpan, Color color) {
        _grid = grid;
        _columnIndex = column;
        _rowIndex = row;
        _color = color;
        _colSpan = colSpan;
        _rowSpan = rowSpan;
    }

    public LabelField build(String text) {
        Label label = new Label(text);
        _grid.add(label, _columnIndex, _rowIndex,_colSpan,_rowSpan);
        TextField field = new TextField();
        _grid.add(field, _columnIndex + 2, _rowIndex,_colSpan,_rowSpan);
        _rowIndex++;
        label.setTextFill(_color);
        return new LabelField(text, label, field);
    }
}
