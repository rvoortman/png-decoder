package png

import exceptions.CorruptedPNGException
import exceptions.InvalidHeaderException
import models.PNGImage
import png.chunks.IHDR
import util.ByteReader
import java.util.zip.CRC32

class ChunkReader {
    companion object {
        var currentIndex = 8
        var firstHeader = true
        fun readPng(bytes: ByteArray): PNGImage {
            if(!PNGHeader.checkHeader(bytes)) {
                throw InvalidHeaderException("Can't read file, invalid header.")
            }
            val image = PNGImage()
            readChunk(bytes, image)
            return image
        }

        private fun readChunk(bytes: ByteArray, image: PNGImage): PNGImage {
            val length = ByteReader.readBytesToInt(4, bytes, currentIndex)
            val type = ByteReader.readBytesToString(4, bytes, currentIndex)

            if(firstHeader && type != "IHDR") {
                throw CorruptedPNGException("First header is not a IHDR")
            }

            when(type) {
                "IHDR" -> IHDR.read(bytes, image)
                "PLTE" -> PLTE.read()
            }

            if(!validCRC(bytes, length)) {
                throw CorruptedPNGException("CRC is not valid!")
            }

            return image
        }

        private fun validCRC(bytes: ByteArray, length: Int) : Boolean {
            val chunkData = ByteReader.slice(currentIndex - length - 4, currentIndex, bytes)
            val pngCRC = ByteReader.readBytesToInt(4, bytes, currentIndex).toLong()

            val crc = CRC32()
            crc.update(chunkData)

            return pngCRC == crc.value
        }
    }
}

