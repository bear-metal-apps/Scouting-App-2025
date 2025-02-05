import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.psi.psiUtil.canPlaceAfterSimpleNameEntry

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    id("kotlin-parcelize")

//    kotlin("jvm") version "1.9.0" //Trying this right now
}

repositories {
    google()
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/betterbearmetalcode/koala")
        credentials {
            username = System.getenv("GITHUB_USERNAME")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "21"
            }
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.material3)
            implementation(libs.coil.compose)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.test)
            // https://mvnrepository.com/artifact/com.github.librepdf/openpdf
            implementation(libs.openpdf)
        }

        commonMain.dependencies {
            implementation(libs.webcam.capture)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(libs.androidx.compiler)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.bumble.appyx.navigation)
            implementation(libs.qrcode.kotlin)
            implementation(libs.okhttp)
            implementation(libs.json)
            implementation(libs.gson)
            implementation(libs.bumble.appyx.navigation)
            implementation(libs.koala)
            api(libs.backstack)
        }
    }
}

android {
    namespace = "org.tahomarobotics.scouting"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "org.tahomarobotics.scouting"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/NOTICE.md"
            excludes += "/META-INF/INDEX.LIST"
            excludes += "/META-INF/native-image/native-image.properties"
            excludes += "/META-INF/native-image/reflect-config.json"
            pickFirst("META-INF/NOTICE.md")
        }

    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
dependencies {
    implementation(libs.androidx.core)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            //appResourcesRootDir = (rootDir.toPath() / "desktopMain").toFile()
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.tahomarobotics.scouting"
            packageVersion = "1.0.0"
        }
    }
}

configurations.all {
    resolutionStrategy {
        force("androidx.core:core:1.13.1")
    }
}