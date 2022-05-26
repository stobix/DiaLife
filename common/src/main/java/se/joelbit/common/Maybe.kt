package se.joelbit.common


fun <A,B> A?.map(f: (A) -> B) = when(this) {
    null -> null
    else -> f(this)
}

/**
 * A general nullable wrapper that can map/run/let over existing values et al.
 */
open class Maybe<E>
constructor(val entry: MaybeEntry<E>){
    constructor(initial: E?=null) : this(
        initial?.let { MaybeEntry.Entry(it) } ?: MaybeEntry.None()
    )

    fun <F> map(f: (E) -> F) = Maybe(entry.map(f))
    fun <R> runEntry(f: E.() -> R) = entry.runEntry(f)
    fun <R> letEntry(f: (E) -> R) = entry.runEntry(f)
    fun asNullable() = entry.asNullable()

    fun orElse(replacement: E) = entry.whenEntry { this } whenNot { Maybe(replacement) }

    /**
     * Used as entry.whenEntry { /* do things with entry */ } whenNot { /* do things without entry */ }
     */
    fun <R> whenEntry(f: (E) -> R) = entry.whenEntry(f)

    sealed class MaybeEntry<E> {
        class None<E> : MaybeEntry<E>() {
            override fun asNullable(): E? = null
        }

        class Entry<E>(val entry: E) : MaybeEntry<E>() {
            override fun asNullable(): E? = entry
        }

        fun <F> map(f: (E) ->F) : MaybeEntry<F> = when(this) {
            is Entry -> Entry(f(entry))
            is None -> None()
        }

        fun <R> runEntry(f: (E) -> R) = when(this) {
            is Entry -> f(entry)
            is None -> null
        }

        fun <R> whenEntry(f: (E) -> R) = when(this) {
            is Entry -> Continuation.EntryResponse(f(entry))
            is None -> Continuation.EmptyResponse()
        }

        sealed class Continuation<R> {
            data class EntryResponse<R>(val response: R): Continuation<R>() {
                override fun whenNot(f: () -> R): R = response
            }
            class EmptyResponse<R>(): Continuation<R>() {
                override fun whenNot(f: () -> R): R = f()
            }

            abstract infix fun whenNot(f: () -> R): R
        }


        abstract fun asNullable(): E?
    }
}