package png

import models.PNGImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.InflaterInputStream

class ImageData {
    companion object {
        fun convertRawDataToImageData(image: PNGImage) {
            image.rawImageData.flush()
            // Now deflate the data.
            val inflaterStream = InflaterInputStream(ByteArrayInputStream(image.rawImageData.toByteArray()))
            val inflatedOut = ByteArrayOutputStream()
            var readLength = 0
            val block = ByteArray(8192)

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

            // We can only deal with non-interlaced images.
            if (image.interlaceMethod == 0) {
                var index = 0
                for (i in 0 until length) {
                    if ((i * 8 / bitsPerPixel) % image.width == 0) {
                        index++; // Skip the filter byte.
                    }
                    prunedData[i] = imageData[index]
                    index++
                }
            } else {
                System.out.println("Couldn't undo interlacing.")
            }

            image.computedImageData = prunedData
        }
    }
}