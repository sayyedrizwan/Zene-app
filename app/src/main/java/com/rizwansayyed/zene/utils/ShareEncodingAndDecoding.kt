package com.rizwansayyed.zene.utils

import android.util.Base64
import com.rizwansayyed.zene.BuildConfig
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object ShareEncodingAndDecoding {

    private const val ALGORITHM = "AES/CBC/PKCS5Padding"
    private val KEY = SecretKeySpec(BuildConfig.APP_ENCODE_KEY.toByteArray(), "AES")
    private val iv = IvParameterSpec(ByteArray(16))

    fun decryptData(cipherText: String): String {
        val v = cipherText.replace("__", "/")
        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, KEY, iv)
            val plainText = cipher.doFinal(Base64.decode(v, Base64.DEFAULT))
            String(plainText)
        } catch (e: Exception) {
            ""
        }
    }

    fun encryptData(txt: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, KEY, iv)
        val cipherText = cipher.doFinal(txt.toByteArray())
        val v = Base64.encodeToString(cipherText, Base64.DEFAULT).replace("=+$".toRegex(), "")
        return v.replace("/", "__")
    }

    fun simpleEncode(txt: String): String {
        return Base64.encodeToString(txt.toByteArray(), Base64.DEFAULT).replace("=+$".toRegex(), "")
    }

    fun simpleDecode(txt: String): String {
        return String(Base64.decode(txt.toByteArray(), Base64.DEFAULT))
    }


}