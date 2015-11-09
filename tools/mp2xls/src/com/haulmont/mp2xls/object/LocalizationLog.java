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


public class LocalizationLog {

    public static enum Type {
        ADDED("A"), 
        ADDED_IN_NEW_FILE("AF"),
        CHANCED("C"),
        MOVED("M");

        private String id;

        Type(String id) {
            this.id = id;
        }

        public static Type fromId(String id) {
            if (id == null) {
                return null;
            }
            for (Type t : values()) {

                if (id.equals(t.id)) {
                    return t;
                }
            }
            return null;
        }
    }
    
    public LocalizationLog(String file, String parameterName, String sourceValue, String excelValue, Type type) {
        this.file = file;
        this.parameterName = parameterName;
        this.sourceValue = sourceValue;
        this.excelValue = excelValue;
        this.type = type;
    }

    private String file;
    private String parameterName;
    private String sourceValue;
    private String excelValue;
    private Type type;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
    }

    public String getExcelValue() {
        return excelValue;
    }

    public void setExcelValue(String excelValue) {
        this.excelValue = excelValue;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
