# 6장 코틀린 타입 시스템

자바와 비교하면 코틀린의 타입 시스템은 코드의 가독성을 향상시키는 데 도움이 되는 몇 가지 특성을
새로 제공한다.

그런 특성으로는 nullable type과 readonly 컬렉션이 있다.
또한 코틀린은 자바 타입 시스템에서 불필요하거나 문제가 되던 부분을 제거했다.

## 널 가능성
널 가능성은 NullPointerException 오류를 피할 수 있게 돕기 위한 코틀린 타입 시스템의 특성이다.
코틸린을 비롯한 최신 언어에서 null에 대한 접근 방법은 가능한 한 이 문제를 실행 시점에서 컴파일 시점으로
옮기는 것이다. 널이 될 수 있는지 여부를 타입 시스템에 추가함으로써 컴파일러가 여러 가지 오류를 컴파일 시 미리 감지해서
실행 시점에 발생할 수 있는 예외의 가능성을 줄일 수 있다.

### 널이 될 수 있는 타입
코틀린과 자바의 첫 번째이자 가장 중요한 차이는 코틀린 타입 시스템이 널이 될 수 있는 타입을 명시적으로 지원한다는 점이다.
코틀린에서는 타입 이름 뒤에 물음표를 넣어 널이 될 수 있는 타입을 표시한다.

```kotlin
fun strLen(s: String) = s.length

strLen(null) // 컴파일 오류
```

코틀린에서는 널이 될 수 있는 타입을 명시적으로 지원하므로 널이 될 수 있는 값을 다루는 코드를 작성할 때
컴파일러가 도와준다. 널이 될 수 있는 값을 다루는 코드는 널이 될 수 없는 값을 다루는 코드보다 훨씬 복잡하다.

이 함수가 널이 될 수 있는 값을 다루도록 만들려면 String 대신 String?을 사용해야 한다.

```kotlin
fun strLenSafe(s: String?): Int =
    if (s != null) s.length else 0
    
strLenSafe(null) // 0
``` 

널이 될 수 있는 타입의 변수가 있다면 그에 대해 수행할 수 있는 연산이 제한된다.
예를 들어 널이 될 수 있는 타입인 변수에 대해 `변수.메소드()`처럼 메소드를 직접 호출할 수는 없다.

```kotlin
fun strLenSafe(s: String?): Int = s.length // 컴파일 오류
```

널이 될 수 있는 값을 널이 될 수 없는 타입의 변수에 대입할 수 없다.

```kotlin
val x: String? = null
val y: String = x // 컴파일 오류
```

널이 될 수 있는 타입의 값을 널이 될 수 없는 타입의 파라미터를 받는 함수에 전달할 수 없다.

```kotlin
strLenSafe(x) // 컴파일 오류
```

이렇게 제약이 많다면 널이 될 수 있는 타입의 값으로 대체 뭘 할 수 있을까? 가장 중요한 일은 바로
null과 비교하는 것이다. 일단 null과 비교하고 나면 컴파일러는 그 사실을 기억하고 null이 아님이
확실한 영역에서는 해당 값을 널이 될 수 없는 타입의 값처럼 사용할 수 있다.

```kotlin
fun strLenSafe(s: String?): Int =
    if (s != null) s.length else 0

fun main(args: Array<String>) {
    val x: String? = null
    println(strLenSafe(x))
    println(strLenSafe("abc"))
}
```

### 타입의 의미
타입의 의미는 분류로 타입은 어떤 값들이 가능한지와 그 타입에 대해 수행할 수 있는 연산의 종류를 결정한다.

> ### NullPointerException 오류를 다루는 다른 방법
> 자바에도 NullPointerException 문제를 해결하는 데 도움을 주는 도구가 있다. 예를 들어
> 애노테이션을 사용해 값이 널이 될 수 있는지 여부를 표시(@Nullable, @NotNull)하기도 한다.
> 이런 애노테이션을 활용해 NullPointerExcepion이 발생할 수 있는 위치를 찾아주는 도구가 있다.
> 
> 하지만 그런 도구는 표준 자바 컴파일 절차의 일부가 아니기 때문에 일관성 있게 적용된다는 보장을 할 수 없다.
> 또한 오류가 발생할 위치를 정확하게 찾기 위해 라이브러리를 포함하는 모든 코드베이스에 애노테이션을 추가하는 일도
> 쉽디는 않다.
> 
> 이 문제를 해결하는 다른 방법은 null 값을 코드에서 절대로 쓰지 않는 것이다. null 대신 자바8에
> 추가된 Optional<T> 타입을 사용하는 방법도 있다. 하지만 Optional<T>는 코드가 더 지저분해지고
> 래퍼가 추가됨에 따라 실행 시점에 성능이 저하되며 전체 에코시스템에서 일관성 있게 활용하기 어렵다.

