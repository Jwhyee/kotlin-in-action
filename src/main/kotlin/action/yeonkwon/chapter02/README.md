## 2.1 기본 요소: 함수와 변수

### **expression(식)과 statement(문)**

- expression은 값을 만들어 낸다
- statement는 **수행해야 할 일부 동작을 표현하는 가장 작은 독립형 요소**입니다.

```kotlin
val bestUsers = users.filter { it.passing } 
      .sortedBy { -it.meanScore }
      .take(10) // <- first statement
print("The best students are: "); // <- second statement
println(bestUsers.joinToString()); // <- third statement
```

- 3개의 **statement**로 이루어져있습니다.
- 첫 번째 문은 best users를 처리하는 **statement**
- 두 번째는 `The best students are:` 출력 **statement**
- 세 번째는 `joinToString` 출력 **statement**

또한 독립형 표현식(**standalone expression**)도 문이 될 수 있고 표현문(**expression statement**)이라고 부르기도 합니다.

```kotlin
updateUser(user) // <- 1 statement
```

순수한 함수형 프로그래밍에서는 **statement**는 없고 **expressions**로만 이루어져 있습니다.

### 기본 구조

```kotlin
fun bigger(a: Int, b: Int) = if(a > b) a else b
```

### **Named arguments**

간결한 코드의 경우 함수를 호출할 때 매개 변수 이름을 포함할 필요가 없지만. 매개 변수 이름을 포함하면 코드를 더 쉽게 읽을 수 있습니다.

- 순서에 상관없이 매개변수의 이름을 쓸 수 있습니다.

```kotlin
fun printMessageWithPrefix(message: String, prefix: String) {
    println("[$prefix] $message")
}

fun main() {
    // Uses named arguments with swapped parameter order
    printMessageWithPrefix(prefix = "Log", message = "Hello")
    // [Log] Hello
}
```

### **Default parameter values**

함수 매개변수에 대한 기본값을 정의할 수 있습니다.

```kotlin
fun printMessageWithPrefix(message: String, prefix: String = "Info") {
    println("[$prefix] $message")
}

fun main() {
    // Function called with both parameters
    printMessageWithPrefix("Hello", "Log") 
    // [Log] Hello
    
    // Function called only with message parameter
    printMessageWithPrefix("Hello")        
    // [Info] Hello
    
    printMessageWithPrefix(prefix = "Log", message = "Hello")
    // [Log] Hello
}
```

### **Functions without return**

함수가 아무것도 반환하지 않으면 Unit을 반환하게 됩니다.

Unit 타입은 Java의 void와 같은 역할을 하며 리턴값이 없음을 나타내는 키워드로 쓰입니다

```kotlin
fun printMessage(message: String) {
    println(message)
    // `return Unit` or `return` is optional
}

fun main() {
    printMessage("Hello")
    // Hello
}
```

### **Lambda expressions**

람다식을 사용하여 더 간결하게 표현할 수 있습니다.

```kotlin
fun uppercaseString(string: String): String {
    return string.uppercase()
}
fun main() {
    println(uppercaseString("hello"))
    // HELLO
}
```

```kotlin
fun main() {
    println({ string: String -> string.uppercase() }("hello"))
    // HELLO
}
```

### 변수

- 변수 이름 뒤에 타입을 명시하거나 생략을 허용
- 초기화 식을 사용하지 않고 변수를 선언하려면 변수 타입을 반드시 명시
- val: immutable 변수
- var: mutable 변수
- 동적타입은 아니기 때문에 변수 재할당으로 타입 변경은 불가능

```kotlin
var answer = 42
answer = "no answer" // type mismatch
```

### **String templates**

다음 세 가지로 사용가능합니다

```kotlin
i = 10
println("i = $i") // i에 값 삽입

val s = "abc"
println("$s.length is ${s.length}")  // brace 사용
// abc.length is 3

val price = """ // multi line
${'$'}_9.99
"""
```

### **String formatting**

포맷팅은 `Kotlin/JVM`에서 String.format() 만 사용이 가능합니다.

