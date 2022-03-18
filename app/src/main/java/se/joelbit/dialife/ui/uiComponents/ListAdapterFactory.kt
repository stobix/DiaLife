package se.joelbit.dialife.ui.uiComponents

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


/**
 * Abstracts away into a function a common use case for a list adapter and view holder.
 * "Why create two classes when one function call suffices?"
 */
object ListAdapterFactory {
    /**
     * Create a simple ListAdapter using a list of [Item]s.
     * Use [binderInflater] to inflate your binding, [itemIdGetter] to get the item id when updating the list,
     * and [itemSetter] to set [Binding] properties from the [Item].
     */
    inline fun <Item, reified Binding: ViewBinding> createListAdapter(
       crossinline binderInflater: (ViewGroup) -> Binding,
        noinline itemIdGetter: (Item) -> Long,
        noinline itemSetter: (Item, Binding) -> Unit,
    ) =
        GeneralSimpleListAdapter({ view ->
            val binding = binderInflater(view)
            GeneralBoundItemViewHolder(binding,itemSetter)
        },itemIdGetter).also { it.setHasStableIds(true) }


    /**
     * Create a simple ListAdapter using a list of [Item]s.
     * Provide [inflater] to inflate your binding, [itemIdGetter] to get the item id when updating the list,
     * and [itemSetter] to set [Binding] properties from the [Item].
     */
    inline fun <Item, reified Binding: ViewBinding> createListAdapter(
        inflater: LayoutInflater,
        noinline itemIdGetter: (Item) -> Long,
        noinline itemSetter: (Item, Binding) -> Unit,
    ) =
        GeneralSimpleListAdapter({ view ->
            val binding = inflateViewBinding<Binding>(inflater, view)
            GeneralBoundItemViewHolder(binding,itemSetter)
        },itemIdGetter).also { it.setHasStableIds(true) }



    /**
     * A list adapter with better default values. Doesn't "forget" which entries are active in edge cases et al.
     */
    open class StableItemListAdapter<A, V : StableItemListAdapter.ItemViewHolder<A>>(
        val holderGenerator: (parent: ViewGroup) -> V,
        val idFetcher: (A) -> Long
    ) : ListAdapter<A, V>(SimpleDiffCallback(idFetcher)) {

        abstract class ItemViewHolder<A>(view: View) : RecyclerView.ViewHolder(view) {
            abstract fun setItem(item: A)
        }

        class SimpleDiffCallback<A, B>(val idFetcher: (A) -> B) : DiffUtil.ItemCallback<A>() {
            override fun areItemsTheSame(oldItem: A, newItem: A) =
                idFetcher(oldItem) == idFetcher(newItem)

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: A, newItem: A) =
                oldItem == newItem
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = holderGenerator(parent)

        override fun onBindViewHolder(holder: V, position: Int) {
            holder.setItem(getItem(position))
        }

        override fun getItemId(position: Int) = idFetcher(getItem(position))

    }

    inline fun <reified Binder: ViewBinding> getBinderConstructor() = Binder::class.members.forEach {
        Log.d("member of ${Binder::class.java.name}", it.name)
        it.parameters.forEach { ott ->
            Log.d("parameter of ${it.name}", "${ott.name} ${ott.kind} ${ott.type}")
        }
    }

    // All ViewBinding classes should have a static "inflate(inflater,parent,attachToParent)" initiator method.
    // This function generalizes the inflate call in order to instantiate whichever ViewBinding class the factory gets
    // without the caller having to write duplicate intantiating code.
    inline fun <reified Binder: ViewBinding> inflateViewBinding(inflater: LayoutInflater, parent:ViewGroup) =
        Binder::class.members
            .find {  it.name == "inflate" &&it.parameters.size == 3 }
            ?.run {
                call(inflater,parent,false)
            } as Binder



    class GeneralBoundItemViewHolder<A, Binding : ViewBinding>(
        private val binding: Binding,
        val itemSetter: (A, Binding) -> Unit
    ) :
        StableItemListAdapter.ItemViewHolder<A>(binding.root) {
        override fun setItem(item: A) {
            itemSetter(item, binding)
        }
    }
}

class GeneralSimpleListAdapter<A, Binding : ViewBinding>(
    holderGenerator: (parent: ViewGroup) -> ListAdapterFactory.GeneralBoundItemViewHolder<A, Binding>,
    idFetcher: (A) -> Long
) : ListAdapterFactory.StableItemListAdapter<A, ListAdapterFactory.GeneralBoundItemViewHolder<A, Binding>>(holderGenerator, idFetcher)
