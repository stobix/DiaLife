package se.joelbit.dialife.visual.ui.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.appcompattheme.AppCompatTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject
import se.joelbit.dialife.databinding.SettingsFragmentBinding
import se.joelbit.dialife.domain.Icon
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayIconHandsMapper
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayIconMapper
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayIconSmiliesMapper
import se.joelbit.dialife.visual.ui.diaryEntries.DiaryEntriesViewModel

class SettingsFragment : Fragment() {

    private var _binding: SettingsFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val viewModel  by viewModel<SettingsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        val icons = Icon.values().toList()
        binding.greeting.setContent {
            AppCompatTheme {
                SettingsView()
            }
        }
        return binding.root
    }

    @Composable
    fun SettingsView() {
        Row {
            Spacer(modifier = Modifier.width(10.dp))
            IconsView(icons = Icon.values().toList())
        }
    }

    // Preview eats resources. Only uncomment when previewing.
//    @Preview
    @Composable
    fun PreviewView() {
        AppCompatTheme() {
            SettingsView()
        }
    }
    

    @Composable
    fun IconsView(icons: List<Icon>) {
        val iconsets = listOf(
            "Smilies" to DisplayIconSmiliesMapper(),
            "Hands" to DisplayIconHandsMapper()
        )
        var clicked by remember { mutableStateOf(-1) }

        LazyColumn {
            itemsIndexed(items=iconsets){ ix,  (title,mapper) ->
                Row(modifier=Modifier.clickable {
                    clicked = ix
                }) {
                    IconSet(ix == clicked, title = title, icons = icons, mapper = mapper)
                }
            }
        }
    }


    @Composable
    fun IconSet(active: Boolean, title: String, icons: List<Icon>, mapper: DisplayIconMapper) {
        Column (modifier = Modifier.background(color = if(active) Color.Blue else Color.Transparent)) {
            Text(title)
            LazyRow (){
                items(icons.size) { ix ->
                    val icon = icons[ix]
                    Image(
                        painter = painterResource( id = mapper.fromIcon(icon).resId),
                        icon.name,
                        colorFilter =
                            if(!icon.isOrdinary)
                                ColorFilter.tint(Color.LightGray, blendMode = BlendMode.Hardlight)
                            else
                                null
                    )
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}