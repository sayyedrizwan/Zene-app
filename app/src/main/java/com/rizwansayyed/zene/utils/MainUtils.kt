package com.rizwansayyed.zene.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.service.ForegroundService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import java.util.concurrent.TimeUnit


object MainUtils {

    val countryCodeMap = hashMapOf(
        "BD" to "880",
        "BE" to "32",
        "BF" to "226",
        "BG" to "359",
        "BA" to "387",
        "BB" to "1246",
        "WF" to "681",
        "BL" to "590",
        "BM" to "1441",
        "BN" to "673",
        "BO" to "591",
        "BH" to "973",
        "BI" to "257",
        "BJ" to "229",
        "BT" to "975",
        "JM" to "1876",
        "BV" to "47",
        "BW" to "267",
        "WS" to "685",
        "BQ" to "599",
        "BR" to "55",
        "BS" to "1242",
        "JE" to "441534",
        "BY" to "375",
        "BZ" to "501",
        "RU" to "7",
        "RW" to "250",
        "RS" to "381",
        "TL" to "670",
        "RE" to "262",
        "TM" to "993",
        "TJ" to "992",
        "RO" to "40",
        "TK" to "690",
        "GW" to "245",
        "GU" to "1671",
        "GT" to "502",
        "GS" to "500",
        "GR" to "30",
        "GQ" to "240",
        "GP" to "590",
        "JP" to "81",
        "GY" to "592",
        "GG" to "441481",
        "GF" to "594",
        "GE" to "995",
        "GD" to "1473",
        "GB" to "44",
        "GA" to "241",
        "SV" to "503",
        "GN" to "224",
        "GM" to "220",
        "GL" to "299",
        "GI" to "350",
        "GH" to "233",
        "OM" to "968",
        "TN" to "216",
        "JO" to "962",
        "HR" to "385",
        "HT" to "509",
        "HU" to "36",
        "HK" to "852",
        "HN" to "504",
        "HM" to "334",
        "VE" to "58",
        "PR" to "1787",
        "PS" to "970",
        "PW" to "680",
        "PT" to "351",
        "SJ" to "47",
        "PY" to "595",
        "IQ" to "964",
        "PA" to "507",
        "PF" to "689",
        "PG" to "675",
        "PE" to "51",
        "PK" to "92",
        "PH" to "63",
        "PN" to "870",
        "PL" to "48",
        "PM" to "508",
        "ZM" to "260",
        "EH" to "212",
        "EE" to "372",
        "EG" to "20",
        "ZA" to "27",
        "EC" to "593",
        "IT" to "39",
        "VN" to "84",
        "SB" to "677",
        "ET" to "251",
        "SO" to "252",
        "ZW" to "263",
        "SA" to "966",
        "ES" to "34",
        "ER" to "291",
        "ME" to "382",
        "MD" to "373",
        "MG" to "261",
        "MF" to "590",
        "MA" to "212",
        "MC" to "377",
        "UZ" to "998",
        "MM" to "95",
        "ML" to "223",
        "MO" to "853",
        "MN" to "976",
        "MH" to "692",
        "MK" to "389",
        "MU" to "230",
        "MT" to "356",
        "MW" to "265",
        "MV" to "960",
        "MQ" to "596",
        "MP" to "1670",
        "MS" to "1664",
        "MR" to "222",
        "IM" to "441624",
        "UG" to "256",
        "TZ" to "255",
        "MY" to "60",
        "MX" to "52",
        "IL" to "972",
        "FR" to "33",
        "IO" to "246",
        "SH" to "290",
        "FI" to "358",
        "FJ" to "679",
        "FK" to "500",
        "FM" to "691",
        "FO" to "298",
        "NI" to "505",
        "NL" to "31",
        "NO" to "47",
        "NA" to "264",
        "VU" to "678",
        "NC" to "687",
        "NE" to "227",
        "NF" to "672",
        "NG" to "234",
        "NZ" to "64",
        "NP" to "977",
        "NR" to "674",
        "NU" to "683",
        "CK" to "682",
        "XK" to "383",
        "CI" to "225",
        "CH" to "41",
        "CO" to "57",
        "CN" to "86",
        "CM" to "237",
        "CL" to "56",
        "CC" to "61",
        "CA" to "1",
        "CG" to "242",
        "CF" to "236",
        "CD" to "243",
        "CZ" to "420",
        "CY" to "357",
        "CX" to "61",
        "CR" to "506",
        "CW" to "599",
        "CV" to "238",
        "CU" to "53",
        "SZ" to "268",
        "SY" to "963",
        "SX" to "599",
        "KG" to "996",
        "KE" to "254",
        "SS" to "211",
        "SR" to "597",
        "KI" to "686",
        "KH" to "855",
        "KN" to "1869",
        "KM" to "269",
        "ST" to "239",
        "SK" to "421",
        "KR" to "82",
        "SI" to "386",
        "KP" to "850",
        "KW" to "965",
        "SN" to "221",
        "SM" to "378",
        "SL" to "232",
        "SC" to "248",
        "KZ" to "7",
        "KY" to "1345",
        "SG" to "65",
        "SE" to "46",
        "SD" to "249",
        "DO" to "1809",
        "DM" to "1767",
        "DJ" to "253",
        "DK" to "45",
        "VG" to "1284",
        "DE" to "49",
        "YE" to "967",
        "DZ" to "213",
        "US" to "1",
        "UY" to "598",
        "YT" to "262",
        "UM" to "1",
        "LB" to "961",
        "LC" to "1758",
        "LA" to "856",
        "TV" to "688",
        "TW" to "886",
        "TT" to "1868",
        "TR" to "90",
        "LK" to "94",
        "LI" to "423",
        "LV" to "371",
        "TO" to "676",
        "LT" to "370",
        "LU" to "352",
        "LR" to "231",
        "LS" to "266",
        "TH" to "66",
        "TF" to "262",
        "TG" to "228",
        "TD" to "235",
        "TC" to "1649",
        "LY" to "218",
        "VA" to "379",
        "VC" to "1784",
        "AE" to "971",
        "AD" to "376",
        "AG" to "1268",
        "AF" to "93",
        "AI" to "1264",
        "VI" to "1340",
        "IS" to "354",
        "IR" to "98",
        "AM" to "374",
        "AL" to "355",
        "AO" to "244",
        "AQ" to "672",
        "AS" to "1684",
        "AR" to "54",
        "AU" to "61",
        "AT" to "43",
        "AW" to "297",
        "IN" to "91",
        "AX" to "35818",
        "AZ" to "994",
        "IE" to "353",
        "ID" to "62",
        "UA" to "380",
        "QA" to "974",
        "MZ" to "258"
    )


    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()!!

