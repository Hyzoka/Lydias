plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.hilt.android)

}

android {
    namespace = "com.test.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)

    //moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    kapt(libs.moshi.kapt)

    //Pagine
    implementation(libs.pagine.runtime)
    implementation(libs.pagine.compose)

    //coroutine
    implementation(libs.coroutine)
    implementation(libs.coroutine.core)

    //room
    kapt(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
}