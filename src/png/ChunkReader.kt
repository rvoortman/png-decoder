package png

import exceptions.CorruptedPNGException
import exceptions.InvalidHeaderException
import models.PNGImage
import png.chunks.IDAT
import png.chunks.IHDR
import png.chunks.PLTE
import png.chunks.HEAD
import util.ByteReader
import java.util.zip.CRC32

class ChunkReader {
    companion object {
        var currentIndex = 8
        var firstHeader = true
        fun readPng(bytes: ByteArray): PNGImage {
            if(!HEAD.checkHeader(bytes)) {
                throw InvalidHeaderException("Can't read file, invalid header.")
            }
            val image = PNGImage()

            readChunk(bytes, image)

            // Last four bytes are the CRC of the IEND chunk.
            if(currentIndex + 4 != bytes.size) {
                throw CorruptedPNGException("Unexpected end of file!")
            }

            ImageData.convertRawDataToImageData(image)

            return image
        }

        private fun readChunk(bytes: ByteArray, image: PNGImage) {
            val length = ByteReader.readBytesToInt(4, bytes, currentIndex)
            val type = ByteReader.readBytesToString(4, bytes, currentIndex)

            if(firstHeader && type != "IHDR") {
                throw CorruptedPNGException("First header is not a IHDR")
            }
            firstHeader = false
            println("$type: $length")
            when(type) {
                "IHDR" -> IHDR.read(bytes, image)
                "PLTE" -> PLTE.read(bytes, image, length)
                "IDAT" -> IDAT.read(bytes, image, length)
                "IEND" -> return
                else -> currentIndex += length
            }

            if(!validCRC(bytes, length)) {
                throw CorruptedPNGException("CRC is not valid!")
            }

            readChunk(bytes, image)
        }

        private fun validCRC(bytes: ByteArray, length: Int) : Boolean {
            val chunkData = ByteReader.slice(currentIndex - length - 4, currentIndex, bytes)
            val pngCRC = ByteReader.readBytesToInt(4, bytes, currentIndex)
            val crc = CRC32()
            crc.update(chunkData)

            return pngCRC == crc.value.toInt()
        }
    }
}

