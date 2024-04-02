import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    kotlin("kapt")
}

android {
    namespace = libs.versions.appPackageName.get()
    compileSdk = libs.versions.targetSDK.get().toInt()

    defaultConfig {
        applicationId = libs.versions.appPackageName.get()
        minSdk = libs.versions.minSDK.get().toInt()
        targetSdk = libs.versions.targetSDK.get().toInt()
        versionCode = libs.versions.currentVersionCode.get().toInt()
        versionName = libs.versions.currentVersionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    packagingOptions {
        jniLibs {
            useLegacyPackaging = true
        }
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(libs.kotlin.core)
    implementation(libs.lifecycle.runtime)
    implementation(libs.activity.compose)
    implementation(libs.lifecycle.process)
    implementation(libs.play.services.ads.lite)



    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.expresso)
    androidTestImplementation(platform(libs.android.compose))
    androidTestImplementation(libs.ui.test)
    androidTestImplementation(libs.ui.tooling)
    androidTestImplementation(libs.ui.manifest)




    implementation(platform(libs.android.compose))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.material3)




    implementation(libs.hilt)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)
    implementation(libs.work.manager)
    implementation(libs.hilt.work.manager)




    implementation(libs.coil)
    implementation(libs.coil.gif)
    implementation(libs.datastore)




    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)



    implementation(libs.room)
    ksp(libs.room.compiler)



    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.retrofit.gson)
    implementation(libs.retrofit.scalars)
    implementation(libs.retrofit.simplexml)
    ksp(libs.moshi.codegen)
    implementation(libs.moshi)



    implementation(libs.browser)
    implementation(libs.js.evaluator)


    implementation(libs.web)
    implementation(libs.jsoup)
    implementation(libs.ffmpeg)



    implementation(libs.amplituda)
    implementation(libs.amplituda.compose)



    implementation(platform(libs.okttp.bom))
    implementation(libs.okttp)



    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)


    implementation(libs.firebase.messaging)
    implementation(libs.firebase.messaging.directboot)


    implementation(libs.paging.runtime)


    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.media3.session)
    implementation(libs.media3.hls)
    implementation(libs.media3.legacy)



    implementation(libs.glide)
    implementation(libs.glide.transformations)


    implementation(libs.googleid)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services)
    implementation(libs.google.signin.auth)
}
