plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
    id ("kotlin-parcelize")
}

kotlin {
    jvmToolchain(17)
}

android {
    compileSdk = 34
    namespace = "at.str.lottery.barcode"

    defaultConfig {
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "1.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    namespace ="at.str.lottery.barcode"
}

val kotlin_version = "1.9.20"
val compose_version = "1.5.4"
val nav_compose_version = "2.7.5"

dependencies {
    implementation ("androidx.navigation:navigation-compose:$nav_compose_version")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.10.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.compose.ui:ui:$compose_version")
    implementation ("androidx.compose.material:material:$compose_version")
    implementation( "androidx.compose.ui:ui-tooling:$compose_version")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    // CameraX core library using camera2 implementation
    implementation ("androidx.camera:camera-camera2:1.3.0")
    // CameraX Lifecycle Library
    implementation ("androidx.camera:camera-lifecycle:1.3.0")
    // CameraX View class
    implementation ("androidx.camera:camera-view:1.3.0")
    implementation ("androidx.compose.ui:ui-viewbinding:1.5.4")

    implementation ("com.google.mlkit:barcode-scanning:17.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation ("com.google.code.gson:gson:2.10.1")

    // FIXME: **Heads up!** Updates to this dependency don"t get highlighted by Android Studio
    implementation ("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

    // FIXME: **Heads up!** Updates to this dependency don"t get highlighted by Android Studio
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")

    // Integration with ViewModels
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Material design icons
    implementation ("androidx.compose.material:material-icons-core:1.5.4")
    implementation ("androidx.compose.material:material-icons-extended:1.5.4")

    // okhttp
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
}