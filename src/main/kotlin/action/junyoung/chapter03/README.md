# 3. 함수 정의와 호출

## 1. 코틀린에서 컬렉션 만들기

자바에서 `List.of()`, `Set.of()`와 같이 컬렉션을 바로 생성하는 것처럼 코틀린에서도 쉽게 컬렉션을 만들 수 있다.

```kotlin
val set = hashSetOf(1, 7, 53)
val list = arrayListOf(1, 7, 53)
val map = hashMapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
```

`hashMap`을 생성할 때 사용한 `to` 키워드는 하나의 `Pair`를 만들 때 주로 사용한다.
`hashMapOf()` 메소드 내부를 살펴보면 다음과 같이 되어있다.

```kotlin
public fun <K, V> hashMapOf(vararg pairs: Pair<K, V>): HashMap<K, V> = HashMap<K, V>(mapCapacity(pairs.size)).apply { putAll(pairs) }

public fun <K, V> MutableMap<in K, in V>.putAll(pairs: Array<out Pair<K, V>>): Unit {
    for ((key, value) in pairs) {
        put(key, value)
    }
}
```

`vararg`를 통해 여러 개의 `pairs`를 받고, `putAll` 함수를 통해 맵에 추가해준다.

```kotlin
println(set.javaClass)
println(list.javaClass)
println(map.javaClass)
```

또한, 위에서 만든 컬렉션의 클래스를 찍어보면, 코틀린만의 컬렉션이 아닌, 기존 자바 컬렉션을 활용하고 있는 것을 볼 수 있다.

## 2. 함수를 호출하기 쉽게 만들기

`list` 하나를 출력하는 코드를 살펴보자.

```kotlin
val list = arrayListOf(1, 7, 53)

// 출력 : [1, 7, 53]
println(list)
```

출력 결과와 같이 디폴트 구현이 되어있는 `toString()`을 호출하는 것을 볼 수 있다.
자바에서 이 형식을 바꾸려면 `list` 내부 원소들을 이터레이션 하면서 출력하겠지만,
코틀린에서는 이런 요구 사항을 처리할 수 있는 함수가 표준 라이브러리에 이미 존재한다.

```kotlin
fun <T> joinToString(
    collection: Collection<T>,
    separator: String,
    prefix: String,
    postfix: String
): String {
    val result = StringBuilder(prefix)
    for ((idx, element) in collection.withIndex()) {
        if (idx > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}
```

위 함수는 원소 사이에 넣을 구분자(separator)를 받고, 맨 앞에 넣을 접두사(prefix)와 맨 뒤에 넣을 접미사(postfix)를 받는다.
또한, 위 함수는 제네릭하기 때문에 어떤 타입의 값을 원소로 하는 컬렉션이든 처리할 수 있다.

```kotlin
// 출력 : [1, 7, 53]
println(list)
// 출력 : {1 - 7 - 53}
println(joinToString(list, " - ", "{", "}"))
```

의도한대로 잘 나오는 것을 볼 수 있다.

### 이름 붙인 인자

호출부를 보면 인자를 넣는 부분이 번잡하게 보여 가독성이 떨어진다.

```kotlin
println(joinToString(list, " ", " ", "."))
```

만약 위처럼 함수를 호출한다고 하면, 어떤 인자가 무엇을 의미하는지 알기 어렵다.
자바의 경우 주석을 이용해 특정 파라미터의 위치임을 알려줄 수 있다.

```java
joinToString (collection, /* separator */ " ", /* prefix */ " ", /* postfix */ " ")
```

파라미터 이름을 주석을 통해 넣는 작업은 번거로우면서, 귀찮기만 하다.
코틀린에서는 다음과 같이 파라미터 이름을 그대로 넣어서 사용이 가능하다.

```kotlin
println(joinToString(list, separator = " ", prefix = " ", postfix = "."))
```

자바에 비해 훨씬 깔끔하고, 보기 좋아진 것을 알 수 있다.

> 파라미터 이름을 변경해야할 경우, 인텔리제이에서 제공하는 Refactor 기능을 통해 Rename하는 것을 권장한다.

> 자바로 작성한 코드를 호출할 때에는 이름 붙인 인자를 사용할 수 없다.

```java
public class UtilClass {
    private UtilClass() {  }
    public static <T> String joinToString(Collection<T> collection, String separator, String prefix, String postfix) {
        final StringBuilder result = new StringBuilder(prefix);
        int idx = 0;
        for (T t : collection) {
            if(idx > 0) result.append(separator);
            result.append(t);
            idx++;
        }
        result.append(postfix);
        return result.toString();
    }
}
```

