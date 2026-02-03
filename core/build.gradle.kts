plugins {
    `maven-publish`
    alias { libs.plugins.kotlin.multiplatform }
    alias { libs.plugins.org.sonarqube }
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

sonar {
    properties {
        property("sonar.projectKey", "kmupdate1_${project.name}")
        property("sonar.organization", "terakoyalabo")
        property("sonar.host.url", "https://sonarcloud.io")

        // 鑑定士に「ここが理の源泉だ」と教える
        property("sonar.sources", "src/commonMain/kotlin")
        // もしテストも鑑定（カバレッジ等）したい場合は、ここも
        property("sonar.tests", "src/commonTest/kotlin")
    }
}
