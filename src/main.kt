import png.ChunkReader
import util.ByteReader
import sun.jvm.hotspot.oops.CellTypeState.bottom
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JFrame



fun main(args:Array<String>) {
    val bytes = ByteReader.readFileIntoBuffer("/Users/robbin/src/png-decoder/src/wallpaper.png")
    val image = ChunkReader.readPng(bytes)

    renderImage(image)
}

private fun renderImage(image: BufferedImage){
    val f = object : JFrame() {
        override fun paint(g: Graphics) {
            val insets = insets
            print("Rendering image...")
            g.drawImage(image, insets.left, insets.top, null)
        }
    }
    f.isVisible = true
    val insets = f.insets
    f.setSize(image.width + insets.left + insets.right, image
            .getHeight()
            + insets.top + insets.bottom)
}