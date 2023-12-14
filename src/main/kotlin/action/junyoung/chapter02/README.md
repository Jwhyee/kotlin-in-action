# 코틀린 기초

## 목차

- 함수, 변수, 클래스, enum, 프로퍼티 선언
- 제어 구조
- 스마트 캐스트
- 예외 던지기와 잡기

## 함수와 변수

```kotlin
fun main(args: Array<String>) {
    println("Hello, World!")
}
```

- 코틀린에서 함수를 선언할 때에는 `fun` 키워드를 사용한다.
- 파라미터 이름 뒤에 파라미터의 타입을 쓴다.
- 자바와 다르게 클래스 안에 함수를 넣는 것이 아닌, 함수를 최상위 수준에 정의할 수 있다.
- 배열도 클래스로 구분한다.
- `System.out.println`이 아닌 `println()`으로 사용한다.
  - 표준 자바 라이브러리 함수를 간결하게 사용할 수 있게 래퍼 클래스를 제공
- 최신 프로그래밍 언어 경향으로 인해 세밐로론을 붙이지 않아도 된다.

### 함수(function)

앞서 본 `main` 함수는 아무런 값도 반환하지 않는다.
그렇다면 특정한 값을 반환하는 함수는 어디에 반환 타입을 지정해야할까?

```kotlin
fun main() {
    println(max(1, 2))
}

fun max(a: Int, b: Int): Int {
    return if(a > b) a else b
}
```

위 예제와 같이, 함수 선언은 `fun` 키워드를 사용하며,
함수의 반환 타입은 파라미터 목록의 닫는 괄호 뒤에 콜론으로 구분지어 명시한다.

또한, 자바에서는 조건문을 통해 값을 변수에 대입하려면 `if`가 아닌 아닌 삼항 연산자를 사용해야 하지만, 
코틀린의 `if`는 문장(Statement)가 아닌 식(Expression)으로 사용할 수 있다.

> 문(Statement)과 식(Expression)의 구분<br>
> 식 : 값을 만들어 내며, 하위 요소로 계산에 참여할 수 있다.<br>
> 문 : 자신을 둘러싸고 있는 가장 안쪽 블록의 최상위 요소로 존재하며, 아무런 값을 만들어내지 않는다.

#### 식이 본문인 함수

위에서 살펴본 `max` 함수를 보다 더 간결하게 표현할 수 있다.


```kotlin
fun max(a: Int, b: Int) = if (a > b) a else b
```

기존의 함수처럼 블록으로 감싸는 것이 아닌, 함수 자체를 하나의 식(Expression)으로 표현이 가능하다.
이는 IDE에서 제공하는 툴팁을 통해 다시 블록 형태로 감쌀 수 있다.

<center>

<img src="https://github.com/Jwhyee/Jwhyee.github.io/assets/82663161/fcb849bd-4713-4dc4-83b0-a80817ce6541">

</center>

또한, 코드를 보면 알 수 있듯, 식이 본문인 함수의 경우 굳이 사용자가 반환 타입을 적지 않아도,
컴파일러가 해당 식을 분석해 식의 결과 타입을 함수 반환 타입으로 정해준다.

### 변수

자바에서는 변수를 선언할 때, 타입이 맨 앞에 오지만, 코틀린에서는 타입 추론을 통해 꼭 지정하지 않아도 된다.

```kotlin
val question = "삶, 우주, 그리고 모든 것에 대한 궁극적인 질문"
val answer = 42
// val answer: Int = 42
```

그렇다면 부동소수점(floating point) 상수를 사용한다면, 변수 타입은 어떻게 될까?

```kotlin
val yearsToCompute = 7.5e6
// Double 출력
println(yearsToCompute::class.simpleName)
```

실수 타입은 기본적으로 `Double` 타입을 사용한다.

#### 변경 가능한 변수와 변경 불가능한 변수

변수 선언 시 사용하는 키워드는 2가지가 있다.

- val(value) : 변경 불가능한(immutable) 참조를 저장하는 변수
  - 초기화하고 나면 재대입이 불가능하며, 자바의 `final` 변수와 동일하다.
- var(variable) : 변경 가능한(mutable) 참조
  - 값이 언제든 바뀔 수 있으며, 자바의 일반 변수와 동일하다.

