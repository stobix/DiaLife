package se.joelbit.dialife.ui.displayEntities

sealed class DisplayOpenDiaryEntry {
    object None : DisplayOpenDiaryEntry()
    class Entry(val entry: DisplayDiaryEntry) : DisplayOpenDiaryEntry()
}
