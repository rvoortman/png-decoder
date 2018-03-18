package nl.pngdecoder

import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JFrame
import nl.pngdecoder.png.PNGReader
import nl.pngdecoder.util.ByteReader


fun main(args: Array<String>) {
    val image = PNGReader.readPng(args.first())

    renderImage(image)
}

/**
 * This method is only used for testing; please do not actually use this.
 */
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
            .height
            + insets.top + insets.bottom)
}
