if (args.length < 4) {
    println('''Arguments:
0: <srcDir> - project root dir
1: <dstDir> - output dir
2: <moduleName>
3: <mainMessagePack>
4: <language> - optional
''')
}

File srcDir = new File(args[0])
File dstDir = new File(args[1])
def moduleName = args[2]
def mainMessagePack = args[3]

def lang
def suffix = ''
if (args.length > 4) {
    lang = args[4]
    suffix = '_' + lang
}

def modulesDir = new File(srcDir, 'modules')
if (!modulesDir.exists()) {
    println("ERROR: $modulesDir does not exist")
}

def moduleDir = new File(modulesDir, moduleName)
def moduleSrcDir = new File(moduleDir, 'src')
if (moduleSrcDir.exists()) {
    def orderedMap = new LinkedHashMap<String, String>();

    List<File> files = []
    moduleSrcDir.eachFileRecurse { files.add(it) }
    files.sort { it.path }

    files.each { file ->
        if (file.name == "messages${suffix}.properties") {
            def properties = new Properties()
            file.withReader('UTF-8') {
                properties.load(it)
            }

            def sortedMap = new TreeMap<String, String>();
            properties.stringPropertyNames().each { key ->
                def value = properties.getProperty(key)
                if (value) {
                    def path = moduleSrcDir.toPath().relativize(file.parentFile.toPath()).toString().replace(File.separator, '.')
                    if (path == args[3])
                        path = ''
                    else
                        path = path + '/'
                    sortedMap.put(path + key, value.replace('\n', '\\n'))
                }
            }

            orderedMap.putAll(sortedMap)
        }
    }

    if (!orderedMap.isEmpty()) {
        File dstFile = new File(dstDir, "${srcDir.name}/modules/${moduleName}/src/${mainMessagePack.replace('.', '/')}/messages${suffix}.properties")
        dstFile.parentFile?.mkdirs()
        dstFile.withWriter('UTF-8') { writer ->
            orderedMap.entrySet().each {
                writer.write(it.key + ' = ' + it.value + '\n')
            }
        }
    }
}
