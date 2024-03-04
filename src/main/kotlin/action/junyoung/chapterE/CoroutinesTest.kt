package action.junyoung.chapterE

import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

fun main() {
   log("main() started.")
//   launchInGlobalScope()
//   log("launchInGlobalScope() executed")
//   runBlockingExample()
//   log("runBlockingExample() executed")
   yieldExample()
   log("yieldExample() executed")
//   Thread.sleep(5000L)
   log("main() terminated")
}

fun now() = ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.MILLIS)

fun log(msg: String) = println("${now()} : ${Thread.currentThread()} : $msg")

fun launchInGlobalScope() {
   GlobalScope.launch {
      log("coroutine started.")
   }
}

fun runBlockingExample() {
   runBlocking {
      launch {
         log("coroutine started.")
      }
   }
}

fun yieldExample() {
   runBlocking {
      launch {
         log("1")
         yield()
         log("3")
         yield()
         log("5")
      }
      log("after first launch")
      launch {
         log("2")
         delay(1000L)
         log("4")
         delay(1000L)
         log("6")
      }
      log("after second launch")
   }
   log("after runBlocking")
}