> 가능한 모든 변수를 `val`로 선언하고, 꼭 필요한 경우에만 `var`로 변경하자.
> 변경 불가능한 참조와 변경 불가능한 객체를 부수 효과가 없는 함수와 조합해 사용하면,
> 코드가 함수형 코드에 가까워 진다.

val 변수는 정확히 한 번만 초기화 되어야 한다.

```kotlin
val message: String
if(answer == 42) {
    message = "Success"
} else {
    message = "Failed"
}
```

또한, `val` 참조 자체는 불변일지라도, 그 참조가 가리키는 객체의 내부 값은 변경될 수 있다.

```kotlin
val languages = arrayListOf("Java", "Kotlin", "Type Script")
languages.add("C++")

// 출력 : [Java, Kotlin, Type Script, C++]
println(languages)
```

위 `languages`는 불변 객체이지만, 참조가 가리키는 객체 내부는 변경되는 것을 알 수 있다.

#### 문자열 형식 지정 : 문자열 템플릿

```kotlin
fun main(args: Array<String>) {
    val name = if (args.size > 0) args[0] else "Kotlin"
    println("Hello, $name")
}
```

위 예제에서는 `name` 변수를 선언한 뒤, 출력하는 과정에서 `$`를 추가해 변수를 대입했다.
만약 `$`를 출력하고 싶다면 `\$`를 통해 이스케이프 시켜야 한다.
또한, 변수 내 함수를 호출해야할 경우 `"${name.uppercase()}"`와 같이 중괄호에 감싸면 된다.

> 한글의 경우 영어와 마찬가지로 변수 이름에 한글이 들어갈 수 있다.
> 때문에 `Hello, $name님 반가워요`라고 작성할 경우 `$name님`이 하나의 변수로 인식되므로,
> `Hello, ${name}님 반가워요`와 같이 중괄호로 감싼 뒤, 사용해야 한다.

## 클래스와 프로퍼티

### 클래스

간단한 자바 `Person` 클래스를 통해 확인해보자.

```java
public class JavaPerson {
    private final String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

위 클래스에는 `name`이라는 프로퍼티(Property)만 들어있다.
그렇다면 코틀린에서는 위 클래스를 어떻게 정의할 수 있을까?

```kotlin
class Person(val name: String)
```

코틀린은 한 줄로 정의가 가능하다.
이렇게 코드가 없이 데이터만 저장하는 클래스를 값 객체(value object)라 부르며,
다양한 언어가 값 객체를 간결하게 기술할 수 있는 구문을 제공한다.
위 코드를 바이트 코드로 변환한 후, 디컴파일 하면 아래와 같은 자바 코드가 나오게 된다.

```java
public final class Person {
   @NotNull
   private final String name;

   @NotNull
   public final String getName() {
      return this.name;
   }

   public Person(@NotNull String name) {
      Intrinsics.checkNotNullParameter(name, "name");
      super();
      this.name = name;
   }
}
```

`null` 체킹하는 구문 이외에 완전히 동일한 코드인 것을 알 수 있으며,
가시성 변경자(visibility modifier)가 없음에도 기본적으로 `public`이 들어간 것을 알 수 있다.

### 프로퍼티

클래스라는 개념의 목적은 데이터를 캡슐화(encapsulate)하고,
그 데이터를 다루는 코드를 한 주체 아래 가두는 것이다.

자바에서는 데이터를 필드(field)에 저장하며, 멤버 필드의 가시성은 보통 비공개(private)이다.
그렇기 때문에 필드를 읽기 위한 `getter`를 제공하고, 필요에 따라 `setter`를 추가해 값을 수정하게 할 수 있다.
이렇게 필드와 접근자를 한데 묶어 프로퍼티(property)라 부른다.

위 코드에서 본 것과 같이 코틀린의 프로퍼티는 클래스 생성자에 선언할 수 있다.

```kotlin
class Person(
    val name: String,
    var isMarried: Boolean
)
```

`val`은 읽기 전용으로, `getter`만 만들어내며, `var`은 수정할 수 있기 때문에 `getter`, `setter`를 모두 만들어낸다.

```java
public final class Person {
   @NotNull
   private final String name;
   private boolean isMarried;

   @NotNull
   public final String getName() {
      return this.name;
   }

