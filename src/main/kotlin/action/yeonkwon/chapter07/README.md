# 7장 연산자 오버로딩과 기타 관례

코틀린에서도 어떤 언어 기능이 정해진 사용자 작성 함수와 연결되는 경우가 몇 가지 있다.
이런 언어 기능이 어떤 타입(클래스)와 연관되기보다는 특정 함수 이름과 연관된다.

예를 들어 어떤 클래스 안에 `plus`라는 이름의 함수를 정의하면 그 클래스의 인스턴스에 대해 `+` 연산자를 사용할 수 있다.
이런 식으로 언어 기능과 연결된 함수를 **관례(convention)**라고 부른다.

이번 장에서는 코틀린에서 어떤 관례가 있는지 알아보고, 관례를 사용하는 방법을 살펴본다.

## 7.1 산술 연산자 오버로딩

BigInetger 클래스에 대해 `+` 연산자를 사용하면 두 BigInteger 인스턴스를 더한 결과를 반환한다.

### 7.1.1 이항 산술 연산 오버로딩

```kotlin
data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}
```

`operator` 키워드는 이 클래스에 여러 관례를 적용할 수 있음을 나타낸다.
이 예제에서는 `plus` 관례를 사용한다.

`operator`가 없는데 `plus` 함수를 정의하면 컴파일러가 `operator modifier is required on 'plus' function`이라는 오류 메시지를 출력한다.

연산자를 멤버함수로 만드는 대신 확장 함수로 만들 수도 있다.

```kotlin
operator fun Point.plus(other: Point): Point {
    return Point(x + other.x, y + other.y)
}
```

오버로딩 가능한 이항 산술 연산자는 다음과 같다.

|  연산자  |     함수 이름      |
|:-----:|:--------------:|
| a + b |      plus      |
| a - b |     minus      |
| a * b |     times      |
| a / b |      div       |
| a % b | mod(1.1부터 rem) |

직접 정의한 함수를 통해 구현하더라도 연산자 우선순위는 언제나 표준 숫자 타입에 대한 연산자 우선순위와 같다.


연산자를 정의할 때 두 피연산자가 같은 타입일 필요는 없다.

```kotlin
operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}
```

코틀린 연산자가 자동으로 교환법칙을 지원하지 않음에 유의하라.
`a * b`를 호출하면 `a.times(b)`가 호출되고, `b * a`를 호출하면 `b.times(a)`가 호출된다.

연산자 함수의 반환 타입이 꼭 두 피연사자 중 하나와 일치해야만 하는 것도 아니다.
```kotlin
operator fun Char.times(count: Int): String {
    return toString().repeat(count)
}

println('a' * 3) // aaa
```

> 비트 연산자에 대해 특별한 연산자 함수를 제공하지 않는다.
> - shl - 왼쪽 시프트
> - shr - 오른쪽 시프트
> - ushr - 부호 없는 오른쪽 시프트
> - and - 비트 AND
> - or - 비트 OR
> - xor - 비트 XOR
> - inv - 비트 역

### 7.1.2 복합 대입 연산자 오버로딩

```kotlin
var point = Point(1, 2)
point += Point(3, 4)
```

`+=` 연산자를 사용하면 `plus` 함수가 호출되고 그 결과가 왼쪽 피연산자에 대입된다.

경우에 따라 += 연산이 객체에 대한 참조를 다른 참조로 바꾸기보다 원래 객체의 내부 상태를 변경하게 만들고 싶을 때가 있다.

`+=` 연산자를 사용하려면 `plusAssign`이라는 이름의 함수를 정의해야 한다.

```kotlin
operator fun Point.plusAssign(other: Point) {
    x += other.x
    y += other.y
}
```

어떤 클래스가 `plus`와 `plusAssign`을 모두 정의하고 둘 다 +=에 사용 가능한 경우
컴파일러는 오류를 보고한다.

일반 연산자를 사용하거나, `var`를 `val`로 바꾸거나, 일관성 있게 동시에 정의하지 않으면 해결할 수 있다.

코틀린 표준 라이브러리는 컬렉션에 대해 두 가지 접근 방법을 함께 제공한다. +와 -는 항상 새로운 컬렉션을 반환하며, +=와 -= 연산자는
항상 변경 가능한 컬렉션에 작용해 메모리에 있는 객체 상태를 변화시킨다.