### 안전한 호출 연산자: ?.
안전한 호출 연산자를 사용하면 null이 될 수 있는 수신 객체를 다룰 때 유용하다. 호출하려는 값이
null이 아니라면 ?.은 일반 메소드 호출처럼 작동한다. 호출하려는 값이 null이면 이 호출은 무시되고
null이 결과 값이 된다.

```kotlin
fun printAllCaps(s: String?) {
    val allCaps: String? = s?.toUpperCase()
    println(allCaps)
}

printAllCaps("abc") // ABC
printAllCaps(null) // null
```

메소드 호출뿐 아니라 프로퍼티를 읽거나 쓸 때도 안전한 호출을 사용할 수 있다.

```kotlin
class Employee(val name: String, val manager: Employee?)

fun managerName(employee: Employee): String? = employee.manager?.name

val ceo = Employee("Da Boss", null)
val developer = Employee("Bob Smith", ceo)

println(managerName(developer)) // Da Boss
println(managerName(ceo)) // null
```

객체 그래프에서 널이 될 수 있는 중간 객체가 여럿 있다면 한 식 안에서 안전한 호출을 연쇄해서
함께 사용하면 편할 때가 자주 있다. 예를 들어 어떤 사람에 대한 정보와 그 사람이
다니는 회사에 대한 정보, 그리고 그 회사의 주소에 대한 정보를 각각 다른 클래스로 표현한다고 가정하자.
회사나 주소는 모두 생각 가능하다.

```kotlin
class Address(val streetAddress: String, val zipCode: Int,
              val city: String, val country: String)

class Company(val name: String, val address: Address?)

class Person(val name: String, val company: Company?)

fun Person.countryName(): String {
    val country = this.company?.address?.country
    return if (country != null) country else "Unknown"
}

val person = Person("Dmitry", null)
println(person.countryName()) // Unknown
```

널 검사가 들어간 호출이 연달아 있는 경우르 자바 코드에서 자주 볼 수 있다.
하지만 코틀린에서는 훨씬 간결하게 널 검사를 할 수 있다.

### 엘비스 연산자: ?:
코틀린은 null 대신 사용할 디폴트 값을 지정할 때 편리하게 사용할 수 있는 연산자를 제공한다.

```kotlin
fun foo(s: String?) {
    val t: String = s ?: ""
}
```

엘비스 연산자는 피연산자가 null이 아니면 그 값을 그대로 쓰고, null이면 연산자 우측의 값을 사용한다.

엘비스 연산자는 널이 될 수 있는 값을 널이 될 수 없는 타입의 변수에 대입할 때 유용하다.

```kotlin
fun strLenSafe(s: String?): Int = s?.length ?: 0
```

코틀린에서는 `return`이나 `throw`등의 연산도 식이다. 따라서 엘비스 연산자의 우항에
`return`이나 `throw`를 넣을 수 있다. 이런 패턴은 함수의 전제 조건을 검사하는 경우 특히 유용하다.

```kotlin
class Address(val streetAddress: String, val zipCode: Int,
              val city: String, val country: String)

class Company(val name: String, val address: Address?)

class Person(val name: String, val company: Company?)

fun Person.countryName(): String {
    val country = this.company?.address?.country ?: throw IllegalArgumentException("No address")
    return country
}

```

### 안전한 캐스트: as?

2장에서 코틀린 타입 캐스트 연산자인 as에 대해 살펴봤다. 자바 타입 캐스트와 마찬가지로
as도 캐스트가 실패하면 ClassCastException을 던진다. 하지만 as?는 캐스트에 실패하면 null을
돌려준다.

```kotlin
class Person(val firstName: String, val lastName: String) {
    override fun equals(o: Any?): Boolean {
        val otherPerson = o as? Person ?: return false // as?를 사용해 캐스트를 시도하고 실패하면 false를 반환한다.

        return otherPerson.firstName == firstName && // 여기서는 Person 타입으로 캐스트되었으므로
                otherPerson.lastName == lastName // Person의 프로퍼티에 접근할 수 있다.
    }

    override fun hashCode(): Int =
        firstName.hashCode() * 37 + lastName.hashCode()
}

val p1 = Person("Dmitry", "Jemerov")
val p2 = Person("Dmitry", "Jemerov")
println(p1 == p2) // true
println(p1.equals(42)) // false
```


