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

import com.haulmont.mp2xls.object.LocalizationsBatch;
import com.haulmont.mp2xls.reader.MessagesFolderReader;
import com.haulmont.mp2xls.object.MessagesLocalization;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class LocalizationBatchExcelWriter {

    public static void exportToXls(LocalizationsBatch localizations, String outputXls) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(outputXls);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet worksheet = workbook.createSheet("localizations");

        HSSFCellStyle systemStyle = workbook.createCellStyle();
        systemStyle.setFillForegroundColor(HSSFColor.RED.index);
        systemStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFRow row = worksheet.createRow(0);
        row.createCell(0).setCellValue("Project path");
        row.createCell(1).setCellValue(localizations.getProjectDirectory());

        HSSFRow headLine = worksheet.createRow(5);
        headLine.createCell(0).setCellValue("Path to File");
        headLine.createCell(1).setCellValue("Parameter Name");

        Map<String, Integer> localeColumn = new HashMap<>();
        int colCount = 1;
        for (String localeId : localizations.getLocalizationIds()) {
            if (localizations.getScanLocalizationIds().
                    contains(localeId == null ? "en" : localeId)) {
                String id = localeId == null ? "default" : localeId;
                headLine.createCell(++colCount).setCellValue(id);
                localeColumn.put(localeId, colCount);
            }
        }

        Integer currentRow = headLine.getRowNum();
        for (String folder : localizations.getMessagesLocalizations().keySet()) {
            Set<MessagesLocalization> locales = localizations.getMessagesLocalizations().get(folder);

            Set<String> parameters = new HashSet<>();
            for (MessagesLocalization locale : locales) {
                if (localizations.getScanLocalizationIds().
                        contains(locale.getLocaleId() == null ? "en" : locale.getLocaleId()))
                    parameters.addAll(locale.getMessages().keySet());
            }

            for (String parameter : parameters) {
                row = worksheet.createRow(++currentRow);
                HSSFCell cell = row.createCell(0);
                cell.setCellValue(folder);
                if (MessagesFolderReader.systemKeys.contains(parameter)) {
                    cell.setCellStyle(systemStyle);
                    row.setZeroHeight(true);
                }

                cell = row.createCell(1);
                cell.setCellValue(parameter);
                if (MessagesFolderReader.systemKeys.contains(parameter)) {
                    cell.setCellStyle(systemStyle);
                }

                for (MessagesLocalization locale : locales) {
                    if (localizations.getScanLocalizationIds().
                            contains(locale.getLocaleId() == null ? "en" : locale.getLocaleId())) {
                        Integer columnNum = localeColumn.get(locale.getLocaleId());
                        cell = row.createCell(columnNum);
                        cell.setCellValue(locale.getMessages().get(parameter));
                    }
                }

            }
        }

        worksheet.setAutoFilter(new CellRangeAddress(headLine.getRowNum(), worksheet.getLastRowNum(), 0, colCount));
        worksheet.createFreezePane(0, headLine.getRowNum() + 1);
/*
        for (int i = 0; i < colCount; i++){
            worksheet.autoSizeColumn(i);
            worksheet.setColumnWidth(i, worksheet.getColumnWidth(i) + 100);
        }
*/
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }
}
