package nl.pngdecoder.exceptions

/**
 * There are some features which are not yet supported by this library
 * This exception is used to mark them.
 */
class UnsupportedFeatureException(override val message: String): Exception(message)
