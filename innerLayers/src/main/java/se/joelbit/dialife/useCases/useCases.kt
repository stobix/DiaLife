package se.joelbit.dialife.useCases

import se.joelbit.dialife.data.DiaryEntryRepository
import se.joelbit.dialife.data.OpenDiaryEntryRepository
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.OpenDiaryEntry


/**
 * When invoked, this use case runs [f] on the repository [r]. It is invoked with no arguments.
 */
open class NoArgsUseCase<Repo, T>(private val r: Repo, private val f:suspend Repo.() -> T){
    suspend operator fun invoke() = f(r)
}

/**
 * When invoked, this use case runs [f] on the repository [r]. The invocation takes one argument of type [Arg] and passes it on to [f].
 */
open class OneArgUseCase<Repo, Arg, T>(private val r: Repo, private val f:suspend Repo.(Arg) -> T){
    suspend operator fun invoke(arg: Arg) = f(r, arg)
}

class GetEntries(repository: DiaryEntryRepository) :
    NoArgsUseCase<DiaryEntryRepository,List<DiaryEntry>>(repository, { getAll() })

class AddEntry(repository: DiaryEntryRepository) :
    OneArgUseCase<DiaryEntryRepository,DiaryEntry,Unit>(repository, { add(it) })

class RemoveEntry(repository: DiaryEntryRepository) :
    OneArgUseCase<DiaryEntryRepository,DiaryEntry,Unit>(repository, { remove(it) })

class GetOpenEntry(repository: OpenDiaryEntryRepository) :
    NoArgsUseCase<OpenDiaryEntryRepository, OpenDiaryEntry>(repository, { get() })

class ClearOpenEntry(repository: OpenDiaryEntryRepository) :
    NoArgsUseCase<OpenDiaryEntryRepository, Unit>(repository, { close() })

class SetOpenEntry(repository: OpenDiaryEntryRepository) :
    OneArgUseCase<OpenDiaryEntryRepository,DiaryEntry, Unit>(repository, { set(it) })
