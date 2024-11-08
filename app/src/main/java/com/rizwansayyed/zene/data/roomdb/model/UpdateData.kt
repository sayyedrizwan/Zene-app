package com.rizwansayyed.zene.data.roomdb.model

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.Utils.RoomDB.UPDATE_ROOM_DB
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Entity(tableName = UPDATE_ROOM_DB)
data class UpdateData(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var macAddress: String = "",
    var latitude: Double? = null,
    var longitude: Double? = null,
    var address: String? = null,
    var ts: Long? = null,
    var type: Int? = null,
) {
    fun types(): String {
        return if (type == UPDATES_TYPE_CONNECT) context.resources.getString(R.string.got_connected)
        else context.resources.getString(R.string.got_disconnected)
    }

    fun typesColor(): Color {
        return if (type == UPDATES_TYPE_CONNECT) Color.Green
        else Color.Red
    }

    fun time(): String {
        val inputTime = ts!!
        val now = System.currentTimeMillis()
        val diff = now - inputTime

        return when {
            diff < TimeUnit.DAYS.toMillis(1) -> {
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(inputTime))
            }

            diff < TimeUnit.DAYS.toMillis(7) -> {
                SimpleDateFormat("EEE HH:mm", Locale.getDefault()).format(Date(inputTime))
            }

            diff < TimeUnit.DAYS.toMillis(30) -> {
                SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(inputTime))
            }

            else -> SimpleDateFormat("MMM dd, yyyy (HH:mm)", Locale.getDefault())
                .format(Date(inputTime))

        }
    }
}

const val UPDATES_TYPE_DISCONNECT = 0
const val UPDATES_TYPE_CONNECT = 1