   public final boolean isMarried() {
      return this.isMarried;
   }

   public final void setMarried(boolean var1) {
      this.isMarried = var1;
   }

   public Person(@NotNull String name, boolean isMarried) {
      Intrinsics.checkNotNullParameter(name, "name");
      super();
      this.name = name;
      this.isMarried = isMarried;
   }
}
```

디컴파일하면 위와 같이 `isMarried`만 `setter`를 가진 것을 볼 수 있다.

#### 객체 프로퍼티 접근

```java
JavaPerson person = new JavaPerson("Bob", true);
System.out.println(person.getName());
System.out.println(person.isMarried());
```

```kotlin
val person = Person("Bob", true)
println(person.name)
println(person.isMarried)
```

자바의 경우 `getField`와 같이 접근을 했지만, 코틀린에서는 필드에 바로 접근을 했다.
또 이 코드를 디컴파일하면 실제 필드에 접근하는 것이 아닌, `getField`를 하고 있다는 것을 알 수 있다.

```java
public static final void main(@NotNull String[] args) {
    Person person = new Person("Bob", true);
    String var2 = person.getName();
    System.out.println(var2);
    boolean var3 = person.isMarried();
    System.out.println(var3);
}
```


#### 커스텀 접근자

대부분의 프로퍼티에는 그 프로퍼티의 값을 저장하기 위한 필드가 있다.
이를 프로퍼티를 뒷받침하는 필드(Backing field)라 부른다.

```kotlin
class Rectangle(val height: Int, val width: Int) {
    val isSquare: Boolean
        // get() = height == width
        get() {
            return height == width
        }
}
```

```kotlin
fun main() {
    val rect = Rectangle(100, 200)
  
    // 에러 발생 : Val cannot be reassigned
    rect.isSquare = false
    
    println(rect.isSquare)
}
```

`isSquare` 프로퍼티에는 자체 값을 저장하는 필드가 필요 없다.
해당 프로퍼티는 `val`로 선언되어 있기 때문에 `getter`만 선언할 수 있고,
필요에 따라 정사각형 여부를 그때그때 다시 계산한다.

#### 소스코드 구조 : 디렉토리와 패키지

자바와 같이 코틀린도 클래스를 패키지 단위로 관리한다.

```kotlin
import java.util.Random

fun createRandomRectangle() : Rectangle {
    val random = Random()
    return Rectangle(random.nextInt(), random.nextInt())
}
```

```kotlin
import geometry.shapes.Rectangle
import geometry.shapes.createRandomRectangle

fun main() {
    val randRect = createRandomRectangle()
    println(randRect.isSquare)
}
```

위와 같이 랜덤한 크기의 `Rectangle`을 반환하는 함수를 만든 뒤, 
다른 패키지에 있는 파일에서 해당 함수를 호출하면 `import`가 되는 것을 볼 수 있다.
또한, 자바와 동일하게 패키지 이름 뒤에 `.*`을 사용하면 패키지 안의 모든 선언을 임포트할 수 있다.

## enum과 when

자바와 동일하게 `enum`을 선언해보자.

```kotlin
enum class Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET
}
```

다른 점이 있다면, 자바에서는 `enum`으로 표현하지만,
코틀린에서는 `enum class`으로 선언하게 된다.
그 이유는 코틀린에서의 `enum`은 소프트 키워드(soft keyword)이기 때문이다.

```kotlin
val enum = 10
```

> 소프트 키워드(soft keyword)란, 특정 단어가 있을 때만 특별한 의미를 지니고, 그 외에는 변수 이름으로 활용할 수 있다.
> 일반 키워드의 경우 변수 이름으로 활용하지 못한다.<br>
> 예) `class`의 경우 키워드이기 때문에 변수 이름으로 사용하려면 `clazz` 혹은 `aClass`라 표현해야 한다.

`enum`은 자바와 마찬가지로 단순히 값만 열거하는 존재가 아니고,
프로퍼티나 메소드를 정의할 수 있다.

```kotlin
enum class Color (
    val r: Int,
    val g: Int,
    val b : Int
) {
    RED(255, 0, 0), ORANGE(255, 165, 0), 
    YELLOW(255, 255, 0), GREEN(0, 255, 0), BLUE(0, 0, 255),
    INDIGO(75, 0, 130), VIOLET(238, 130, 238);
    
    fun rgb() = (r * 256 + g) * 256 + b
}
```

일반적인 클래스와 마찬가지로 생성자와 프로퍼티를 선언할 수 있으며,
상수를 정의할 때는 그 상수에 해당하는 프로퍼티 값을 지정해야 한다.

### when으로 enum 다루기

> `when`은 자바의 `switch`문과 동일하다.

무지개의 색을 기억하기 위한 연상법으로 'Richard Of York Gave Battle In Vain'을 주로 사용한다.
각 단어의 첫 글자만 가져오면 '빨주노초파남보'가 된다.
각 무지개 색에 대해 상응하는 연상 단어를 짝지어주는 코드를 작성해보자.

```kotlin
fun main() {
    println(getMnemonic(Color.BLUE))
}

