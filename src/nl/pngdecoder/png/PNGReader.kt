package nl.pngdecoder.png

import png.chunks.IDAT
import png.chunks.IHDR
import png.chunks.PLTE
import png.chunks.HEAD
import java.awt.image.DataBufferByte
import java.awt.image.Raster
import java.awt.image.WritableRaster
import java.util.zip.CRC32
import java.awt.image.IndexColorModel
import java.awt.image.ColorModel
import java.awt.image.BufferedImage
import nl.pngdecoder.constants.ColorType
import nl.pngdecoder.exceptions.CorruptedPNGException
import nl.pngdecoder.exceptions.InvalidHeaderException
import nl.pngdecoder.exceptions.UnsupportedFeatureException
import nl.pngdecoder.png.models.PNGImage
import nl.pngdecoder.util.ByteReader

/**
 * The main class
 * Use PNGReader.readPng() to get an BufferedImage, which you can render however you like.
 */
class PNGReader {
    companion object {
        var currentIndex = 0
        var firstHeader = true
        fun readPng(filename: String): BufferedImage {
            val bytes = ByteReader.readFileIntoBuffer(filename)

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
            val raster = getRaster(image)
            val colorModel = getColorModel(image)
            print("Buffering image...")
            return BufferedImage(colorModel, raster, false, null)
        }

        private fun getRaster(image: PNGImage): WritableRaster {
            if (image.colorType == ColorType.Palette) {
                val dataBuffer = DataBufferByte(image.computedImageData, image.computedImageData.size)
                return Raster.createPackedRaster(dataBuffer, image.width, image.height, image.bitDepth, null)
            }
            throw UnsupportedFeatureException("This colortype is not yet supported.")
        }

        private fun getColorModel(image: PNGImage): ColorModel {
            val colorType = image.rawColorType
            val bitsPerPixel = image.bitDepth

            if (colorType == 3) {
                val paletteData = image.palette!!.palette // Palette must not be nil!
                val paletteLength = paletteData.size / 3
                return IndexColorModel(bitsPerPixel, paletteLength,
                        paletteData, 0, false)
            }

            throw UnsupportedFeatureException("This colortype is not yet supported")
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

