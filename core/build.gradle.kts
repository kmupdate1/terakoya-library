import java.net.NetworkInterface

plugins {
    `maven-publish`
    alias { libs.plugins.kotlin.multiplatform }
    alias { libs.plugins.org.sonarqube }
}

val isAtLabo = NetworkInterface.getNetworkInterfaces().asSequence().any { iface ->
    iface.inetAddresses.asSequence().any { addr ->
        addr.hostAddress.startsWith("192.168.11.")
    }
}

repositories {
    mavenCentral()
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
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test.common)
        }
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
            val repoType = if (project.version.toString().endsWith("-SNAPSHOT")) "snapshots" else "releases"

            val address = if (isAtLabo) rootProject.findProperty("nexus.ip.labonet")?.toString()
            else rootProject.findProperty("nexus.ip.vpn")?.toString() ?: "100.98.144.29"

            name = "TerakoyaNexus"
            url = uri("http://$address:8081/repository/terakoyalabo-library-$repoType")

            isAllowInsecureProtocol = true
            credentials {
                username = rootProject.findProperty("nexus.username")?.toString()
                password = rootProject.findProperty("nexus.password")?.toString()
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
