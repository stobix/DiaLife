package se.joelbit.dialife.ui.displayEntities.mappers

import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.ui.displayEntities.DisplayDiaryEntry

class IconDisplayDiaryEntryMapper(val mapper: DisplayIconMapper): DisplayDiaryEntryMapper {
    override fun toDisplayEntry(entry: DiaryEntry) =
        DisplayDiaryEntry(
            id = entry.id,
            title = entry.title,
            text = entry.text,
            datetime = entry.datetime,
            icon = mapper.fromIcon(entry.icon)
        )

    override fun fromDisplayEntry(entry: DisplayDiaryEntry) =
        DiaryEntry(
            id = entry.id,
            title = entry.title,
            text = entry.text,
            datetime = entry.datetime,
            icon = mapper.toIcon(entry.icon)
        )

}