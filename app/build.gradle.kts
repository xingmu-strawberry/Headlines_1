import java.util.Properties // 修正: 导入 java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

// 读取local.properties中的API密钥
val localProperties = Properties() // 使用导入的 Properties
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}
val juheApiKey = "\"${localProperties.getProperty("JUHE_API_KEY", "")}\""

android {
    namespace = "com.example.headlines"
    compileSdk = 34 // 假设您使用的是最新的稳定版本，这里根据您项目的实际情况来定
    // compileSdk { version = release(36) } 这一语法通常只在特定的 libs.versions.toml 配合下使用，
    // 在 build.gradle.kts 中直接指定版本号更常见，我暂时将其替换为 34 或您需要的版本。

    defaultConfig {
        applicationId = "com.example.headlines"
        minSdk = 24
        targetSdk = 34 // 与 compileSdk 保持一致
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 将API密钥注入到BuildConfig中
        buildConfigField("String", "JUHE_API_KEY", juheApiKey)
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

    // 添加
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.identity.jvm)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // 核心 UI 组件
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // 生命周期组件
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // 下拉刷新
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // 协程
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // 图片加载 (Glide)
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // 网络请求 (Retrofit & OkHttp)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // 安全存储API密钥
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Fragment KTX
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    implementation("de.hdodenhof:circleimageview:3.1.0")
}