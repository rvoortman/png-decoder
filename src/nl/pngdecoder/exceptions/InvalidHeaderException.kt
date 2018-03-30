package nl.pngdecoder.exceptions

/**
 * This exception is thrown if the PNG does not have a valid PNG Header.
 */
class InvalidHeaderException(override val message: String): Exception(message)
