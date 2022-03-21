package se.joelbit.dialife.visual.displayEntities

sealed class DisplayOpenDiaryEntry {
    object None : DisplayOpenDiaryEntry()
    class Entry(val entry: DisplayDiaryEntry) : DisplayOpenDiaryEntry()
}