    fun Any.toast() = CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, this@toast.toString(), Toast.LENGTH_LONG).show()
        if (isActive) cancel()
    }

    fun getDeviceInfo(): String {
        val manufacturer = Build.MANUFACTURER.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
        val model = Build.MODEL
        val osVersion = Build.VERSION.RELEASE
        return "$manufacturer $model $osVersion"
    }

    fun isLocationPermissionGranted(): Boolean {
        val location = checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        return location == PackageManager.PERMISSION_GRANTED
    }

    fun startAppService(context: Context) {
        Intent(context, ForegroundService::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(this)
            else context.startService(this)
        }
    }

    fun loadIcon(context: Context, url: String?, placeHolder: Int): BitmapDescriptor? {
        try {
            var bitmap: Bitmap? = null
            Glide.with(context).asBitmap().load(url).error(placeHolder)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap, transition: Transition<in Bitmap>?
                    ) {
                        bitmap = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
            return BitmapDescriptorFactory.fromBitmap(bitmap!!)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun getBitmapFromURL(strURL: String?): Bitmap? = runBlocking(Dispatchers.IO) {
        try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val myBitmap = BitmapFactory.decodeStream(input)
            return@runBlocking myBitmap
        } catch (e: IOException) {
            val icon = BitmapFactory.decodeResource(context.resources, R.drawable.zusers_img)
            return@runBlocking icon
        }
    }

    fun timeDifferenceInMinutes(ts: Long): Long {
        val currentTime = System.currentTimeMillis()
        val timeDifferenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - ts)
        return timeDifferenceInMinutes
    }

    fun openShareConnectShareSMS(url: String, number: String) {
        val uri = Uri.parse(java.lang.String.format("smsto:%s", number))
        val smsIntent = Intent(Intent.ACTION_SENDTO, uri)
        smsIntent.putExtra("sms_body", url)
        smsIntent.setPackage("com.google.android.apps.messaging")
        context.startActivity(smsIntent)
    }
}