또한 읽기 전용 컬렉션에서 +=와 -=는 변경을 적용한 복사본을 반환한다.

### 7.1.3 단항 연산자 오버로딩

```kotlin
operator fun Point.unaryMinus(): Point {
    return Point(-x, -y)
}

val point = Point(10, 20)
println(-point) // Point(x=-10, y=-20)
```

단항 연산자를 오버로딩하기 위해 사용하는 함수는 인자를 취하지 않는다.

오버로딩 할 수 있는 단항 산술 연산자

| 연산자 | 함수 이름 |
|:-----:|:--------:|
| +a | unaryPlus |
| -a | unaryMinus |
| !a | not |
| ++a, a++ | inc |
| --a, a-- | dec |


```kotlin
operator fun BigDecimal.inc() = this + BigDecimal.ONE

var bd = BigDecimal.ZERO
println(bd++) // 0
println(++bd) // 2
```
전위와 후위 연산을 처리하기 위해 별다른 처리를 해주지 않아도 증가 연산자가 잘 동작한다.

## 7.2 비교 연산자 오버로딩

`equals`나 `compareTo`를 직접 호출하는 대신 코틀린에서는 `==` 비교 연산자를 직접 사용할 수 있어서
비교 코드가 더 간결해진다.

### 7.2.1 동등성 연산자: equals

`==` 연산자는 `equals` 함수를 호출한다.
내부에서 널인지 검사하므로 다른 연산과 달리 널이 될 수 있는 값에도 적용할 수 있다.

```kotlin
data class Point(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false
        return x == other.x && y == other.y
    }
}

println(Point(10, 20) == Point(10, 20)) // true
println(Point(10, 20) != Point(5, 5)) // true
println(null == Point(1, 2)) // false
```

식별자 비교(`identity equals`)연산자 (`===`)를 사용해 `equals`의 파라미터가 수신 객체와 같은지 살펴본다.
`===`는 오버로딩 할 수 없다.

### 7.2.2 순서 연산자: compareTo
자바에서 정렬이나 최댓값, 최솟값 등 값을 비교해야 한느 알고리즘에 사용할 클랫느는 `Comparable` 인터페이스를 구현해야 한다.
`Comparable`에 들어있는 `compareTo` 메소드는 한 객체와 다른 객체의 크기를 비교해 정수로 나타내준다.

코틀린도 똑같은 `Comparable` 인터페이스를 제공한다.
게다가 코틀린은 `Comparable` 인터페이스를 구현하는 클래스에 대해 `compareTo`를 호출하는 대신 `>`와 `<`와 같은 연산자를 사용할 수 있다.

```kotlin
data class Point(val x: Int, val y: Int) : Comparable<Point> {
    override fun compareTo(other: Point): Int {
        return compareValuesBy(this, other, Point::x, Point::y)
    }
}

println(Point(10, 20) > Point(5, 5)) // true
```

코틀린 표준 라이브러리인 `compareValuesBy` 함수는 여러 프로퍼티를 비교해 그 결과를 반환한다.
`compareValuesBy`는 두 객체와 여러 비교함수를 인자로 받는다.
첫 번째 비교 함수에 두 객체를 인자로 넘겨 호출한 결과가 0이 아니면 그 결과를 반환하고, 0이면 두 번째 비교 함수를 호출한다.
각 비교 함수는 람다나 프로퍼티/메소드 참조일 수 있다.

## 7.3 컬렉션과 범위에 대해 쓸 수 있는 관례
컬렉션을 다룰 때 가장 많이 쓰는 연산은 인덱스를 사용해 원소를 읽거나 쓰는 연산과 어떤 값이 컬렉션에 속해있는지 검사하는 연산이다.

### 7.3.1 인덱스로 원소에 접근하기
코틀린에서는 `get`이나 `set`이라는 이름의 함수를 호출해 인덱스 연산을 수행한다.
`get`이나 `set` 함수를 정의하면 `[]` 연산자를 사용할 수 있다.

```kotlin
operator fun Point.get(index: Int): Int {
    return when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

val p = Point(10, 20)
println(p[1]) // 20
```

