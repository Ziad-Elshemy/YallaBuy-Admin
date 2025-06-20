import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "eg.gov.iti.yallabuyadmin"
    compileSdk = 35

    defaultConfig {
        applicationId = "eg.gov.iti.yallabuyadmin"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SHOPIFY_USERNAME", localProperties.getProperty("SHOPIFY_USERNAME"))
        buildConfigField("String", "SHOPIFY_ACCESS_TOKEN", localProperties.getProperty("SHOPIFY_ACCESS_TOKEN"))

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
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

    val nav_version = "2.8.8"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    //Serialization for NavArgs
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    //Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    // Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    implementation ("androidx.compose.material3:material3:1.2.0")

    //Glide
    implementation ("com.github.bumptech.glide:compose:1.0.0-beta01")
    //Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.github.skydoves:landscapist-glide:2.2.9")

    //Async image
    implementation("io.coil-kt:coil-compose:2.4.0")

    //Koin
    val koin_android_version = "4.0.2"
    implementation("io.insert-koin:koin-android:$koin_android_version")
    implementation("io.insert-koin:koin-androidx-compose:$koin_android_version")
    implementation("io.insert-koin:koin-androidx-compose-navigation:$koin_android_version")


    implementation("com.google.accompanist:accompanist-flowlayout:0.34.0")

    implementation ("com.google.maps.android:maps-compose:4.3.2")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")



    // TESTING

    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    // AndroidX JUnit
    testImplementation ("io.mockk:mockk:1.13.8")
    // AndroidX and Robolectric
    testImplementation ("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation ("androidx.test:core-ktx:1.5.0")
    testImplementation ("org.robolectric:robolectric:4.11.1")
    //kotlinx-coroutines
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")


}