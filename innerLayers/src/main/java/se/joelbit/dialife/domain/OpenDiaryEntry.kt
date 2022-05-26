package se.joelbit.dialife.domain

import se.joelbit.common.Maybe

class OpenDiaryEntry(entry: DiaryEntry?=null): Maybe<DiaryEntry>(entry)
//{
//    object None : OpenDiaryEntry()
//    class Entry(val entry: DiaryEntry) : OpenDiaryEntry()
//}


