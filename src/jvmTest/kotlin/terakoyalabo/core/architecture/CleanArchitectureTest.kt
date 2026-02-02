package terakoyalabo.core.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withPackage
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

class CleanArchitectureTest {
    private val scope = Konsist.scopeFromProject()

    /*
    @Test
    fun primitiveパッケージは他のドメイン層に依存してはならない() {
        Konsist
            .scopeFromProject()
            .files
            .withPackage("..primitive..")
            .assertTrue {
                // 依存先が primitive, error, function もしくは Kotlin/Java 標準のみであることを確認
                it.hasOnlyImports(
                    "..primitive..",
                    "..error..",
                    "..function..",
                    "kotlin..",
                    "java.."
                )
            }
    }
    */

    /*
    @Test
    fun `domain内のパッケージ依存関係を縛る`() {
        val scope = Konsist.scopeFromProject()

        // primitiveはどこにも依存してはならない（循環の根絶）
        scope.files
            .withPackage("..primitive..")
            .assertTrue { file ->
                file.imports.all { import ->
                    import.name.contains("..primitive..") ||
                            import.name.startsWith("kotlin.") ||
                            import.name.startsWith("java.")
                }
            }
    }
    */

    @Test
    fun `domain内のパッケージ依存関係を縛る`() {
        // primitiveは、自パッケージ、error、function、および標準ライブラリ以外に依存してはならない
        scope.files
            .withPackage("..primitive..")
            .assertTrue { file ->
                file.imports.all { import ->
                    // 1. 同一パッケージ内（primitive）の参照を許可
                    import.name.contains("..primitive..") ||
                            // 2. 寺子屋の共通基盤（error / function）を許可
                            import.name.contains("..error..") ||
                            import.name.contains("..function..") ||
                            // 3. Kotlin/JVMの標準ライブラリを許可
                            import.name.startsWith("kotlin.") ||
                            import.name.startsWith("java.")
                }
            }
    }

    @Test
    fun domain内の各パッケージ間に循環参照がないことを確認する() {
        scope
    }
}
