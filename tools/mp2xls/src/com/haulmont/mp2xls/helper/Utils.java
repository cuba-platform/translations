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

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


public class Utils {
    public static CharsetDecoder getUtf8Decoder() {
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        decoder.onMalformedInput(CodingErrorAction.REPORT);
        decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
        return decoder;
    }

    public static List<File> listFilesForFolder(final File folder, String mask, Set<String> skipFolders) {
        Pattern p = Pattern.compile(mask);
        List<File> result = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files != null) {
            for (final File fileEntry : files) {
                if (fileEntry.isDirectory() && !skipFolders.contains(fileEntry.getName()))
                    result.addAll(listFilesForFolder(fileEntry, mask, skipFolders));
                else if (p.matcher(fileEntry.getName()).matches())
                    result.add(fileEntry);
            }
        }
        return result;
    }

}
