import png.ChunkReader
import util.ByteReader

fun main(args:Array<String>) {
    val bytes = ByteReader.readFileIntoBuffer("/Users/robbin/src/png-decoder/src/png-test.png")
    val image = ChunkReader.readPng(bytes)

    System.out.println(image.toString())
}