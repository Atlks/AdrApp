plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
   }

android {
    namespace = "com.aaapkg"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aaapkg"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            //unresloved reference : debuggable
           // debuggable   false  // 确保 release 构建没有开启调试
             isMinifyEnabled  = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
           // debuggable true  // debug 版本应该是可以调试的
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
        viewBinding = true
    }
}

//build.gradle.kts
dependencies {


    implementation ("com.google.code.gson:gson:2.8.8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.bitcoinj:bitcoinj-core:0.15.10")
    implementation("org.bouncycastle:bcprov-jdk15to18:1.70")
     implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}