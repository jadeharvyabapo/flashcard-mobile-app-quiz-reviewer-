buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2") // ✅ Correct Kotlin DSL syntax
    }
}

plugins {
    id("com.android.application") version "8.8.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
