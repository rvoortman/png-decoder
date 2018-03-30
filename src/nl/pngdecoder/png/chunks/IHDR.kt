package png.chunks

import nl.pngdecoder.exceptions.CorruptedPNGException
import nl.pngdecoder.png.models.PNGImage
import nl.pngdecoder.constants.ColorType
import nl.pngdecoder.util.ByteReader

/**
 * Header Chunk
 *
 * Width:              4 bytes
 * Height:             4 bytes
 * Bit depth:          1 byte
 * Color type:         1 byte
 * Compression method: 1 byte
 * Filter method:      1 byte
 * Interlace method:   1 byte
 */
class IHDR {
    companion object {
        fun read(bytes: ByteArray, image: PNGImage) {
            image.width = ByteReader.readBytesToInt(4, bytes)
            image.height = ByteReader.readBytesToInt(4, bytes)
            image.bitDepth = getBitDepth(ByteReader.readBytesToInt(1, bytes))
            image.rawColorType = ByteReader.readBytesToInt(1, bytes)
            image.colorType = getColorType(image.rawColorType , image)
            image.compressionMethod = ByteReader.readBytesToInt(1, bytes)
            image.filterMethod = ByteReader.readBytesToInt(1, bytes)
            image.interlaceMethod = ByteReader.readBytesToInt(1, bytes)
        }

        private fun getBitDepth(rawBitDepth: Int): Int {
            when (rawBitDepth) {
                1, 2, 4, 8, 16 -> return rawBitDepth
                else -> throw CorruptedPNGException("Invalid bit depth")
            }
        }

        private fun getColorType(rawColorType: Int, image: PNGImage): Int {
            when (rawColorType) {
                0 -> return ColorType.GrayScale
                2 -> if (image.bitDepth % 8 == 0) return ColorType.RGB
                3 -> if (image.bitDepth != 16) return ColorType.Palette
                4 -> if (image.bitDepth % 8 == 0) {
                        image.alpha = true
                        return ColorType.GrayScale
                    }
                6 -> if (image.bitDepth % 8 == 0) {
                        image.alpha = true
                        return ColorType.RGB
                    }
            }
            throw CorruptedPNGException("Invalid color type")
        }
    }
}
