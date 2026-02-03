#!/bin/bash
# 寺子屋ラボ Releases 出荷スクリプト (正式版・高セキュリティ)

# 🏛️ [理] スクリプトの場所に関わらず、プロジェクトルートに移動する
# これにより、scripts/ 内から叩いてもルートから叩いても正しく gradle を見つけられます
cd "$(dirname "$0")/.."

echo "🏛️ [Location] 現在地をプロジェクトルート $(pwd) に固定しました。"

echo "🏛️ [Releases] 正式な理を Nexus へ結晶化中..."

# 1. 環境変数の存在チェック
if [ -z "$NEXUS_USERNAME" ] || [ -z "$NEXUS_PASSWORD" ]; then
    echo "❌ Error: 環境変数が必要です。"
    exit 1
fi

# 2. 実行確認
echo "⚠️  警告: これは Snapshot ではなく【正式リリース】です。"
# shellcheck disable=SC2162
read -p "本当に実行する場合は大文字で 'YES' と入力してください: " CONFIRM

if [ "$CONFIRM" != "YES" ]; then
    echo "🛑 意志が確認できませんでした（入力: $CONFIRM）。リリースを中止します。"
    exit 0
fi

# 3. 変数の準備（空白洗浄済み）
VERSION=$(grep 'library.version' gradle.properties | cut -d'=' -f2 | tr -d '[:space:]')
TAG_NAME="v$VERSION"
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)

echo "📦 [History] 正史(main)への統合を開始します..."

# 4. 歴史の固定 (Fail-fast)
set -e
git checkout main
git pull origin main
git merge "$CURRENT_BRANCH" --no-ff -m "Release $TAG_NAME: 概念の浄化と防衛線の確立"

# 5. 歴史の刻印 (Tag)
if git rev-parse "$TAG_NAME" >/dev/null 2>&1; then
    echo "⚠️  $TAG_NAME は既に歴史に存在します。タグ打ちはスキップします。"
else
    echo "🏷️  $TAG_NAME を歴史に刻印中..."
    git tag -a "$TAG_NAME" -m "Release $TAG_NAME"
    git push origin main --tags
fi

# 6. Nexusへの放流
echo "🚀 承認されました。出荷を開始します..."
./gradlew publish -Prelease=true

# 7. 成功後の「自動代謝」プロセス
echo "✨ 正式版 $TAG_NAME の出荷に成功しました。"
echo "📝 次のバージョンへ移行中..."

# バージョンインクリメント (0.0.2 -> 0.0.3)
NEXT_VERSION=$(echo "$VERSION" | awk -F. '{$NF = $NF + 1;} 1' | sed 's/ /./g')
TODAY=$(date +%Y/%m/%d)

# gradle.properties の書き換えと履歴追記 (macOS sed)
sed -i '' "s/library.version=$VERSION/library.version=$NEXT_VERSION/" gradle.properties
sed -i '' "/library.version=$NEXT_VERSION/a\\
# $TODAY [$VERSION]
" gradle.properties

# 変更の保存と正史への反映
git add gradle.properties
git commit -m "Chore: $TAG_NAME 出荷完了。バージョンを $NEXT_VERSION へ更新"
git push origin main

# 8. 現場（開発ブランチ）へ帰還
git checkout "$CURRENT_BRANCH"

echo "✅ 全行程が完了しました。"
echo "🔥 次の『千本ノック』の準備が整いました（Next: $NEXT_VERSION）"
