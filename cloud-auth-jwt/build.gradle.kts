plugins {
    `maven-publish`
    alias { libs.plugins.kotlin.multiplatform }
}

repositories {
    mavenLocal()
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
            implementation(project(":cloud-auth"))
        }
        commonTest.dependencies {}
        jvmMain.dependencies {
            implementation(libs.ktor.server.auth.jwt)
        }
        jvmTest.dependencies {
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.cio)
            implementation(libs.ktor.server.netty)
            implementation(libs.ktor.server.auth.jwt)

            implementation(project(":cloud-auth"))
            implementation(project(":foundation"))
        }
    }
}
