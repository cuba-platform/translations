import java.nio.file.Files
import java.nio.file.StandardCopyOption

if (args.length < 2) {
    println('''Arguments:
0: <srcDir> - project root dir
1: <dstDir> - output dir
''')
}

File srcDir = new File(args[0])
File dstDir = new File(args[1])

srcDir.eachFileRecurse { file->
	if (file.name.endsWith('.properties')) {
        def relPath = srcDir.toPath().relativize(file.toPath())
        def dstPath = dstDir.toPath().resolve(relPath)
        if (Files.exists(dstPath)) {
            Files.copy(file.toPath(), dstPath, StandardCopyOption.REPLACE_EXISTING)
        } else {
			Files.createDirectories(dstPath);
			Files.copy(file.toPath(), dstPath, StandardCopyOption.REPLACE_EXISTING)
		}
    }
}