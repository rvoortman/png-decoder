package util

import java.io.File
import java.nio.file.Files

class ByteReader {
    companion object {
        fun readFileIntoBuffer(file: String): ByteArray {
            return Files.readAllBytes(File(file).toPath())
        }
    }

}