이 패턴을 사용하면 파라미터로 받은 값이 원하는 타입인지 쉽게 검사하고 캐스트할 수 있고,
타입이 맞지 않으면 쉽게 `false`를 반환할 수 있다. 이 모든 동작을 한 식으로 해결 가능하다.

### 널 아님 단언: !!
not-null assertion은 코틀린에서 널이 될 수 있는 타입의 값을 다룰 때 사용할 수 있는 도구 중에서
가장 단순하면서도 무딘 도구다. 이 연산자는 피연산자가 null이 아님을 단언하며, null이라면
NullPointerException을 던진다.

```kotlin
fun ignoreNulls(s: String?) {
    val sNotNull: String = s!!
    println(sNotNull.length)
}
   
ignoreNulls(null) // NPE
```

s가 null이면 함수 안에서 어떤 일이 벌어질까? 예외(NullPointerException)를 던지는 일
외에 코틀린이 택할 수 있는 대안이 별로 없다. 근본적으로 !!는 컴파일러에서 "나는 이 값이 null이
아님을 잘 알고 있다 내가 잘못 생각했다면 에외가 발생해도 감수하겠다"라고 말하는 것과 같다.

기억해야만 하는 함정이 한 가지 더 있다. !!를 널에 대해 사용해서 발생하는 예외의 스택 트레이스에는
어떤 파일의 몇 번째 줄인지에 대한 정보는 들어있지만 어떤 식에서 예외가 발생했는지에 대한 정보는 들어있지 않다.
어떤 값이 널이었는지 확실히하기 위해 여러 !! 단언문을 한 줄에 함께 쓰는 일을 피하라.

## let 함수
`let` 함수를 사용하면 널이 될 수 있는 식을 더 쉽게 다룰 수 있다. `let` 함수를 안전한
호출 연산자와 함께 사용하면 원하는 식을 평가해서 결과가 널인지 검사한 다음에 그 결과를 변수에 넣는 작업을 간단한
식을 사용해 한꺼번에 처리할 수 있다.

```kotlin
fun sendEmailTo(email: String) {
    println("Sending email to $email")
}

fun main(args: Array<String>) {
    var email: String? = "
    email?.let { sendEmailTo(it) }
}
```

`let` 함수는 자신의 수신 객체를 인자로 전달받은 람다에게 넘긴다. 널이 될 수 있는 값에 대해
안전한 호출 구문을 사용해 let을 호출하되 널이 될 수 없는 타입을 인자로 받는 람다를 `let`에 전달한다.

이렇게 하면 널이 될 수 있는 값을 널이 될 수 없는 타입의 값으로 바꿔서 람다에 전달하게 된다.

### 나중에 초기화할 프로퍼티
코틀린에서 클래스 안의 널이 될 수 없는 프로퍼티를 생성자 안에서 초기화 하지 않고 특별한 메소드 안에서 초기화
할 수는 없다.

게다가 프로퍼티 타입이 널이 될 수 없는 타입이라면 반드시 널이 아닌 값으로 그 프로퍼티를 초기화해야 한다.
그런 초기화 값을 제공할 수 없으면 널이 될 수 있는 타입을 사용할 수밖에 없다.
하지만 널이 될 수 있는 타입을 사용하면 모든 프로퍼티 접근에 널 검사는 넣거나 !! 연산자를 써야한다.

이런 문제를 해결하는 방법은 프로퍼티를 널이 될 수 있는 타입으로 선언하고 생성자 안에서 초기화하는 대신
나중에 초기화할 수 있도록 만들어주는 `lateinit` 변경자를 사용하는 것이다.

```kotlin
class MyService {
    fun performAction(): String = "foo"
}

class MyTest {
    private lateinit var myService: MyService

    @Before fun setUp() {
        myService = MyService()
    }

    @Test fun testAction() {
        Assert.assertEquals("foo", myService.performAction())
    }
}
```

나중에 초기화할 프로퍼티는 var로 선언해야 한다. 또한 널이 될 수 없는 타입이어야 한다.
나중에 초기화할 프로퍼티를 선언할 때는 생성자에서 초기화하지 않아도 된다. 하지만
나중에 초기화할 프로퍼티를 사용하기 전에 반드시 초기화해야 한다. 그렇지 않으면
`lateinit` 프로퍼티에 접근하면 `UninitializedPropertyAccessException`이 발생한다.

## 널이 될 수 있는 타입 확장
널이 될 수 있는 타입에 확장 함수를 정의하면 null 값을 다루는 강력한 도구로 활용할 수 있다.
예를 들어 널이 될 수 있는 타입에 대해 `isEmptyOrNull`이라는 확장 함수를 정의해보자.

