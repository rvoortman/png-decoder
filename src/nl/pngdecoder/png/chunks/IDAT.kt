package png.chunks

import nl.pngdecoder.png.models.PNGImage
import nl.pngdecoder.png.PNGReader
import nl.pngdecoder.util.ByteReader

class IDAT {
    companion object {
        fun read(bytes: ByteArray, image: PNGImage, size: Int) {
            val dataBytes = ByteReader.slice(PNGReader.currentIndex, PNGReader.currentIndex + size, bytes)
            image.rawImageData.write(dataBytes)

            PNGReader.currentIndex += size
        }
    }
}