fun getMnemonic(color: Color) =
    when (color) {
        Color.RED -> "Rechard"
        Color.ORANGE -> "Of"
        Color.YELLOW -> "York"
        Color.GREEN -> "Gave"
        Color.BLUE -> "Battle"
        Color.INDIGO -> "In"
        Color.VIOLET -> "Vain"
    }
```

`if`와 마찬가지로 `when`도 값을 만들어내는 식으로 활용할 수 있다.
또한, 자바와 달리 각 분기 끝에 `break`를 넣지 않아도 된다.

> 자바에서도 switch-case를 Expression으로 사용이 가능하다.
> [JDK 12(non-LTS)](https://openjdk.org/jeps/325)때 1차 Preview로 나오고,
> [JDK 13(non-LTS)](https://openjdk.org/jeps/354)에 2차 Preview로 등장한 뒤,
> JDK 17(LTS)에 정식으로 추가되었다.

또한, 한 분기에 여러 값을 사용하는 방법도 자바와 동일하다.

```kotlin
fun main() {
    println(getWarmth(Color.ORANGE))
}

fun getWarmth(color: Color) = when (color) {
    Color.RED, Color.ORANGE, Color.YELLOW -> "warm"
    Color.GREEN -> "neutral"
    Color.BLUE, Color.INDIGO, Color.VIOLET -> "cold"
}
```

여기서 중복되는 `Color` 클래스를 import하면 더 깔끔하게 코드를 작성할 수 있게된다.

```kotlin
import action.chapter02.Color.*

fun main() {
    println(getWarmth(ORANGE))
}

fun getWarmth(color: Color) = when (color) {
    RED, ORANGE, YELLOW -> "warm"
    GREEN -> "neutral"
    BLUE, INDIGO, VIOLET -> "cold"
}
```

### when과 임의의 객체를 함께 사용

코틀린에서의 `when`은 자바의 `switch`보다 훨씬 더 강력하다.
자바에서는 분기 조건에 상수(enum, 상수, 숫자 리터럴)만 사용할 수 있지만,
코틀린의 `when` 분기 조건은 임의의 객체를 허용한다.

```kotlin
fun main() {
    println(mix(BLUE, YELLOW))
}

fun mix(c1: Color, c2: Color) = when (setOf(c1, c2)) {
    setOf(RED, YELLOW) -> ORANGE
    setOf(YELLOW, BLUE) -> GREEN
    setOf(BLUE, VIOLET) -> INDIGO
    else -> throw IllegalArgumentException("Dirty color")
}
```

위 함수는 두 색을 혼합했을 때, 미리 정해진 팔레트에 들어있는 색이 될 수 있는지 알 수 있는 함수이다.
여기서 사용한 `setOf()`는 우리가 흔히 알 고 있는 `HashSet`으로 두 색을 담은 것이라고 생각하면 된다.

`when` 식은 인자 값과 매치하는 조건 값을 찾을 때까지 각 분기를 검사하며,
분기 조건에 있는 객체 사이를 매체할 때, 동등성(equality)을 사용한다.

### 인자 없는 when 사용

위에서 본 `mix` 함수는 약간 비효율적으로 작동한다.
함수가 호출될 때마다 함수 인자로 주어진 두 색이 `when`의 분기 조건에 있는 다른 두 색과 같은지 비교하기 위해 여러 `set` 인스턴스를 생성한다.
만약 이 함수가 자주 호출된다면, 불필요한 가비지 객체가 늘어나게 된다.

```kotlin
fun mixOptimized(c1: Color, c2: Color) = when {
    (c1 == RED && c2 == YELLOW) ||
    (c1 == YELLOW && c2 == RED) -> 
        ORANGE
    (c1 == YELLOW && c2 == BLUE) ||
    (c1 == BLUE && c2 == YELLOW) -> 
        GREEN
    (c1 == BLUE && c2 == VIOLET) ||
    (c1 == VIOLET && c2 == BLUE) -> 
        INDIGO
    else -> throw IllegalArgumentException("Dirty color")
}
```

위와 같이 `when`에 아무런 인자를 넣지 않으려면,
각 분기의 조건이 `boolean` 결과를 계산하는 식이어야 한다.

### 스마트 캐스트 : 타입 검사와 타입 캐스트를 조합

(1 + 2) + 4와 같은 식을 인코딩하는 방법을 생각해보자.
우선, 식을 트리 구조로 저장하고, 노드는 합계(Sum)나 수(Num) 중에 하나이며,
Num은 항상 말단(leaf or terminal) 노드지만, Sum은 자식이 있는 중간(non-terminal) 노드이다.

```kotlin
interface Expr

