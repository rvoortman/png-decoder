package png.chunks

import models.PNGImage
import png.ChunkReader
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.zip.Inflater
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil.close



class IDAT {
    companion object {
        fun read(bytes: ByteArray, image: PNGImage, size: Int) {
            val inflater = Inflater()
            inflater.setInput(bytes, ChunkReader.currentIndex, size)
            val output = ByteArrayOutputStream(size)
            val buffer = ByteArray(1024)
            while (!inflater.finished()) {

                val count = inflater.inflate(buffer)

                output.write(buffer, 0, count)

            }
            output.close()

            val finishedOutput = output.toByteArray()
            finishedOutput.forEach {
                println(it)
            }
            println(finishedOutput.size)
            ChunkReader.currentIndex += size
        }
    }
}
