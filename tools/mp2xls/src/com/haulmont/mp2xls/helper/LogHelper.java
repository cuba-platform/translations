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

import com.haulmont.mp2xls.object.LocalizationLog;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;


public class LogHelper {

    public static String getMessageByType(LocalizationLog.Type type) {
        String message = "";
        switch (type) {
            case ADDED : {
                message = "Properties are not found in source files, but found in excel file:";
            } break;
            case ADDED_IN_NEW_FILE : {
                message = "Properties are added to new files:";
            } break;
            case CHANCED : {
                message = "Properties are different in source files and excel file:";
            } break;
            case MOVED: {
                message = "Properties are found in source files, but not found in excel file:";
            } break;
        }
        return message;
    }

    public static CellStyle getStyleByType(Workbook workbook, LocalizationLog.Type type) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        switch (type) {
            case ADDED : {
                font.setColor(HSSFColor.GREEN.index);
                style.setFillBackgroundColor(HSSFColor.GREEN.index);
            } break;
            case ADDED_IN_NEW_FILE : {
                font.setColor(HSSFColor.BLUE.index);
                style.setFillBackgroundColor(HSSFColor.BLUE.index);
            } break;
            case CHANCED : {
                font.setColor(HSSFColor.ORANGE.index);
                style.setFillBackgroundColor(HSSFColor.ORANGE.index);
            } break;
            case MOVED: {
                font.setColor(HSSFColor.RED.index);
                style.setFillBackgroundColor(HSSFColor.RED.index);
            } break;
        }

        style.setFont(font);
        return style;
    }
}