class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr
```

`Expr` 인터페이스에는 아무런 함수를 정의하지 않고, `Sum`, `Num` 클래스는 이 인터페이스를 구현한다.
`Sum`은 `Expr`의 왼쪽과 오른쪽 인자에 대한 참조를 `left`, `right` 프로퍼티로 저장한다.

즉, `left`, `right`는 각각 `Num`이나 `Sum`이 될 수 있는 것이다.

```kotlin
(1 + 2) + 4
Sum(Sum(Num(1), Num(2)), Num(4))
```

위 식의 값을 계산하기 위해서는 아래 두 가지 경우를 고려해야 한다.

- 어떤 식이 수라면 그 값을 반환한다.
- 어떤 식이 합이라면 좌항과 우항의 값을 계산한 다음, 두 값을 합한 값을 반환한다.

아래는 자바 스타일로 작성한 함수이다.

```kotlin
fun eval(e: Expr): Int {
    if (e is Num) {
        val n = e as Num
        return n.value
    } else if (e is Sum) {
        return eval(e.right) + eval(e.left)
    }
    throw IllegalArgumentException("Unknown expression")
}
```

```java
public static int eval(Expr e) throws IllegalAccessException {
    if (e instanceof Num) {
        final Num n = (Num) e;
        return n.getValue();
    } else if (e instanceof Sum) {
        return eval(((Sum) e).getRight()) + eval(((Sum) e).getLeft());
    }
    throw new IllegalAccessException("Unknown expression");
}
```

코틀린 코드의 `if`문을 보면 알 수 있듯, `e`가 `Num`인지 확인한 후 `e as Num`으로 다시 형변환을 한다.
하지만 `else if`문을 보면 타입 검사 이후 원하는 타입으로 캐스팅하지 않아도 마치 처음부터 그 변수가 원하는 타입으로 선언된 것처럼 사용할 수 있다.
이는 컴파일러가 캐스팅을 수행해주기 때문에 가능한 것이며, 이를 **스마트 캐스트(smart cast)**라고 부른다.

<center>

<img src="https://github.com/Jwhyee/kotlin-ps/assets/82663161/df4bd975-5f48-45b7-8062-6b4ea48ad9a2">

</center>

```java
// https://openjdk.org/jeps/394
// JDK 16부터 지원하는 패턴 매칭 기능
if (e instanceof Num n) {
    return n.getValue();
}
```

스마트 캐스트를 사용할 때 주의할 점은 해당 프로퍼티가 반드시 `val`이어야 하며, 커스텀 접근자를 사용한 것이어도 안 된다.
`val`이 아니거나, `val`이지만 커스텀 접근자를 사용하는 경우에는 해당 프로퍼티에 대한 접근이 항상 같은 값을 내놓는다고 확실할 수 없기 때문이다.

```kotlin
class Sum(left: Expr, right: Expr) : Expr {
    var left = left
        get() = field
    var right = right
}

