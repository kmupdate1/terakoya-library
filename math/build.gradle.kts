import java.net.NetworkInterface

plugins {
    `maven-publish`
    alias { libs.plugins.kotlin.multiplatform }
    alias { libs.plugins.org.sonarqube }
}

group = "jp.terakoyalabo"
version = "0.1.0-SNAPSHOT"

val repoType = if (project.version.toString().endsWith("-SNAPSHOT")) "snapshots" else "releases"

val isAtLabo = NetworkInterface.getNetworkInterfaces().asSequence().any { iface ->
    iface.inetAddresses.asSequence().any { addr ->
        addr.hostAddress.startsWith("192.168.11.")
    }
}
val address = if (isAtLabo) rootProject.findProperty("nexus.ip.labonet")?.toString() ?: "192.168.11.6"
else rootProject.findProperty("nexus.ip.vpn")?.toString() ?: "100.98.144.29"

val domain = "http://$address:8081"
val repoUri = uri("$domain/repository/terakoyalabo-library-$repoType")

repositories {
    mavenCentral()
    maven {
        url = repoUri
        isAllowInsecureProtocol = true

        credentials {
            username = rootProject.findProperty("nexus.username")?.toString()
            password = rootProject.findProperty("nexus.password")?.toString()
        }
    }
}

kotlin {
    jvm {
        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }
    linuxArm64()
    linuxX64()
    macosArm64()
    macosX64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
        }
        commonTest.dependencies {  }
        jvmTest.dependencies {
            implementation(libs.lemonappdev.konsist)
            implementation(libs.junit.jupiter)
            runtimeOnly("org.junit.platform:junit-platform-launcher")
        }
    }

    jvmToolchain(21)
}

publishing {
    repositories {
        maven {
            name = "TerakoyaNexus"
            url = repoUri

            isAllowInsecureProtocol = true
            credentials {
                username = project.findProperty("nexus.username")?.toString()
                password = project.findProperty("nexus.password")?.toString()
            }
        }
    }
}

sonar {
    properties {
        property("sonar.projectKey", "kmupdate1")
        property("sonar.organization", "terakoyalabo")
        property("sonar.host.url", "https://sonarcloud.io")

        // 鑑定士に「ここが理の源泉だ」と教える
        property("sonar.sources", "src/commonMain/kotlin")
        // もしテストも鑑定（カバレッジ等）したい場合は、ここも
        property("sonar.tests", "src/commonTest/kotlin")
    }
}

// build.gradle.kts のトップレベル（または一番下）に追記
configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}
