package nl.pngdecoder.png.models

import java.io.ByteArrayOutputStream
import nl.pngdecoder.constants.ColorType

data class PNGImage(var width: Int = 0,
                    var height: Int = 0,
                    var bitDepth: Int = 0,
                    var colorType: Int = ColorType.GrayScale,
                    var rawColorType: Int = 0,
                    var compressionMethod: Int = 0,
                    var filterMethod: Int = 0,
                    var interlaceMethod: Int = 0,
                    val rawImageData: ByteArrayOutputStream = ByteArrayOutputStream(),
                    var computedImageData: ByteArray = ByteArray(0),
                    var alpha: Boolean = false,
                    var palette: Palette? = null)
