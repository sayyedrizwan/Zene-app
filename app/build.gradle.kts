import java.io.FileInputStream
import java.util.Properties

val apikeyPropertiesFile = rootProject.file("gradle.properties")
val apikeyProperties = Properties().apply { load(FileInputStream(apikeyPropertiesFile)) }


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.services.android)
    alias(libs.plugins.hilt.android)
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
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
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
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.moshi)
    implementation(libs.browser)
    implementation(libs.coil.compose)
    implementation(libs.nav.compose)
    implementation(libs.webkit)


    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.googleid)
    implementation(libs.ads)
    implementation(libs.billing.client)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    implementation(libs.lifecycle.compose)

    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)

    implementation("androidx.media:media:1.1.0")
}
