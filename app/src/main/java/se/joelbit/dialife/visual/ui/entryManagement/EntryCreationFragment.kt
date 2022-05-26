package se.joelbit.dialife.visual.ui.entryManagement

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.material.icons.sharp.WhereToVote
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.appcompattheme.AppCompatTheme
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import se.joelbit.dialife.MainViewModel
import se.joelbit.dialife.R
import se.joelbit.dialife.databinding.FragmentManagementBinding
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.Icon as DomainIcon
import se.joelbit.dialife.domain.Picture
import se.joelbit.dialife.experimental.CameraView
import se.joelbit.dialife.visual.displayEntities.DisplayIcon
import se.joelbit.dialife.visual.ui.entryManagement.EntryCreationFragment.ViewState.*
import se.joelbit.dialife.visual.uiComponents.IconArrayAdapter
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.concurrent.Executors
import kotlin.random.Random

class EntryCreationFragment : Fragment() {

    private var _binding: FragmentManagementBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val rnd = Random.Default

//    val viewModel by viewModel<EntryManagementViewModel>()
    val viewModel  by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cameraExecutor = Executors.newSingleThreadExecutor()

        _binding = FragmentManagementBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addEntry.setOnClickListener {
            val displayIcon = binding.iconSpinner.selectedItem as DisplayIcon
            viewModel.addEntry(
                DiaryEntry(
                    id = rnd.nextLong() ,
                    title = binding.entryTitle.text.toString(),
                    text = binding.entryText.text.toString(),
                    icon = DomainIcon.fromOrdinal(displayIcon.ordinal),
                    datetime =LocalDateTime.now(),
                    pictures = listOf()
                )
            )
        }

        binding.compose.setContent {
            AppCompatTheme {
                val state = remember {
                    mutableStateOf(
                        if (viewModel.hasCamPerm.value)
                            NoPhoto
                        else
                            Disabled
                    )
                }

                Log.d("Camera", "Rerendering: ${state.value}")

                when (val currState = state.value) {
                    is TakingPhoto -> {
                        val prev = currState.previous
                        CameraView(
                            context = requireActivity(),
                            outdir = viewModel.mediaDir.value,
                            executor = cameraExecutor,
                            onError = {
                                Log.e("Camera","${it.message}")
                                state.value =
                                    if(prev!=null)
                                        Photo(prev)
                                    else NoPhoto
                            },
                            onSuccess = {
                                state.value = Photo(TakenPhoto(it, LocalDateTime.now()))
                            }
                        )
                    }
                    else ->
                        DisplayState(state)
                }
            }
        }
        
        with (binding.iconSpinner) {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            adapter = IconArrayAdapter(context, viewModel.iconMapper)

        }
        return root
    }

    @Preview
    @Composable
    fun DisabledPreview() {
        AppCompatTheme() {
            Row {
                DisplayState( remember { mutableStateOf(Disabled) } )
            }
        }
    }

    @Preview
    @Composable
    fun NoPhotoPreview() {
        AppCompatTheme() {
            Row {
                DisplayState( remember { mutableStateOf(NoPhoto) } )
            }
        }
    }

    @Preview
    @Composable
    fun TakingPhotoPreview() {
        AppCompatTheme() {
            Row {
                DisplayState( remember { mutableStateOf(TakingPhoto(null)) } )
            }
        }
    }

    @Preview
    @Composable
    fun PhotoTakenPreview() {
        AppCompatTheme() {
            Column {
                DisplayState( remember { mutableStateOf(Photo(TakenPhoto("".toUri(), LocalDateTime.now()))) } )
            }
        }
    }

    data class TakenPhoto(
        val uri: Uri,
        val timestamp: LocalDateTime,
    )

    sealed class ViewState {
        object Disabled : ViewState()
        data class TakingPhoto(val previous: TakenPhoto?) : ViewState()
        object NoPhoto : ViewState()
        data class Photo(val photo: TakenPhoto) : ViewState()
    }

    @Composable
    fun DisplayState(state: MutableState<ViewState>, id: Int = 3){
        val title = remember { mutableStateOf( "Titel" ) }
        val body = remember { mutableStateOf( "Hej" ) }
        var imageRes by remember(key1 = id ) {
            mutableStateOf<String?>(null)
        }
        Column() {
            TextField(value = title.value, onValueChange = { text ->
                Log.d(this.javaClass.name, text)
                title.value = text
            } , singleLine = true,
                readOnly = false)
            Spacer(modifier = Modifier.width(10.dp))
            TextField(value = body.value,
                singleLine = false,
                onValueChange = { text ->
                body.value = text
                Log.d(this.javaClass.name,text)
            })
            when(val currState = state.value){
                NoPhoto ->
                    Row {
                        Button(onClick = { state.value = TakingPhoto(null) }) {
                            Text("Take picture")
                            Image(
                                painterResource(id = R.drawable.ic_error1),
                                contentDescription = "Take picture"
                            )
                        }
                        IconButton(onClick = {
                            viewModel.addEntry(
                                DiaryEntry(
                                    id = rnd.nextLong() ,
                                    title = title.value,
                                    text = body.value,
                                    icon = DomainIcon.Happy,
                                    datetime =LocalDateTime.now(),
                                    pictures = emptyList()
                                )
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.WhereToVote,
                                contentDescription = "Save",
                                tint = Color.Green,
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(1.dp)
                                    .border(1.dp, Color.Blue, CircleShape)
                            )
                        }
                    }
                is Photo ->
                    Row {
                        Button(onClick = { state.value = TakingPhoto(currState.photo) }) {
                            Text("Retake picture")
                            Image(
                                rememberAsyncImagePainter(currState.photo.uri),
                                contentDescription = "Taken picture",
                                Modifier.width(100.dp)
                            )
                        }
                        Button(onClick = {
                            viewModel.addEntry(
                                DiaryEntry(
                                    id = rnd.nextLong() ,
                                    title = title.value,
                                    text = body.value,
                                    icon = DomainIcon.Happy,
                                    datetime =LocalDateTime.now(),
                                    pictures = listOf(
                                        Picture(0,currState.photo.uri.path!!, timestamp = currState.photo.timestamp)
                                    )
                                )
                            )
                        }) {

                        }
                    }
                is TakingPhoto ->
                {}
                else -> Row{
                    Button(onClick = {
                        viewModel.addEntry(
                            DiaryEntry(
                                id = rnd.nextLong() ,
                                title = title.value,
                                text = body.value,
                                icon = DomainIcon.Happy,
                                datetime =LocalDateTime.now(),
                                pictures = emptyList(),
                            )
                        )
                    }) {

                    }

                }
            }
            Box(modifier = Modifier.height(100.dp))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