```kotlin
fun String?.isEmptyOrNull(): Boolean =
    this == null || this.isEmpty()
```

이 확장 함수를 사용하면 널이 될 수 있는 타입의 변수에 대해 안전하게 호출할 수 있다.

### 타입 파라미터의 널 가능성
코틀린에서는 타입 파라미터에 대한 널 가능성을 명시적으로 지정할 수 있다. 타입 파라미터에 대한 널 가능성을
지정하면 그 타입 파라미터를 사용하는 모든 타입에 대해 널 가능성을 지정한 것과 같은 효과가 있다.

```kotlin
fun <T> printHashCode(t: T) {
    println(t?.hashCode()) // t는 널이 될 수 있다.
}

printHashCode(null) // T의 타입은 Any?이다.
```

`printHashCode` 호출에서 타입 파라미터 T에 대해 추론한 타입은 널이 될 수 있는 Any?다.
t 파라미터의 타입 이름 T에는 물음표가 붙어있지 않지만 t는 null을 받을 수 있따.

타입 파라미터가 널이 아님을 확실히 하려면 널이 될 수 없는 타입 상한(upper bound)을 지정해야 한다.

```kotlin
fun <T: Any> printHashCode(t: T) { // T는 널이 될 수 없다.
    println(t.hashCode())
}

printHashCode(null) // 컴파일 오류
```

### 널 가능성과 자바
코틀린에서는 널이 될 수 있는 타입과 널이 될 수 없는 타입을 구분해서 사용한다.
하지만 자바에서는 모든 타입이 널이 될 수 있다. 자바에서는 널이 될 수 있는 타입을
사용할 때마다 그 타입이 널이 될 수 있는지 여부를 검사해야 한다.

자바 코트에도 애노테이션으로 널 가능성 정보가 있다. 이런 정보가 코드에 있으면 코틀린도 그 정보를 활용한다.
따라서 자바의 @Nullable String은 코틀린 쪽에서 볼 때 `String?`와 같고, 자바의 `@NotNull String`은 코틀린 쪽에서 볼 때 `String`과 같다.

코틀린은 여러 널 가능성 애노테이션을 알아본다. JSR-305 표준, 안드로이드, JetBrains 도구들이 지원하는 애노테이션 등이
코틀린이 이해할 수 있는 널 가능성 애노테이션들이다. 이런 널 가능성 애노테이션이 소스코드에 없는 경우는 더 흥미롭다.
그런 경우 자바의 타입은 코틀린의 플랫폿 타입이 된다.

### 플랫폼 타입
플랫폼 타입은 코틀린이 널 관련 정보를 알 수 없는 타입을 말한다. 그 타입을 널이 될 수 있는 타입으로 처리해도 되고
널이 될 수 없는 타입으로 처리해도 된다. 이는 자바와 마찬가지로 플랫폼 타입에 수행하는 모든 연산에 대한 책임은 온전히 여러분에게 있다는 뜻이다.

컴파일러는 모든 연산을 허용한다. 어떤 플랫폼 타입의 값이 널이 아님을 알고 있다면 아무 널 검사 없이
그 값을 직접 사용해도 된다. 자바와 마찬가지로 여러분이 틀렸다면 NullPointerException이 발생한다.

```kotlin
fun yellAt(person: Person) {
    println(person.name.toUpperCase() + "!!!")
}

yellAt(Person(null))
// java.lang.IllegalArgumentException: Parameter specified as non-null is null: method toUpperCase, parameter $receiver

```

여기서 `NullPointerException`이 아니라 `toUppercase()`가 수신 객체로 널을 받을 수 없단는 더 자세한
예외가 발생함에 유의하라.

실제로 코틀린 컴파일러는 공개(public) 가시성인 코틀린 함수의 널이 아닌 타입인 파라미터와 수신 객체에 대한 널 검사를 추가해준다.
따라서 공개 가시성 함수에 널 값을 사용하면 즉시 예외가 발생한다.

이런 파라미터 값 검사는 함수 내부에서 파라미터를 사용하는 시점이 아니라 함수 호출 시점에 이뤄진다.
따라서 잘못된 인자로 함수를 호출해도 그 인자가 여러 함수에 전달돼 전혀 엉뚱한 위치에서 예외가 발생하지 않고 가능한 한 빨리
예외가 발생하기 때문에 예외가 발생해도 더 원인을 파악할 수 있다.

물론 `getName()`의 반환 타입을 널이 될 수 있는 타입으로 해석해서 널 안전성 연산을 활용해도 된다.

```kotlin
fun yellAtSafe(person: Person) {
    println((person.name ?: "Anyone").toUpperCase() + "!!!")
}

yellAtSafe(Person(null)) // ANYONE!!!
```

