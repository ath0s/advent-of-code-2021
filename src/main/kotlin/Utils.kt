import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.toPath
import kotlin.reflect.KClass
import kotlin.reflect.KFunction


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

internal fun <T> KClass<*>.newInstance() =
    constructors.filterIsInstance<KFunction<T>>().first().call()

private fun String.asResourceUrl() =
    Thread.currentThread().contextClassLoader.getResource(this)

