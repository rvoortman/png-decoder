package png.chunks

import models.PNGImage
import png.ChunkReader
import util.ByteReader

class IDAT {
    companion object {
        fun read(bytes: ByteArray, image: PNGImage, size: Int) {
            val dataBytes = ByteReader.slice(ChunkReader.currentIndex, ChunkReader.currentIndex + size, bytes)
            image.rawImageData.write(dataBytes)

            ChunkReader.currentIndex += size
        }
    }
}
