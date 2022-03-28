package se.joelbit.dialife.structure

sealed class DataPackage<A,E> {
    object Loading: DataPackage<Nothing,Nothing>()
    class Data<A>(val data:A): DataPackage<A,Nothing>()
    class Error<E:Throwable>(val error: E): DataPackage<Nothing,E>()
}