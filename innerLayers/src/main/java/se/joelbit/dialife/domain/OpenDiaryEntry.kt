package se.joelbit.dialife.domain

sealed class OpenDiaryEntry {
    object None : OpenDiaryEntry()
    class Entry(val entry: DiaryEntry) : OpenDiaryEntry()
}