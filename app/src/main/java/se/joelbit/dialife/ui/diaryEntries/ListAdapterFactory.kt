package se.joelbit.dialife.ui.diaryEntries

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


/**
 * Abstracts away into a function a common use case for a list adapter and view holder.
 * Why create two classes when one function call suffices?
 */
object ListAdaptorFactory {
    /**
     * Create a simple ListAdapter using a list of [Item]s.
     * Use [binderInflator] to inflate your binding, [itemIdGetter] to get the item id when updating the list,
     * and [itemSetter] to set [Binding] properties from the [Item].
     */
    fun <Item, Binding: ViewBinding> createListAdapter(
        binderInflator: (ViewGroup) -> Binding,
        itemIdGetter: (Item) -> Long,
        itemSetter: (Item, Binding) -> Unit,
    ) =
        GeneralSimpleListAdapter({ view ->
            val binding = binderInflator(view)
            GeneralBoundItemViewHolder(binding,itemSetter)
        },itemIdGetter).also { it.setHasStableIds(true) }
    /**
     * A list adapter with better default values.
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
    holderGenerator: (parent: ViewGroup) -> ListAdaptorFactory.GeneralBoundItemViewHolder<A, Binding>,
    idFetcher: (A) -> Long
) : ListAdaptorFactory.StableItemListAdapter<A, ListAdaptorFactory.GeneralBoundItemViewHolder<A, Binding>>(holderGenerator, idFetcher)