인덱스에 해당하는 컬렉션 원소로 쓰고 싶은 떄는 `set`이라는 이름의 함수를 정의하면 된다.

```kotlin
operator fun Point.set(index: Int, value: Int) {
    when (index) {
        0 -> x = value
        1 -> y = value
        else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

p[1] = 42
println(p) // Point(x=10, y=42)
```

### 7.3.2 in 관례
컬렉션에 원소가 포함되어 있는지 검사하려면 `contains` 함수를 호출한다.

```kotlin
data class Rectangle(val upperLeft: Point, val lowerRight: Point)

operator fun Rectangle.contains(p: Point): Boolean {
    return p.x in upperLeft.x until lowerRight.x && // 열린 구간
            p.y in upperLeft.y until lowerRight.y
}

val rect = Rectangle(Point(10, 20), Point(50, 50))
println(Point(20, 30) in rect) // true
println(Point(5, 5) in rect) // false
```

### 7.3.3 rangeTo 관례
`..` 연산자는 `rangeTo` 함수를 호출한다.
`rangeTo` 함수는 `Comparable` 인터페이스를 구현하는 클래스에 대해 정의되어 있다.
어떤 클래스가 `Comparable`을 구현하면 `rangeTo`를 정의할 필요가 없다.
코틀린 표준 라이브러리를 통해 비교가능한 원소로 이뤄진 범위를 쉽게 만들 수 있다.
코틀린 표준 라이브러리에는 모든 `Comparable` 타입에 대해 `rangeTo`를 호출해 범위를 만들어주는 확장 함수가 있다.

```kotlin
operator fun <T : Comparable<T>> T.rangeTo(that: T): ClosedRange<T> {
    return ComparableRange(this, that)
}
```

이 함수는 범위를 반환하며, 어떤 원소가 범위 안에 들어있는지 `in`을 통해 검사할 수 있다.

```kotlin
val now = LocalDate.now()
val vacation = now..now.plusDays(10)
println(now.plusWeeks(1) in vacation) // true, 특정 날짜가 범위 안에 있는지 검사
```

### 7.3.4 for 루프를 위한 iterator 관례
`for` 루프를 사용해 컬렉션을 이터레이션할 때 컴파일러는 `iterator` 함수를 호출해 이터레이터를 얻는다.
이터레이터는 `next` 함수를 호출해 컬렉션 원소를 하나씩 가져온다.

```kotlin
operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
    object : Iterator<LocalDate> {
        var current = start

        override fun hasNext() =
            current <= endInclusive

        override fun next() = current.apply {
            current = plusDays(1)
        }
    }

val newYear = LocalDate.ofYearDay(2017, 1)
val daysOff = newYear.minusDays(1)..newYear
for (dayOff in daysOff) {
    println(dayOff)
}
// 2016-12-31
// 2017-01-01
```

## 7.4 구조 분해 선언과 component 함수
코틀린에서는 `componentN`이라는 이름의 함수를 호출해 객체의 속성을 여러 변수로 쪼갤 수 있다.
`componentN` 함수는 `operator`로 표시된다.
```kotlin
data class Point(val x: Int, val y: Int) {
    operator fun component1() = x
    operator fun component2() = y
}
```


```kotlin
data class NameComponents(val name: String, val extension: String)

fun splitFilename(fullName: String): NameComponents {
    val result = fullName.split('.', limit = 2)
    return NameComponents(result[0], result[1])
}

val (name, ext) = splitFilename("example.kt")
println(name) // example
println(ext) // kt
```

배열이나 컬렉션 에서도 `componentN` 함수가 있음을 안다면 이 예제를 더 개선할 수 있다.

```kotlin
fun splitFilename(fullName: String): NameComponents {
    val (name, ext) = fullName.split('.', limit = 2)
    return NameComponents(name, ext)
}
```

### 7.4.1 구조 분해 선언과 루프
함수 본문 내의 선언문뿐 아니라 변수 선언이 들어갈 수 있는 장소라면 어디든 구조분해 선언을 사용할 수 있다.
예를 들어 루프 안에서도 구조 분해 선언을 사용할 수 있다.

```kotlin
val map = mapOf("Oracle" to "Java", "JetBrains" to "Kotlin")

for ((key, value) in map) {
    println("$key -> $value")
}
// Oracle -> Java
// JetBrains -> Kotlin
```

