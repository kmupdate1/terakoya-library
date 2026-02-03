import java.net.NetworkInterface

plugins {
    // 規格書 (toml) から Kotlin Multiplatform プラグインを読み込む
    // ここ（中央政府）では適用せず、各州に配る準備だけを行う
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.org.sonarqube)
}

val isAtLabo = NetworkInterface.getNetworkInterfaces().asSequence().any { iface ->
    iface.inetAddresses.asSequence().any { addr ->
        addr.hostAddress.startsWith("192.168.11.")
    }
}
// コマンド実行時に -Prelease=true をつけた時だけ本番モード
val isRelease = project.hasProperty("release") && project.property("release") == "true"
val v = rootProject.findProperty("library.version")?.toString() ?: "unspecified"

sonar {
    properties {
        property("sonar.projectKey", "kmupdate1_terakoyalabo_library_project")
        property("sonar.organization", "terakoya-labo")
        property("sonar.projectName", "Terakoyalabo Library Project")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.token", System.getenv("SONAR_QUBE_CLOUD_TOKEN"))

        // 解析から除外したいファイル（自動生成コードなど）があればここで
        property("sonar.exclusions", "**/generated/**")
    }
}

allprojects {
    group = "jp.terakoyalabo"
    version = if (isRelease) v else "$v-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

// 各州（子プロジェクト）に、共通の「外交（Publishing）ルール」を授ける
subprojects {
    // 1. 各州が「外交官(maven-publish)」を雇っている場合のみ、中央政府が介入する
    plugins.withId("maven-publish") {
        configure<PublishingExtension> {
            publications {
                // ここに各州ごとの成果物定義を書く
                // (現状の core や math にある publishing ブロックをここに統合できます)

                // 2. 「maven」という名前の成果物を自動生成
                withType<MavenPublication>().configureEach {
                    if (artifactId == project.name + "-kotlinMultiplatform") artifactId = project.name
                }
            }

            // 3. 輸出先（Nexus）の判定と設定
            repositories {
                maven {
                    val repoType = if (isRelease) "releases" else "snapshots"
                    val address = if (isAtLabo) rootProject.findProperty("nexus.ip.labonet")?.toString() ?: "192.168.11.6"
                    else rootProject.findProperty("nexus.ip.vpn")?.toString() ?: "100.98.144.29"

                    val domain = "http://$address:8081"
                    val uri = uri("$domain/repository/terakoyalabo-library-$repoType")

                    url = uri

                    isAllowInsecureProtocol = true
                    credentials {
                        username = System.getenv("NEXUS_USERNAME")
                        password = System.getenv("NEXUS_PASSWORD")
                    }
                }
            }
        }
    }
}
