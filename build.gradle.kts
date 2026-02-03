plugins {
    // 規格書 (toml) から Kotlin Multiplatform プラグインを読み込む
    // ここ（中央政府）では適用せず、各州に配る準備だけを行う
    alias(libs.plugins.kotlin.multiplatform) apply false
}

allprojects {
    group = "jp.terakoyalabo"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