```kotlin
val integerNumber = String.format("%07d", 31416)
println(integerNumber)
// 0031416

// Formats with four decimals and sign
val floatNumber = String.format("%+.4f", 3.141592)
println(floatNumber)
// +3.1416

// Formats with uppercase for two placeholders
val helloString = String.format("%S %S", "hello", "world")
println(helloString)
// HELLO WORLD
```

## 2.2 클래스와 프로퍼티

### 생성자

Kotlin의 클래스에는 클래스 헤더에 선언되는 **주 생성자**와 하나 이상의 **보조 생성자**가 있습니다.

```kotlin
class Person constructor(firstName: String) { /*...*/ }
class Person(firstName: String) { /*...*/ } // constuctor에 적용하는 annotation 이나 visibility modifiers가 없으면 생략도 가능
```

### 프로퍼티

Kotlin 클래스의 속성은 `var` 키워드를 사용하여 사용하여 mutable하게 선언할 수 있습니다.

`val` 키워드로 읽기전용으로 생성할 수 있습니다.

```kotlin
class Address {
    val name: String = "Holmes, Sherlock" // <- read-only 비공개 필드와 단순한 공개 게터를 만든다.
    var street: String = "Baker" // mutable 비공개 필드, 공개 게터, 공개 세터를 만든다.
    var city: String = "London"
    var state: String? = null
    var zip: String = "123456"
}
```

### **커스텀 접근자**

선언하는 전체 문법은 다음과 같습니다.

```kotlin
var <propertyName>[: <PropertyType>] [= <property_initializer>]
    [<getter>]
    [<setter>]
```

파라미터가 없는 함수를 정의하는 방식과 커스텀 게터를 정의하는 방식 모두 비슷하고 차이가 나는 부분은 가독성뿐입니다.

### **Backing fields**

코틀린에서 필드는 오직 메모리에 값을 유지하기 위해 사용 되기 때문에 직접적으로 선언할 수 없습니다.

직접적으로 선언하면 setValue recursive가 발생할 수 있습니다.

```kotlin
var counter = 0 // the initializer assigns the backing field directly
  set(value) {
      if (value >= 0)
          field = value
          // counter = value // ERROR StackOverflow: Using actual name 'counter' would make setter recursive
  }
```

### 디렉터리와 패키지

코틀린 파일 맨 앞에 package문을 넣으면 파일 안에 있는 모든 선언(클래스, 함수, 프로퍼티 등)이 해당 패키지에 들어갑니다.

같은 패키지에 속해 있다면 다른 파일에서 정의한 선언일지라도 직접 사용할 수 있는 반면 다른 패키지에 정의한 선언을 사용하려면 임포트를 통해 선언을 불러와야 합니다.

```kotlin
package geometry.shapes
import java.util.Random

class Rectangle(val height: Int, val width: Int) {
		val isSquare: Boolean
			get() = height == width
}

fun createRandomRectangle(): Rectangle {
	val random = Random()
	return Rectangle(random.nextInt(), random.nextInt())
}
```

코틀린에서는 여러 클래스를 한 파일에 넣을 수 있고, 파일의 이름도 마음대로 정할 수 있다. 디스크상의 어느 디렉터리에 소스코드 파일을 위치시키든 관계없다. 따라서 원하는 대로 소스코드를 구성할 수 있다.

하지만 대부분이 경우 **자바와 같이 패키지별로 디렉터리를 구성하는 편이 낫습니다.**

## **2.3 선택 표현과 처리: enum과 when**

```kotlin
enum class Color (
	val r: Int, val g: Int, val b: Int
) {
	RED(255, 0, 0), ORANGE(255, 165, 0),
	YELLOW(255, 255, 0), GREEN(0, 255, 0), BLUE(0, 0, 255),
	INDIGO(75, 0, 130), VIOLET(238, 130, 238);

	fun rgb() = (r * 256 + g) * 256 + b // enum 클래스 안에서 메소드 정의
}
```

### **when으로 enum 클래스 다루기**

> if와 마찬가지로 when도 값을 만들어내는 식이기 때문에 식이 본문인 함수에 when을 사용 가능합니다.

