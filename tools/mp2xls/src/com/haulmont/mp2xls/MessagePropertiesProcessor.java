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

package com.haulmont.mp2xls;

import com.haulmont.mp2xls.object.LocalizationsBatch;
import com.haulmont.mp2xls.writer.LocalizationBatchExcelWriter;
import com.haulmont.mp2xls.writer.LocalizationBatchFileWriter;
import org.apache.commons.cli.*;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class MessagePropertiesProcessor {

    public static final String READ_OPT = "r";
    public static final String WRITE_OPT = "w";
    public static final String PROJECT_DIR_OPT = "d";
    public static final String XLS_FILE_OPT = "f";
    public static final String LOG_FILE_OPT = "log";
    public static final String LANGUAGES_OPT = "l";

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(READ_OPT, "read", false, "read messages from project and save to XLS");
        options.addOption(WRITE_OPT, "write", false, "load messages from XLS and write to project");
        options.addOption(PROJECT_DIR_OPT, "projectDir", true, "project root directory");
        options.addOption(XLS_FILE_OPT, "xlsFile", true, "XLS file with translations");
        options.addOption(LOG_FILE_OPT, "logFile", true, "log file");
        options.addOption(LANGUAGES_OPT, "languages", true, "list of locales separated by comma, for example: 'de,fr'");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            if ((!cmd.hasOption(READ_OPT) && !cmd.hasOption(WRITE_OPT))
                    || !cmd.hasOption(PROJECT_DIR_OPT)
                    || !cmd.hasOption(XLS_FILE_OPT)
                    || !cmd.hasOption(LANGUAGES_OPT)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Messages To/From XLS Convertor", options);
                System.exit(-1);
            }
            if (cmd.hasOption(READ_OPT) && cmd.hasOption(WRITE_OPT)) {
                System.out.println("Please provide either 'read' or 'write' option");
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Messages To/From XLS Convertor", options);
                System.exit(-1);
            }

            Set<String> languages = getLanguages(cmd.getOptionValue(LANGUAGES_OPT));

            if (cmd.hasOption(READ_OPT)) {
                LocalizationsBatch localizationsBatch = new LocalizationsBatch(cmd.getOptionValue(PROJECT_DIR_OPT));
                localizationsBatch.setScanLocalizationIds(languages);

                LocalizationBatchExcelWriter.exportToXls(localizationsBatch, cmd.getOptionValue(XLS_FILE_OPT));

            } else if (cmd.hasOption(WRITE_OPT)) {
                LocalizationsBatch sourceLocalization = new LocalizationsBatch(cmd.getOptionValue(PROJECT_DIR_OPT));
                sourceLocalization.setScanLocalizationIds(languages);

                LocalizationsBatch fileLocalization = new LocalizationsBatch(cmd.getOptionValue(XLS_FILE_OPT), cmd.getOptionValue(PROJECT_DIR_OPT));
                fileLocalization.setScanLocalizationIds(languages);

                LocalizationBatchFileWriter fileWriter = new LocalizationBatchFileWriter(sourceLocalization, fileLocalization);
                fileWriter.process(StringUtils.isNotEmpty(cmd.getOptionValue(LOG_FILE_OPT)) ?
                        cmd.getOptionValue(LOG_FILE_OPT) : "log.xls");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static Set<String> getLanguages(String optValue) {
        Set<String> languages = new HashSet<>();
        if (StringUtils.isNotEmpty(optValue)) {
            languages.addAll(Arrays.asList(optValue.split(",")));
        }
        return languages;
    }
}
