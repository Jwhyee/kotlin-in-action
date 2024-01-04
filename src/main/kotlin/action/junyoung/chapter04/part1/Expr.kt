package action.junyoung.chapter04.part1

sealed class Expr {
    class Num(val value: Int) : Expr()
    class Sum(val left: Expr, val right: Expr) : Expr()
}

// Expr의 하위 클래스는 Num, Sum 뿐이기 때문에 별도의 else를 처리해줄 필요가 없다.
fun eval(e: Expr): Int = when (e) {
    is Expr.Num -> e.value
    is Expr.Sum -> eval(e.right) + eval(e.left)
}