package se.joelbit.dialife.ui.diaryEntries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.joelbit.dialife.databinding.FragmentDashboardBinding
import se.joelbit.dialife.databinding.ViewholderEntriesBinding
import se.joelbit.dialife.domain.DiaryEntry

class DiaryEntriesFragment : Fragment() {


    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val viewModel  by viewModel<DiaryEntriesViewModel>()

    inner class EntryViewHolder(val binding: ViewholderEntriesBinding): RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: DiaryEntry) {
            binding.text.text = item.text
            binding.text.setOnClickListener {
                viewModel.setActiveEntry(item)
                refreshActiveText()
            }
        }
    }

    fun refreshActiveText() =
        viewModel.displayActiveEntry()

    val itemDiffCallback = object: DiffUtil.ItemCallback<DiaryEntry>() {
        override fun areItemsTheSame(oldItem: DiaryEntry, newItem: DiaryEntry) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DiaryEntry, newItem: DiaryEntry) =
            oldItem == newItem
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard

        binding.entries.layoutManager = LinearLayoutManager(context)
        val adapter = object: ListAdapter<DiaryEntry, EntryViewHolder>(itemDiffCallback){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
                val entryBinding = ViewholderEntriesBinding.inflate(inflater,parent,false)
                return EntryViewHolder(entryBinding)
            }

            override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
                holder.setItem(getItem(position))
            }

            override fun getItemId(position: Int): Long {
                return getItem(position).id
            }
        }
        adapter.setHasStableIds(true)
        binding.entries.adapter = adapter

        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        viewModel.entries.observe(viewLifecycleOwner) { fetchedEntries ->
            val adapter =
                binding.entries.adapter as ListAdapter<DiaryEntry, EntryViewHolder>
            adapter.submitList(fetchedEntries)
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadEntries()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}