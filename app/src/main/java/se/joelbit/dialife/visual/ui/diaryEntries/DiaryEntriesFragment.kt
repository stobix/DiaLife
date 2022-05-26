package se.joelbit.dialife.visual.ui.diaryEntries

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import se.joelbit.dialife.MainViewModel
import se.joelbit.dialife.databinding.FragmentEntriesBinding
import se.joelbit.dialife.databinding.ViewholderEntriesBinding
import se.joelbit.common.DataPackage
import se.joelbit.dialife.visual.displayEntities.DisplayDiaryEntry
import se.joelbit.dialife.visual.displayEntities.DisplayIcon
import se.joelbit.dialife.visual.displayEntities.DisplayOpenDiaryEntry
import se.joelbit.dialife.visual.displayEntities.DisplayOpenDiaryEntry.*
import se.joelbit.dialife.visual.uiComponents.GeneralSimpleListAdapter
import se.joelbit.dialife.visual.uiComponents.IconArrayAdapter
import se.joelbit.dialife.visual.uiComponents.ListAdapterFactory

class DiaryEntriesFragment : Fragment() {


    private var _binding: FragmentEntriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val viewModel  by sharedViewModel<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.entries.layoutManager = GridLayoutManager(context, 2)

        val adapter =
            ListAdapterFactory.createListAdapter<DisplayDiaryEntry, ViewholderEntriesBinding>(
                inflater = inflater,
                itemSetter = { item, binding ->
                    binding.text.text = item.title ?: item.text
                    binding.imageView.setImageResource(item.icon.resId)
                    binding.text.setOnClickListener {
                        viewModel.setActiveEntry(item)
                    }
                    binding.imageView.setOnClickListener{
                        viewModel.setActiveEntry(item)
                    }
                }
            )

        binding.entries.adapter = adapter


        binding.editButton.setOnClickListener {
            openEntry.value.runEntry {
                val newEntry = copy(
                    title = binding.title.string,
                    text = binding.text.string?:"",
                    icon = binding.iconSpinner.selectedItem as DisplayIcon
                )
                viewModel.updateEntry(newEntry)
                viewModel.setActiveEntry(null)
            }
        }
        binding.deleteButton.setOnClickListener {
            openEntry.value.runEntry {
                viewModel.removeEntry(this)
                viewModel.setActiveEntry(null)
            }
        }

        binding.cancelButton.setOnClickListener {
            viewModel.setActiveEntry(null)
        }

        viewModel.setActiveEntry(null)

        with (binding.iconSpinner) {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            this.adapter = IconArrayAdapter(context, viewModel.iconMapper)
        }
        return root
    }

    val openEntry = MutableStateFlow<DisplayOpenDiaryEntry>(DisplayOpenDiaryEntry())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.entryFlow.collect { entries ->
                    when(entries) {
                        is se.joelbit.common.DataPackage.Data -> {
                            val adapter =binding.entries.adapter as GeneralSimpleListAdapter<DisplayDiaryEntry,ViewholderEntriesBinding>?
                            adapter?.submitList(entries.data)
                        }
                        is se.joelbit.common.DataPackage.Error ->
                            null // TODO add some error message
                        se.joelbit.common.DataPackage.Loading ->
                            null // TODO add some spinner
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.activeEntryFlow.collect{ activeEntry ->
                    Log.d("Open entry", "$activeEntry")
                    openEntry.value = activeEntry

                    activeEntry.whenEntry { entry ->
                        binding.itemDisplay.visibility = View.VISIBLE
                        binding.info.visibility = View.GONE

                        binding.iconSpinner.setSelection(entry.icon.ordinal)
//                            binding.imageView2.setImageResource(entry.icon.resId)
                        binding.title.string = entry.title
                        binding.text.string = entry.text
                        binding.date.string =
                            "${entry.datetime.toLocalDate()} ${entry.datetime.toLocalTime()}"

                    } whenNot {
                        binding.itemDisplay.visibility = View.GONE
                        binding.info.visibility = View.VISIBLE
                        binding.info.string = "Click an entry to see details, or manage entries on the manage tab."
                    }
                }
            }
        }
    }

var TextView.string:String?
        get() = text.toString()
        set(value) { text = value}

    var EditText.string:String?
        get() = text.toString()
        set(s) {
            text.apply {
                clear()
                if (s != null) {
                    append(s)
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