```kotlin
// 컴파일 에러 발생 : Named arguments are not allowed for non-Kotlin functions
println(UtilClass.joinToString(list, separator = " - ", "{", "}"))
```

위와 같이 자바에서 작성한 코드를 호출할 때에는 이름 붙인 인자를 사용할 수 없다.

### 디폴트 파라미터 값

자바에서는 일부 클래스에서 오버로딩(overloading)한 메소드가 너무 많아진다는 문제가 있다.

<center>

<img src="https://github.com/Jwhyee/kenteen-project/assets/82663161/3738c9c1-1f03-4aee-a038-7c9fbc353ec9">

java.lang.Thread

</center>

오버로딩 메소드들은 하위 호환성을 유지하거나, API 사용자에게 편의를 더하는 등의 여러 가지 이유로 만들어진다.
코틀린에서는 함수 선언에서 파라미터의 디폴트 값을 지정할 수 있으므로 이런 오버로드 중 상당수를 피할 수 있다.

```kotlin
fun main() {
    println(joinToString(list))
}

fun <T> joinToString(
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String { ... }
```

디폴트로 지정하지 않은 매개변수는 무조건 인자로 받아야하지만, 이 외에는 넘겨주지 않을 시 디폴트값을 사용하게 된다.
또한, 이름 붙인 인자를 사용하는 경우 인자 목록의 중간에 있는 인자를 생략하고, 지정하고 싶은 인자를 이름을 붙여서 순서와 관계없이 지정할 수 있다.

```kotlin
// separator 생략
println(joinToString(list, prefix = "{", postfix = "}"))
```

> 자바에는 디폴트 파라미터 값이라는 개념이 없다.
> 때문에 자바에서 코틀린 함수를 호출하는 경우에는 디폴트 파라미터 값을 제공하더라도 모든 인자를 입력해야 한다.
> 하지만 `@JvmOverloads`를 함수에 추가하면, 코틀린 컴파일러가 자동으로 자바 메소드를 추가해준다.

```kotlin
@JvmOverloads
fun <T> joinToString(
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String { ... }
```

```java
// 코틀린 코드를 바이트 코드로 변환 후 디컴파일된 자바 파일
@JvmOverloads
@NotNull
public static final String joinToString(@NotNull Collection collection, @NotNull String separator, @NotNull String prefix, @NotNull String postfix) { ... }

// $FF: synthetic method
public static String joinToString$default(Collection var0, String var1, String var2, String var3, int var4, Object var5) {
    if ((var4 & 2) != 0) {var1 = ", ";}
    if ((var4 & 4) != 0) {var2 = "";}
    if ((var4 & 8) != 0) {var3 = "";}
    return joinToString(var0, var1, var2, var3);
}

@JvmOverloads
@NotNull
public static final String joinToString(@NotNull Collection collection, @NotNull String separator, @NotNull String prefix) {
    return joinToString$default(collection, separator, prefix, (String)null, 8, (Object)null);
}

@JvmOverloads
@NotNull
public static final String joinToString(@NotNull Collection collection, @NotNull String separator) {
    return joinToString$default(collection, separator, (String)null, (String)null, 12, (Object)null);
}

@JvmOverloads
@NotNull
public static final String joinToString(@NotNull Collection collection) {
    return joinToString$default(collection, (String)null, (String)null, (String)null, 14, (Object)null);
}
```

```java
public class KtJoinToStringTest {
    public static void main(String[] args) {
        final List<Integer> list = List.of(1, 7, 53);
        System.out.println(CollectionTestKt.joinToString(list));
    }
}
```

### 정적인 유틸리티 클래스 없애기

#### 최상위 함수

객체지향 언어인 자바에서는 모든 코드를 클래스 내부에 작성해야 한다.
어느 한 클래스에 포함시키기 어렵고, 비슷하게 중요한 역할을 하는 클래스가 둘 이상 있을 수 있다.
때문에 다양한 정적 메소드를 모아두는 역할만 담당하며, 특별한 상태나 인스턴스 메소드는 없는 유틸리티 클래스가 생겨난다.

코틀린에서는 이런 무의미한 클래스를 만들 필요가 없다.
함수를 직접 소스 파일의 최상위 수준, 모든 다른 클래스의 밖에 위치시키면 된다.

```java
import action.chapter03.part1.CollectionTestKt;

System.out.println(CollectionTestKt.joinToString(list));
```

자바에서는 위 코드와 같이 클래스 파일을 부른 뒤, 내부 함수를 호출하면 된다.

이렇게 사용할 수 있는 이유는, 컴파일러가 `kt` 파일을 컴파일 할 때, 새로운 클래스를 정의해주기 때문이다.
때문에 위 코드와 같이 `CollectionTest.kt` 파일이 그 이름에 대응하는 `CollectionTestKt` 클래스로 바뀌게 된다.

