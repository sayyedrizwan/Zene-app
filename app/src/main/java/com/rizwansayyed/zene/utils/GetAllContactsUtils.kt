package com.rizwansayyed.zene.utils

import android.content.ContentResolver
import android.provider.ContactsContract
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context


data class ContactData(val name: String, val number: String)

class GetAllContactsUtils {

    fun getContactList(): ArrayList<ContactData> {
        val list = ArrayList<ContactData>()

        val cr: ContentResolver = context.contentResolver
        val cur = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )

        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id = cur.getStringOrNull(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cur.getStringOrNull(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                if ((cur.getIntOrNull(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        ?: 0) > 0
                ) {
                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id), null
                    )
                    while (pCur!!.moveToNext()) {
                        val phoneNo = pCur.getStringOrNull(
                            pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )

                        phoneNo?.let { p -> list.add(ContactData(name ?: "", phoneNo)) }
                    }
                    pCur.close()
                }
            }
        }
        cur?.close()

        return list
    }
}