fun eval(e: Expr): Int {
    if (e is Num) { 
        var n = e as Num
        return n.value
    }
    ...
}
```

위와 같이 코드를 작성해도 컴파일러가 에러를 내주지는 않지만, `var n`에 대한 부분은 `val`로 수정하라고 경고를 내뿜는다.

### 리팩토링 : if를 when으로 변경

앞서 살펴본 것처럼 코틀린의 `if`는 자바의 삼항 연산자처럼 식(Expression)으로 사용이 가능하다.

```kotlin
fun eval(e: Expr): Int =
    if (e is Num) e.value
    else if (e is Sum) eval(e.right) + eval(e.left)
    else throw IllegalArgumentException("Unknown expression")
```

위 코드도 충분히 깔끔하게 보이지만, `e is`가 중복되어 나오고 있다.
나중에 `Mul`, `Div` 등과 같은 코드가 추가되면 보기 흉한 코드가 될 수 있으니 이를 `when`으로 수정해보자.

```kotlin
fun eval(e: Expr): Int = when (e) {
    is Num -> e.value
    is Sum -> eval(e.right) + eval(e.left)
    else -> throw IllegalArgumentException("Unknown expression")
}
```

중복된 검사를 제거함으로써 더 깔끔하게 코드가 변한 것을 볼 수 있다.
`when` 식을 앞서 본 동등성 검사가 아닌 다른 기능에도 사용할 수 있다.
`if` 예제와 마찬가지로 타입을 검사하고 나면 스마트 캐스트가 이뤄지므로, 변수를 강제로 캐스팅할 필요가 없다.

### if와 when 분기에서 블록 사용

식으로 사용하는 `if`나 `when` 모두 분기에 블록을 사용할 수 있다.
그런 경우 블록의 마지막 문장이 블록 전체의 결과로 반환된다.

```kotlin
fun evalWithLogging(e: Expr): Int = when (e) {
    is Num -> {
        println("num : ${e.value}")
        e.value
    }
    is Sum -> {
        val left = evalWithLogging(e.left)
        val right = evalWithLogging(e.left)
        println("sum : $left + $right")
        left + right
    }
    else -> throw IllegalArgumentException("Unknown expression")
}
```

**블록의 마지막 식이 블록의 결과**라는 규칙은 블록이 값을 만들어내야 하는 경우 항상 성립한다.
이 규칙은 함수에 대해서는 성립하지 않으며, 식이 본문인 함수에서만 사용이 가능하다.

## 대상을 이터레이션 : for 루프

코틀린에서는 자바의 `for` 루프(어떤 변수를 초기화하고, 그 변수를 루프를 한 번 실행할 때마다 갱신하고, 조건이 거짓이 될 때 반복을 마치는 형태의 루프)에 해당하는 요소가 없다.
즉, 초기값, 범위, 증감연산자를 대신해 범위(Range)를 사용한다.

```kotlin
val oneToDown = 1 .. 10
```

코틀린의 범위는 폐구간(닫힌 구간) 또는 양 끝을 포함하는 구간이다.
위 코드에서 두 번째 값(10)이 항상 범위에 포함된다는 뜻이다.
이와 같이 어떤 범위에 속한 값을 일정한 순서로 이터레이션하는 경우를 수열(Progression)이라고 부른다.

아래 코드는 피즈버즈 게임에 대한 코드이다.
피즈버즈 게임은 순차적으로 수를 세면서 3으로 나눠 떨어지는 수에 대해서는 피즈, 5로 나눠 떨어지는 수에 대해서는 버즈라고 말해야 한다.
만약, 3과 5로 모두 나눠 떨어진다면 피즈버즈라고 말해야 한다.

```kotlin
fun main() {
  for (i in 1..100) {
    println(fizzBuzz(i))
  }
}

