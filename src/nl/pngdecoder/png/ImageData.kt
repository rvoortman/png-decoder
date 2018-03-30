package nl.pngdecoder.png

import nl.pngdecoder.constants.ColorType
import nl.pngdecoder.exceptions.UnsupportedFeatureException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.InflaterInputStream
import nl.pngdecoder.png.models.PNGImage
import java.awt.image.*

/**
 * This class inflates (decompresses) the zlib compressed IDAT's into actual image data.
 */
class ImageData {
    companion object {
        fun toBufferedImage(image: PNGImage): BufferedImage {
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


        private fun convertRawDataToImageData(image: PNGImage) {
            image.rawImageData.flush()
            // We can only deal with non-interlaced images.
            if (image.interlaceMethod == 0) {
                // Now deflate the data.
                val inflaterStream = InflaterInputStream(ByteArrayInputStream(image.rawImageData.toByteArray()))
                val inflatedOut = ByteArrayOutputStream()
                var readLength = 0
                val block = ByteArray(BUFFER_SIZE)

                while (readLength != -1) {
                    readLength = inflaterStream.read(block)
                    if (readLength != -1) {
                        inflatedOut.write(block, 0, readLength)
                    }
                }

                inflatedOut.flush()
                val imageData = inflatedOut.toByteArray()

                // Compute the real length.
                val bitsPerPixel = image.bitDepth
                val length = image.width * image.height * bitsPerPixel / 8

                val prunedData = ByteArray(length)

                var index = 0
                for (i in 0 until length) {
                    if (i * BYTE_SIZE / bitsPerPixel % image.width == 0) {
                        index++; // Skip the filter byte.
                    }
                    prunedData[i] = imageData[index]
                    index++
                }
                image.computedImageData = prunedData
            } else {
                throw UnsupportedFeatureException("Interlaced images are not yet supported!")
            }
        }

        const val BUFFER_SIZE = 8192
        const val BYTE_SIZE = 8
    }
}
