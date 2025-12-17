import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    // ---------- ANDROID ----------
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // ---------- iOS ----------
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    // ---------- DESKTOP ----------
    jvm("desktop")

    // ---------- JS ----------
    js {
        browser()
        binaries.executable()
    }

    // ---------- SOURCE SETS ----------
    sourceSets {

        // COMMON MAIN
        val commonMain by getting {
            dependencies {
                // Koin for Dependency Injection
                implementation(libs.koin.core)
                implementation(libs.koin.compose)

                // Ktor HTTP Client og JSON Serialization
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.contentNegotiation)
                implementation(libs.ktor.serialization.kotlinxJson)

                // Multiplatform Coroutines
                implementation(libs.kotlinx.coroutines.core)

                // GitLive Firebase
                implementation(libs.firebase.common)
                implementation(libs.firebase.auth)

                // Compose Frameworks
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                // Navigation - KUN Jetpack Compose Multiplatform version
                implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")

                // Lifecycle - KUN Jetpack Compose Multiplatform version
                implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
                implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:2.8.3")

                implementation("dev.gitlive:firebase-auth:2.4.0")
                implementation("dev.gitlive:firebase-common:2.4.0")
                implementation("dev.gitlive:firebase-firestore:2.4.0")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        // ANDROID MAIN
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)

                // Ktor
                implementation(libs.ktor.client.android)

                // Firebase
                implementation("com.google.firebase:firebase-auth:24.0.1")
                implementation("com.google.firebase:firebase-common:22.0.1")

                // AndroidX Core for Notifications
                implementation("androidx.core:core-ktx:1.13.1")
            }
        }

        // DESKTOP MAIN
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesSwing)
                implementation(libs.ktor.client.cio)
            }
        }

        // JS MAIN
        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }

        // ---------- iOS MAIN ----------
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)

            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.example.project.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.project"
            packageVersion = "1.0.0"
        }
    }
}