package se.joelbit.common

sealed class DataPackage<out A,out E> {
    object Loading: DataPackage<Nothing, Nothing>()
    class Data<A,E>(val data:A): DataPackage<A, E>()
    class Error<A,E:Throwable>(val error: E): DataPackage<A, E>()
}