## 7.5 프로퍼티 접근자 로직 재활용: 위임 프로퍼티
코틀린이 제공하는 관례에 의존하는 특성 중에 독특하면서 강력한 기능인 위임 프로퍼티이다.

위임 프로퍼티를 사용하면 값을 뒷받침하는 필드에 단순히 저장하는 것보다 더 복잡한 방식으로 작동하는 프로퍼티를 쉽게 구현할 수 있다.

이러한 특성의 기반에는 위임이 있다. 위임은 객체가 직접 작업을 수행하지 않고 다른 도우미 객체가 그 작업을 처리하게 맡기는 디자인 패턴을 말한다.

이때 작업을 처리하는 도우미 객체를 위임 객체(delegate)라고 부른다.

### 7.5.1 위임 프로퍼티 소개

```kotlin
class Foo {
    var p: Type by Delegate()
}
```

p 프로퍼티는 접근자 로직을 다른 객체에게 위임한다.

```kotlin
class Foo {
    private val delegate = Delegate() // 컴파일러가 생성한 도우미 프로퍼티다.
    var p: Type
        get() = delegate.getValue(this, this::p)
        set(value: Type) = delegate.setValue(this, this::p, value)
}
```

위임 객체는 getValue와 setValue라는 두 개의 함수를 제공해야 한다.

```kotlin
class Delegate {
    operator fun getValue(...): Type {
        ...
    }

    operator fun setValue(...) {
        ...
    }
}

class Foo {
    var p: Type by Delegate() // by 키워드로 위임 객체를 연결한다.
}

val foo = Foo()
val oldValue = foo.p // Delegate.getValue 호출
foo.p = newValue // Delegate.setValue 호출
```

`foo.p`는 일반 프로퍼티처럼 쓸 수 있고, 일반 프로퍼티 같아 보인다. 하지만 실제로 p의 게터나 세터는 `Delegate` 타입의 위임 프로퍼티 객체에 있는 메소드를 호출한다.

### 7.5.2 위임 프로퍼티 사용: by lazy() 를 사용한 프로퍼티 초기화 지연
지연 초기화는 객체의 일부분을 초기화하지 않고 남겨뒀다가 실제로 그 부분의 값이 필요할 경우 초기화할 때 흔히 쓰이는 패턴이다.
초기화 과정에 자원을 많이 사용하거나 객체를 사용할 때마다 꼭 초기화하지 않아도 되는 프로퍼티에 대해 지연 초기화 패턴을 사용할 수 있다.

```kotlin
class Email { ... }

fun loadEmails(person: Person): List<Email> {
    println("Load emails for ${person.name}")
    return listOf(/*...*/)
}

class Person(val name: String) {
    private var _emails: List<Email>? = null
    val emails: List<Email>
        get() {
            if (_emails == null) {
                _emails = loadEmails(this)
            }
            return _emails!!
        }
}

val p = Person("Alice")
p.emails // Load emails for Alice
p.emails
```

여기서는 `backing property`라는 기법을 사용한다. 이 기법은 프로퍼티의 값을 저장하는 데 추가적인 프로퍼티를 선언하는 것이다.

지연 초기화를 위해 사용했지만 프로퍼티가 많아지면 이런 코드를 만드는 일은 약간 성가시다.
코틀린 표준 라이브러리는 이런 경우를 위해 `lazy`라는 함수를 제공한다.

```kotlin
class Person(val name: String) {
    val emails by lazy { loadEmails(this) }
}
```

`lazy` 함수는 코틀린 관례에 맞는 시그니처의 `getValue` 메소드가 들어있는 객체를 반환한다. 따라서 `lazy`와 `by`키워드를 함께 사용해
위임 프로퍼티를 만들 수 있다. `lazy`함수의 인자는 값을 초기화 할 때 호출할 람다다.

`lazy`함수는 기본적으로 스레드 안전하다. 하지만 필요에 따라 동기화에 사용할 락을 `lazy` 함수에 전달할 수도 있고, 다중 스레드 환경에서
사용하지 않을 프로퍼티를 위해 `lazy(LazyThreadSafetyMode.NONE)`을 사용할 수도 있다.

### 7.5.3 위임 프로퍼티 구현

