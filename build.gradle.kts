import java.net.NetworkInterface

plugins {
    // 規格書 (toml) から Kotlin Multiplatform プラグインを読み込む
    // ここ（中央政府）では適用せず、各州に配る準備だけを行う
    alias(libs.plugins.kotlin.multiplatform) apply false
    // alias(libs.plugins.org.sonarqube)
}

val isAtLabo = NetworkInterface.getNetworkInterfaces().asSequence().any { iface ->
    iface.inetAddresses.asSequence().any { addr ->
        addr.hostAddress.startsWith("192.168.11.")
    }
}
// コマンド実行時に -Prelease=true をつけた時だけ本番モード
val isRelease = project.hasProperty("release") && project.property("release") == "true"
val v = rootProject.findProperty("library.version")?.toString() ?: "unspecified"

extra["isAtLabo"] = isAtLabo
extra["isRelease"] = isRelease

// apply(from = "gradle/sonar.gradle.kts")
apply(from = "gradle/publishing.gradle.kts")

allprojects {
    group = "jp.terakoyalabo"
    version = if (isRelease) v else "$v-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
