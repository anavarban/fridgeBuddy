plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id ("kotlin-parcelize")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.mready.myapplication"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.mready.myapplication"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "API_HOST", "\"https://tasty.p.rapidapi.com/\"")
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
}

dependencies {


    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")

    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3-android:1.2.0-alpha02")

    implementation("com.airbnb.android:lottie-compose:6.0.1")

    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("io.coil-kt:coil-svg:2.2.2")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.6.0")

    implementation("androidx.activity:activity-compose:1.7.1")

    implementation("com.google.accompanist:accompanist-insets:0.30.1")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
    implementation("com.google.android.gms:play-services-auth:20.6.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // DI
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")

    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")

    implementation("net.mready.apiclient:apiclient:1.0.0-rc05")
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation("com.github.chuckerteam.chucker:library:4.0.0")

    // Firebase

    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation ("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    //Room
    implementation ("androidx.room:room-runtime:2.5.2")
    implementation( "androidx.room:room-ktx:2.5.2")
    implementation ("androidx.compose.runtime:runtime-livedata:1.5.0")
    annotationProcessor ("androidx.room:room-compiler:2.5.2")
    kapt ("androidx.room:room-compiler:2.5.2")

    //Accompanist & Notifications
    implementation ("com.google.accompanist:accompanist-permissions:0.29.0-alpha")
    implementation ("androidx.core:core-ktx:1.6.0")
    implementation ("androidx.work:work-runtime-ktx:2.7.0")

    //Splash screen
    implementation("androidx.core:core-splashscreen:1.0.0")

    //ExoPlayer
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")

    //worker
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    //google scanner
    implementation ("com.google.android.gms:play-services-code-scanner:16.1.0")

    //mlkit
    implementation("com.google.mlkit:text-recognition:16.0.0")

    //CameraX
    implementation ("androidx.camera:camera-core:1.0.0")
    implementation ("androidx.camera:camera-camera2:1.0.0")
    implementation ("androidx.camera:camera-lifecycle:1.0.0")
    implementation ("androidx.camera:camera-view:1.2.0")
    implementation ("androidx.camera:camera-extensions:1.2.0")
}
