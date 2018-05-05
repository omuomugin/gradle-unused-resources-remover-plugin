package com.github.konifar.gradle.remover.remover

import com.github.konifar.gradle.remover.remover.util.ColoredLogger
import com.github.konifar.gradle.remover.remover.util.DirectoryMatcher

abstract class AbstractFileRemover extends AbstractRemover {

    @Override
    def removeEach(File resDirFile, List<String> moduleSrcDirs) {
        def checkResult = false
        resDirFile.eachDirRecurse { dir ->
            if (DirectoryMatcher.matchLast(dir.path, fileType)) {
                dir.eachFile { f ->
                    checkResult |= removeFileIfNeed(f, moduleSrcDirs)
                }
            }
        }

        if (checkResult) {
            ColoredLogger.log "[${fileType}]   Complete to remove files."
        } else {
            ColoredLogger.log "[${fileType}]   No unused ${fileType} files."
        }
    }

    def removeFileIfNeed(File file, List<String> moduleSrcDirs) {
        boolean isMatched = checkTargetTextMatches(extractFileName(file), moduleSrcDirs)

        if (!isMatched) {
            ColoredLogger.logGreen("[${fileType}]   Remove ${file.name}")
            file.delete()
            return true
        } else {
            return false
        }
    }

    private static String extractFileName(File file) {
        return file.name.take(file.name.lastIndexOf('.'))
    }

}