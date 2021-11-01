plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-allopen")
}

allOpen {
    // kotlin class are default final, all-open plugin allow class to be mock by mockito
    annotation ("com.learn.codingchallenge.network.testing.OpenClass")
}

android {
    compileSdkVersion(BuildConfig.compileSdkVersion)
    buildToolsVersion(BuildConfig.buildToolsVersion)

    defaultConfig {
        minSdkVersion(BuildConfig.minSdkVersion)
        targetSdkVersion(BuildConfig.targetSdkVersion)
        versionCode(BuildConfig.versionCode)
        versionName(BuildConfig.versionName)

        testInstrumentationRunner(BuildConfig.testRunner)
    }

    buildTypes {
        getByName("debug"){
            buildConfigField("String", "AUTH_TOKEN", "\"DfBVgBEsIuBkmypL5mddFsPOe0BYF2MH\"")
            buildConfigField("String", "BASE_URL", "\"https://api.giphy.com\"")
        }
        getByName("release") {
            buildConfigField("String", "AUTH_TOKEN", "\"DfBVgBEsIuBkmypL5mddFsPOe0BYF2MH\"")
            buildConfigField("String", "BASE_URL", "\"https://api.giphy.com\"")
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
}

dependencies {

    implementation(KotlinDependencies.kotlinStd)

    //hilt
    implementation(Libraries.hilt)
    kapt(Libraries.hiltAnnotationProcessor)

    //For instrumentation tests
    implementation(Libraries.hiltInstrumentation)
    kapt(Libraries.hiltInsAnnotationProcessor)

    //For local unit tests
    implementation(Libraries.hiltUnitTest)
    kapt(Libraries.hiltUnitTestAnnotationProcessor)

    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitMoshiConverter)
    implementation(Libraries.retrofitRxAdapter)

    implementation(Libraries.moshi)
    kapt(Libraries.moshiKotlinCodeGen)

    implementation(Libraries.loggingInterceptor) {
        this.exclude("org.json", "json")
    }

    //Stetho https://github.com/facebook/stetho
    implementation (Libraries.stetho)
    implementation (Libraries.stethoOkhttp)
    implementation (Libraries.stethoJSRhino)

    //testing
    testImplementation(TestingDependencies.junit)
    testImplementation(TestingDependencies.mockWebServer)
    testImplementation(TestingDependencies.assertj)
    testImplementation(TestingDependencies.mockito)
}