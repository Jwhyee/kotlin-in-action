---
marp: true
theme: uncover
class: invert
---

# 3 장 - 함수 정의와 호출
--- 

## 코틀린에서 컬렉션 만들기

---

**코틀린이 자체 컬렉션을 제공하지 않는 이유는 뭘까?**

표준 자바 컬렉션을 활용하면 자바 코드와 상호작용하기가 훨씬 더 쉽기 때문이다. 때문에 자바에서 코틀린 함수를 호출하거나 코틀린에서 자바 함수를 호출할 때 자바와 코틀린 컬렉션을 서로 변환할 필요가 없다. 하지만 코틀린에서는 자바보다 더 많은 기능을 쓸 수 있다.

---

## 함수를 호출하기 쉽게 만들기

자바 컬렉션에는 default toSting 구현이 들어있다. 하지만 그 디폴트 toString의 출력 형식은 고정되어있고 우리에게 필요한 형식이 아닐 수도 있다.

```java
val list = listOf(1, 2, 3)
println(list)
// [1, 2, 3]
```

--- 

디폴트 구현과 달리 (1; 2; 3)처럼 원소 사이를 세미콜론으로 구분하고 괄호로 리스트를 둘러싸고 싶다면 어떻게 해야 할까?

```kotlin
fun <T> joinToString(
    collection: Collection<T>,
    separator: String,
    prefix: String,
    postfix: String
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}
```

---

어떤 타입의 값을 원소로 하는 컬렉션이든 처리할 수 있는 제네릭한 함수를 만들었고 선언 부분, 호출 부분에 대한 개선이 필요하다.

---


### Named arguments

코틀린으로 작성한 함수를 호출할 때 함수 인수 중 하나 이상의 이름을 지정할 수 있다.

호출 시 인자 중 어느 하나라도 이름을 명시하고 나면 혼동을 막기 위해 그 뒤에 오는 모든 인자는 이름을 꼭 명시해야 한다.

```kotlin
println(joinToString(list, separator = "; ", prefix = "(", postfix = ")"))
```

--- 

### **Default arguments**

코틀린에서는 함수 선언에서 파라미터의 디폴트 값을 지정할 수 있으므로 오버로드의 상당수를 피할 수 있다.

```kotlin
fun <T> joinToString(
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}
```

---

### 정적인 유틸리티 클래스 없애기: 최상위 함수와 프로퍼티

자바는 모든 코드를 클래스의 메소드로 작성해야한다. 실전에서는 어느 한 클래스에 포함시키기 어려운 코드가 많이 생기는데 일부 연산에서는 비슷하게 중요한 역할을 하는 클래스가 둘 이상 있을 수 있다.

코틀린에서는 이런 무의미한 클래스가 필요 없다.

함수와 마찬가지로 프로퍼티도 파일의 최상위 수준에 놓을 수 있다.

이런 프로퍼티의 값은 정적 필드에 저장된다.

---

### 메소드를 다른 클래스에 추가: 확장 함수와 확장 프로퍼티

기존 코드와 코틀린 코드를 자연스럽게 통합하는 것은 코틀린의 핵심 목표 중 하나다. 이런 기존 자바 API를 재작성하지 않고도 코틀린이 제공하는 여러 편리한 기능을 사용할 수 는 없을까?

**바로 확장 함수가 그런 역할을 해줄 수 있다.**

개념적으로 확장 함수는 단순하다. 확장 함수는 어떤 클래스의 멤버 메소드인 것처럼 호출할 수 있지만 그 클래스의 밖에 선언된 함수다. 문자열의 마지막 문자를 돌려주는 확장 메소드를 추가해보자

```kotlin
package strings
fun String.lastChar(): Char = this.get(this.length - 1)
```
---

확장 함수를 만들려면 추가하려는 함수 이름 앞에 그 함수가 확장할 클래스의 이름을 덧붙이기만 하면 된다. 클래스 이름을 수신 객체 타입(receiver type)이라 부르며, 확장 함수가 호출되는 대상이 되는 값(객체)을 수신 객체(receiver object)라고 부른다.

