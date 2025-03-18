package com.rizwansayyed.zene.data.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class MoviesTvShowResponse(
    val categories: String?,
    val certification: String?,
    val clips: ZeneMusicDataList?,
    val seasons: ZeneMusicDataList?,
    val cover: String?,
    val credits: List<Credit?>?,
    val description: String?,
    val releaseOn: String?,
    val id: String?,
    val isReleased: Boolean?,
    val name: String?,
    val ott: List<Ott?>?,
    val poster: String?,
    val release: Int?,
    val rank: Int?,
    val runtime: Int?,
    val score: Score?,
    val similar: ZeneMusicDataList?,
    val smallId: String?,
    val type: String?
) {

    fun asMusicData(): ZeneMusicData {
        return ZeneMusicData(
            description, smallId, name, "", poster, MusicDataTypes.MOVIES_SHOW.name
        )
    }

    fun readReleaseDate(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(releaseOn ?: "") ?: return ""

        val outputFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        val formattedDate = outputFormat.format(date)

        val calendar = Calendar.getInstance()
        calendar.time = date
        val inputYear = calendar.get(Calendar.YEAR)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return if (inputYear == currentYear) formattedDate else "$formattedDate $inputYear"
    }

    fun readRuntime(): String {
        val hours = (runtime ?: 0) / 60
        val minutes = (runtime ?: 0) % 60

        return if (hours > 0) {
            "${hours}h ${minutes}min"
        } else {
            "${minutes}min"
        }
    }

    data class Score(
        val imdbScore: Double?,
        val imdbVotes: Double?,
        val jwRating: Double?,
        val tmdbPopularity: Double?,
        val tmdbScore: Double?,
        val tomatoMeter: Double?
    )


    data class Credit(
        val characterName: String?,
        val name: String?,
        val personId: Int?,
        val role: String?
    )

    data class Ott(
        val audio: List<String?>?,
        val icon: String?,
        val ottName: String?,
        val price: String?,
        val quality: String?,
        val web: String?
    )
}