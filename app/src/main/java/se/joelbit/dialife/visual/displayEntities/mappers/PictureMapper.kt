package se.joelbit.dialife.visual.displayEntities.mappers

import android.net.Uri
import se.joelbit.dialife.domain.Picture
import se.joelbit.dialife.structure.InvokeMapper
import se.joelbit.dialife.visual.displayEntities.DisplayPicture

interface PictureMapper : InvokeMapper<DisplayPicture, Picture>

class PictureMapperImpl: PictureMapper {
    override fun invoke(entry: DisplayPicture) = Picture(
        id = entry.id,
        timestamp = entry.time,
        uri = entry.uri.path!!
    )

    override fun invoke(entry: Picture)= DisplayPicture (
        id=entry.id,
        time = entry.timestamp,
        uri = Uri.parse(entry.uri)
    )
}