```kotlin
println("Kotlin".lastChar())

// String = 수신 객체 타입
// "Kotlin" = 수신 객체
```

확장 함수는 클래스 안에서 정의한 메소드와 달리 확장 함수 안에서는 클래스 내부에서만 사용할 수 있는 비공개(private) 멤버나 보호된(protected) 멤버를 사용할 수 없다.

---

### 임포트와 확장 함수

확장 함수를 사용하기 위해서는 그 함수를 다른 클래스나 함수와 마찬가지로 임포트해야만 한다. 확장 함수를 임포트 없이 사용한다면 동일한 이름의 확장 함수와 충돌할 수도 있기 때문에 임포트로 어떤 확장함수인지 명시해 주어야 한다.

```kotlin
import strings.lastChar // 명시적으로 사용
import strings.* // * 사용 가능
import strings.lastChar as last // as 키워드를 사용 가능
```

---

### **자바에서 확장 함수 호출**

자바에서 확장 함수를 사용하기도 편하다. 단지 정적 메소드를 호출하면서 첫 번째 인자로 수신 객체를 넘기기만 하면 된다. 다른 최상위 함수와 마찬가지로 확장 함수가 들어있는 자바 클래스 이름도 확장 함수가 들어있는 파일 이름에 따라 결정된다. 따라서 확장 함수를 StringUtil.kt 파일에 정의했다면 다음과 같이 호출할 수 있다.

```kotlin
char c = StringUtilKt.lastChar("java");
```

---


### **확장 함수로 유틸리티 함수 정의**

이제 joinToString 함수의 최종 버전을 만들자. 이제 이 함수는 코틀린 하이브러리가 제공하는 함수와 거의 같아졌다.

```kotlin
fun <T> Collection<T>.joinToString(
        separator: String = ", ",
        prefix: String = "",
        postfix: String = ""
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}

fun main(args: Array<String>) {
    val list = arrayListOf(1, 2, 3)
    println(list.joinToString(" "))
}
```

확장 함수는 단지 정적 메소드 호출에 대한 문법적인 편의일 뿐이다. 그래서 클래스가 아닌 더 구체적인 타입을 수신 객체 타입으로 지정할 수도 있다.

---

### 확장 함수는 오버라이드할 수 없다

확장 함수는 클래스의 일부가 아니다. 확장 함수는 클래스 밖에 선언된다. 이름과 파라미터가 완전히 같은 확장 함수를 기반 클래스와 하위 클래스에 대해 정의해도 실제로는 확장 함수를 호출할 때 수신 객체로 지정한 변수의 정적 타입에 의해 어떤 확장함수가 호출될지 결정되지, 그 변수에 저장된 객체의 동적인 타입에 의해 확장 함수가 결정되지 않는다.

<aside>
💡 어떤 클래스를 확장한 함수와 그 클래스의 멤버 함수의 이름과 시그니처가 같다면 확장 함수가 아니라 멤버 함수가 호출된다(멤버 함수의 우선순위가 더 높다). 클래스의 API를 변경할 경우 항상 이를 염두에 둬야 한다.

</aside>

---

### **확장 프로퍼티**

확장 프로퍼티를 사용하면 기존 클래스 객체에 대한 프로퍼티 형식의 구문으로 사용할 수 있는 API를 추가할 수 있다. 프로퍼티라는 이름으로 불리기는 하지만 상태를 저장할 적절한 방법이 없기 때문에 실제로 확장 프로퍼티는 아무 상태도 가질 수 없다.

```kotlin
val String.lastChar: Char
		get() = get(length -1)
```

뒷받침하는 필드가 없어서 기본 게터 구현을 제공할 수 없으므로 최소한 게터는 꼭 정의를 해야 한다. 마찬가지로 초기화 코드에서 계산한 값을 담을 장소가 전혀 없으므로 초기화 코드도 쓸 수 없다.

```kotlin
var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value: Char) {
        this.setCharAt(length - 1, value)
    }

fun main(args: Array<String>) {
    println("Kotlin".lastChar)
    val sb = StringBuilder("Kotlin?")
    sb.lastChar = '!'
    println(sb)
}
```

---

### 유용한 확장함수

