# 코틀린 기초

## 기본 요소: 함수와 변수
``` kotlin
fun main(args: Array<String>) {
	println("Hello, World!")
}
```
- 함수 선언에는 fun 키워드가 사용된다.
- 파라미터 이름 뒤에 파라미터의 타입을 작성한다.
- 함수가 클래스 내부에 존재하는것을 강제하지 않는다.
- 세미콜론이 필요하지 않다.
- 자바 라이브러리 함수를 간결하게 사용할 수 있는 Wrapper를 제공한다.
    - System.out.println() >>> println()

### 함수
``` kotlin
fun max(a: Int, b: Int): Int {
	return if (a > b) a else b
}

// 함수의 몸통이 한줄인 경우 아래와 같이 작성 가능
// 팀 내 컨벤션에 따라 사용하는 것을 추천
fun max(a: Int, b: Int): Int = if (a > b) a else b
fun max(a: Int, b: Int) = if (a > b) a else b
```

## 변수
코틀린에는 변경 가능한 변수와 변경 불가능한 변수가 존재
- val (value) - immutable
- var (variable) - mutable
  변수를 사용할 때 꼭 변경이 필요하지 않다면 val 키워드를 사용해 불변 변수를 선언하는 것을 지향
``` kotlin
// 타입추론으로 타입 생략 가능
val question = "질문"
val answer = "답변"

var cursor = 0
cursor = 10
```
val 키워드로 변수를 선언하고 즉시 초기값을 설정하지 않더라도 한 번만 초기화되는것을 컴파일러가 알 수 있게 보장해주면 아래와 같이 작성 가능
``` kotlin
val message: String
if (canPerformOperation()) {
	message = "Success"
} else {
	message = "Failed"
}
```
``` kotlin
// 위의 코드를 좀더 간결하게 작성
val message = if (canPerformOperation()) {
	"Success"
} else {
	"Failed"
}
```
val 키워드로 선언된 변수가 객체일 경우 참조 값 자체는 변경될 수 없지만 객체 내부의 상태는 변경이 가능할 수 있다.
``` kotlin
val languages = arrayListOf("Java", "C", "Python")
languages.add("Kotlin")
```

### 문자열 템플릿
코틀린은 문자열 안에서 변수를 사용할 수 있다.
``` kotlin
val result = "Success"
println("this call is $result")
```

## 클래스와 프로퍼티
``` java
@Getter
public class Person {
	private final String name;

	public Person(String name) {
		this.name = name;
	}
}
```
위와 같이 작성된 자바 빈 클래스를 코틀린으로 아래와 같이 표현할 수 있다.
``` kotlin
class Person(val name: String) // default visibility modifier is public
```

클래스를 다루는 언어에서 보통 클래스 내부에 상태(혹은 필드)가 선언 되는데 이러한 필드와 접근자를 묶어 프로퍼티라고 부른다.
대부분의 프로퍼티에는 그 프로퍼티의 값을 저장하기 위한 필드가 있는데 이를 **backing field**라고 부른다.
``` kotlin
class Person(
	val name: String, // getter 제공
	var isMarried: Boolean, // getter, setter 제공
)

// Person 클래스 인스턴스 생성 방법
val person = Person(name = "Bob", isMarried = false)
println(person.name)
println(person.isMarried)

person.isMarried = true
println(person.isMarried)
```

### 커스텀 접근자
``` kotlin
class Rectangle(val height: Int, val widht: Int) {
	val isSquare: Boolean
		get() {
			return height == width
		}
}
```

### 소스코드 구조: 디렉터리와 패키지
Java
- 모든 클래스를 패키지 단위로 관리한다.
- 패키지의 구조와 일치하는 디렉터리 계층 구조를 만들고 클래스의 소스코드를 그 클래스가 속한 패키지와 같은 디렉터리에 위치 시켜야 한다.
  Kotlin
- 여러 클래스를 한 파일에 넣을 수 있고 파일 이름도 마음대로 정할 수 있다.
- 디렉터리상 어느 디렉터리에 소스코드 파일을 위치시키든 관계없다.

### 선택 표현과 처리: enum, when
``` kotlin
enum class Color {
	RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET
}
```

``` kotlin
enum class Color(
	val r: Int,
	val g: Int,
	val b: Int
) {
	RED(255, 0, 0),
	ORANGE(255, 165, 0),
	YELLOW(255, 255, 0),
	GREEN(0, 255, 0),
	BLUE(0, 0, 255),
	INDIGO(75, 0, 130),
	VIOLET(238, 130, 238);

	fun rgb() = (r * 256 + g) * 256 + b
}
```

``` kotlin
when (color) {
	Color.RED -> "Richard"
	Color.ORANGE -> "Of"
	else -> "Unknown"
}

val result = when (color) {
	Color.RED -> "Richard"
	Color.ORANGE -> "Of"
	else -> "Unknown"
}
```

``` kotlin
// 인자없는 when
when {
	...
}
```

### 스마트 캐스트
``` kotlin
if (e is Num) {
	val n = e as Num // 불필요
}

if (e is Num) {
	val n = e // 스마트 캐스트 되어 Num 타입
}
```

### 이터레이션
``` kotlin
for (i in 1..100) {
	...
}

for (i in 100 downTo 1 step 2) {
	...
}

// 0 ~ size - 1
for (i in 0 until size) {
	...
}

// Map
for ((letter, binary) in binaryReps) {
	...
}
```

### 원소 검사
``` kotlin
fun recognize(c: Char) = when(c) {
	in '0'..'9' -> "It's a digit!"
	in 'a'..'z' -> "It's a letter!"
	else -> "I don't know..."
}
```
- Comparable 인터페이스를 구현한 클래스라면 범위를 만들 수 있다.

### 예외 처리
코틀린은 자바와 비슷하게 throw를 던질 수 있으며 호출하는 쪽에서 그 예외를 잡아 처리할 수 있다.
호출하는 곳에서 처리하지 않으면 예외가 처리될 때 까지 스택을 거슬러 올라간다.
``` kotlin
if (percentage !in 0..100) {
	throw IllegalArgumentException("...")
}
```
예외는 자바와 마찬가지로 try - catch - finally 절을 사용해 처리할 수 있다.
``` kotlin
try {
	// body
} catch (e: Exception) {
	//
} finally {
	//
}

// try - catch를 식으로 사용 가능
val number = try {
	return 0
} catch (e: Exception) {
	return -1
}
```
자바와 가장 큰 차이는 checked exception이 코틀린에는 존재하지 않는다.
그렇기 때문에 호출하는 쪽에서는 어떤 예외가 발생할 지 모른다는 점이 존재하기 때문에 Result 객체 등을 사용해서 처리하는것을 추천한다.
``` kotlin
fun processing() {
	try {
		client.call()
	} catch (e: Exception) {
		// 어떤 예외가 발생할 지 모르기때문에 예외에 대한 디테일 한 처리가 어려움
		log.error("Unknown Exception. message=${e.message}", e)
	}
}
```

``` kotlin
fun processing() {
	val result<?> = client.call()
	// after process
	if (!result.success()) {
		// error handling
		val error = result.error!!
	}
}

class Result<T> {
	val error: Error?,
	val data: T?

	fun success(): Boolean = Objects.isNull(error)
}
```
