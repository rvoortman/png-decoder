import png.PNGHeader
import util.ByteReader

fun main(args:Array<String>) {
    val bytes = ByteReader.readFileIntoBuffer("/Users/robbin/src/png-decoder/src/png-test.png")
    System.out.println(PNGHeader.checkHeader(bytes))
}