package com.rizwansayyed.zene.utils

import android.content.Intent
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MusicDataTypes.AI_MUSIC
import com.rizwansayyed.zene.data.model.MusicDataTypes.ALBUMS
import com.rizwansayyed.zene.data.model.MusicDataTypes.ARTISTS
import com.rizwansayyed.zene.data.model.MusicDataTypes.MOVIES
import com.rizwansayyed.zene.data.model.MusicDataTypes.NEWS
import com.rizwansayyed.zene.data.model.MusicDataTypes.NONE
import com.rizwansayyed.zene.data.model.MusicDataTypes.PLAYLISTS
import com.rizwansayyed.zene.data.model.MusicDataTypes.PODCAST
import com.rizwansayyed.zene.data.model.MusicDataTypes.PODCAST_AUDIO
import com.rizwansayyed.zene.data.model.MusicDataTypes.PODCAST_CATEGORIES
import com.rizwansayyed.zene.data.model.MusicDataTypes.RADIO
import com.rizwansayyed.zene.data.model.MusicDataTypes.SONGS
import com.rizwansayyed.zene.data.model.MusicDataTypes.TEXT
import com.rizwansayyed.zene.data.model.MusicDataTypes.VIDEOS
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.connect_status.ConnectStatusActivity
import com.rizwansayyed.zene.utils.MainUtils.copyTextToClipboard
import com.rizwansayyed.zene.utils.MainUtils.moshi
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL
import java.math.BigInteger

enum class SharingContentType {
    COPY, SHARE_TO, CONNECT, WHATS_APP, INSTAGRAM, SNAPCHAT, FACEBOOK, X, PINTEREST
}

object ShareContentUtils {

    fun shareTheData(data: ZeneMusicData?, type: SharingContentType) {
        val id = encryptSharingId(data?.id)

        val url = when (data?.type()) {
            NONE -> ZENE_URL
            SONGS -> "$ZENE_URL/song/${id}"
            RADIO -> "$ZENE_URL/radio/${id}"
            VIDEOS -> "$ZENE_URL/video/${id}"
            PLAYLISTS, ALBUMS -> "$ZENE_URL/mix/${id}"
            ARTISTS -> "$ZENE_URL/artist/${data.name}"
            PODCAST -> "$ZENE_URL/podcast-series/${data.name}"
            PODCAST_AUDIO -> "$ZENE_URL/podcast/${data.id}"
            PODCAST_CATEGORIES -> ZENE_URL
            NEWS -> "$ZENE_URL/news/${data.id}"
            MOVIES -> "$ZENE_URL/m/${data.id}"
            AI_MUSIC -> "$ZENE_URL/ai_music/${data.id}"
            TEXT -> ZENE_URL
            null -> ZENE_URL
        }

        when (type) {
            SharingContentType.COPY -> {
                SnackBarManager.showMessage(context.getString(R.string.copied_link))
                copyTextToClipboard(url)
            }

            SharingContentType.SHARE_TO -> {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, url)
                    setType("text/plain")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }

                context.startActivity(sendIntent)
            }

            SharingContentType.CONNECT -> {
                Intent(context, ConnectStatusActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    val json = moshi.adapter(ZeneMusicData::class.java).toJson(data)
                    putExtra(Intent.ACTION_SEND, json)
                    context.startActivity(this)
                }
            }

            SharingContentType.WHATS_APP -> {}
            SharingContentType.INSTAGRAM -> {}
            SharingContentType.SNAPCHAT -> {}
            SharingContentType.FACEBOOK -> {}
            SharingContentType.X -> {}
            SharingContentType.PINTEREST -> {}
        }
    }


    private const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
    private fun toBigInteger(input: String, alphabet: String = ALPHABET): BigInteger {
        val base = BigInteger.valueOf(alphabet.length.toLong())
        var result = BigInteger.ZERO
        for (char in input) {
            val index = alphabet.indexOf(char)
            if (index == -1) return BigInteger.ZERO
            result = result.multiply(base).add(BigInteger.valueOf(index.toLong()))
        }
        return result
    }

    private fun fromBigInteger(
        number: BigInteger, length: Int, alphabet: String = ALPHABET
    ): String {
        val base = BigInteger.valueOf(alphabet.length.toLong())
        var num = number
        val sb = StringBuilder()
        while (num > BigInteger.ZERO) {
            val rem = num.mod(base)
            sb.append(alphabet[rem.toInt()])
            num = num.divide(base)
        }

        var result = sb.reverse().toString()
        while (result.length < length) result = alphabet[0] + result

        return result
    }

    private fun encryptSharingId(input: String?): String {
        val a = BigInteger.valueOf(3)
        val b = BigInteger.valueOf(7)

        val length = input?.length ?: 0
        val base = BigInteger.valueOf(ALPHABET.length.toLong())
        val modulus = base.pow(length)
        val value = toBigInteger(input ?: "")
        val encrypted = (a.multiply(value).add(b)).mod(modulus)
        return fromBigInteger(encrypted, length)
    }

    fun decryptSharingId(input: String): String {
        val a = BigInteger.valueOf(3)
        val b = BigInteger.valueOf(7)


        val length = input.length
        val base = BigInteger.valueOf(ALPHABET.length.toLong())
        val modulus = base.pow(length)
        val value = toBigInteger(input)

        val aInv = a.modInverse(modulus)

        val decrypted = (aInv.multiply(value.subtract(b))).mod(modulus)
        return fromBigInteger(decrypted, length)
    }
}