package action.junyoung.chapterE

fun main() {
   for (i in countdown(10)) {
      println(i)
   }
}

fun countdown(n: Int): Sequence<Int> = sequence {
   var count = n
   while (count > 0) {
      yield(count)
      count -= 1
   }
}

fun subRoutine(i: Int) {
   var localVar = "Hello"

   if(i == 10) localVar += i

   println(localVar)
}