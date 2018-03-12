package Interface.Controls;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class LabelFieldCollection{
    public List<LabelField> LabelFields = new ArrayList<>();
    private GridPane _grid;
    private int _columnIndex;
    private int _rowIndex;
    private Color _color;
    private int _colSpan;
    private int _rowSpan;

    public LabelFieldCollection(GridPane grid, int column, int row,int colSpan,int rowSpan, Color color) {
        _grid = grid;
        _columnIndex = column;
        _rowIndex = row;
        _color = color;
        _colSpan = colSpan;
        _rowSpan = rowSpan;
    }

    public void add(String text) {
        LabelField lf = new LabelField(text);
        lf.Label.setTextFill(_color);
        LabelFields.add(lf);
        _grid.add(lf.Label, _columnIndex, _rowIndex,_colSpan,_rowSpan);
        _grid.add(lf.Field, _columnIndex + 2, _rowIndex,_colSpan,_rowSpan);
        _rowIndex++;
        LabelFields.add(lf);
    }

    public LabelField find(String name){
        for (LabelField lf : LabelFields){
            if (lf.Name.contains(name)) return lf;
        }
        return null;
    }
}
