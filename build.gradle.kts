import java.net.NetworkInterface

plugins {
    `maven-publish`
    alias { libs.plugins.kotlin.multiplatform }
}

group = "jp.terakoyalabo"
version = "0.1.0-SNAPSHOT"

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

            val address = if (isAtLabo) project.findProperty("nexus.ip.labonet")?.toString()
            else project.findProperty("nexus.ip.vpn")?.toString() ?: "100.98.144.29"

            name = "TerakoyaNexus"
            url = uri("http://$address:8081/repository/terakoyalabo-library-$repoType")

            isAllowInsecureProtocol = true
            credentials {
                username = project.findProperty("nexus.username")?.toString()
                password = project.findProperty("nexus.password")?.toString()
            }
        }
    }
}
