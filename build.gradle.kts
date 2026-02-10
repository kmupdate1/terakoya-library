plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    // alias(libs.plugins.org.sonarqube)
}

// コマンド実行時に -Prelease=true をつけた時だけ本番モード
val isRelease = project.hasProperty("release") && project.property("release") == "true"
val currentVersion = rootProject.findProperty("version.library")?.toString() ?: "unspecified"

extra["isRelease"] = isRelease

// apply(from = "gradle/sonar.gradle.kts")
apply(from = "gradle/publishing.gradle.kts")

allprojects {
    group = "jp.terakoyalabo"
    version = if (isRelease) currentVersion else "$currentVersion-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    // --- ここから追加：JVM バージョンの統制 ---
    // Kotlin Multiplatform プラグインが適用されているプロジェクトに対して
    // JVM ツールチェーンを一括で Java 21 に強制する
    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        configure<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension> {
            jvmToolchain(21) // これで魂（Compile）と肉体（Runtime）が 21 に揃う
        }
    }
}