https://medium.com/@summitkumar/10-useful-kotlin-extension-functions-for-simplifying-your-code-ff68a2a44bc7

샘플 소개

```kotlin
fun <T> Collection<T>?.notEmpty(): Boolean {
    return this?.isNotEmpty() == true
}

val list: List<Int> = emptyList()
if (list.notEmpty()) {
    // Code here will only be executed if list is not empty
}
```

---

## 컬렉션 처리: 가변 길이 인자, 중위 함수 호출, 라이브러리 지원

### **자바 컬렉션 API 확장**

코틀린 컬렉션은 자바와 같은 클래스를 사용하지만 더 확장된 API를 제공한다고 했다. 그 이유는 last와 max 등 모두 확장 함수로 정의 되었던 것이다.

코틀린 표준 라이브러리를 모두 다 알 필요는 없다. 컬렉션이나 다른 객체에 대해 사용할 수 있는 메소드나 함수가 무엇인지 궁금할 때마다 IDE의 코드 완성 기능을 통해 그런 메소드나 함수를 살펴볼 수 있다.

---

### 가변 인자 함수: 인자의 개수가 달라질 수 있는 함수 정의

가변 길이 인자(varargs)는 메소드를 호출할 때 원하는 개수만큼 값을 인자로 넘기면 자바 컴파일러가 배열에 그 값들을 넣어주는 기능이다. 코틀린의 가변 길이 인자도 자바와 비슷하다. 다만 문법이 조금 다르다. 타입 뒤에 ...를 붙이는 대신 코틀린에서는 파라미터 앞에 varag 변경자를 붙인다.

```kotlin
public fun <T> listOf(vararg elements: T): List<T> = if (elements.size > 0) elements.asList() else emptyList()

fun main(args: Array<String>) {
    val list = listOf("one", "two", "eight")
}
```

이미 배열에 들어있는 원소를 가변 길이 인자로 넘길 때도 코틀린과 자바 구문이 다르다. 자바에서는 배열을 그냥 넘기면 되지만 코틀린에서는 배열을 명시적으로 풀어서 배열의 각 원소가 인자로 전달되게 해야 한다. 기술적으로는 스프레드(spread) 연산자가 그런 작업을 해준다.

```kotlin
fun main(args: Array<String>) {
    val list = listOf("args: ", *args)
}
```

---

### 값의 쌍 다루기: 중위 호출과 구조 분해 선언

맵을 만들려면 mapOf 함수를 사용한다.

```kotlin
val map = mapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
```

여기서 to라는 단어는 코틀린 키워드가 아니다. 이 코드는 중위 호출이라는 특별한 방식으로 to라는 일반 메소드를 호출한 것이다. 중위 호출 시에는 수신 객체와 유일한 메소드 인자 사이에 메소드 이름을 넣는다.

---

```kotlin
1.to("one") // "to" 메소드를 일반적인 방식으로 호출함
1 to "one" // "to" 메소드를 중위 호출 방식으로 호출함
```

함수(메소드)를 중위 호출에 사용하게 허용하고 싶으면 infix 변경자를 함수(메소드) 선언 앞에 추가해야 한다.

```kotlin
infix fun Any.to(other: Any) = Pair(this, other)
```
---

to 함수는 Pair의 인스턴스를 반환한다.

구조 분해 선언(destructuring declaration)으로 Pair의 내용으로 두 변수를 즉시 초기화할 수 있다.

```kotlin
val (number, name) = 1 to "one"
```

---

### 문자열과 정규식 다루기

코틀린 문자열은 자바 문자열과 같다. 특별한 변환도 필요 없고 자바 문장열을 감싸는 별도의 래퍼도 생기지 않는다.

코틀린은 다양한 확장 함수를 제공함으로써 표준 자바 문자열을 더 즐겁게 다루게 해준다. 또한 혼동이 야기될 수 있는 일부 메소드에 대해 더 명확한 코틀린 확장 함수를 제공함으로써 프로그래머의 실수를 줄여준다.

---

### 문자열 나누기

