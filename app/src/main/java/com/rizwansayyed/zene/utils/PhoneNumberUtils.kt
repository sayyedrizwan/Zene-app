package com.rizwansayyed.zene.utils

import android.provider.ContactsContract
import android.util.Log
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.rizwansayyed.zene.data.db.DataStoreManager
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

object PhoneNumberUtils {

    fun getContactsLists() = CoroutineScope(Dispatchers.IO).launch {
        val address = DataStoreManager.ipDB.firstOrNull()?.countryCode ?: ""
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val name =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                Log.d("TAG", "getContactsLists: runnedd on $name == $number == ${hasCountryCode(number, address)}")
            }
        }
    }

    private fun hasCountryCode(rawNumber: String, defaultCountryCode: String): Boolean {
        val phoneUtil = PhoneNumberUtil.getInstance()
        return try {
            val phoneNumber = phoneUtil.parse(rawNumber, defaultCountryCode)
            phoneUtil.getCountryCodeForRegion(defaultCountryCode) != phoneNumber.countryCode
        } catch (e: NumberParseException) {
            false
        }
    }
}