이 예제에서는 널 값을 제대로 처리하므로 실행 시점에 예외가 발생하지 않는다.

자바 API를 다룰 때는 조심해야 한다. 대부분의 라이브러리는 널 관련 애노테이션을 쓰지 않는다.
따라서 모든 타입을 널이 아닌 것처럼 다루기 쉽지만 그렇게 하면 오류가 발생할 수 있다.
오류를 피하려면 사용하려는 자바 메소드의 문서를 자세히 살펴봐서 그 메소드가 널을 반환할지 알아내고
널을 반환하는 메소드에 대한 널 검사를 추가해야 한다.

> ### 코틀린이 왜 플랫폼 타입을 도입했는가?
> 모든 자바 타입을 널이 될 수 있는 타입으로 다루면 더 안전하지 않을까? 물론 그래도 되지만
> 모든 타입을 널이 될 수 있는 타입으로 다루면 결코 널이 될 수 없는 값에 대해서도 불필요한
> 널 검사가 들어간다.

### 상속
코틀린에서 자바 메소드를 오버라이드할 때 그 메소드의 파라미터와 반환 타입을 널이 될 수 있는 타입으로
선언할지 널이 될 수 없는 타입으로 선언할지 결정해야 한다. 예를 들어 자바의 `StringProcessor`를 살펴보자.

```java
public interface StringProcessor {
    void process(String value);
}
```

코틀린 컴파일러는 다음과 같은 두 구현을 다 받아들인다.

```kotlin
class StringPrinter : StringProcessor {
    override fun process(value: String) {
        println(value)
    }
}

class NullableStringPrinter : StringProcessor {
    override fun process(value: String?) {
        if (value != null) {
            println(value)
        }
    }
}
```

자바 클래스나 인터페이스를 코틀린에서 구현할 경우 널 가능성을 제대로 처리하는 일이 중요하다.
구현 메소드를 다른 코틀린 코드가 호출할 수 있으므로 코틀린 컴파일러는 널이 될 수 없는 타입으로
선언한 모든 파라미터에 대해 널이 아님을 검사하는 단언문을 만들어준다. 
자바 코드가 그 메소드에게 널 값을 넘기면 이 단언문이 발동돼 예외가 발생한다.

## 코틀린의 원시 타입

코틀린은 자바와 마찬가지로 원시 타입을 제공한다. 하지만 코틀린은 원시 타입을 클래스로 감싸서
표현한다. 따라서 코틀린에서는 원시 타입에 대한 메소드를 호출할 수 있다.

### 원시 타입: Int, Boolean 등
코틀린은 원시 타입과 래퍼 타입을 구분하지 않으므로 항상 같은 타입을 사용한다.
다음 예제는 정수를 표현하는 Int 타입을 사용한다.

```kotlin
fun showProgress(progress: Int) {
    val percent = progress.coerceIn(0, 100)
    println("We're ${percent}% done!")
}

showProgress(146) // We're 100% done!
```

래퍼타입을 따로 구분하지 않으면 편리하다. 더 나아가 코틀린에서는 숫자 타입 등 원시 타입의 값에 대해
메소드를 호출할 수 있다.

코틀린은 실행 시점의 숫자 타입을 가능한 한 효율적인 방식으로 처리한다. 대부분의 경우 코틀린의 Int 타입은
자바 int 타입으로 컴파일 된다. 이런 컴파일이 불가능한 경우는 컬렉션과 같은 제네릭 클래스를 사용하는 경우 뿐이다.

### 널이 될 수 있는 원시 타입: Int?, Boolean? 등
null 참조를 자바의 참조 타입의 변수에만 대입할 수 있기 때문에 널이 될 수 있는 코틀린 타입은
자바 원시 타입으로 표현할 수 있다. 따라서 코틀린에서 널이 될 수 있는 원시타입을 사용하면
그 타입은 자바의 래퍼 타입으로 컴파일된다.

```kotlin
data class Person(val name: String,
                  val age: Int? = null) {
    fun isOlderThan(other: Person): Boolean? {
        if (age == null || other.age == null)
            return null
        return age > other.age
    }
}

fun main(args: Array<String>) {
    println(Person("Sam", 35).isOlderThan(Person("Amy", 42))) // false
    println(Person("Sam", 35).isOlderThan(Person("Jane"))) // null
}
```

여기서 널 가능성 관련 규칙을 어떻게 적용하는가 살펴보라. 널이 될 가능성이 있으므로 
`Int?` 타입의 두 값을 직접 비교할 수는 없다. 먼저 두 값이 모두 널이 아닌지 검사해야한다.
컴파일러는 널 검사를 마친 다음에야 두 값을 일반적인 값처럼 다루게 허용한다.

