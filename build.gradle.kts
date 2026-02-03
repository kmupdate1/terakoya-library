import java.net.NetworkInterface

plugins {
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
val currentVersion = rootProject.findProperty("library.version")?.toString() ?: "unspecified"

extra["isAtLabo"] = isAtLabo
extra["isRelease"] = isRelease

// apply(from = "gradle/sonar.gradle.kts")
apply(from = "gradle/publishing.gradle.kts")

allprojects {
    group = "jp.terakoyalabo"
    version = if (isRelease) currentVersion else "$currentVersion-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
