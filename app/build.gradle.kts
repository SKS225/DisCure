plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.discure"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.discure"
        minSdk = 24
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
    buildFeatures{
        dataBinding=true
        viewBinding=true
        mlModelBinding=true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.viewpager2)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.room.ktx)
    //implementation(libs.compose.preview.renderer)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(platform(libs.firebase.bom))

    implementation(libs.material.v1120) // Replace with the latest version

    //implementation("com.google.firebase:firebase-auth-ktx:23.0.0")
    //implementation("com.google.android.gms:play-services-auth:21.2.0")
    //implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    implementation(libs.androidx.annotation)
    //implementation("com.google.firebase:firebase-storage-ktx:21.0.0")
    //implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation(libs.androidx.navigation.ui.ktx)
    implementation (libs.mpandroidchart)
    implementation ("com.github.Gruzer:simple-gauge-android:0.3.1")

    //implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    //implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation ("androidx.fragment:fragment-ktx:1.8.0")

    implementation("com.zjun:rule-view:0.0.1")
    implementation ("com.neobaran.open:ruler-view:0.0.5")
    //implementation ("com.scwang.smartrefresh:SmartRefreshLayout:1.1.0")
    //implementation("com.kevalpatel2106:ruler-picker:1.1")


    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler)

    // Optionally, add support for Room Kotlin extensions and RxJava
    //implementation (libs.androidx.room.ktx)



    val lifecycle_version = "2.5.1"
    val arch_version = "2.1.0"

    //implementation ("androidx.core:core-ktx:1.7.0")
    //implementation ("androidx.appcompat:appcompat:1.5.1")
    //implementation ("com.google.android.material:material:1.7.0")
    //androidTestImplementation ("androidx.test.ext:junit:1.1.4")
    //androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.0")

    // material theme
    //implementation ("com.google.android.material:material:1.7.0-rc01")

    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // Lifecycles only (without ViewModel or LiveData)
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    // Saved state module for ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")
    // alternately - if using Java8, use the following instead of lifecycle-compiler
    //implementation ("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")
    // optional - helpers for implementing LifecycleOwner in a Service
    //implementation ("androidx.lifecycle:lifecycle-service:$lifecycle_version")

    // optional - ProcessLifecycleOwner provides a lifecycle for the whole application process
    //implementation ("androidx.lifecycle:lifecycle-process:$lifecycle_version")

    // optional - ReactiveStreams support for LiveData
    implementation ("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycle_version")

    // optional - Test helpers for LiveData
    //testImplementation ("androidx.arch.core:core-testing:$arch_version")

    // optional - Test helpers for Lifecycle runtime
    //testImplementation ("androidx.lifecycle:lifecycle-runtime-testing:$lifecycle_version")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    //Gson
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    //coroutines
    //implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    //Shimmer
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    //swipe
    implementation ("com.github.xabaras:RecyclerViewSwipeDecorator:1.4")
    //circular ImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")

    //Charts
    implementation ("com.github.AnyChart:AnyChart-Android:1.1.2")
    //multidex
    implementation ("androidx.multidex:multidex:2.0.0")
    //Lottie*/
    implementation ("com.airbnb.android:lottie:5.2.0")

    //implementation("com.google.android.material:material:1.12.0")
}