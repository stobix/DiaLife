package se.joelbit.dialife.network.ktorServer

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.*
import se.joelbit.dialife.data.DiaryEntryDataSource
import se.joelbit.dialife.domain.DiaryEntry
import se.joelbit.dialife.domain.Icon
import se.joelbit.dialife.useCases.*
import java.time.LocalDateTime
import java.time.ZoneOffset


data class KtorUseCases (
    val addEntry: AddEntry,
    val updateEntry: UpdateEntry,
    val removeEntry: RemoveEntry,
    val getEntries: GetEntries,
)


@Serializable
data class NetworkDiaryEntry(
    val id: Long,
    val title: String?,
    val text: String,
    val datetime: Long,
    val datetimeNS: Int,
    val icon: Int,
)

// https://youtrack.jetbrains.com/issue/KT-31420
@Suppress("INAPPLICABLE_JVM_NAME")
interface InvokeMapper<From,To> {
    @JvmName("invokeFromTo")
    operator fun invoke(entry: From): To
    @JvmName("invokeToFrom")
    operator fun invoke(entry: To): From
}

@Suppress("INAPPLICABLE_JVM_NAME")
class NetworkDiaryEntryMapper: InvokeMapper<DiaryEntry, NetworkDiaryEntry> {
    @JvmName("invokeFromTo")
    override fun invoke(entry: NetworkDiaryEntry) =
        DiaryEntry(
            id = entry.id,
            title = entry.title,
            text = entry.text,
            datetime = LocalDateTime.ofEpochSecond(
                entry.datetime,
                entry.datetimeNS,
                ZoneOffset.UTC
            ),
            icon = Icon.fromOrdinal(entry.icon)
        )

    @JvmName("invokeToFrom")
    override fun invoke(entry: DiaryEntry) =
        NetworkDiaryEntry(
            id = entry.id,
            title = entry.title,
            text = entry.text,
            datetime =
                entry.datetime.toEpochSecond(ZoneOffset.UTC),
            datetimeNS = entry.datetime.nano,
            icon = entry.icon.ordinal
        )


}


class KtorDiaryEntries(
    private val mapper: NetworkDiaryEntryMapper,
    private val retrofitApi: KtorApi,
): DiaryEntryDataSource {
    override suspend fun add(entry: DiaryEntry) {
        retrofitApi.create(mapper(entry))
    }

    override suspend fun getAll() =
        retrofitApi.getAll().map { mapper(it) }


    override suspend fun update(entry: DiaryEntry) {
        TODO("update")
    }

    override suspend fun remove(id: Long) {
        retrofitApi.delete(id)
    }

}

object KtorRetrofitReceiver {
    fun <T> getApiInstance(api: Class<T>) =
        Log.d("Ktor","Initiating api instance").let {
            Retrofit
                .Builder()
                .baseUrl("http://$ktorHost:$ktorPort")
//                .baseUrl("https://$ktorHost:$ktorPort")
                .addConverterFactory(
                    Json.asConverterFactory(
                        MediaType.get("application/json")
                    )
                )
                .build()
                .create(api)
        }
}

interface KtorApi {
    @GET("/all")
    suspend fun getAll() : List<NetworkDiaryEntry>

    @POST("/create")
    suspend fun create(@Body entry: NetworkDiaryEntry)

    @PUT("/update")
    suspend fun update(@Body entry: NetworkDiaryEntry)

    @DELETE("/delete/{id}")
    suspend fun delete(@Path("id") id: Long) : ResponseBody
}
//const val ktorHost = "localhost"
const val ktorHost = "localhost"
const val ktorPort = 8000

class KtorDbProvider(dbUseCases: KtorUseCases, mapper: NetworkDiaryEntryMapper)  {
    val engine =embeddedServer(
        Netty,
        host = ktorHost,
        port = ktorPort,

    ) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get("/all") {
                Log.d("Ktor", "got a get call")
                call.attributes.allKeys.forEach {key ->
                    Log.d("Ktor", "${key.name} = ?? ")
                }
                call.respondBytes {
                    Log.d("Ktor", "responding to a get call")
                    val data = dbUseCases.getEntries().map {
                        mapper(it)
                    }
                    val string = Json.encodeToString(data)
                    Log.d("Ktor", "response: $string")
                    string.toByteArray()
                }
            }

            delete(path = "/delete/{id}"){
                Log.d("Ktor", "responding to a delete call")
                call.parameters.forEach { s, list ->
                    Log.d("Ktor", "$s = $list")
                }
                call.parameters["id"]?.toLongOrNull()?.let { id ->
                    dbUseCases.removeEntry(id)
                }
                call.respondText("Deleted", status = HttpStatusCode.OK)
            }

            post("/create") { // thing ->
                val thing = call.receive<NetworkDiaryEntry>()
                Log.d("Ktor", "responding to a put call with $thing")
                call.parameters.forEach { s, list ->
                    Log.d("Ktor", "$s = $list")
                }
                val converted = mapper(thing)
                dbUseCases.addEntry(converted)
                call.respondText("Cretaed", status = HttpStatusCode.Created )
            }

            put("/update") { // thing ->
                val thing = call.receive<NetworkDiaryEntry>()
                Log.d("Ktor", "responding to a put call with $thing")
                call.parameters.forEach { s, list ->
                    Log.d("Ktor", "$s = $list")
                }
                val converted = mapper(thing)
                dbUseCases.updateEntry(converted)
                call.respondText("Cretaed", status = HttpStatusCode.Created )
            }
        }
    }
    init {
        Log.d("Ktor","Initiating server")
    }
}