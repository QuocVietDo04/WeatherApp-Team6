// Import các lớp cần thiết từ Java
import com.android.build.api.dsl.AaptOptions
import java.util.Properties
import java.io.InputStream // Quan trọng: Import InputStream để khai báo kiểu

// Tạo một đối tượng Properties rỗng
val localProperties = Properties()

// Lấy đối tượng File trỏ đến local.properties ở thư mục gốc dự án
val localPropertiesFile = rootProject.file("local.properties")

// (Tùy chọn) Thêm log để biết nó đang đọc file nào
println("INFO: Checking for local properties file at: ${localPropertiesFile.absolutePath}")

// Kiểm tra xem tệp có tồn tại và có phải là file không
if (localPropertiesFile.isFile) {
    println("INFO: Reading properties from ${localPropertiesFile.name}")
    // Mở luồng đọc InputStream từ tệp một cách an toàn
    localPropertiesFile.inputStream().use { inputStream: InputStream -> // Đổi tên biến cho rõ ràng hơn
        // Đọc các thuộc tính từ luồng và nạp vào đối tượng properties
        localProperties.load(inputStream)
        println("INFO: Properties loaded successfully.")
    }
} else {
    // Cảnh báo nếu không tìm thấy file
    println("WARN: local.properties file not found. API keys or other properties might be missing.")
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.nhom6.weatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nhom6.weatherapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Xử lý LOCATION_BASE_URL
        val locationBaseUrl = localProperties.getProperty("LOCATION_BASE_URL")
        if (locationBaseUrl.isNullOrEmpty()) {
            println("WARN: LOCATION_BASE_URL not found in local.properties. Build might fail or use default values.")
            buildConfigField("String", "LOCATION_BASE_URL", "\"https://default-api.com/v1/\"") // Thêm giá trị mặc định cho BASE_URL
        } else {
            buildConfigField("String", "LOCATION_BASE_URL", "\"$locationBaseUrl\"")
        }

        // Xử lý WEATHER_BASE_URL
        val weatherBaseUrl = localProperties.getProperty("WEATHER_BASE_URL")
        if (weatherBaseUrl.isNullOrEmpty()) {
            println("WARN: WEATHER_BASE_URL not found in local.properties. Build might fail or use default values.")
            buildConfigField("String", "WEATHER_BASE_URL", "\"https://default-api.com/v1/\"") // Thêm giá trị mặc định cho BASE_URL
        } else {
            buildConfigField("String", "WEATHER_BASE_URL", "\"$weatherBaseUrl\"")
        }
        buildConfigField("String", "WEATHER_BASE_URL", "\"$weatherBaseUrl\"")

        // Xử lý WEATHER_API_KEY
        val weatherApiKey = localProperties.getProperty("WEATHER_API_KEY")
        if (weatherApiKey.isNullOrEmpty()) {
            println("WARN: WEATHER_API_KEY not found in local.properties. Build might fail or use default values.")
            buildConfigField("String", "WEATHER_API_KEY", "\"\"") // Thêm giá trị mặc định cho API_KEY
        } else {
            buildConfigField("String", "WEATHER_API_KEY", "\"$weatherApiKey\"")
        }
    }

    buildFeatures {
        viewBinding = true  // Kích hoạt ViewBinding để không cần sử dụng findViewById
        buildConfig = true  // Kích hoạt BuildConfig để sử dụng các biến buildConfigField
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Scalable Size Unit (Hỗ trợ điều chỉnh kích thước ở các màn hình khác nhau)
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.intuit.ssp:ssp-android:1.0.6")

    // Retrofit để gọi API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Koin cho Dependency Injection
    implementation("io.insert-koin:koin-android:3.4.0")

    // Material Design
    implementation("com.google.android.material:material:1.11.0")

    // Flexbox Layout
    implementation ("com.google.android.flexbox:flexbox:3.0.0")

    // Location
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Coil cho tải ảnh
    implementation("io.coil-kt:coil:2.4.0")

    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}