> 만약 파일의 이름과 다른 이름으로 호출하고 싶을 경우, `@file:JvmName` 어노테이션을 활용해 파일 최상단에 위치시키면 된다.

```kotlin
// CollectionTest.kt 파일
@file:JvmName("StringFunctions")

package action.chapter03.part1

import java.lang.StringBuilder

@JvmOverloads
fun <T> joinToString(
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String { ... }
```

```java
import action.chapter03.part1.StringFunctions;
System.out.println(StringFunctions.joinToString(list));
```

#### 최상위 프로퍼티

함수와 마찬가지로 프로퍼티 또한 파일의 최상위 수준에 놓을 수 있다.

```kotlin
var opCount = 0
val UNIX_LINE_SEPARATOR = "\n"

fun performOperation() {
    opCount++
}

fun reportOperationCount() {
    println("Operation performed $opCount times")
}
```

이런 최상위 프로퍼티를 활용하면 정적 필드에 저장되며, 최상위 프로퍼티를 활용해 코드에 상수를 추가할 수 있다.
기본적으로 최상위 프로퍼티도 다른 모든 프로퍼티처럼 접근자 메소드를 통해 자바 코드에 노출된다.

- val : getter
- var : getter, setter

더 자연스럽게 사용하려면 이 상수를 `public static final` 필드로 컴파일해야 한다.
이럴 경우 `const`를 사용하면 위 필드로 컴파일하게 만들 수 있다.
단, 원시 타입과 String 타입의 프로퍼티만 지정할 수 있다.

```kotlin
const val UNIX_LINE_SEPARATOR = "\n"
```

```java
@NotNull
public static final String UNIX_LINE_SEPARATOR = "\n";
```

## 3. 메소드를 다른 클래스에 추가

기존 코드와 코틀린 코드를 자연스럽게 통합하는 것은 코틀린의 핵심 목표 중 하나이다.

### 확장 함수(Extension function)

확장 함수는 어떤 클래스의 멤버 메소드인 것처럼 호출할 수 있지만, 그 클래스의 밖에 선언된 함수다.

```kotlin
fun String.lastChar(): Char = this[this.length - 1]
```

확장 함수를 만들려면 추가하려는 함수 이름 앞에 그 함수가 확장할 클래스의 이름을 덧붙이기만 하면 된다.
클래스 이름을 수신 객체 타입(receiver type)이라 부르며, 확장 함수가 호출되는 대상이 되는 값(객체)을 수신 객체(receiver object)라고 부른다.

```kotlin
// String : 수신 객체 타입
// this : 수신 객체
fun String.lastChar(): Char = this[this.length - 1]
// fun String.lastChar(): Char = get(length - 1)

fun main() {
    val ch = "Hello".lastChar()

    // 출력 : o
    println(ch)
}
```

이 함수를 호출하는 구문은 다른 일반 클래스 멤버를 호출하는 구문과 동일하며, `this`를 생략해서 사용할 수도 있다.

> 클래스 내부 함수로 정의되어 있지 않아도, 확장 함수로 정의할 경우 내부 함수에 정의된 것처럼 사용할 수 있다.

#### 임포트와 확장 함수

확장 함수를 정의하더라도 모든 소스 코드에서 그 함수를 사용할 수 있지는 않다.
어디서든 그 함수를 사용할 수 있다면, 이름이 충돌하는 경우가 자주 생길 수 있기 때문이다.

```kotlin
// 특정 함수 import
import action.chapter03.part3.lastChar
// 특정 함수 import에 대한 별칭 지정
import action.chapter03.part3.lastChar as last
// 전체 임포트
import action.chapter03.part3.*

val c = "Kotlin".lastChar()
val c = "Kotlin".last()
```

위 코드와 같이 `import`를 통해 다른 패키지에서 정의한 확장 함수를 가져올 수 있다. 
만약 다른 여러 패키지에 속해있는 이름이 같은 함수를 가져와 사용해야 하는 경우 `as`를 통해 이름을 바꿔서 `import`하면 이름 충돌을 막을 수 있다.

> 코틀린 문법상 확장 함수는 반드시 짧은 이름을 써야한다.

역자의 말에 의하면 다음과 같다. 

<center>

<img src="https://github.com/Jwhyee/kenteen-project/assets/82663161/5e3b4e41-2bd7-445c-9180-99126be5112b">

</center>

```kotlin
// 잘못된 사용
val c = "Kotlin".action.chapter03.part3.lastChar()
```

### 확장 프로퍼티

