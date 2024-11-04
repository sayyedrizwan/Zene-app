import java.io.FileInputStream
import java.util.Properties

val apikeyPropertiesFile = rootProject.file("gradle.properties")
val apikeyProperties = Properties().apply { load(FileInputStream(apikeyPropertiesFile)) }


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.services.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    id("kotlin-kapt")
}

android {
    namespace = libs.versions.packagename.get()
    compileSdk = libs.versions.compilesdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.packagename.get()
        minSdk = libs.versions.minsdk.get().toInt()
        targetSdk = libs.versions.compilesdk.get().toInt()
        versionCode = libs.versions.appversion.get().toInt()
        versionName = libs.versions.versionname.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            "String", "GOOGLE_SERVER_ID", apikeyProperties.getProperty("GOOGLE_SERVER_ID")
        )
        buildConfigField(
            "String", "AUTH_HEADER", apikeyProperties.getProperty("AUTH_HEADER")
        )
        buildConfigField(
            "String", "DOMAIN_BASE_URL", apikeyProperties.getProperty("DOMAIN_BASE_URL")
        )
        buildConfigField(
            "String", "IP_BASE_URL", apikeyProperties.getProperty("IP_BASE_URL")
        )
        buildConfigField(
            "String", "IMG_BB_API", apikeyProperties.getProperty("IMG_BB_API")
        )
        buildConfigField(
            "String", "APP_ENCODE_KEY", apikeyProperties.getProperty("APP_ENCODE_KEY")
        )
        buildConfigField(
            "String", "SPOTIFY_CLIENT_ID", apikeyProperties.getProperty("SPOTIFY_CLIENT_ID")
        )

        resValue("string", "FB_APP_ID", apikeyProperties.getProperty("FB_APP_ID"))
        resValue("string", "FB_SECRET_KEY", apikeyProperties.getProperty("FB_SECRET_KEY"))
        resValue("string", "FB_LOGIN_PROTOCOL_SCHEME", apikeyProperties.getProperty("FB_LOGIN_PROTOCOL_SCHEME"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlincompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.moshi)
    implementation(libs.browser)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.nav.compose)
    implementation(libs.webkit)
    implementation(libs.spotify)


    implementation(libs.facebook.login)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.inappmessaging.ktx)
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.perf.ktx)
    implementation(libs.firebase.auth)

    implementation(libs.googleid)

    implementation(libs.auth)
    implementation(libs.ads)

    implementation(libs.billing)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.lifecycle.compose)

    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)

    implementation(libs.jsoup)


    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
}