`Person` 클래스에 선언된 `age` 프로퍼티의 값은 `java.lang.Integer`로 저장된다.

하지만 그런 자세한 사항은 자바에서 가져온 클래스를 다룰 때만 문제가 된다. 코틀린에서
적절한 타입을 찾으려면 그 변수나 프로퍼티에 널이 들어갈 수 있는지만 고민하면 된다.

### 숫자 변환
코틀린과 자바의 가장 큰 차이점 중 하나는 숫자를 변환하는 방식이다. 코틀린은 한
타입의 숫자를 다른 타입의 숫자로 자동 변환하지 않는다. 결과 타입이 허용하는 숫자의
범위가 원리 타입의 범위보다 넓은 경우조차도 자동 변환은 불가능하다. 예를 들어
코틀린 컴파일러는 다음 코드를 거부한다.

```kotlin
val i = 1
val l: Long = i // 컴파일 오류
```

대신 직접 변환 메소드를 호출해야 한다.

```kotlin
val i = 1
val l: Long = i.toLong()
```

코틀린은 모든 원시타입(Boolean을 제외한)에 대해 다음 변환 메소드를 제공한다.


### Any, Any?: 최상위 타입
자바에서 `Object`가 클래스 계층의 최상위 타입이듯 코틀린에서는 `Any`가 최상위 타입이다.
하지만 자바에서는 참조 타입만 `Object`를 정점으로 하는 타입 계층에 포함되며, 원시 타입은
그런 계층에 들어있지 않다. 이는 자바에서는 `Object` 타입의 객체가 필요할 경우 `int`와 같은 원시타입을
`java.lang.Integer` 같은 래퍼 타입으로 감싸야만 한다는 뜻이다.

하지만 코틀린에서는 `Any`가 `Int`등의 원시 타입을 포함한 모든 타입의 조상 타입이다.

자바와 마찬가지로 코틀리넹서도 원시 타입을 `Any` 타입의 변수에 대입하면 자동으로 값을 객체로 감싼다.

```kotlin
fun main(args: Array<String>) {
    val answer: Any = 42
    println(answer)
}
```

내부에서 `Any` 타입은 `java.lang.Object`에 대응한다. 자바 메소드에서 `Object` 타입을
인자로 받거나 반환하면 코틀린에서는 `Any`로 그 타입을 취급한다. 코틀린 함수가 `Any` 타입을
사용하면 자바 바이트 코드에서는 `Object`로 컴파일 된다.

### Unit 타입: 코틀린의 void
코틀린 `Unit` 타입은 자바 `void`와 같은 기능을 한다. 관심을 가질 만한 내용을 전혀
반환하지 않는 함수의 반환 타입으로 `Unit`을 사용한다.

```kotlin
fun f(): Unit { // 반환 타입이 Unit인 함수는 반환 타입을 생략할 수 있다.
    println("f()")
}

fun f2() { // 반환 타입이 Unit인 함수는 반환 타입을 생략할 수 있다.
    println("f2()")
}
```

코틀린의 `Unit`이 자바 `void`와 다른 점은 무엇일까?

`Unit`은 모든 기능을 갖는 일반적인 타입이며, `void`와 달리 `Unit`은 타입 인자로 쓸 수 있다.
`Unit` 타입에 속한 값은 단 하나 뿐이며, 그 이름도 `Unit`이다. 

`Unit`타입의 함수는 `Unit`값을 묵시적으로 반환한다. 이 두 특성은 제네릭 파라미터를 반환하는 함수를
오버라이드하면서 반환 타입으로 Unit을 쓸 때 유용하다.

```kotlin
interface Processor<T> {
    fun process(): T
}

class NoResultProcessor: Processor<Unit> {
    override fun process() { // 반환 타입이 Unit인 함수는 반환 타입을 생략할 수 있다.
        // do something
    } // 여기서 return을 명시할 필요가 없다.
}
```

### Nothing 타입: 절대 반환되지 않는 함수의 반환 타입
코틀린에서는 결코 성공적으로 값을 돌려주는 일이 없으므로 '반환 값'이라는 개념 자체가 의미 없는
함수가 일부 존재한다. 예를 들어 테스트 라이브러리들은 `fail`이라는 함수를 제공하는 경우가 많다.
`fail`은 특별한 메시지가 들어있는 예외를 던져서 현재 테스트를 실패시킨다.

다른 예로 무한 루프를 도는 함수도 결코 값을 반환하지 않으며, 정상적으로 끝나지 않는다.

