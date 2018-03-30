package nl.pngdecoder

import nl.pngdecoder.png.ImageData
import java.awt.Graphics
import javax.swing.JFrame
import nl.pngdecoder.png.PNGReader
import javax.swing.WindowConstants
import javax.swing.JPanel

fun main(args: Array<String>) {
    val frame = buildFrame()

    val image = PNGReader.readPng(args.first())
    val bufferedImage = ImageData.toBufferedImage(image)

    val pane = object : JPanel() {
        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            g.drawImage(bufferedImage, 0, 0, null)
        }
    }

    frame.add(pane)
}

private fun buildFrame(): JFrame {
    val frame = JFrame()
    frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    frame.setSize(200, 200)
    frame.isVisible = true
    return frame
}
