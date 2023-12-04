import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}

android {
    namespace = AndroidVersion.PACKAGE_NAME.version
    compileSdk = CompileSDK.MAX_SDK.version

    defaultConfig {
        applicationId = AndroidVersion.PACKAGE_NAME.version
        minSdk = CompileSDK.MIN_SDK.version
        targetSdk = CompileSDK.MAX_SDK.version
        versionCode = CompileSDK.VERSION_CODE.version
        versionName = AndroidVersion.APP_VERSION.version

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
        kotlinCompilerExtensionVersion = AndroidVersion.KOTLIN_COMPILE.version
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
    implementation("androidx.core:core-ktx:${DependenciesVersion.KOTLIN_VERSION.version}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${DependenciesVersion.RUNTIME_KTX.version}")
    implementation("androidx.activity:activity-compose:${DependenciesVersion.ACTIVITY_COMPOSE.version}")
    implementation(platform("androidx.compose:compose-bom:${DependenciesVersion.COMPOSE.version}"))
    implementation("androidx.compose.ui:ui-util:${DependenciesVersion.COMPOSE_UI_UTILS.version}")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:${TestingDependenciesVersion.JUnit.version}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${TestingDependenciesVersion.ESPRESSO.version}")
    androidTestImplementation(platform("androidx.compose:compose-bom:${DependenciesVersion.COMPOSE.version}"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.google.dagger:hilt-android:${DependenciesVersion.HILT.version}")
    kapt("com.google.dagger:hilt-android-compiler:${DependenciesVersion.HILT.version}")
    implementation("androidx.hilt:hilt-navigation-compose:${DependenciesVersion.HILT_COMPOSE.version}")

    implementation("io.coil-kt:coil-compose:${DependenciesVersion.COIL.version}")

    implementation("androidx.datastore:datastore-preferences:${DependenciesVersion.V.version}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${DependenciesVersion.COROUTINES.version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${DependenciesVersion.COROUTINES.version}")


    implementation("androidx.room:room-ktx:${DependenciesVersion.ROOM.version}")
    ksp("androidx.room:room-compiler:${DependenciesVersion.ROOM.version}")

    implementation("com.squareup.retrofit2:retrofit:${DependenciesVersion.RETROFIT.version}")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:${DependenciesVersion.MOSHI.version}")
    implementation("com.squareup.moshi:moshi-kotlin:${DependenciesVersion.MOSHI.version}")
    implementation("com.squareup.retrofit2:converter-moshi:${DependenciesVersion.RETROFIT.version}")
    implementation("com.squareup.retrofit2:converter-gson:${DependenciesVersion.RETROFIT.version}")
    implementation("com.squareup.retrofit2:converter-scalars:${DependenciesVersion.RETROFIT.version}")
    implementation("com.squareup.retrofit2:converter-simplexml:${DependenciesVersion.RETROFIT.version}")

    implementation("androidx.browser:browser:${DependenciesVersion.BROWSER.version}")
    implementation("com.github.evgenyneu:js-evaluator-for-android:${DependenciesVersion.JS_EVALUATOR.version}")

    implementation("org.jsoup:jsoup:${DependenciesVersion.JSOUP.version}")
    implementation("androidx.webkit:webkit:${DependenciesVersion.WEBKIT.version}")

    implementation(platform("com.squareup.okhttp3:okhttp-bom:${DependenciesVersion.OK_HTTP.version}"))
    implementation("com.squareup.okhttp3:okhttp")

    implementation("androidx.work:work-runtime-ktx:${DependenciesVersion.WORK_MANAGER.version}")
    implementation("androidx.hilt:hilt-work:${DependenciesVersion.HILT_COMPOSE.version}")
    kapt("androidx.hilt:hilt-compiler:${DependenciesVersion.HILT_COMPOSE.version}")

    implementation(platform("com.google.firebase:firebase-bom:${DependenciesVersion.FIREBASE.version}"))
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")


    implementation("androidx.media3:media3-exoplayer:${DependenciesVersion.MEDIA3.version}")
    implementation("androidx.media3:media3-ui:${DependenciesVersion.MEDIA3.version}")
    implementation("androidx.media3:media3-session:${DependenciesVersion.MEDIA3.version}")
    implementation("androidx.media3:media3-exoplayer-hls:${DependenciesVersion.MEDIA3.version}")
    implementation("androidx.legacy:legacy-support-v4:${DependenciesVersion.V.version}")

    implementation("androidx.paging:paging-runtime-ktx:${DependenciesVersion.PAGING.version}")
    implementation("androidx.paging:paging-compose:3.3.0-alpha02")
}

enum class CompileSDK(val version: Int) {
    MAX_SDK(34), MIN_SDK(24), VERSION_CODE(23)
}

enum class AndroidVersion(val version: String) {
    PACKAGE_NAME("com.rizwansayyed.zene"), APP_VERSION("1.0.000352"), KOTLIN_COMPILE("1.5.3"),
    ANDROID_VERSION("8.1.1")
}

enum class DependenciesVersion(val version: String) {
    KOTLIN_VERSION("1.12.0"), RUNTIME_KTX("2.6.2"), ACTIVITY_COMPOSE("1.8.1"), COMPOSE("2023.09.00"),
    HILT("2.48.1"), HILT_COMPOSE("1.1.0"), V("1.0.0"), COIL("2.4.0"), COROUTINES("1.7.3"), ROOM("2.6.1"),
    RETROFIT("2.9.0"), MOSHI("1.14.0"), JSOUP("1.16.1"), OK_HTTP("4.10.0"), FIREBASE("32.3.1"),
    MEDIA3("1.2.0"), COMPOSE_UI_UTILS("1.5.4"), JS_EVALUATOR("v6.0.0"), BROWSER("1.7.0"), WEBKIT("1.9.0"),
    WORK_MANAGER("2.9.0"), PAGING("3.2.1")
}

enum class TestingDependenciesVersion(val version: String) {
    JUnit("1.1.5"), ESPRESSO("3.5.1")
}
