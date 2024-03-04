package action.junyoung.chapterE

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
   sumAll()
}

fun sumAll() {
   runBlocking {
      val d1 = async {
         log("d1 start")
         delay(1000L);
         log("d1 end")
         1
      }
      log("after async(d1)")
      val d2 = async {
         log("d2 start")
         delay(1000L);
         log("d2 end")
         2
      }
      log("after async(d2)")
      val d3 = async {
         log("d3 start")
         delay(1000L);
         log("d3 end")
         3
      }
      log("after async(d3)")

      val v1 = d1.await()
      log("v1 call")
      val v2 = d2.await()
      log("v2 call")
      val v3 = d3.await()
      log("v3 call")

      log("1 + 2 + 3 = ${v1 + v2 + v3}")
      log("after await all & add")
   }
}