```kotlin
open class PropertyChangeAware {
    protected val changeSupport = PropertyChangeSupport(this)

    fun addPropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.addPropertyChangeListener(listener)
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.removePropertyChangeListener(listener)
    }
}

// 나이나 급여가 바뀌면 PropertyChangeSupport 인스턴스에 이를 알리는 코드
class Person(val name: String, age: Int, salary: Int) : PropertyChangeAware() {
    var age: Int = age
        set(newValue) {
            val oldValue = field // field 키워드로 backing field에 접근한다.
            field = newValue
            changeSupport.firePropertyChange(
                "age", oldValue, newValue
            )
        }

    var salary: Int = salary
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange(
                "salary", oldValue, newValue
            )
        }
}

val p = Person("Dmitry", 34, 2000)
p.addPropertyChangeListener(
    PropertyChangeListener { event ->
        println(
            "Property ${event.propertyName} changed " +
                    "from ${event.oldValue} to ${event.newValue}"
        )
    }
)

p.age = 35
// Property age changed from 34 to 35
p.salary = 2100
// Property salary changed from 2000 to 2100
```

위임 프로퍼티를 사용하면 이 코드를 더 간결하게 만들 수 있다.

```kotlin
class ObservableProperty(
    var propValue: T, val changeSupport: PropertyChangeSupport
) {
    operator fun getValue(p: Person, prop: KProperty<*>): T = propValue

    operator fun setValue(p: Person, prop: KProperty<*>, newValue: T) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }
}

class Person(
    val name: String, age: Int, salary: Int
) : PropertyChangeAware() {
    var age: Int by ObservableProperty(age, changeSupport)
    var salary: Int by ObservableProperty(salary, changeSupport)
}

val p = Person("Dmitry", 34, 2000)
p.addPropertyChangeListener(
    PropertyChangeListener { event ->
        println(
            "Property ${event.propertyName} changed " +
                    "from ${event.oldValue} to ${event.newValue}"
        )
    }
)

p.age = 35
// Property age changed from 34 to 35
p.salary = 2100
// Property salary changed from 2000 to 2100
```

이전 코드와 비교해보면 다음과 같은 차이가 있다.
- 코틀린 관레에 사용하는 다른 함수와 마찬가지로 `getValue`와 `setValue` 함수는 `operator`로 표시된다.
- `getValue`와 `setValue`는 프로퍼티가 포함된 객체와 프로퍼티를 표현하는 객체를 파라미터로 받는다. 코틀린은 `KProperty` 타입의 객체를 사용해 프로퍼티를 표현한다.
- `KProperty` 인자를 통해 프로퍼티 이름을 전달받으므로 주 생성자에서는 `name` 프로퍼티를 없앤다.

observable 프로퍼티를 위한 위임 클래스를 만들었지만, 이런 클래스를 직접 만들 필요는 없다.
표준 라이브러리에는 이미 `ObservableProperty`와 비슷한 클래스가 있다. 다만 이 표준 라이브러리의 클래스는
`PropertyChangeSupport` 와는 연결돼 있지 않다.
따라서 프로퍼티 값의 변경을 통지할 때 `PropertyChangeSupport`를 사용하는 방법을 알려주는 람다를
그 표준 라이브러리 클래스에게 넘겨야 한다.


```kotlin
class Person(
    val name: String, age: Int, salary: Int
) : PropertyChangeAware() {
    private val observer = {
        prop: KProperty<*>, oldValue: Int, newValue: Int ->
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }
    var age: Int by Delegates.observable(age, observer)
    var salary: Int by Delegates.observable(salary, observer)
}

val p = Person("Dmitry", 34, 2000)
p.addPropertyChangeListener(
    PropertyChangeListener { event ->
        println(
            "Property ${event.propertyName} changed " +
                    "from ${event.oldValue} to ${event.newValue}"
        )
    }
)

p.age = 35
// Property age changed from 34 to 35
p.salary = 2100
// Property salary changed from 2000 to 2100
```

`by`의 오른쪽에 있는 식이 꼭 새 인스턴스를 만들 필요는 없다. 함수 호출, 다른 프로퍼티, 다른 식 등이 `by`의 우항에 올 수 있다.
다만 우항에 있는 식을 계산한 결과인 객체는 컴파일러가 호출할 수 있는 올바른 타입의 `getValue`와 `setValue`를 제공해야 한다.

