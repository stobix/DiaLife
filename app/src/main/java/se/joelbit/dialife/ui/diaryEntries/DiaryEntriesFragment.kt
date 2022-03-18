package se.joelbit.dialife.ui.diaryEntries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.joelbit.dialife.databinding.FragmentEntriesBinding
import se.joelbit.dialife.databinding.ViewholderEntriesBinding
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.OpenDiaryEntry
import se.joelbit.dialife.ui.DisplayIcon
import se.joelbit.dialife.ui.uiComponents.ListAdapterFactory
import se.joelbit.dialife.ui.res

class DiaryEntriesFragment : Fragment() {


    private var _binding: FragmentEntriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val viewModel  by viewModel<DiaryEntriesViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getActiveEntry()

        binding.entries.layoutManager = GridLayoutManager(context, 2)

        val adapter =
            ListAdapterFactory.createListAdapter<DiaryEntry, ViewholderEntriesBinding>(
                inflater = inflater,
                itemIdGetter = { it.id },
                itemSetter = { item, binding ->
                    binding.text.text = item.title ?: item.text
                    val displayIcon = DisplayIcon.fromIcon(item.icon)
                    binding.imageView.setImageResource(displayIcon.resId)
                    binding.text.setOnClickListener {
                        viewModel.setActiveEntry(item)
                        viewModel.getActiveEntry()
                    }
                    binding.imageView.setOnClickListener{
                        viewModel.setActiveEntry(item)
                        viewModel.getActiveEntry()
                    }
                }
            )

        binding.entries.adapter = adapter

//        viewModel.text.observe(viewLifecycleOwner) {
//            binding.info.text=it
//        }

        viewModel.activeEntry.observe(viewLifecycleOwner){ activeEntry ->
            when(activeEntry){
                is OpenDiaryEntry.Entry -> {

                    binding.itemDisplay.visibility = View.VISIBLE
                    binding.info.visibility = View.INVISIBLE

                    val entry = activeEntry.entry
                    binding.imageView2.setImageResource(entry.icon.res)
                    binding.title.text = entry.title
                    binding.text.text = entry.text
                    binding.date.text =
                        entry.datetime.toLocalDate().toString()+" "+
                                entry.datetime.toLocalTime().toString()

                }
                OpenDiaryEntry.None -> {
                    binding.itemDisplay.visibility = View.INVISIBLE
                    binding.info.visibility = View.VISIBLE
                    binding.info.text = "Click an entry to see details, or manage entries on the manage tab."
                }
            }



        }

        viewModel.entries.observe(viewLifecycleOwner) { fetchedEntries ->
            adapter.submitList(fetchedEntries)
        }
        return root
    }

    fun EditText.setText(s: String?) = text.apply {
        clear()
        if (text != null) {
            append(text)
        }
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
