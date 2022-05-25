package com.olivia.architecturetemplate.data.util

import java.io.File

/**
 * # bemily_messenger-app-android
 * # file utils
 * @author hsjun85
 * @since 2022-03-02
 */
object FileUtils {

    fun deleteFile(path: String) {
        deleteFile(File(path))
    }

    fun deleteFile(file: File) {
        if (file.exists()) {
            if (file.isDirectory) {
                val files = file.listFiles()
                if (files != null && files.isNotEmpty()) {
                    for (i in files.indices) {
                        deleteFile(files[i])
                    }
                }
            }
            file.delete()
        }
    }

}