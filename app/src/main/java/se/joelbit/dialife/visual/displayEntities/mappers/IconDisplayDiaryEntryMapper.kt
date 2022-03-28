package se.joelbit.dialife.visual.displayEntities.mappers

import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.visual.displayEntities.DisplayDiaryEntry

class IconDisplayDiaryEntryMapper(val mapper: DisplayIconMapper): DisplayDiaryEntryMapper {

    override fun invoke(entry: DiaryEntry) =
        DisplayDiaryEntry(
            id = entry.id,
            title = entry.title,
            text = entry.text,
            datetime = entry.datetime,
            icon = mapper(entry.icon)
        )

    override fun invoke(entry: DisplayDiaryEntry)=
        DiaryEntry(
            id = entry.id,
            title = entry.title,
            text = entry.text,
            datetime = entry.datetime,
            icon = mapper(entry.icon)
        )

}