코틀린에서는 자바의 split 대신에 여러 가지 다른 조합의 파라미터를 받는 split 확장 함수를 제공함으로써 혼동을 야기하는 메소드를 감춘다.

```kotlin
println("12.345-6.A".split("\\\\.|-".toRegex())) // 정규식을 명시적으로 만든다.
```

```kotlin
println("12.345-6.A".split(".","-")) // 여러 구분 문자열을 지정한다. 
[12, 345, 6, A]
```

---

### 정규식과 3중 따옴표로 묶은 문자열

```kotlin
fun parsePath(path: String) {
    val directory = path.substringBeforeLast("/")
    val fullName = path.substringAfterLast("/")

    val fileName = fullName.substringBeforeLast(".")
    val extension = fullName.substringAfterLast(".")

    println("Dir: $directory, name: $fileName, ext: $extension")
}

fun main(args: Array<String>) {
    parsePath("/Users/yole/kotlin-book/chapter.adoc")
}

// Dir: /Users/yole/kotlin-book, name: chapter, ext: adoc
```

path에서 처음부터 마지막 슬래시 직전까지의 부분 문자열은 파일이 들어있는 디렉터리경로다. path에서 마지막 마침표 다음부터 끝까지의 부분 문자열은 파일 확장자다.

코틀린에서는 정규식을 사용하지 않고도 문자열을 쉽게 파싱할 수 있다. 정규식은 강력하기는 하지만 나중에 알아보기 힘든 경우가 많다. 정규식이 필요할 때는 코틀린 라이브러리를 사용하면 더 편하다.

---


```kotlin
fun parsePath(path: String) {
    val regex = """(.+)/(.+)\\.(.+)""".toRegex()
    val matchResult = regex.matchEntire(path)
    if (matchResult != null) {
        val (directory, filename, extension) = matchResult.destructured
        println("Dir: $directory, name: $filename, ext: $extension")
    }
}
```

3중 따옴표 문자열에서는 역슬래시(\)를 포함한 어떤 문자도 이스케이프할 필요가 없다.

우선 정규식을 만들고 path에 매치시킨다. 성공하면 그룹별로 분해한다.

---

### 여러 줄 3중 따옴표 문자열(**String literals - Multiline strings)**

3중 따옴표 문자열을 사용하면 줄 바꿈이 들어있는 프로그램 텍스트를 쉽게 문자열로 만들 수 있다.

```kotlin
val text = """
    |Tell me and I forget.
    |Teach me and I remember.
    |Involve me and I learn.
    |(Benjamin Franklin)
    """.trimMargin()

//Tell me and I forget.
//Teach me and I remember.
//Involve me and I learn.
//(Benjamin Franklin)
```

---

### 코드 다듬기: 로컬 함수와 확장

코틀린에서는 함수에서 추출한 함수를 원 함수 내부에 중첩시킬 수 있다. 그렇게 하면 문법적인 부가 비용을 들이지 않고도 깔끔하게 코드를 조작할 수 있다.

```kotlin
fun saveUser(user: User) {

    fun validate(user: User,
                 value: String,
                 fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException(
                "Can't save user ${user.id}: empty $fieldName")
        }
    }

    validate(user, user.name, "Name")
    validate(user, user.address, "Address")
}
```

---

또한 로컬함수는 자신이 속한 바깥 함수의 모든 파라미터와 변수를 사용할 수 있다 (****Closure)****

```kotlin
fun saveUser(user: User) {
    fun validate(value: String, fieldName: String) { // user 파라미터를 중복 사용하지 않는다. 
        if (value.isEmpty()) {
            throw IllegalArgumentException(
                "Can't save user ${user.id}: " + // 바깥 함수의 파라미터에 직접 접근할 수 있다. 
                    "empty $fieldName")
        }
    }

    validate(user.name, "Name")
    validate(user.address, "Address")

}
```

---

검증 로직을 확장함수로 만들어서 더 개선할 수 있다.

```kotlin
fun User.validateBeforeSave() {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException(
               "Can't save user $id: empty $fieldName")
        }
    }

    validate(name, "Name")
    validate(address, "Address")
}

fun saveUser(user: User) {
    user.validateBeforeSave()

}
```