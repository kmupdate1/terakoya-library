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
        }
        commonTest.dependencies {}
        jvmMain.dependencies {
            implementation(libs.ktor.server.auth.jwt)
        }
    }

    jvmToolchain(21)
}
