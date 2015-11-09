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
import com.haulmont.mp2xls.object.LocalizationLog;
import com.haulmont.mp2xls.object.MessagesLocalization;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LocalizationBatchFileWriter {

    private LocalizationsBatch sourceLocalization;
    private LocalizationsBatch fileLocalization;

    public LocalizationBatchFileWriter(LocalizationsBatch sourceLocalization, LocalizationsBatch fileLocalization) {
        this.sourceLocalization = sourceLocalization;
        this.fileLocalization = fileLocalization;
    }

    public void process(String localizationFile) throws IOException {
        File basePath = new File(fileLocalization.getProjectDirectory());
        List<LocalizationLog> differences = new ArrayList<>();
        for (String localizationPack : fileLocalization.getMessagesLocalizations().keySet()) {
            Set<MessagesLocalization> messagesLocalizations = fileLocalization.getMessagesLocalizations().get(localizationPack);
            Set<MessagesLocalization> sourceMessagesLocalizations = sourceLocalization.getMessagesLocalizations().get(localizationPack);
            for (MessagesLocalization localization : messagesLocalizations) {
                if (localization.getMessages().size() == 0)
                    continue;

                if (!fileLocalization.getScanLocalizationIds().
                        contains(localization.getLocaleId() == null ? "en" : localization.getLocaleId()))
                    continue;

                List<String> parameters = Arrays.asList(localization.getMessages().keySet().toArray(new String[localization.getMessages().keySet().size()]));
                Collections.sort(parameters, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        o1 = o1.replaceFirst("^#", "");
                        o2 = o2.replaceFirst("^#", "");
                        Boolean o1System = MessagesFolderReader.systemKeys.contains(o1);
                        Boolean o2System = MessagesFolderReader.systemKeys.contains(o2);

                        if (o1System && o2System)
                            return o1.compareTo(o2);
                        if (o1System)
                            return -1;
                        if (o2System)
                            return 1;

                        return o1.compareTo(o2);
                    }
                });

                List<String> stringsToPrint = new ArrayList<>();
                List<LocalizationLog> addedInNewFile = new ArrayList<>();
                for (String param : parameters) {
                    if (localization.getMessages().get(param) != null) {
                        stringsToPrint.add(param + " = " + localization.getMessages().get(param));
                        addedInNewFile.add(
                                new LocalizationLog(localization.getPath(), param, "", localization.getMessages().get(param), LocalizationLog.Type.ADDED_IN_NEW_FILE)
                        );
                    }
                }

                if (stringsToPrint.size() > 0) {
                    differences.addAll(getDifferences(localization, sourceMessagesLocalizations));
                    File messageFile = new File(basePath, localization.getPath());

                    if (!messageFile.exists()) {
                        createNewFileLocalization(stringsToPrint, messageFile);
                        differences.addAll(addedInNewFile);
                    }
                }
            }

        }

        for (String localizationPack : sourceLocalization.getMessagesLocalizations().keySet()) {
            Set<MessagesLocalization> messagesLocalizations = fileLocalization.getMessagesLocalizations().get(localizationPack);
            Set<MessagesLocalization> sourceMessagesLocalizations = sourceLocalization.getMessagesLocalizations().get(localizationPack);
            for (MessagesLocalization localization : sourceMessagesLocalizations) {
                if (localization.getMessages().size() == 0)
                    continue;

                if (!sourceLocalization.getScanLocalizationIds().
                        contains(localization.getLocaleId() == null ? "en" : localization.getLocaleId()))
                    continue;

                List<String> parameters = Arrays.asList(localization.getMessages().keySet().toArray(new String[localization.getMessages().keySet().size()]));
                Collections.sort(parameters, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        o1 = o1.replaceFirst("^#", "");
                        o2 = o2.replaceFirst("^#", "");
                        Boolean o1System = MessagesFolderReader.systemKeys.contains(o1);
                        Boolean o2System = MessagesFolderReader.systemKeys.contains(o2);

                        if (o1System && o2System)
                            return o1.compareTo(o2);
                        if (o1System)
                            return -1;
                        if (o2System)
                            return 1;

                        return o1.compareTo(o2);
                    }
                });

                List<String> stringsToPrint = new ArrayList<>();
                for (String param : parameters) {
                    if (localization.getMessages().get(param) != null) {
                        stringsToPrint.add(param + " = " + localization.getMessages().get(param));
                    }
                }

                if (stringsToPrint.size() > 0) {
                    differences.addAll(getNotFoundLocalizationInExcel(localization, messagesLocalizations));
                    File messageFile = new File(basePath, localization.getPath());

                    if (!messageFile.exists()) {
                        createNewFileLocalization(stringsToPrint, messageFile);
                    }
                }
            }

        }

        if (differences.size() > 0) {
            LocalizationLogExcelWriter.exportToXls(differences, localizationFile);
        }
    }

    protected void createNewFileLocalization(List<String> stringsToPrint, File messageFile) throws IOException {
        if (!messageFile.getParentFile().exists())
            messageFile.getParentFile().mkdirs();
        messageFile.createNewFile();

        try (PrintWriter writer = new PrintWriter(messageFile, "UTF-8")) {
            writer.println("#\n" +
                    "# Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.\n" +
                    "# Haulmont Technology proprietary and confidential.\n" +
                    "# Use is subject to license terms.\n" +
                    "#");

            String prev = "#";
            for (String str : stringsToPrint) {
                if (addEmptyLine(str, prev))
                    writer.println();
                writer.println(str);
                prev = str;
            }
        }
    }

    private boolean addEmptyLine (String currentLine, String prevLine){
        Pattern p = Pattern.compile("[ \\t]*#?(?<keyPrefix>[a-zA-Z0-9_]+).?(?<keyPostfix>[a-zA-Z0-9_\\.]+)?[ \\t]*=[ \\t]*(?<value>.+)?");
        Matcher cur = p.matcher(currentLine);
        Matcher prev = p.matcher(prevLine);

        return !(cur.matches() && prev.matches()) || !cur.group("keyPrefix").equals(prev.group("keyPrefix"));

    }

    private List<LocalizationLog> getNotFoundLocalizationInExcel(MessagesLocalization sourceMessagesLocalization,  Set<MessagesLocalization> messagesLocalizations) {
        List<LocalizationLog> notFoundLocalizations = new ArrayList<>();
        List<LocalizationLog> movedMessages = new ArrayList<>();
        MessagesLocalization messagesLocalization = notFindMessagesLocalizationInExcel(sourceMessagesLocalization, messagesLocalizations);
        Map<String, String> messages = sourceMessagesLocalization.getMessages();
        if (messagesLocalization != null) {
            for (String message : messages.keySet()) {
                String key = messages.get(message);
                if (StringUtils.isNotEmpty(key)) {
                    if (StringUtils.isNotEmpty(messages.get(message))) {
                        movedMessages.add(
                                new LocalizationLog(messagesLocalization.getPath(), message, "", messages.get(message), LocalizationLog.Type.MOVED)
                        );
                    }
                }
            }
        }

        notFoundLocalizations.addAll(movedMessages);

        return notFoundLocalizations;
    }

    private List<LocalizationLog> getDifferences(MessagesLocalization messagesLocalization, Set<MessagesLocalization> sourceMessagesLocalizations) {
        List<LocalizationLog> differences = new ArrayList<>();

        List<LocalizationLog> addedMessages = new ArrayList<>();
        List<LocalizationLog> changedMessages = new ArrayList<>();
        List<LocalizationLog> movedMessages = new ArrayList<>();

        MessagesLocalization sourceMessagesLocalization = findMessagesLocalizationInSource(messagesLocalization, sourceMessagesLocalizations);
        Map<String, String> messages = messagesLocalization.getMessages();
        if (sourceMessagesLocalization == null) {
            for (String message : messages.keySet()) {
                String key = messages.get(message);
                if (StringUtils.isNotEmpty(key)) {
                    if (StringUtils.isNotEmpty(messages.get(message))) {
                        addedMessages.add(
                                new LocalizationLog(messagesLocalization.getPath(), message, "", messages.get(message), LocalizationLog.Type.ADDED_IN_NEW_FILE)
                        );
                    }
                }
            }
        } else {
            Map<String, String> sourceMessages = sourceMessagesLocalization.getMessages();

            for (String message : messages.keySet()) {
                String key = sourceMessages.get(message);
                if (StringUtils.isEmpty(key)) {
                    if (StringUtils.isNotEmpty(messages.get(message))) {
                        addedMessages.add(
                                new LocalizationLog(sourceMessagesLocalization.getPath(), message, "", messages.get(message), LocalizationLog.Type.ADDED)
                        );
                    }
                } else if (!StringUtils.equals(messages.get(message), sourceMessages.get(message))) {
                    changedMessages.add(
                            new LocalizationLog(sourceMessagesLocalization.getPath(), message, sourceMessages.get(message), messages.get(message), LocalizationLog.Type.CHANCED)
                    );
                }
            }

            for (String message : sourceMessages.keySet()) {
                String key = messages.get(message);
                if (StringUtils.isEmpty(key)) {
                    if (StringUtils.isNotEmpty(sourceMessages.get(message))) {
                        movedMessages.add(
                                new LocalizationLog(sourceMessagesLocalization.getPath(), message, sourceMessages.get(message), "", LocalizationLog.Type.MOVED)
                        );
                    }
                }
            }
        }

        differences.addAll(addedMessages);
        differences.addAll(movedMessages);
        differences.addAll(changedMessages);

        return differences;
    }

    private MessagesLocalization notFindMessagesLocalizationInExcel(MessagesLocalization sourceMessagesLocalization, Set<MessagesLocalization> messagesLocalizations) {
        MessagesLocalization notFoundMessagesLocalization = null;
        boolean found = false;
        if (messagesLocalizations != null) {
            Set<MessagesLocalization> messagesLocalizationsWithoutEmpty = excludeEmptyLocalization(messagesLocalizations);
            for (MessagesLocalization messagesLocalization : messagesLocalizationsWithoutEmpty) {
                if (sourceMessagesLocalization.getPath().equals(messagesLocalization.getPath())) {
                    found = true;
                    break;
                }
            }
        }
        if(!found) {
            notFoundMessagesLocalization = sourceMessagesLocalization;
        }

        return notFoundMessagesLocalization;
    }

    protected Set<MessagesLocalization> excludeEmptyLocalization(Set<MessagesLocalization> messagesLocalizations) {
        Set<MessagesLocalization> messagesLocalizationsWithoutEmpty = new HashSet<>();
        for (MessagesLocalization messagesLocalization : messagesLocalizations) {
            boolean found = false;
            if (messagesLocalization.getMessages() != null) {
                for (String key : messagesLocalization.getMessages().keySet()) {
                    if (StringUtils.isNotEmpty(messagesLocalization.getMessages().get(key))) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    messagesLocalizationsWithoutEmpty.add(messagesLocalization);
                }
            }
        }
        return messagesLocalizationsWithoutEmpty;
    }

    private MessagesLocalization findMessagesLocalizationInSource(MessagesLocalization messagesLocalization, Set<MessagesLocalization> sourceMessagesLocalizations) {
        if (sourceMessagesLocalizations != null && sourceMessagesLocalizations.size() > 0) {
            for (MessagesLocalization sourceMessagesLocalization : sourceMessagesLocalizations) {
                if (sourceMessagesLocalization.getPath().equals(messagesLocalization.getPath())) {
                    return sourceMessagesLocalization;
                }
            }
        }
        return null;
    }
}