```kotlin
fun fail(message: String): Nothing {
    throw IllegalStateException(message)
}
```

`Nothing` 타입은 아무 값도 포함하지 않는다. 따라서 `Nothing`은 함수의 반환 타입이나
반환 타입으로 쓰일 타입 파라미터로만 쓸 수 있다.

`Nothing`을 반환하는 함수를 엘비스 연산자의 우항에 사용해서 전제 조건을 검사할 수 있다.

```kotlin
fun main(args: Array<String>) {
    val address = Company("JetBrains", null).address
        ?: fail("No address")
    println(address.city)
}
```

컴파일러는 `Nothing`이 반환 타입인 함수가 결코 정상 종료되지 않음을 알고 그 함수를 호출하는
코드를 분석할 때 사용한다.

## 컬렉션과 배열

### 널 가능성과 컬렉션

변수 타입 뒤에 ?를 붙이면 그 변수에 널을 저장할 수 있다는 뜻인 것처럼 타입 인자로 쓰인
타입에도 같은 표시를 사용할 수 있다.

```kotlin
fun readNumbers(reader: BufferedReader): List<Int?> {
    val result = ArrayList<Int?>() // 널이 될 수 있는 타입의 리스트를 만든다.
    for (line in reader.lineSequence()) {
        try {
            val number = line.toInt()
            result.add(number) // 정수(널이 아닌 값)를 리스트에 추가한다.
        } catch (e: NumberFormatException) {
            result.add(null) // 현재 줄을 파싱할 수 없으므로 null을 리스트에 추가한다.
        }
    }
    return result
}
```

널이 될 수 있는 값으로 이루어진 리스트를 다루는 예를 살펴보자.
정상적인 숫자를 따로 모으고 그렇지 않은 숫자(그런 경우 그 값은 null이다)의 개수를 세는 함수를 작성해보자.

```kotlin
fun addValidNumbers(numbers: List<Int?>) {
    val validNumbers = numbers.filterNotNull()
    println("Sum of valid numbers: ${validNumbers.sum()}")
    println("Invalid numbers: ${numbers.size - validNumbers.size}")
}
```

물론 걸러내는 연산도 컬렉션의 타입에 영향을 끼친다. `filterNotNull`이 컬렉션 안에
널이 들어있지 않음을 보장해주므로 `validNumbers`는 `List<Int>` 타입이다.

### 읽기 전용과 변경 가능한 컬렉션
코틀린 컬렉션과 자바 컬렉션을 나누는 가장 중요한 특성 하나는 코틀린에서는 컬렉션 안에 데이터에 접근하는
인터페이스와 컬렉션 안의 데이터를 변경하는 인터페이스를 분리했다는 점이다.

이런 구분은 코틀린 컬렉션을 다룰 때 사용하는 가장 기초적인 인터페이스인 `kotlin.collections.Collection`부터 시작한다.
이 `Collection` 인터페이스를 사용하면 컬렉션 안의 원소에 대해 이터레이션하고, 컽렉션의 크기를 얻고,
어떤 값이 컬렉션 안에 들어있는지 검사하고, 컬렉션 안에서 데이터를 읽는 여러 다른 연산을 수행할 수 있다.

하지만 `Collection` 인터페이스에는 컬렉션 안의 원소를 변경하는 연산은 들어있지 않다.

컬렉션의 데이터를 수정하려면 `kotlin.collections.MutableCollection` 인터페이스를 사용해야 한다.
코드에서 가능하면 `MutableCollection` 대신 `Collection`을 사용하는 것이 좋다.

예를 들어 다음 `copyElements` 함수가 `source` 컬렉션은 변경하지 않지만 `target` 컬렉션은 변경하리라는 사실을 분명히 알 수 있다.
```kotlin
fun <T> copyElements(source: Collection<T>,
                     target: MutableCollection<T>) {
    for (item in source) {
        target.add(item)
    }
}

fun main(args: Array<String>) {
    val source: Collection<Int> = arrayListOf(3, 5, 7)
    val target: MutableCollection<Int> = arrayListOf(1)
    copyElements(source, target)
    println(target) // [1, 3, 5, 7]
}
```

컬렉션 인터페이스를 사용할 때 항상 염두에 둬야 할 핵심은 읽기 전용 컬렉션이라고해서
꼭 변경 불가능한 컬렉션일 필요는 없다는 점이다. 읽기 전용 인터페이스 타입인 변수를 사용할 때
그 인터페이스는 실제로는 어떤 컬렉션 인스턴스를 가리키는 수많은 참조 중 하나일 수 있다.

