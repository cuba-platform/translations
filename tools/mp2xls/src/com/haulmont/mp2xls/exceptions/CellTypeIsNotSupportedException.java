package com.haulmont.mp2xls.exceptions;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by stukalov on 03.05.2016.
 */
public class CellTypeIsNotSupportedException extends RuntimeException {

    private Cell cell;

    public CellTypeIsNotSupportedException(Throwable cause, Cell cell) {
        super(CellTypeIsNotSupportedException.getErrorMessage(cell), cause);
        this.cell = cell;
    }

    public CellTypeIsNotSupportedException(Cell cell) {
        super(CellTypeIsNotSupportedException.getErrorMessage(cell));
        this.cell = cell;
    }

    public static String getErrorMessage(Cell cell) {
        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.format("Boolean cell type is not supported (row: %d, col: %d). Please change cell type to 'Text'," +
                    " re-type cell value and try again.", cell.getRow().getRowNum(), cell.getColumnIndex());
        } else {
            return String.format("Cell type '%s' is not supported", cell.getCellType());
        }
    }
}
