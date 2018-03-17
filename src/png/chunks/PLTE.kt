package png.chunks

import exceptions.CorruptedPNGException
import models.PNGImage
import models.Palette
import png.constants.ColorType
import util.ByteReader
import kotlin.math.pow

/**
 * PNG Palette
 */
class PLTE {
    companion object {
        fun read(bytes: ByteArray, image: PNGImage, size: Int) {
            if(image.colorType == ColorType.GrayScale) {
                throw CorruptedPNGException("Contains color palette in Grayscale image")
            }

            if(size > image.bitDepth.toDouble().pow(2 * 3)) {
                throw CorruptedPNGException("Palette is bigger then allowed")
            }
            if(size % 3 != 0) {
                throw CorruptedPNGException("Palette is not power of three")
            }

            image.palette = Palette(ByteReader.readBytes(size, bytes))
        }
    }
}