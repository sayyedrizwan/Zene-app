buildscript {
    val agp_version by extra("8.1.2")
    val agp_version1 by extra("8.1.3")
    val agp_version2 by extra("8.1.4")
}
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
}