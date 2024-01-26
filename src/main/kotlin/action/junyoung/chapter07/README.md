# 7. 연산자 오버로딩과 기타 관계


어떤 클래스 안에 plus라는 이름의 특별한 메소드를 정의하면, 그 클래스의 인스턴스에 대해 + 연산자를 사용할 수 있다. 이렇게 어떤 언어 기능과 미리 정해진 이름의 함수를 연결해주는 기법을 관례(convention)이라고 부른다.

## 1. 산술 연산자 오버로딩

자바에서는 String 값에 대해 + 혹은 += 연산을 사용할 수 있었다. 이처럼 코틀린에서도 관례를 사용하는 가장 단순한 예는 산술 연산자이다.

### 1-1. 이항 산술 연산과 오버로딩

아래 코드는 plus에 대한 산술 연산을 구현한 코드이다.

```kotlin
data class Point(val x: Int, val y: Int) {  
    operator fun plus(other: Point): Point {  
        return Point(x + other.x, y + other.y)  
    }  
}  
  
fun main() {  
    val p1 = Point(10, 20)  
    val p2 = Point(30, 40)  
    println(p1 + p2)  
}
```

이처럼 연산자를 오버로딩하는 함수 정의 앞에 operator를 붙여야 한다. 만약 operator를 정의하지 않을 경우 해당 연산을 사용하는 부분에서 다음과 같은 에러가 발생한다.

```kotlin
// 'operator' modifier is required on 'plus' in 'part1.Point'
println(p1 + p2)
```

연산자를 기준으로 좌측에 있는 인스턴스가 수신 객체처럼 활용되고, 우측에 있는 인스턴스가 매개변수로 활용된다.

plus 외에도 사용할 수 있는 연산은 다음과 같다.

| 식 | 함수 이름 |
| ---- | ---- |
| a * b | times |
| a / b | div |
| a % b | mod -> rem |
| a + b | plus |
| a - b | minus |
만약 a + b `*` c와 같은 식이 있다면, 연산자 우선 순위에 따라 `*`를 먼저 진행하고, +를 진행한다. 또한, 이런 연산자 오버로딩은 클래스 내부가 아닌 외부에서도 확장 함수로 정의할 수 있다.

```kotlin
operator fun Point.times(scale: Double): Point {  
    return Point((x * scale).toInt(), (y * scale).toInt())  
}
```

코틀린은 연산자가 자동으로 교환 법칙(commutativity)을 지원하지 않는다.

```kotlin
p * 1.5 == 1.5 * p
```

위 교환 법칙을 준수하려면, 같은 식에 대응하는 연산자 함수인 Double.times에 대한 확장 함수를 추가적으로 정의해야 한다.

### 1-2. 복합 대입 연산자 오버로딩

코틀린은 +와 같은 일반 연산자 뿐만 아니라 +=과 같은 복합 대입(compound assignment) 연산자도 지원한다. 대표적으로 List, Map, Set과 같은 컬렉션에서 사용이 가능하다.

```kotlin
operator fun <T> MutableCollection<T>.plusAssign(element: T) {
	this.add(element)
}
```

plusAssign을 사용하려면, val이 아닌 var로 정의되어 있어야 하며, 가능한 plus와 plusAssign 연산을 동시에 정의하지 않는 것이 좋다. 코틀린 컬렉션은 두 가지를 함께 제공한다. +, - 연산은 항상 새로운 컬렉션을 반환하고, +=과 -= 연산자는 항상 변경 가능한 컬렉션에 작용해 메모리에 있는 객체 상태를 변화시킨다. 만약 읽기 전용 컬렉션일 경우 변경을 적용한 복사본을 반환한다.

```kotlin
fun main() {  
    val list = arrayListOf(1, 2)  
    list += 3  
    val newList = list + listOf(4, 5)  
    // [1, 2, 3]
    println(list)
	// [1, 2, 3, 4, 5]  
    println(newList)  
}
```

### 1-3. 단항 연산자 오버로딩

앞서 본 연산은 모두 두 값에 대해 작용하는 이항(binary) 연산에 대한 내용이었다. 단항 연산자는 함수에 파라미터 없이 사용하는 함수이다.

```kotlin
operator fun Point.unaryMinus(): Point {  
    return Point(-x, -y)  
}

fun main() {
	val p = Point(10, 20)
	// Point(x=-10, y=-20)
	println(-p)
}
```

| 식 | 함수 이름 |
| ---- | ---- |
| +a | unaryPlus |
| -a | unaryMinus |
| !a | not |
| ++a, a++ | inc |
| --a, a-- | dec |

BigDecimal과 같은 클래스를 살펴보면 ++ 연산을 오버로딩한 것을 볼 수 있다.

```kotlin
operator fun BigDecimal.inc() = this + BigDecimal.ONE
```

## 2. 비교 연산자 오버로딩

equals나 compareTo를 호출해야할 경우 자바와 달리 == 비교 연산자를 직접 사용할 수 있어, 코드가 더 간결하며, 이해하기 쉬워진다.
### 2-1. 동등성 연산자: equals

315page