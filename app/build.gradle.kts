import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.rizwansayyed.zene"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rizwansayyed.zene"
        minSdk = 24
        targetSdk = 34
        versionCode = 7
        versionName = "1.0.000333"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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

    buildTypes {
        debug {
//            applicationIdSuffix = ".debug"
//            versionNameSuffix = "-DEBUG"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
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
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    val ktxCore = "1.10.1"
    val composeBom = "2023.03.00"
    val firebaseBom = "32.2.2"
    val retrofitVersion = "2.9.0"
    val moshiVersion = "1.14.0"
    val hiltVersion = "2.47"
    val hiltCompilerVersion = "2.45"
    val productionVersion = "1.0.0"
    val runtimeCompose = "2.7.0-alpha01"
    val jsoupVersion = "1.16.1"
    val coilVersion = "2.4.0"
    val roomVersion = "2.5.2"
    val jsEvaluatorVersion = "v6.0.0"
    val media3Version = "1.1.1"
    val workManagerVersion = "2.8.1"
    val adMobVersion = "22.3.0"

    implementation("androidx.core:core-ktx:$ktxCore")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:$composeBom"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeBom"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")

//    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.11.0"))
//    implementation("com.squareup.okhttp3:okhttp")
//    implementation("com.squareup.okhttp3:logging-interceptor")

    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltCompilerVersion")
    implementation("androidx.hilt:hilt-navigation-compose:$productionVersion")

    implementation("androidx.datastore:datastore-preferences:$productionVersion")
    implementation("org.jsoup:jsoup:$jsoupVersion")
    implementation("com.github.evgenyneu:js-evaluator-for-android:$jsEvaluatorVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$runtimeCompose")

    implementation("io.coil-kt:coil-compose:$coilVersion")
    implementation("io.coil-kt:coil:$coilVersion")

    implementation("org.jsoup:jsoup:1.16.1")

    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    implementation("com.google.android.gms:play-services-ads:$adMobVersion")

    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")
    implementation("androidx.media3:media3-session:$media3Version")


    implementation(platform("com.google.firebase:firebase-bom:$firebaseBom"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-config-ktx")

    implementation("androidx.work:work-runtime-ktx:$workManagerVersion")
    implementation("androidx.hilt:hilt-work:$productionVersion")
    kapt("androidx.hilt:hilt-compiler:$productionVersion")
}