package nl.pngdecoder.exceptions

/**
 * This exception is thrown when the PNG is corrupted.
 * Please provide in the message where and how the PNG is corrupted.
 */
class CorruptedPNGException(override val message: String): Exception(message)
