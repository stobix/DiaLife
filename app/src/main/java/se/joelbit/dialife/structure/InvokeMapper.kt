package se.joelbit.dialife.structure

/**
 * A general mapper that uses invoke() to convert from [From] to [To] and back.
 *
 * Also converts lists of [From] and [To] to their list counterparts, with a default trivial implementation.
 */
// https://youtrack.jetbrains.com/issue/KT-31420
@Suppress("INAPPLICABLE_JVM_NAME")
interface InvokeMapper<From,To> {
    @JvmName("invokeFromTo")
    operator fun invoke(entry: From): To
    @JvmName("invokeToFrom")
    operator fun invoke(entry: To): From
    @JvmName("invokeFromToMap")
    operator fun invoke(entries: List<From>): List<To> = entries.map { this(it) }
    @JvmName("invokeToFromMap")
    operator fun invoke(entries: List<To>): List<From> = entries.map { this(it) }
}