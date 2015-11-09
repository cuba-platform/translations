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

package com.haulmont.mp2xls.object;

import com.haulmont.mp2xls.helper.XlsHelper;
import com.haulmont.mp2xls.helper.Utils;
import com.haulmont.mp2xls.reader.MessagesFolderReader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LocalizationsBatch {

    private Map<String, Set<MessagesLocalization>> messagesLocalizations = new HashMap<>();
    protected Set<String> skipFolders = new HashSet<String>() {{
        add("out");
        add("build");
        add("tomcat");
    }};
    private String projectDirectory;
    private Set<String> localizationIds = new HashSet<>();
    private Set<String> scanLocalizationIds = new HashSet<>();

    public LocalizationsBatch(String sourcePath) throws IOException {
        File source = new File(sourcePath);
        scanSourceFolder(source);
    }

    public LocalizationsBatch(String sourcePath, String localizationFilePath) throws IOException {
        File source = new File(sourcePath);
        readLocalizationFile(source, localizationFilePath);
    }

    protected void scanSourceFolder(File source) throws IOException {
        projectDirectory = source.getAbsolutePath();

        Pattern p = Pattern.compile(MessagesFolderReader.FILE_MASK);

        //File searchDirectory = new File(source);
        List<File> files = Utils.listFilesForFolder(source, MessagesFolderReader.FILE_MASK, skipFolders);
        for (File f : files) {
            Matcher m = p.matcher(f.getName());
            m.matches();
            String prefix = m.group("prefix");
            String relative = source.toURI().relativize(f.getParentFile().toURI()).getPath() + prefix;
            Set<MessagesLocalization> localizations;
            if (messagesLocalizations.keySet().contains(relative))
                localizations = messagesLocalizations.get(relative);
            else
                localizations = new HashSet<>();
            localizations.add(MessagesFolderReader.readMessagesFile(f.getAbsolutePath(), source.toURI().getPath()));
            messagesLocalizations.put(relative, localizations);

            for (MessagesLocalization localization : localizations)
                localizationIds.add(localization.getLocaleId());
        }
    }

    protected void readLocalizationFile(File source, String lcFilePath) throws IOException {
        FileInputStream fis = new FileInputStream(source);
        HSSFWorkbook wb = new HSSFWorkbook(fis);
        HSSFSheet sheet = wb.getSheetAt(0);

        if (lcFilePath == null)
            projectDirectory = (String) XlsHelper.getCellValue(sheet.getRow(0).getCell(1));
        else
            projectDirectory = lcFilePath;

        int firstDataRowIndex = 6;
        int headingRow = 5;

        for (int currentRowIndex = firstDataRowIndex; !XlsHelper.eof(sheet.getRow(currentRowIndex)); currentRowIndex++) {
            HSSFRow row = sheet.getRow(currentRowIndex);

            String path = (String) XlsHelper.getCellValue(row.getCell(0));
            String param = (String) XlsHelper.getCellValue(row.getCell(1));

            Set<MessagesLocalization> localizations;
            if (messagesLocalizations.keySet().contains(path))
                localizations = messagesLocalizations.get(path);
            else {
                localizations = new HashSet<>();
                messagesLocalizations.put(path, localizations);
            }

            for (int col = 2; XlsHelper.getCellValue(sheet.getRow(headingRow).getCell(col)) != null; col++) {
                String localeId = (String) XlsHelper.getCellValue(sheet.getRow(headingRow).getCell(col));
                Object val = XlsHelper.getCellValue(row.getCell(col));
                String value = val != null ? val.toString() : null;
                localeId = localeId.equals("default") ? null : localeId;
                String relPath = path + (localeId == null ? "" : "_" + localeId) + ".properties";

                MessagesLocalization local = null;
                for (MessagesLocalization l : localizations) {
                    if (l.getPath().equals(relPath)) {
                        local = l;
                        break;
                    }
                }

                if (local == null) {
                    local = new MessagesLocalization(localeId, relPath);
                    localizationIds.add(localeId);
                    localizations.add(local);
                }

                local.getMessages().put(param, value);
            }
        }
    }

    public Map<String, Set<MessagesLocalization>> getMessagesLocalizations() {
        return messagesLocalizations;
    }

    public String getProjectDirectory() {
        return projectDirectory;
    }

    public Set<String> getLocalizationIds() {
        return localizationIds;
    }

    public Set<String> getScanLocalizationIds() {
        return scanLocalizationIds;
    }

    public void setScanLocalizationIds(Set<String> scanLocalizationIds) {
        this.scanLocalizationIds = scanLocalizationIds;
    }
}
