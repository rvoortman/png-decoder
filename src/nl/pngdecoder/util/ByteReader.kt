package nl.pngdecoder.util

import java.io.File
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import nl.pngdecoder.png.PNGReader

/**
 * This class is used to read the bytes from a file and convert it into strings, integers or ByteArrays.
 */
class ByteReader {
    companion object {
        fun readFileIntoBuffer(file: String): ByteArray {
            return Files.readAllBytes(File(file).toPath())
        }

        fun readBytesToInt(size: Int, bytes: ByteArray, from: Int = PNGReader.currentIndex, increaseIndex: Boolean = true): Int {
            if(increaseIndex) PNGReader.currentIndex += size
            val chunk =  ByteBuffer.wrap(slice(from, from + size, bytes))
            return BigInteger(chunk.array()).toInt()
        }

        fun readBytesToString(size: Int, bytes: ByteArray, from: Int = PNGReader.currentIndex, encoding: Charset = StandardCharsets.UTF_8): String {
            PNGReader.currentIndex += size
            return String(slice(from, from + size, bytes), encoding)
        }

        fun readBytes(size: Int, bytes: ByteArray, from: Int = PNGReader.currentIndex): ByteArray {
            PNGReader.currentIndex += size
            return slice(from, from + size, bytes)
        }

        fun slice(from: Int, to: Int, bytes: ByteArray): ByteArray {
            return bytes.sliceArray(from until to)
        }
    }
}
