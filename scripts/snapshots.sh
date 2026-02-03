#!/bin/bash
# 寺子屋ラボ Snapshots 出荷スクリプト

# 🏛️ [理] スクリプトの場所に関わらず、プロジェクトルートに移動する
# これにより、scripts/ 内から叩いてもルートから叩いても正しく gradle を見つけられます
# shellcheck disable=SC2164
cd "$(dirname "$0")/.."

echo "🏛️ [Location] 現在地をプロジェクトルート $(pwd) に固定しました。"

echo "🚀 [Snapshots] 探求の成果を Nexus へ配送中..."

# 環境変数の存在チェック（防衛策）
if [ -z "$NEXUS_USERNAME" ] || [ -z "$NEXUS_PASSWORD" ]; then
    echo "❌ Error: 環境変数 NEXUS_USERNAME または NEXUS_PASSWORD が設定されていません。"
    echo "代表の MBP2018 で export されているか確認してください。"
    exit 1
fi

# 2. 現在の状態を把握するための情報を抽出
VERSION=$(grep 'library.version' gradle.properties | cut -d'=' -f2 | tr -d '[:space:]')
COMMIT_HASH=$(git rev-parse --short HEAD)
BRANCH_NAME=$(git rev-parse --abbrev-ref HEAD)

echo "📦 Target: $VERSION (Branch: $BRANCH_NAME, Commit: $COMMIT_HASH)"

./gradlew publish

# shellcheck disable=SC2181
if [ $? -eq 0 ]; then
    echo "✅ Snapshots ($VERSION) の配送に成功しました。"
    echo "🔗 Nexus で最新の成果を確認してください。"
else
    echo "❌ 配送に失敗しました。ネットワークまたは権限を確認してください。"
    exit 1
fi
