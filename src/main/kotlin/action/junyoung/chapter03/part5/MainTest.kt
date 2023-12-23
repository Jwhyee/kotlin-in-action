package action.junyoung.chapter03.part5

fun main() {
    val str = "12.345-6.A"
    println(str.split("\\.|-".toRegex()))
    println(str.split(".", "-"))

    val path = "/users/yole/kotlin-book/chapter.adoc"
    parsePath(path)
    parsePathVer2(path)

    printKotlinLogo()
}

fun parsePath(path: String) {
    val directory = path.substringBeforeLast("/")
    val fullName = path.substringAfterLast("/")
    val fileName = fullName.substringBeforeLast(".")
    val extension = fullName.substringAfterLast(".")

    println("dir : ${directory}, name : ${fileName}, ext : ${extension}")
}

fun parsePathVer2(path: String) {
    val regex = """(.+)/(.+)\.(.+)""".toRegex()
    val mathResult = regex.matchEntire(path)
    if (mathResult != null) {
        val (dir, name, ext) = mathResult.destructured
        println("dir : ${dir}, name : ${name}, ext : ${ext}")
    }
}

fun printKotlinLogo() {
    val kotlinLogo = """| //
                       .| //
                       .|/ \"""
    println(kotlinLogo.trimMargin("."))
}