```kotlin
fun getMnemonic(color: Color) =
    when (color) {
        Color.RED -> "Richard"
        Color.ORANGE -> "Of"
        Color.YELLOW -> "York"
        Color.GREEN -> "Gave"
        Color.BLUE -> "Battle"
        Color.INDIGO -> "In"
        Color.VIOLET -> "Vain"
    }
```

한 분기 안에서 여러 값을 매치 패턴으로 사용할 수도 있는데, 이런 경우 값 사이를 콤마로 분리합니다.

```kotlin
fun getWarmth(color: Color) = when (color) {
    Color.RED, Color.ORANGE, Color.YELLOW -> "warm"
    Color.GREEN -> "neutral"
    Color.BLUE, Color.INDIGO, Color.VIOLET -> "cold"
}
```

### **when과 임의의 객체를 함께 사용**

> 자바 switch와 달리 코틀린 when의 분기 조건은 임의의 객체를 허용합니다.

```kotlin
fun mix(c1: Color, c2: Color) =
  when (setOf(c1, c2)) {
      setOf(RED, YELLOW) -> ORANGE
      setOf(YELLOW, BLUE) -> GREEN
      setOf(BLUE, VIOLET) -> INDIGO
      else -> throw Exception("Dirty color") 
  }
```

### **인자 없는 when 사용**

> 인자없는 when으로 `if`-`else` `if` 체인의 대체로도 사용할 수 있습니다. 인자가 제공되지 않은 when은 bool이 분기 조건이 되고 참일 때 분기가 실행됩니다.

```kotlin
when {
    x.isOdd() -> print("x is odd")
    y.isEven() -> print("y is even")
    else -> print("x+y is odd")
}
```

### **스마트 캐스트**

> 대부분의 경우, 컴파일러가 타입을 추적하기 때문에 명시적인 타입 캐스팅이 필요 없습니다.

```kotlin
fun demo(x: Any) {
    if (x is String) {
        print(x.length) // x is automatically cast to String
    }
}

when (x) {
    is Int -> print(x + 1)
    is String -> print(x.length + 1)
    is IntArray -> print(x.sum())
}
```

## **2.4 대상을 이터레이션: while 과 for 루프**

- rangeTo 연산자(..)가 존재합니다.
- 이터레이션 반복문, `for in` 문에서 다양한 응용이 가능합니다.
- `Int`, `Long`, `Char` 타입의 범위는 [arithmetic progressions](https://en.wikipedia.org/wiki/Arithmetic_progression) 로 취급할 수 있습니다.

```kotlin
println(4 in 1..4) // true
println(4 in 1..<4) // false
for (i in 1..8 step 2) print(i) // 1357
```

## **2.5 코틀린의 예외 처리**

- 자바와 동일하게 Kotlin의 모든 예외는 `Throwable` 클래스를 상속합니다.
- 모든 예외는 message, stack trace, optional cause를 갖고 있습니다.

### **Checked exceptions**

> 코틀린은 checked exception이 없습니다. 이유는 여러가지가 있습니다.

- effective java 77 item 에서는 `Don't ignore exceptions.` 라고 합니다.
- Runtime Exception랑 Checked Exception는 기능적으로 동일하다.
- 중간부분의 API는 낮은 수준에서 발생할 수 있는 특정 타입의 장애에 대해 알 필요가 없거나 알고 싶어하지 않는다.
- 예외 사항을 요구하는 것이 대규모 소프트웨어 프로젝트에서는 생산성이 저하되고 코드 품질이 전혀 향상되지 않았다는 결과가 나타났다.
- [Java's checked exceptions were a mistake](https://radio-weblogs.com/0122027/stories/2003/04/01/JavasCheckedExceptionsWereAMistake.html) (Rod Waldhoff)
- [The Trouble with Checked Exceptions](https://www.artima.com/intv/handcuffs.html) (Anders Hejlsberg)

### References
- [Kotlin programmer dictionary: Statement vs Expression](https://blog.kotlin-academy.com/kotlin-programmer-dictionary-statement-vs-expression-e6743ba1aaa0)
- [Kotlin Docs](https://kotlinlang.org/docs/home.html)
