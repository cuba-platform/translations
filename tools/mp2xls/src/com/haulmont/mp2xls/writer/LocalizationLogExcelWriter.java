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

package com.haulmont.mp2xls.writer;

import com.haulmont.mp2xls.helper.LogHelper;
import com.haulmont.mp2xls.object.LocalizationLog;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LocalizationLogExcelWriter {

    public static void exportToXls(List<LocalizationLog> differences, String outputXls) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(outputXls);
        HSSFWorkbook workbook = new HSSFWorkbook();
        try {
            HSSFSheet worksheet = workbook.createSheet("localizations");

            HSSFCellStyle systemStyle = workbook.createCellStyle();
            systemStyle.setFillForegroundColor(HSSFColor.RED.index);
            systemStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            Integer currentRow = 0;
            HSSFRow headLine = worksheet.createRow(++currentRow);
            headLine.createCell(0).setCellValue("File");
            headLine.createCell(1).setCellValue("Property");
            headLine.createCell(2).setCellValue("Source Value");
            headLine.createCell(3).setCellValue("Excel Value");

            HSSFRow row;

            for (LocalizationLog.Type type : LocalizationLog.Type.values()) {
                List<LocalizationLog> logs = getLogsByType(differences, type);
                if (logs.size() > 0) {
                    row = worksheet.createRow(++currentRow);
                    HSSFCell cell = row.createCell(0);
                    cell.setCellValue(LogHelper.getMessageByType(type));
                    cell.setCellStyle(LogHelper.getStyleByType(workbook, type));
                    for (int i = 1; i < 4; i++) {
                        row.createCell(i);
                    }

                    CellRangeAddress region = new CellRangeAddress(currentRow, currentRow, 0, 3);
                    worksheet.addMergedRegion(region);

                    for (LocalizationLog log : logs) {
                        createNewLogRow(worksheet, ++currentRow, log);
                    }

                    row = worksheet.createRow(++currentRow);
                    for (int i = 0; i < 4; i++) {
                        row.createCell(i);
                    }

                    region = new CellRangeAddress(currentRow, currentRow, 0, 3);
                    worksheet.addMergedRegion(region);
                }
            }

            worksheet.setAutoFilter(new CellRangeAddress(headLine.getRowNum(), worksheet.getLastRowNum(), 0, 3));
            worksheet.createFreezePane(0, headLine.getRowNum() + 1);

            for (int i = 0; i < worksheet.getLastRowNum(); i++) {
                worksheet.autoSizeColumn(i);
            }
        } finally {
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        }
    }

    protected static List<LocalizationLog> getLogsByType(List<LocalizationLog> differences, LocalizationLog.Type type) {
        List<LocalizationLog> localizationLogs = new ArrayList<>();
        for (LocalizationLog log : differences) {
            if (log.getType().equals(type)) {
                localizationLogs.add(log);
            }
        }
        return localizationLogs;
    }

    protected static HSSFRow createNewLogRow(HSSFSheet worksheet, Integer currentRow, LocalizationLog log) {
        HSSFRow row;
        row = worksheet.createRow(currentRow);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue(log.getFile());

        cell = row.createCell(1);
        cell.setCellValue(log.getParameterName());

        cell = row.createCell(2);
        cell.setCellValue(log.getSourceValue());

        cell = row.createCell(3);
        cell.setCellValue(log.getExcelValue());
        return row;
    }

}

