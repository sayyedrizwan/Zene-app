import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.firebase.performance)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = libs.versions.packagename.get()
    compileSdk = libs.versions.compilesdk.get().toInt()

    defaultConfig {
        multiDexEnabled = true
        applicationId = libs.versions.packagename.get()
        minSdk = libs.versions.minsdk.get().toInt()
        targetSdk = libs.versions.compilesdk.get().toInt()
        versionCode = libs.versions.appversion.get().toInt()
        versionName = libs.versions.versionname.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        val keystoreFile = project.rootProject.file("key.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        val socketBaseURL = properties.getProperty("SOCKET_ZENE_BASE_URL") ?: ""
        buildConfigField("String", "SOCKET_ZENE_BASE_URL", socketBaseURL)

        val zeneTopAlertDocs = properties.getProperty("ZENE_TOP_ALERT_DOCS") ?: ""
        buildConfigField("String", "ZENE_TOP_ALERT_DOCS", zeneTopAlertDocs)

        val apiZeneBaseURL = properties.getProperty("API_ZENE_MUSIC_BASE_URL") ?: ""
        buildConfigField("String", "API_ZENE_MUSIC_BASE_URL", apiZeneBaseURL)

        val appEncodeKey = properties.getProperty("APP_ENCODE_KEY") ?: ""
        buildConfigField("String", "APP_ENCODE_KEY", appEncodeKey)

        val clarityProjectId = properties.getProperty("CLARITY_PROJECT_ID") ?: ""
        buildConfigField("String", "CLARITY_PROJECT_ID", clarityProjectId)

        val deviceEncKey = properties.getProperty("DEVICE_ENC_KEY") ?: ""
        buildConfigField("String", "DEVICE_ENC_KEY", deviceEncKey)

        val googleServerKey = properties.getProperty("GOOGLE_SERVER_KEY") ?: ""
        buildConfigField("String", "GOOGLE_SERVER_KEY", googleServerKey)

        val facebookApplicationID = properties.getProperty("FACEBOOK_APPLICATION_ID") ?: ""
        resValue("string", "FACEBOOK_APPLICATION_ID", facebookApplicationID)

        val facebookClientToken = properties.getProperty("FACEBOOK_CLIENT_TOKEN") ?: ""
        resValue("string", "FACEBOOK_CLIENT_TOKEN", facebookClientToken)

        val googleMapKey = properties.getProperty("GOOGLE_MAP_KEY") ?: ""
        resValue("string", "GOOGLE_MAP_KEY", googleMapKey)

        val trueCallerKey = properties.getProperty("TRUE_CALLER_KEY") ?: ""
        resValue("string", "TRUE_CALLER_KEY", trueCallerKey)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    signingConfigs {
        create("release") {
            val properties = Properties().apply {
                load(File("signing.properties").reader())
            }
            storeFile = File(properties.getProperty("storeFilePath"))
            storePassword = properties.getProperty("storePassword")
            keyPassword = properties.getProperty("keyPassword")
            keyAlias = properties.getProperty("keyAlias")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_20
        targetCompatibility = JavaVersion.VERSION_20
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_20)
            javaParameters.set(true)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // --- Core & Lifecycle ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.process.phoenix)


// --- Dependency Injection : Hilt ---
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)


// --- UI : Jetpack Compose ---
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)

    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)


// --- Navigation & Web ---
    implementation(libs.navigation.compose)
    implementation(libs.browser)
    implementation(libs.androidx.webkit)


// --- Location & Maps ---
    implementation(libs.play.services.location)
    implementation(libs.maps.compose)
    implementation(libs.maps.compose.utils)


// --- Authentication / Identity / Login ---
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.services)
    implementation(libs.androidx.play.services)
    implementation(libs.androidx.credentials.googleid)

    implementation(libs.facebook.login)
    implementation(libs.truecaller)


// --- Firebase / Analytics / Crash / Push ---
    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.auth)
    implementation(libs.firebase.perf)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)


// --- Play Core : Update & Review ---
    implementation(libs.app.update)
    implementation(libs.app.review)


// --- Networking / API / Serialization ---
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)

    implementation(libs.gson)
    implementation(libs.jsoup)


// --- Database / Local Storage ---
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.ktx)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.storage)
    implementation(libs.opencsv)


// --- Images / Graphics ---
    implementation(libs.glide)
    implementation(libs.glide.compose)

    implementation(libs.image.cropper)
    implementation(libs.exifinterface)
    implementation(libs.palette.ktx)


// --- Media Playback (Media3) ---
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.compose)
    implementation(libs.media3.exoplayer.dash)
    implementation(libs.media3.exoplayer.hls)
    implementation(libs.media3.ui)
    implementation(libs.media3.transformer)
    implementation(libs.media3.session)


// --- Camera ---
    implementation(libs.camera.lifecycle)
    implementation(libs.camera2)
    implementation(libs.camera.view)
    implementation(libs.camera.core)


// --- Emoji / Rich Input ---
    implementation(libs.emoji)
    implementation(libs.emoji.picker)
    implementation(libs.emoji2.bundled)


// --- Security / Encryption ---
    implementation(libs.security.crypto)


// --- Billing / Monetization / Ads ---
    implementation(libs.billing)
    implementation(libs.play.services.ads)


// --- Widgets / UI Extras ---
    implementation(libs.glance.appwidget)
    implementation(libs.reorderable)
    implementation(libs.konfetti.compose)
    implementation(libs.colorpicker.compose)


// --- Realtime / Sockets ---
    implementation(libs.socket.io.client)


// --- Utilities / Misc ---
    implementation(libs.custom.qr.generator)
    implementation(libs.hashids)
    implementation(libs.clarity)


// --- Testing : Unit ---
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.core)
    testImplementation(libs.truth)


// --- Testing : Android / Instrumentation ---
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)


// --- Debug / Tooling ---
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")
}