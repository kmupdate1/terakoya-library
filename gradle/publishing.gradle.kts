subprojects {
    // maven-publish プラグインが適用されているサブプロジェクトにのみ、この設定を注入する
    plugins.withId("maven-publish") {
        // 型を明示的に指定して configure を呼ぶ
        configure<org.gradle.api.publish.PublishingExtension> {
            publications {
                // KMP特有の artifactId 補正
                withType<org.gradle.api.publish.maven.MavenPublication>().configureEach {
                    if (artifactId == "${project.name}-kotlinMultiplatform") artifactId = project.name
                }
            }

            repositories {
                maven {
                    // ルートプロジェクトで定義した変数を参照
                    val isRelease = rootProject.extra["isRelease"] as Boolean
                    val repoType = if (isRelease) "releases" else "snapshots"
                    val domain = System.getenv("TERAKOYALABO_DOMAIN")

                    url = uri("$domain/repository/terakoyalabo-library-$repoType")

                    credentials {
                        username = System.getenv("NEXUS_USERNAME")
                        password = System.getenv("NEXUS_PASSWORD")
                    }
                }
            }
        }
    }
}
