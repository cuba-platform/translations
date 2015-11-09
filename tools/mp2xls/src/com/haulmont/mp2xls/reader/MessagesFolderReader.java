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

package com.haulmont.mp2xls.reader;

import com.haulmont.mp2xls.helper.Utils;
import com.haulmont.mp2xls.object.MessagesLocalization;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MessagesFolderReader {
    public static final String FILE_MASK = "(?<prefix>.*messages)(_(?<id>[A-Za-z_]*))?.properties";
    public static final String PROPERTY_MASK = "[ \\t]*(?<key>[^\\s]+)[ \\t]*=[ \\t]*(?<value>.+)?";
    public static final Set<String> systemKeys = new HashSet<String>(){{
        add("@include");
    }};

    public static MessagesLocalization readMessagesFile(final String path, final String baseFolder) throws IOException {
        File file = new File(path);
        File base = new File(baseFolder);
        String relPath = base.toURI().relativize(file.toURI()).getPath();
        MessagesLocalization result = new MessagesLocalization(getLocalizationId(file.getName()), relPath);
        result.getMessages().putAll(readLines(path));
        return result;
    }

    private static String getLocalizationId(String filename){
        Pattern p = Pattern.compile(FILE_MASK);
        Matcher m = p.matcher(filename);
        if (m.matches())
            return m.group("id");
        else
            return null;
    }

    private static Map<String, String> readLines(String path) throws IOException {
        Map<String, String> result = new HashMap<>();
        InputStream is = new FileInputStream(path);
        InputStreamReader reader = new InputStreamReader(is, Utils.getUtf8Decoder());
        BufferedReader br = new BufferedReader(reader);

        Pattern p = Pattern.compile(PROPERTY_MASK);

        String currentString;
        Integer lineNumber = 0;
        while ((currentString = br.readLine()) != null) {
            lineNumber++;
            Matcher m = p.matcher(currentString);
            if (m.matches()/* && !keysToIgnore.contains(m.group("key").trim())*/)
                try {
                    String property = m.group("key");
                    String value = m.group("value");
                    result.put(property, value == null ? null : value.trim());
                } catch (Exception e){
                    System.out.println("Error: Error occurred while reading '" + path + "' file at line #" + lineNumber.toString());
                    e.printStackTrace();
                }
        }

        return result;
    }
}