이런 상황(읽기 전용 인터페이스 타입의 변수가 실제로는 변경 가능한 컬렉션을 가리키고 있을 때)에서
이 컬렉션ㅇ르 참조하는 다른 코드를 호출하거나 병렬 실행한다면 컬렉션을 사용하는 도중에 다른 컬렉션이 그 컬렉션의
내용을 변경하는 상황이 생길 수 있고, 이런 상황에서는 `ConcurrentModificationException`이나 다른 오류가 발생할 수 있다.

따라서 읽기 전용 컬렉션이 항상 스레드 안전(thread-safe)하지 않다는 점을 명심해야 한다.


### 코틀린 컬렉션과 자바
모든 코틀린 컬렉션은 그에 상응하는 자바 컬렉션 인터페이스의 인스턴스라는 점은 사실이다.
따라서 코틀린과 자바 사이를 오갈 때 아무 변환도 필요 없다. 또한 래퍼 클래스를 만들거나 복사할 필요도 없다.
하지만 코틀린은 자바 컬렉션 인터페이스마다 읽기 전용 인터페이스와 변경 가능한 인터페이스라는 두 가지 표현을 제공한다.

자바는 읽기 전용 컬렉션과 변경 가능한 컬렉션을 구분하지 않으므로, 코틀린에서 읽기 전용 `Collection`으로
선언된 객체라도 자바 코드에서는 그 컬렉션 객체의 내용을 변경할 수 있다.

### 컬렉션을 플랫폼 타입으로 다루기
컬렉션 타입이 시그니처에 들어간 자바 메소드 구현을 오버라이드하려는 경우 읽기 전용 컬렉션과 변경 가능
컬렉션의 차이가 문제가 된다. 플랫폼 타입에서 널 가능성을 다룰 때처럼 이런 경우에도 오버라이드하려는 메소드의 자바 컬렉션 타입을
어떤 코틀린 컬렉션 타입으로 표현할지 결정해야 한다.

이런 상황에서는 여러 가지를 선택해야 한다. 그리고 이렇게 선택한 내용을 코틀린에서 사용할 컬렉션 타입에 반영해야한다.
- 컽렉션이 널이 될 수 있는가?
- 컬렉션의 원소가 널이 될 수 있는가?
- 오버라이드하는 메소드가 컬렉션을 변경할 수 있는가?

### 객체의 배열과 원시 타입의 배열
코틀린 배열은 타입 파라미터를 받는 클래스다. 배열의 원소 타입은 바로 그 타입 파라미터에 의해 정해진다.
코틀린에서 배열을 만드는 방법은 다양하다.

- arrayOf 함수를 사용해 배열을 만들 수 있다.
- arrayOfNulls 함수를 사용해 원소가 널이 될 수 있는 배열을 만들 수 있다.
- Array 생성자를 사용해 배열을 만들 수 있다.

```kotlin
val letters = Array<String>(26) { i -> ('a' + i).toString() }
println(letters.joinToString("")) // abcdefghijklmnopqrstuvwxyz
```

람다는 배열 원소의 인덱스를 받아서 배열의 해당 위치에 들어갈 원소를 반환한다.
여기서는 인덱스 값에 a 문자 값을 더한 결과를 문자열로 변환한다.

컬렉션을 배열로 바꾸기
```kotlin
val strings = listOf("a", "b", "c")
println("%s/%s/%s".format(*strings.toTypedArray())) // a/b/c
```

코틀린은 원시 타입의 배열을 표현하는 별도 클래스를 각 원시 타입마다 하나씩 제공한다.
예를 들어 `Int` 타입의 배열은 `IntArray`다. 코틀린은 `ByteArray`, `ShortArray`, `LongArray`, `FloatArray`, `DoubleArray`, `BooleanArray`, `CharArray`를 제공한다.

이 모든 타입은 자바 원시 타입 배열인 `int[]`, `byte[]`, `short[]`, `long[]`, `float[]`, `double[]`, `boolean[]`, `char[]`로 컴파일된다.

원시 타입의 배열을 만드는 방법은 다음과 같다.
- 각 배열 타입의 생성자는 size 인자를 받아서 해당 타입의 원소를 갖는 배열을 만든다.
- 팩토리 함수는 여러 값을 받아서 해당 타입의 원소를 갖는 배열을 만든다.
- 크기와 람다를 인자로 받는 생성자를 사용한다.

```kotlin
val fiveZeros = IntArray(5)
val fiveZerosToo = intArrayOf(0, 0, 0, 0, 0)
val squares = IntArray(5) { i -> (i + 1) * (i + 1) }
println(squares.joinToString()) // 1, 4, 9, 16, 25
```