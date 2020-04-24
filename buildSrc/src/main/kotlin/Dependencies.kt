import org.gradle.api.JavaVersion

object Config {
    const val applicationId = "com.spendy"
    const val minSdk = 21
    const val compileSdk = 29
    const val targetSdk = 29
    val javaVersion = JavaVersion.VERSION_1_8
    const val buildTools = "29.0.2"
    const val versionCode = 9
    const val versionName = "1.0.2"
}

object Versions {
    val material = "1.2.0-alpha04"
    val constraintlayout = "2.0.0-beta4"
    val fragment = "1.3.0-alpha03"
    val core_ktx = "1.2.0"
    val kotlin_stdlib = "1.3.50"
    val junit = "4.12"
    val junit_ext = "1.1.1"
    val espresso = "3.2.0"
    val fragnav = "3.2.0"
    val dime = "1.0.6"
    val room = "2.1.0"
    val lifecycle = "2.1.0"
    val rxandroid = "2.1.1"
    val rxjava2 = "2.0.1"
    val lifecycle_extensions = "2.1.0"
    val coroutines = "1.3.2"
    val viewmodel_lifecycle = "2.2.0"
    val room_ktx = "2.2.3"
    val play_services = "18.3.0"
    val pageindicator = "0.2.8"
    val nav_version = "2.2.1"
}

object Deps {
    //UI
    val material = "com.google.android.material:material:${Versions.material}"
    val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"

    val fragment = "androidx.fragment:fragment-ktx:${Versions.fragment}"


    val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
    val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin_stdlib}"

    //Testing
    val junit = "junit:junit:${Versions.junit}"
    val junit_ext = "androidx.test.ext:junit:${Versions.junit_ext}"
    val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"

    val fragnav = "com.ncapdevi:frag-nav:${Versions.fragnav}"

    /*Dimension*/
    val sdp = "com.intuit.sdp:sdp-android:${Versions.dime}"
    val ssp = "com.intuit.ssp:ssp-android:${Versions.dime}"

    //Room
    val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    val room_ktx = "androidx.room:room-ktx:${Versions.room_ktx}"
    val play_services = "com.google.android.gms:play-services-ads:${Versions.play_services}"
    val pageindicator = "com.github.chahine:pageindicator:${Versions.pageindicator}"

    val viewmodel_lifecycle =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewmodel_lifecycle}"
    val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.nav_version}"
    val navigation_ui = "androidx.navigation:navigation-ui-ktx:${Versions.nav_version}"

}