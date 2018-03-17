package models

import png.constants.ColorType

data class PNGImage(var width: Int = 0,
                    var height: Int = 0,
                    var bitDepth: Int = 0,
                    var colorType: Int = ColorType.GrayScale,
                    var compressionMethod: Int = 0,
                    var filterMethod: Int = 0,
                    var interlaceMethod: Int = 0,
                    val dataChunks: ArrayList<Chunk> = ArrayList(),
                    var alpha: Boolean = false)

//png.setWidth(             readUInt32(chunk, 0));
//png.setHeight(            readUInt32(chunk, 4));
//png.setBitDepth(          readUInt8(chunk,  8));
//png.setColorType(         readUInt8(chunk,  9));
//png.setCompressionMethod( readUInt8(chunk, 10));
//png.setFilterMethod(      readUInt8(chunk, 11));
//png.setInterlaceMethod( readUInt8(chunk, 12));