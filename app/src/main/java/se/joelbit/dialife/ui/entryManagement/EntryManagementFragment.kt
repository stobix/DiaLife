package se.joelbit.dialife.ui.entryManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.joelbit.dialife.databinding.FragmentManagementBinding
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.Icon
import se.joelbit.dialife.ui.DisplayIcon
import se.joelbit.dialife.ui.uiComponents.IconArrayAdapter
import java.time.LocalDateTime
import kotlin.random.Random

class EntryManagementFragment : Fragment() {

    private var _binding: FragmentManagementBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val rnd = Random.Default

    val viewModel by viewModel<EntryManagementViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentManagementBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addEntry.setOnClickListener {
            val displayIcon = binding.iconSpinner.selectedItem as DisplayIcon
            viewModel.addEntry(
                DiaryEntry(
                    id = rnd.nextLong() ,
                    title = binding.entryTitle.text.toString(),
                    text = binding.entryText.text.toString(),
                    icon = Icon.fromOrdinal(displayIcon.ordinal),
                    datetime =LocalDateTime.now()
                )
            )
        }

        binding.removeLastEntry.setOnClickListener {
            viewModel.removeLastEntry()
        }
        with (binding.iconSpinner) {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            adapter = IconArrayAdapter(context)

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

