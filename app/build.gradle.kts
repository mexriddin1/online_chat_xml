plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.example.chatapponline"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chatapponline"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //binding kirich
    implementation(libs.kirich1409.viewbindingpropertydelegate.full)

    //viewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)
    // google auth
    implementation(libs.com.google.firebase.firebase.auth)
    implementation(libs.gms.play.services.auth)

    // glide
    implementation (libs.glide)

    //lifecycle
    implementation (libs.androidx.lifecycle.process) // Use the latest version


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
