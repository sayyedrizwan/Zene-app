plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
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
    implementation("androidx.hilt:hilt-navigation-compose:${DependenciesVersion.V.version}")

    implementation("io.coil-kt:coil-compose:${DependenciesVersion.COIL.version}")

    implementation("androidx.datastore:datastore-preferences:${DependenciesVersion.V.version}")
}

enum class CompileSDK(val version: Int) {
    MAX_SDK(34), MIN_SDK(24), VERSION_CODE(23)
}

enum class AndroidVersion(val version: String) {
    PACKAGE_NAME("com.rizwansayyed.zene"), APP_VERSION("1.0.000352"), KOTLIN_COMPILE("1.5.3"),
    ANDROID_VERSION("8.1.1")
}

enum class DependenciesVersion(val version: String) {
    KOTLIN_VERSION("1.12.0"), RUNTIME_KTX("2.6.2"), ACTIVITY_COMPOSE("1.7.2"), COMPOSE("2023.09.00"),
    HILT("2.48"), V("1.0.0"), COIL("2.4.0")
}

enum class TestingDependenciesVersion(val version: String) {
    JUnit("1.1.5"), ESPRESSO("3.5.1")
}
