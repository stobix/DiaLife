package se.joelbit.dialife.ui.entryManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.joelbit.dialife.databinding.FragmentNotificationsBinding
import se.joelbit.dialife.domain.DiaryEntry
import kotlin.random.Random

class EntryManagementFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val rnd = Random.Default

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel by viewModel<EntryManagementViewModel>()

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addEntry.setOnClickListener {
            notificationsViewModel.addEntry(
                DiaryEntry(
                    id = rnd.nextLong() ,
                    text = binding.entryText.text.toString()
                )
            )
        }

        binding.removeLastEntry.setOnClickListener {
            notificationsViewModel.removeLastEntry()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}