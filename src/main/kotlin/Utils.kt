import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.toPath


/**
 * Converts string to md5 hash.
 */
internal fun String.md5(): String =
    BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * As a path on the classpath
 */
internal fun String.asPath() =
    asResourceUrl()!!.toURI().toPath()

private fun String.asResourceUrl() =
    Thread.currentThread().contextClassLoader.getResource(this)

