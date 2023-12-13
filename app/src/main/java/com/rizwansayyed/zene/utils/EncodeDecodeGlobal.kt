package com.rizwansayyed.zene.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object EncodeDecodeGlobal {

    private const val algorithm = "AES/CBC/PKCS5Padding"
    val key = SecretKeySpec("zene_rizwan_app0".toByteArray(), "AES")
    private val iv = IvParameterSpec(ByteArray(16))


    fun decryptData(cipherText: String): String {
        return try {
            val cipher = Cipher.getInstance(algorithm)
            cipher.init(Cipher.DECRYPT_MODE, key, iv)
            val plainText = cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT))
            String(plainText)
        } catch (e: Exception) {
            ""
        }
    }

    fun encryptData(txt: String): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val cipherText = cipher.doFinal(txt.toByteArray())
        return Base64.encodeToString(cipherText, Base64.DEFAULT)
    }

    fun simpleEncode(txt: String): String? {
        return Base64.encodeToString(txt.toByteArray(), Base64.DEFAULT)
    }

    fun simpleDecode(txt: String): String {
        return String(Base64.decode(txt.toByteArray(), Base64.DEFAULT))
    }


}