fun fizzBuzz(i: Int) = when {
    i % 15 == 0 -> "FizzBuzz"
    i % 3 == 0 -> "Fizz"
    i % 5 == 0 -> "Buzz"
    else -> "$i"
}
```

위에서 본 것과 같이 `1 .. 100`을 사용하게 되면 1부터 100까지 1씩 증가하며, 변수 `i`를 활용해 반복한다는 것이다.
이번에는 반대로 100에서 1까지 2씩 감소하는 코드를 확인해보자.

```kotlin
fun main() {
    for (i in 100 downTo 1 step 2) {
        print("${fizzBuzz(i)} ")
    }
}
```

`100 downTo 1`을 사용하면, 1 ~ 100까지의 수열을 반대로 뒤집어 역방향 수열을 만들어준다.
또한, `step` 키워드를 사용해 값이 2씩 움직이도록 만들었다.

추가적으로 `..`을 사용하면 항상 범위의 끝 값을 포함하게 되는데, 
만약 끝 값을 포함하지 않는 반만 닫힌 범위(half-closed range, 반폐구간 또는 반개구간)에 대해서는 아래와 같이 작성한다.

```kotlin
for (i in 1 until 100) {
    print("${fizzBuzz(i)} ")
}
```

### Map에 대한 이터레이션

아래 자바 코드는 이진 표현을 맵에 저장한 뒤, 출력하는 코드이다.

```java
public class MapTest {
    public static void main(String[] args) {
        Map<Character, String> binaryReps = new TreeMap<>();
        for (char i = 'A'; i < 'F'; i++) {
            binaryReps.put(i, Integer.toBinaryString(i));
        }
        for (Map.Entry<Character, String> entry : binaryReps.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}
```

`Map`에 대한 이터레이션을 돌기 위해 `Map.Entry`로 감싼, `entry` 객체를 통해 key, value를 가져오게 된다.

```kotlin
fun main() {
    val binaryReps = TreeMap<Char, String>()
    for (c in 'A'..'F') {
        val binary = Integer.toBinaryString(c.code)
        binaryReps[c] = binary
    }
    for ((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }
}
```

코틀린의 경우 A ~ F까지 반복하도록 `Range`를 지정해 반복하였고, 자바와 다른 점이 있다면, 맵에 값을 추가할 때, 배열과 같이 사용했다는 점이다.
`binaryReps[c] = binary` 코드에서 `[c]`는 키를 의미하고, `binary`는 값을 의미한다.
또한, 맵에 대한 이터레이션을 보면 `entry` 객체로 사용해 값을 꺼내는 것이 아닌, 바로 `(key, value)` 형태로 꺼내 사용할 수 있다.

이러한 구조 분해 구문을 맵이 아닌 다른 컬렉션에도 활용할 수 있다.

> 구조 분해 구문이란, 객체가 갖고 있는 여러 값을 분해하여 변수에 한 꺼번에 초기화하는 방식

```kotlin
fun main() {
    val list = arrayListOf("10", "11", "1001")
    /*
    for (element in list) {
        print("$element ")
    }
    */
    for ((idx, element) in list.withIndex()) {
        println("$idx : $element")
    }
}
```

원래 리스트를 이터레이션할 때에는 주석 내부에 있는 코드처럼 사용하지만,
`withIndex`를 사용할 경우 인덱스와 함께 컬렉션을 이터레이션할 수 있도록 도와준다.

### in으로 컬렉션이나 범위의 원소 검사

`in` 연산자를 사용하면 어떤 값이 범위에 속하는지 검사할 수 있으며, 반대로 `!in`을 사용할 경우 속하지 않는지 확인할 수 있다.

```kotlin
fun main() {
    println(isLetter('q'))
    println(isNotDigit('x'))
}

fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
fun isNotDigit(c: Char) = c !in '0'..'9'
```

위 코드처럼 `in`과 `..` 연산을 함께 사용할 경우 `'a' <= c && c <= 'z'`로 변환된다.
이러한 `in`, `!in` 연산자는 `when` 식에서도 사용할 수 있다.

```kotlin
fun main() {
  println(recognize('가'))
  println(recognize('8'))
}

fun recognize(c: Char) = when (c) {
    in '0'..'9' -> "It's a digit!"
    in 'a'..'z', in 'A'..'Z' -> "It's a letter!"
    else -> "IDK"
}
```

위와 같이 문자에 사용할 수도 있고, 비교가 가능한 클래스(Comparable)라면 그 클래스의 인스턴스 객체를 사용해 범위를 만들 수 있다.

```kotlin
// false
println("ab" in "ac".."zz")
// true
println("Kotlin" in "Java".."Scala")
// false
println("Kotlin" in setOf("Java", "Scala"))
```

문자열은 해당 범위에 있는 모든 문자열을 이터레이션을 할 수는 없지만, `in` 연산자를 사용하면 값이 범위 안에 속하는지 확인할 수 있다.

## 예외 처리
