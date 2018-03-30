package png.chunks

import nl.pngdecoder.png.PNGReader

/**
 * This checks if the header is correct.
 */
class HEAD {
    companion object {
        fun checkHeader(byteArray: ByteArray) : Boolean {
            val pngHeader = ByteArray(8, {intArrayOf(137, 80, 78, 71, 13, 10, 26, 10)[it].toByte()})
            pngHeader.forEachIndexed { index: Int, byte: Byte ->
                if(byteArray[index] != byte) {
                    return false
                }
            }
            PNGReader.currentIndex += 8
            return true
        }
    }
}

