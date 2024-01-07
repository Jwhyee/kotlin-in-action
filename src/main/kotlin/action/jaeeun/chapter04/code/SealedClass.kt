package action.jaeeun.chapter04.code

interface ExprInterface
class Num(val value: Int) : ExprInterface
class Sum(val left: ExprInterface, val right: ExprInterface) : ExprInterface
class Minus(val left: ExprInterface, val right: ExprInterface) : ExprInterface

fun eval(e: ExprInterface): Int =
    when(e) {
        is Num -> e.value
        is Sum -> eval(e.right) + eval(e.left)
        else -> throw IllegalArgumentException()
    }


sealed class Expr {
    class Num(val value:Int) : Expr()
    class Sum(val left: Expr, val right: Expr) : Expr()
    class Minus(val left: Expr, val right: Expr) : Expr()
}

fun eval(e: Expr): Int =
    when(e) {
        is Expr.Num -> e.value
        is Expr.Sum -> eval(e.right) + eval(e.left)
//        else -> throw IllegalArgumentException()
        is Expr.Minus -> eval(e.left) - eval(e.right)
    }
