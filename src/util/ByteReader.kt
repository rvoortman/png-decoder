package util

import png.ChunkReader
import java.io.File
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files

class ByteReader {
    companion object {
        fun readFileIntoBuffer(file: String): ByteArray {
            return Files.readAllBytes(File(file).toPath())
        }

        fun readBytesToInt(size: Int, bytes: ByteArray, from: Int = ChunkReader.currentIndex): Int {
            ChunkReader.currentIndex += size
            val chunk =  ByteBuffer.wrap(slice(from, from + size, bytes))
            return BigInteger(chunk.array()).toInt()
        }

        fun readBytesToString(size: Int, bytes: ByteArray, from: Int = ChunkReader.currentIndex, encoding: Charset = StandardCharsets.UTF_8): String {
            ChunkReader.currentIndex += size
            return String(slice(from, from + size, bytes), encoding)
        }

        fun readBytes(size: Int, bytes: ByteArray, from: Int = ChunkReader.currentIndex): ByteArray {
            ChunkReader.currentIndex += size
            return slice(from, from + size, bytes)
        }

        fun slice(from: Int, to: Int, bytes: ByteArray): ByteArray {
            return bytes.sliceArray(from until to)
        }
    }

}