### 7.5.4 위임 프로퍼티 컴파일 규칙
컴파일러가 위임 프로퍼티를 어떻게 컴파일하는지 알아보자.

```kotlin
class C {
    var prop: Type by MyDelegate()
}
```

컴파일러는 `MyDelegate` 클래스의 인스턴스를 감춰진 프로퍼티에 저장하며 그 감춰진 프로퍼티를 `<delegate>`라는 이름으로 부른다.
또한 컴파일러는 프로퍼티를 표현하기 위해 `KProperty` 타입의 객체를 사용한다. 이 객체를 `<property>`라고 부른다.

```kotlin
class C {
    private val <delegate> = MyDelegate()
    var prop: Type
        get() = <delegate>.getValue(this, <property>)
        set(value: Type) = <delegate>.setValue(this, <property>, value)
}
```

다시 말해 컴파일러는 모든 프로퍼티 접근자 안에 `getValue`와 `setValue` 호출을 추가한다.
이 메커니즘은 상당히 단순하지만 상당히 흥미로운 활용법이 많다.

프로퍼티 값이 저장될 장소를 바꿀 수도 있고(맵, 데이터베이스 테이블, 사용자 세션의 쿠키 등) 프로퍼티를 읽거나 쓸 때
벌어질 일을 변경할 수도 있다.

## 7.5.5 프로퍼티 값을 맵에 저장
자신의 프로퍼티를 동적으로 정의할 수 있는 객체를 만들 때 위임 프로퍼티를 활용하는 경우가 자주 있다. 그런 객체를
**확장 가능한 객체**(extensible object)라고 부른다.

```kotlin
class Person {
    private val _attributes = hashMapOf<String, String>()
    fun setAttribute(attrName: String, value: String) {
        _attributes[attrName] = value
    }

    val name: String
        get() = _attributes["name"]!!
}

val p = Person()
val data = mapOf("name" to "Dmitry", "company" to "JetBrains")
for ((attrName, value) in data) {
    p.setAttribute(attrName, value)
}
println(p.name) // Dmitry
```

위임 프로퍼티를 사용하면 이 코드를 더 간결하게 만들 수 있다.

```kotlin
class Person {
    private val _attributes = hashMapOf<String, String>()
    fun setAttribute(attrName: String, value: String) {
        _attributes[attrName] = value
    }

    val name: String by _attributes
}

val p = Person()
val data = mapOf("name" to "Dmitry", "company" to "JetBrains")
for ((attrName, value) in data) {
    p.setAttribute(attrName, value)
}
println(p.name) // Dmitry
```

이런 코드가 작동하는 이유는 표준 라이브러리가 `Map`과 `MutableMap` 인터페이스에 대해 `getValue`와 `setValue`를
정의해두었기 때문이다.

### 7.5.6 프레임워크에서 위임 프로퍼티 활용
객체 프로퍼티를 저장하거나 변경하는 방법을 바꿀 수 있으면 프레임워크를 개발할 때 유용하다.

```kotlin
object Users : IdTable() { // 객체는 데이터베이스 테이블에 해당한다.
    val name = varchar("name", length = 50).index()
    val age = integer("age")
}

class User(id: EntityID) : Entity(id) { // 각 User 인스턴스는 테이블에 들어있는 구체적인 엔티티에 해당한다.
    var name: String by Users.name // 사용자 이름은 데이터베이스 `name` 컬럼에 들어 있다.
    var age: Int by Users.age
}
```

`Users` 객체는 데이터베이스 테이블을 표현한다. 데이터베이스 전체에 단 하나만 존재하는 테이블을 표현하므로 `Users`를 (싱글턴) 객체로 선언했다.
객체의 프로퍼티는 테이블 컬럼을 표현한다.

`User`의 상위 클래스인 `Entity` 클래스는 데이터베이스 컬럼을 엔티티의 속성(`attribute`)값으로 연결해주는 매핑이 있다.

이 프레임워크를 사용하면 `User`의 프로퍼티에 접근할 때 자동으로 `Entity` 클래스에 정의된 데이터베이스 매핑으로부터 필요한 값을 가져오므로 편리하다.
