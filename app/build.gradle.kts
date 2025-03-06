plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.food_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.food_app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "CLOUDINARY_CLOUD_NAME",
                "\"${project.findProperty("CLOUDINARY_CLOUD_NAME") ?: ""}\""
            )
            buildConfigField(
                "String",
                "CLOUDINARY_API_KEY",
                "\"${project.findProperty("CLOUDINARY_API_KEY") ?: ""}\""
            )
            buildConfigField(
                "String",
                "CLOUDINARY_API_SECRET",
                "\"${project.findProperty("CLOUDINARY_API_SECRET") ?: ""}\""
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField(
                "String",
                "CLOUDINARY_CLOUD_NAME",
                "\"${project.findProperty("CLOUDINARY_CLOUD_NAME") ?: ""}\""
            )
            buildConfigField(
                "String",
                "CLOUDINARY_API_KEY",
                "\"${project.findProperty("CLOUDINARY_API_KEY") ?: ""}\""
            )
            buildConfigField(
                "String",
                "CLOUDINARY_API_SECRET",
                "\"${project.findProperty("CLOUDINARY_API_SECRET") ?: ""}\""
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.circleimageview)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.cloudinary.android)
    implementation(libs.picasso)
    implementation(libs.lottie)


}
