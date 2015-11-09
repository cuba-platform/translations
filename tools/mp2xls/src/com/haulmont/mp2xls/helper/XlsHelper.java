/*
 * Copyright 2015 Haulmont
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.mp2xls.helper;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;


public class XlsHelper {
    protected static final char NON_BREAKING_SPACE = (char) 160;

    public static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                return null;
            case Cell.CELL_TYPE_STRING:
                String formattedCellValue = cell.getStringCellValue().replace(String.valueOf(NON_BREAKING_SPACE), " ").trim();
                return formattedCellValue.isEmpty() ? null : formattedCellValue;
            case Cell.CELL_TYPE_NUMERIC:
                if (isDateCell(cell)) {
                    return cell.getDateCellValue();
                }

                Double numericCellValue = cell.getNumericCellValue();
                return isAlmostInt(numericCellValue) ? numericCellValue.intValue() : numericCellValue;
            case Cell.CELL_TYPE_FORMULA:
                /*
                formattedCellValue = cell.getStringCellValue();
                if (formattedCellValue != null) {
                    formattedCellValue = formattedCellValue.replace(String.valueOf(NON_BREAKING_SPACE), " ").trim();
                }
                */
                return getFormulaCellValue(cell/*, formattedCellValue*/);
            default:
                throw new IllegalStateException(String.format("Cell type '%s' is not supported", cell.getCellType()));
        }
    }

    protected static boolean isDateCell(Cell cell) {
        return HSSFDateUtil.isCellDateFormatted(cell);
    }

    protected static boolean isAlmostInt(Double numericCellValue) {
        return Math.abs(numericCellValue - numericCellValue.intValue()) < 1e-10;
    }

    protected static Object getFormulaCellValue(Cell cell/*, String formattedCellValue*/) {
        switch (cell.getCachedFormulaResultType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                String formattedCellValue = cell.getStringCellValue();
                if (formattedCellValue != null) {
                    formattedCellValue = formattedCellValue.replace(String.valueOf(NON_BREAKING_SPACE), " ").trim();
                    if (formattedCellValue.isEmpty())
                        return null;
                }
                return formattedCellValue;
            default:
                throw new IllegalStateException(String.format("Formula cell type '%s' is not supported", cell.getCachedFormulaResultType()));
        }
    }

    public static Object getCellValue(Cell cell, Boolean forceToString) {
        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return null;
        }

        cell.setCellType(Cell.CELL_TYPE_STRING);
        return cell.getStringCellValue().replace(String.valueOf(NON_BREAKING_SPACE), " ").trim();
    }

    public static boolean eof(HSSFRow row) {
        return row == null || getCellValue(row.getCell(0)) == null;
    }
}
