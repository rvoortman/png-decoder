package nl.pngdecoder.png

import nl.pngdecoder.exceptions.UnsupportedFeatureException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.InflaterInputStream
import nl.pngdecoder.png.models.PNGImage

/**
 * This class inflates (decompresses) the zlib compressed IDAT's into actual image data.
 */
class ImageData {
    companion object {
        fun convertRawDataToImageData(image: PNGImage) {
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
            } else {
                throw UnsupportedFeatureException("Interlaced images are not yet supported!")
            }

            image.computedImageData = prunedData
        }

        const val BUFFER_SIZE = 8192
        const val BYTE_SIZE = 8
    }
}
