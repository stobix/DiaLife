package se.joelbit.dialife.visual.uiComponents

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import se.joelbit.dialife.databinding.SpinnerItemBinding
import se.joelbit.dialife.domain.Icon
import se.joelbit.dialife.visual.displayEntities.DisplayIcon
import se.joelbit.dialife.visual.displayEntities.mappers.DisplayIconMapper

class IconArrayAdapter(context: Context, private val converter: DisplayIconMapper)
: ArrayAdapter<DisplayIcon>(
    context, 0, Icon.values().filter { it.isOrdinary }.map { converter.fromIcon(it) }
)
{
    private infix fun View?.boundFor(parent: ViewGroup) =
        if (this == null)
            SpinnerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        else
            SpinnerItemBinding.bind(this)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
        with(convertView boundFor parent) {
            val resId = getItem(position)?.resId ?: converter.fromIcon(Icon.Error).resId
            Log.d("resId", "$resId")
            iconImage.setImageResource(resId)
            root
        }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
        getView(position,convertView,parent)

}