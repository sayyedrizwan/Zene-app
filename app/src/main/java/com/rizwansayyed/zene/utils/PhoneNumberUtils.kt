package com.rizwansayyed.zene.utils

import android.provider.ContactsContract
import android.util.Log
import com.rizwansayyed.zene.data.api.model.CountryCodeModel
import com.rizwansayyed.zene.data.db.DataStoreManager
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.Utils.getAllPhoneCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


object PhoneNumberUtils {

    fun getContactsLists() = CoroutineScope(Dispatchers.IO).launch {
        val countryCodes = getAllPhoneCode()
        val address = DataStoreManager.ipDB.firstOrNull()?.countryCode ?: ""
        val phoneNumberCode = countryCodes.first { it.iso.lowercase() == address.lowercase() }.code

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
                        .replace(" ", "").replace("-", "")
                val hasCountryCode = hasCountryCode(number, countryCodes)
                val finalNumber = if (hasCountryCode) number else "+${phoneNumberCode}${number}"
                Log.d(
                    "TAG",
                    "getContactsLists: runnedd on $name == $finalNumber"
                )
            }
        }
    }

    private fun hasCountryCode(number: String, allPhoneCode: Array<CountryCodeModel>): Boolean {
        return number.contains("+") && allPhoneCode.any { number.contains("+${it.code}") }
    }
}