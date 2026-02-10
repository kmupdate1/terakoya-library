plugins {
    `maven-publish`
    alias { libs.plugins.kotlin.multiplatform }
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
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
        commonTest.dependencies {

        }
        jvmMain.dependencies {
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.cio)
            implementation(libs.ktor.server.netty)

            // erase
            implementation(libs.ktor.server.auth)
            implementation(libs.ktor.server.auth.jwt)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.serialization.json)

            implementation(libs.logback.classic)

            implementation(kotlin("test"))

        }
        jvmTest.dependencies {
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.cio)
            implementation(libs.ktor.server.netty)
            implementation(libs.ktor.server.auth)
            implementation(libs.ktor.server.auth.jwt)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.serialization.json)

            implementation(libs.logback.classic)

            implementation(kotlin("test"))
        }
    }
}
