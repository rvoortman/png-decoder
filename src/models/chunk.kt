package models

data class Chunk(val length: Long, val chunkType: String, val data: ByteArray, val CRC: Long, val startIndex: Long)

//                 Length 	Chunk type 	Chunk data 	CRC