# 4장 클래스, 객체, 인터페이스

코틀린의 클래스와 인터페이스는 자바 클래스, 인터페이스와는 약간 다르다. 예를 들어 인터페이스에서 프로퍼티 선언이 들어갈 수 있다. (Projection에서 사용 가능)

자바와 달리 코틀린 선언은 기본적으로 final이며 public 이다. 게다가 중첩 클래스는 기본적으로 내부 클래스가 아니다. 즉 코틀린 중첩 클래스에는 외부 클래스에 대한 참조가 없다.

## 클래스 계층 정의

### 코틀린 인터페이스

코틀린 인터페이스는 자바 8 인터페이스와 비슷하다. 코틀린 인터페이스 안에서는 추상 메소드뿐 아니라 구현이 있는 메소드도 정의할 수 있다. 다만 인터페이스에는 아무런 상태(필드)도 들어갈 수 없다.

```kotlin
interface User {
	String getUserName();
}

interface User {
	val userName: String;
}

data class User(
	val userName: String;
)
```

```kotlin
interface Clickable {
    fun click()
    fun showOff() = println("I'm clickable!")
}

class Button : Clickable {
    override fun click() = println("I was clicked")
}
```

자바에서는 `extends`와 `implements` 키워드를 사용하지만, 코틀린에서는 클래스 이름 뒤에 콜론(:)을 붙이고 인터페이스와 클래스 이름을 적는 것으로 클래스 확장과 인터페이스 구현을 모두 처리한다.

자바와 마찬가지로 클래스는 인터페이스를 원하는 만큼 개수 제한 없이 마음대로 구현할 수 있지만, 클래스는 오직 하나만 확장할 수 있다.

자바의 `@Override` 어노테이션과 비슷한 `override` 변경자는 상위 클래스나 상위 인터페이스에 있는 프로퍼티나 메소드를 오버라이드한다는 표시다. 하지만 자바와 달리 코틀린에서는 `override` 변경자를 꼭 사용해야한다. `override` 변경자는 실수로 상위 클래스에 있는 메소드와 시그니처가 같은 메소드를 우연히 하위 클래스에서 선언하는 경우 컴파일이 안 되기 때문에 `override`를 붙이거나 메소드 이름을 바꿔야만 한다.

클래스가 구현하는 두 상위 인터페이스에 정의된 메소드 구현을 대체할 오버라이딩 메소드를 직접 제공하지 않으면 다음과 같은 컴파일러 오류가 발생한다.

```kotlin
interface Clickable {
    fun click()
    fun showOff() = println("I'm clickable!")
}

interface Focusable {
    fun setFocus(b: Boolean) = println("I ${if (b) "got" else "lost"} focus")
    fun showOff() = println("I'm focusable!")
}

class Button : Clickable, Focusable {
    override fun click() = println("I was clicked")
}

/*
Class 'Button' must override public open fun showOff(): Unit defined in Clickable because it inherits multiple interface methods of it
 */
```

```kotlin
class Button : Clickable, Focusable {
    override fun click() = println("I was clicked")
    override fun showOff() {
// 하위 클래스에서 명시적으로 새로운 구현을 제공해야 한다.
        super<Clickable>.showOff() // 상위 타입의 이름을 꺾쇠 괄호(<>) 사이에 넣어서 "super"를 지정하면 어떤 상위 타입의 멤버 메소드를 호출할지 지정할 수 있다.
        super<Focusable>.showOff()
    }
}
```

### open, final, abstract 변경자: 기본적으로 final

자바에서는 `final`을 명시적으로 상속을 금지하지 않는 모든 클래스를 다른 클래스가 상속할 수 있다. 이렇게 기본적으로 상속이 가능하면 편리한 경우도 많지만 문제가 생기는 경우도 많다.

취약한 기반 클래스(부모 클래스 변경에 의해 자식 클래스가 영향을 받는 현상, 캡슐화를 약화시키고 결합도를 높인다.)라는 문제는 하위 클래스가 기반 클래스에 대해 가졌던 가정이 기반 클래스 변경함으로써 깨져버린 경우에 생긴다.

어떤 클래스가 자신을 상속하는 방법에 대해 정확한 규칙(어떤 메소드를 어떻게 오버라이드해야 하는지 등)을 제공하지 않는다면 그 클래스의 클라이언트는 기반 클래스를 작성한 사람의 의도와 다른 방식으로 메소드를 오버라이드할 위험이 있다.

이펙티드 자바에서는 “상속을 위한 설계와 문서를 갖추거나, 그럴 수 없다면 상속을 금지하자”라는 조언을 한다. 이는 특별히 하위 클래스에서 오버라이드하게 의도된 클래스와 메소드가 아니라면 모두 `final`로 만들라는 뜻이다.

코틀린의 클래스와 메소드는 기본적으로 `final`이다 어떤 클래스의 상속을 허용하려면 클래스 앞에 `open` 변경자를 붙여야 한다. 그와 더불어 오버라이드를 허용하고싶은 메소드나 프로퍼티 앞에도 `open` 변경자를 붙여야 한다.

```kotlin
open class RichButton : Clickable {
    fun disable() {}
    open fun animate() {}
// final은 쓸데 없이 붙은 중복이 아니다. final이 없는 override 메소드나 프로퍼티는 기본적으로 열려있다.
    final override fun click() {} 
}
```

자바처럼 코틀린에서도 클래스는 abstract로 선언할 수 있다. abstract로 선언한 추상 클래스는 인스턴스화 할 수 없다. 추상 클래스에는 구현이 없는 추상 멤버가 있기 때문에 하위 클래스에서 그 추상 멤버를 오버라이드해야만 하는 게 보통이다.

| 변경자 | 이 변경자가 붙는 멤버는 | 설명 |
| --- | --- | --- |
| final | 오버라이드할 수 없음 | 클래스 멤버의 기본 변경자다. |
| open | 오버라이드할 수 있음 | 반드시 open을 명시해야 오버라이드할 수 있다. |
| abstract | 반드시 오버라이드해야 함 | 추상 클래스의 멤버에만 이 변경자를 붙일 수 있다.
추상 멤버에는 구현이 있으면 안된다. |
| override | 상위 클래스나 상위 인스턴스의 멤버를 오버라이드하는 중 | 오버라이드하는 멤버는 기본적으로 열려있다. 하위 클래스의 오버라이드를 금지하여면 final을 명시해야 한다. |

### 가시성 변경자(Visibility Modifier): 기본적으로 공개

가시성 변경자는 코드 기반에 있는 선언에 대한 클래스 외부 접근을 제어한다.

어떤 클래스의 구현에 대한 접근을 제한함으로써 그 클래스에 의존하는 외부 코드를 깨지 않고도 클래스 내부 구현을 변경할 수 있다.

코틀린의 기본 가시성은 자바와 다르게 아무 변경자도 없는 경우 선언은 모두 public이다.

코틀린은 패키지를 네임스페이스를 관리하기 위한 용도로 사용하기 때문에 자바의 기본 가시성인 package-private이 없고 대안으로는 internal이라는 키워드로 한 번에 한꺼번에 컴파일되는 코틀린 파일들을 의미한다.

모듈 내부 가시성은 모듈의 구현에 대해 진정한 캡슐화를 제공한다는 장점이 있다. 자바에서는 패키지가 같은 클래스를 선언하기만 하면 어떤 프로젝트의 외부에 있는 코드라도 패키지 내부에 있는 패키지 전용 선언에 쉽게 접근할 수 있다. → 모듈의 캡슐화 깨짐 (java에서 적용된 패키지 개념에서 패키지 명만 같은 접근이 가능하다)

```java
// apple.java
package test.apple;

class Apple {
}

// banana.java
package test.apple;

class Banana {

	test() {  }
}

```

또한 코틀린은 최상위 선언에 대해 private 가시성을 허용해서, 그 선언이 들어있는 파일 내부에서만 사용가능하게 할 수 있다.

| 변경자 | 클래스 멤버 | 최상위 선언 |
| --- | --- | --- |
| public | 모든 곳에서 볼 수 있다. | 모든 곳에서 볼 수 있다. |
| internal | 같은 모듈 안에서만 볼 수 있다. | 같은 모듈 안에서만 볼 수 있다. |
| protected | 하위 클래스 안에서만 볼 수 있다. | (최상위 선언에 적용할 수 없음) |
| private | 같은 클래스 안에서만 볼 수 있다. | 같은 파일 안에서만 볼 수 있다. |

```kotlin
internal open class TalkativeButton : Focusable {
    private fun yell() = println("Hey!")
    protected fun whisper() = println("Let's talk!")
}

fun TalkativeButton.giveSpeech() { //에러: public 맴버가 internal을 노출시킴
    yell() //에러: yell은 TalkativeButton에서 private으로 선언됨
    whisper() //에러: whisper은 TalkativeButton에서 protected로 선언됨
}
```

- protected 를 갖고 있는 클래스를 상속하면 사용가능
- 확장함수는 protected, private 접근 불가

### 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스

자바처럼 코틀린에서도 클래스 안에 다른 클래스를 선언할 수 있다. 도움이 클래스를 캡슐화하거나 코드 정의를 그 코드를 사용하는 곳 가까이에 두고 싶을 때 유용한다.

자바와의 차이는 코틀린의 중첩 클래스는 명시적으로 요청하지 않는 한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없다는 것이다.

```kotlin
interface State: Serializable

interface View {
		fun getCurrentState(): State
		fun restoreState(state: State) { }
}
```

```kotlin
public class Button implements View {
		@Override
		public State getCurrentState() {
				return new ButtonState();
		}
		
		@Override
		public void restoreState(State state) { /*...*/ }
		public class ButtonState implements State { /*...*/ }
}
```

이 코드의 어디가 잘못된걸까?

왜 선언한 버튼의 상태를 직렬화하면 `java.io.NotSerializableException: Button`이라는 오류가 발생할까? 직렬화하려는 변수는 ButtonState 타입의 state였는데 왜 Button을 직렬화할 수 없다는 예외가 발생할까?

자바에서 다른 클래스 안에 정의한 클래스는 자동으로 내부 클래스가 된다.

이 예제의 ButtonState 클래스는 바깥쪽 Button 클래스에 대한 참조를 묵시적으로 포함한다. 그 참조로 인해 ButtonState를 직렬화할 수 없다. Button을 직렬화할 수 없으므로 버튼에 대한 참조가 ButtonState의 직렬화를 방해한다.

→ 외부 클래스를 먼저 초기화한 후 내부 클래스를 초기화한다. ([https://inpa.tistory.com/entry/JAVA-☕-자바의-내부-클래스는-static-으로-선언하자#inner_클래스는_외부_참조를_한다](https://inpa.tistory.com/entry/JAVA-%E2%98%95-%EC%9E%90%EB%B0%94%EC%9D%98-%EB%82%B4%EB%B6%80-%ED%81%B4%EB%9E%98%EC%8A%A4%EB%8A%94-static-%EC%9C%BC%EB%A1%9C-%EC%84%A0%EC%96%B8%ED%95%98%EC%9E%90#inner_%ED%81%B4%EB%9E%98%EC%8A%A4%EB%8A%94_%EC%99%B8%EB%B6%80_%EC%B0%B8%EC%A1%B0%EB%A5%BC_%ED%95%9C%EB%8B%A4))

```kotlin
public class Outer_Class {
   int field = 10;
   int getField() {
       return field;
   }

   class Inner_Class {
       int inner_field = 20;
       int getOuterfield() {
           return Outer_Class.this.getField(); // 숨은 외부 참조가 있기 때문에 가능
       }
   }
}
```

이 문제를 해결하려면 ButtonState를 static 클래스로 선언해야 한다. 자바에서 중첩 클래스를 static으로 선언하면 그 클래스를 둘러싼 바깥쪽 클래스에 대한 묵시적인 참조가 사라진다.

코틀린에서 중첩 클래스가 기본적으로 동작하는 방식은 static 중첩 클래스와 같다.

```kotlin
class Button: View {
    override fun getCurrentState(): State = ButtonState()
    override fun restoreState(state: State) { }
    class ButtonState: State
}
```

```kotlin
public final class Button implements View {
   @NotNull
   public State getCurrentState() {
      return (State)(new ButtonState());
   }

   public void restoreState(@NotNull State state) {
      Intrinsics.checkNotNullParameter(state, "state");
   }

   @Metadata(
      mv = {1, 9, 0},
      k = 1,
      d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0003"},
      d2 = {"LButton$ButtonState;", "LState;", "()V", "kotlin-in-action"}
   )
   public static final class ButtonState implements State {
   }
}
```

| 클래스 B 안에 정의된 클래스 A | 자바에서는 | 코틀린에서는 |
| --- | --- | --- |
| 중첩 클래스 (바깥쪽 클래스에 대한 참조를 저장하지 않음) | static class A | class A |
| 내부 클래스(바깥쪽 클래스에 대한 참조를 저장함) | class A | inner class A |

코틀린에서 바깥쪽 클래스의 인스턴스를 가리키는 참조를 표기하는 방법도 자바와 다르다.

```kotlin
class Duck {
    private var duckName: String = "Donald Duck"
    inner class DuckNamePrinter {
        fun getOuterReference(): Duck = this@Duck
    }
}
```

### 봉인된 클래스: 클래스 계층 정의 시 계층 클래스 확장 제한

상위 클래스인 Expr에는 숫자를 표현하는 Num과 덧셈 연산을 표현하는 Sum이라는 두 하위 클래스가 있다. when 식에서 이 모든 하위 클래스를 처리하면 편리하다. 하지만 when 식에서 Num과 Sum이 아닌 경우를 처리하는 else 분기를 반드시 넣어줘야만 한다.

```kotlin
interface Expr
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr

fun eval(e: Expr): Int =
    when (e) {
        is Num -> e.value
        is Sum -> eval(e.right) + eval(e.left)
        else -> // "else" 분기가 꼭 있어야 한다. 
            throw IllegalArgumentException("Unknown expression")
    }
```

항상 디폴트 분기를 추가하는 게 편하지는 않다. 그리고 디폴트 분기가 있으면 이런 클래스 계층에 새로운 하위 클래스를 추가하더라도 컴파일러가 when이 모든 경우를 처리하는지 제대로 검사할 수 없다. 혹 실수로 새로운 클래스 처리를 잊어버렸더라도 디폴트 분기가 선택되기 때문에 심각한 버그가 발생할 수 있다.

코틀린은 이런 문제에 대한 해법을 제공한다. sealed 클래스가 그 답이다. 상위 클래스에 sealed 변경자를 붙이면 그 상위 클래스를 상속한 하위 클래스 정의를 제한할 수 있다. sealed 클래스의 하위 클래스를 정의할 때는 반드시 상위 클래스 안에 중첩시켜야 한다.

```kotlin
sealed class Expr {
    class Num(val value: Int) : Expr()
    class Sum(val left: Expr, val right: Expr) : Expr()
}

fun eval(e: Expr): Int =
    when (e) {
        is Expr.Num -> e.value
        is Expr.Sum -> eval(e.right) + eval(e.left)
    }
```

// seal factory method랑 error

> `sealed`는 너무 제약이 심하다. 예를 들어 모든 하위 클래스는 중첩 클래스여야 하고, 데이터 클래스로 `sealed` 클래스를 상속할 수도 없다. 코틀린 1.1부터는 이 제한이 완화됐다. 봉인된 클래스와 같은 파일의아무데서나 봉인된 클래스를 상속한 하위 클래스를 만들 수 있고, 데이터 클래스로 하위 클래스를 정의할 수도 있다.****
>

## 뻔하지 않은 생성자와 프로퍼티를 갖는 클래스 선언

### 클래스 초기화: 주 생성자와 초기화 블록

```kotlin
class User constructor(_nickname: String) { // 파라미터가 하나만 있는 주 생성자
	val nickName: String

	init { // 초기화 블록
		nickName = _nickName
	}
}
```

constructor 키워드는 주 생성자난 부 생성자 정의를 시작할 때 사용한다. init 키워드는 객체가 만들어질 때 초기화 코드가  들어간다.

클래스에 기반 클래스가 있다면 주 생성자에서 기반 클래스의 생성자를 호출해야할 필요가 있다. 기반 클래스를 초기화하려면 기반 클래스 이름 뒤에 괄호를 치고 생성자 인자를 넘긴다.

```kotlin
open class User(val nickname: String) {}

class TwitterUser(nickname: String): User(nickname) {}
```

### 부 생성자: 상위 클래스를 다른 방식으로 초기화

일반적으로 코틀린에서는 생성자가 여럿 있는 경우가 자바보다 훨씬 적다. 자바에서 오버로드한 생성자가 필요한 상황중 상당수는 코틀린의 디폴트 파라미터 값과 이름붙은 인자 문법을 사용해 해결할 수 있다.

하지만 부 생성자가 필요한 다른 경우도 있다. 클래스 인스턴스를 생성할 때 파라미터 목록이 다른 생성 방법이 여럿 존재하는 경우에는 부 생성자를 여럿 둘 수밖에 없다.

### 인터페이스에 선언된 프로퍼티 구현

코틀린에서는 인터페이스에 추상 프로퍼티 선언을 넣을 수 있다.

```kotlin
interface User {
    val nickname: String
}
```

이는 User 인터페이스를 구현하는 클래스가 nickname을 넣을 수 있는 방법을 제공해야한다는 뜻이다.

```kotlin
class PrivateUser(override val nickname: String): User

class SubscribingUser(val email: String): User {
    override val nickname: String
        get() = email.substringBefore('@')
}
```

### 게터와 세터에서 뒷받침하는 필드에 접근

```kotlin
var counter = 0 // the initializer assigns the backing field directly
    set(value) {
        if (value >= 0)
            field = value
        // counter = value // ERROR StackOverflow: Using actual name 'counter' would make setter recursive
    }
```

### 접근자의 가시성 변경

```kotlin
class LengthCounter {
    var counter: Int = 0
        private set // 이 클래스 밖에서 이 프로퍼티의 값을 바꿀 수 없다.
    fun addWord(word: String) {
        counter += word.length
    }
}

/*
사용법
val lengthCounter = LengthCounter()
lengthCounter.addWord("Hi!")
println(lengthCounter.counter)
*/
```

### 컴파일러가 생성한 메소드: 데이터 클래스와 클래스 위임

자바 플랫폼에서는 클래스가 equals, hashCode, toString등의 메소드를 구현해야한다. 그리고 이런 메소드들은 보통 비슷한 방식으로 기계적으로 구현할 수 있다.

코틀린 컴파일러는 한걸음 더 나가서 이런 메소드를 기계적으로 생성하는 작업을 보이지 않는 곳에서 해준다.

```kotlin
data class Client(val name: String, val postalCode: Int)
```

Client 클래스는 자바에서 요구하는 모든 메소드를 포함한다.

- 인스턴스 간 비교를 위한 equals
- HashMap과 같은 해시 기반 컨테이너에서 키로 사용할 수 있는 hashCode
- 클래스의 각 필드를 선언 순서대로 표시하는 문자열 표현을 만들어주는 toString

equals와 hashCode는 주 생성자에 나열된 모든 프로퍼티를 고려해 만들어진다. 생성된 equals 메소드는 모든 프로퍼티 값의 동등성을 확인하다. hashCode는 모든 프로퍼티의 해시값을 바탕으로 계산한 해시 값을 반환한다. 이때 주 생성자 밖에 정의된 프로퍼티는 equals나 hashCode를 계산할 때 고려의 대상이 아니라는 사실에 유의하라

### 데이터 클래스와 불변성: copy() 메소드

데이터 클래스의 프로퍼티가 꼭 val일 필요는 없다. 원한다면 var 프로퍼티를 써도 된다. 하지만 데이터 클래스의 모든 프로퍼티를 읽기 전용으로 만들어서 데이터 클래스를 불변 클래스로 만들라고 권장한다. HashMap 등의 컨테이너에 데이터 클래스 객체를 담는 경우엔 불변성이 필수적이다.

데이터 클래스 객체를 키로 하는 값을 컨테이너에 담은 다음에 키로 쓰인 데이터 객체의 프로퍼티를 변경하면 컨테이너 상태가 잘못될 수 있다.

데이터 클래스 인스턴스를 불변 객체로 더 쉽게 활용할 수 있게 코틀린 컴파일러는 한 가지 편의 메소드를 제공한다. 그 메소드는 객체를 복사하면서 일부 프로퍼티를 바꿀 수 있게 해주는 copy 메소드다. 객체를 메모리 상에서 직접 바꾸는 대신 복사본은 만드는 편이 더 낫다.

### 클래스 위임: by 키워드 사용

대규모 객체지향 시스템을 설계할 때 시스템을 취약하게 만드는 문제는 보통 구현 상속에 의해 발생한다. 하위 클래스가 상위 클래스의 메소드 중 일부를 오버라이드하면 하위 클래스는 상위 클래스의 세부 구현 사항에 의존하게 된다.

코틀린을 설계하면서 우리는 이런 문제를 인식하고 기본적으로 클래스를 final로 취급하기로 결정했다. 모든 클래스를 기본적으로 final로 취급하면 상속을 염두에 두고 open 변경자로 열어둔 클래스만 확장할 수 있다.

하지만 종종 상속을 허용하지 않는 클래스에 새로운 동작을 추가해야 할 때가 있다. 이럴 때 사용하는 일반적인 방법이 데코레이터 패턴이다. 이 패턴의 핵심은 상속을 허용하지 않는 클래스 대신 사용할 수 있는 새로운 클래스를 만들되 기존 클래스와 같은 인터페이스를 데코레이터가 제공하게 만들고 기존 클래스를 데코레이터 내부에 필드로 유지하는 것이다.

이런 접근 방법의 단점은 준비 코드가 상당히 많이 필요하다는 점이다.

```kotlin
class DelegatingCollection<T>: Collection<T> {
    private val innerList = arrayListOf<T>()
    override val size: Int
        get() = innerList.size
    override fun isEmpty(): Boolean = innerList.isEmpty()
    override fun contains(element: T): Boolean = innerList.contains(element)
    override fun iterator(): Iterator<T> = innerList.iterator()
    override fun containsAll(elements: Collection<T>): Boolean = innerList.containsAll(elements)
}
```

이런 위임을 언어가 제공하는 일급 시민 기능으로 지원한다는 점이 코틀린의 장점이다.

인터페이스를 구현할 때 by 키워드를 통해 그 인터페이스에 대한 구현을 다른 객체에 위임중이라는 사실을 명시할 수 있다.

```kotlin
class DelegatingCollection<T>(
    innerList: Collection<T> = ArrayList<T>()
): Collection<T> by innerList {}
```

컴파일러가 전달 메소드를 자동으로 생성하며 자동 생성한 코드의 구현은 DelegatingCollection에 있던 구현과 비슷하다. 메소드 중 일부의 동작을 변경하고 싶은 경우 메소드를 오버라이드하면 컴파일러가 생성한 메소드 대신 오버라이드한 메소드가 쓰인다.

## object 키워드: 클래스 선언과 인스턴스 생성

코틀린에서는 object 키워드를 다양한 상황에서 사용하지만 모든 경우 클래스를 정의하면서 동시에 인스턴스를 생성한다는 공통점이 있다.

### 객체 선언: 싱글턴을 쉽게 만들기

생성자는 객체 선언에 쓸 수 없다. 일반 클래스 인스턴스와 달리 싱글턴 객체는 객체 선언문이 있는 위치에서 생성자 호출 없이 즉시 만들어진다. 따라서 객체 선언에는 생성자 정의가 필요 없다.

- 객체 선언은 싱글턴을 정의하는 방법 중 하나다.
- 동반 객체(companion object)는 인스턴스 메소드는 아니지만 어떤 클래스와 관련 있는 메소드와 팩토리 메소드를 담을 때 쓰인다.
- 객체 식은 자바의 무명 내부 클래스(anonymous inner class) 대신 쓰인다.

### 동반 객체: 팩토리 메소드와 정적 멤버가 들어갈 장소

코틀린 클래스 안에는 정적인 멤버가 없다. 그 대신 코틀린 에서는 패키지 수준의 최상위 함수와 객체 선언을 활용한다.

하지만 최상위 함수는 private로 표시된 클래스 비공개 멤버에 접근할 수 없다. 클래스 내부 정보에 접근 해야하는 함수가 필요할 때는 클래스에 중첩된 객체 선언의 멤버 함수로 정의해야 한다.

```kotlin
class User private constructor(val id: Int, val name: String, val address: String) {
    companion object {
        fun newSubscribingUser(email: String) = User(0, email.substringBefore('@'), email)
        fun newFacebookUser(accountId: Int) = User(accountId, getFacebookName(accountId), "")
    }
}
```

### 동반 객체를 일반 객체처럼 사용

동반 객체는 클래스 안에 정의된 일반 객체다. 따라서 동반 객체에 이름을 붙이거나 동반 객체가 인터페이스를 상속하거나, 동반 객체 안에 확장 함수와 프로퍼티를 정의할 수 있다.

```kotlin
class Person2(val name: String) {
    companion object Loader {
        fun fromJSON(jsonText: String): Person2 = Person2(jsonText)
    }
}

/* 디컴파일 시 아래와 같이 노출
@NotNull
   public static final Loader Loader = new Loader((DefaultConstructorMarker)null);
*/
```

### 동반 객체에서 인터페이스 구현하기

```kotlin
interface JSONFactory<T> {
    fun fromJSON(jsonText: String): T
}
 
 
class Person(val name: String) {
    companion object Loader : JSONFactory<Person> {
        override fun fromJSON(jsonText: String): Person = Person("Kim")
    }
}
```

클래스의 동반 객체는 일반 객체와 비슷한 방식으로 클래스에 정의된 인스턴스를 가리키는 정적 필드로 컴파일된 동반 객체에 이름을 붙이지 않았다면 자바 쪽에서 Companion이라는 이름으로 그 참조에 접근할 수 있다.

### 동반 객체 확장

```kotlin
class Person(val firstName: String, val lastName: String) {
    companion object {}
}

fun Person.Companion.fromJSON(json: String): Person {
    return Person("firstName", "lastName")
}
```

### 객체 식: 무명 내부 클래스를 다른 방식으로 작성

object 키워드를 싱글턴과 같은 객체를 정의하고 그 객체에 이름을 붙일 때만 사용하지 않는 무명 객체를 정의할 때도 object 키워드를 쓴다.

무명 객체는 자바의 무명 내부 클래스를 대신한다. 예를 들어 자바에서 흔히 무명 내부 클래스로 구현하는 이벤트 리스너를 코틀린에서 구현해보자.

```kotlin
window.addMouseListener(object: MouseAdapter() { // object 키워드를 사용해 익명 객체를 만든다.
    override fun mouseClicked(e: MouseEvent) { // 오버라이드한 메소드
        // ...
    }

    override fun mouseEntered(e: MouseEvent) {
        // ...
    }
})
```

객체 선언과 달리 무명 객체는 싱글턴이 아니다. 객체 식이 쓰일 때마다 새로운 인터페이스가 생성된다.

또한 자바의 무명 클래스와 같이 객체 식 안의 코드는 그 식이 포함된 함수의 변수에 접근할 수 있다. 하지만 자바와 달리 final이 아닌 변수도 객체 식 안에서 사용할 수 있다.

```kotlin

class { <- 생명주기 <- gc
	val value <- 불변하게만 사용가능
	object { <- 생명주기
		method() {
				print(value)
		}
	}
}

window.addMouseListener(object: MouseAdapter() { // object 키워드를 사용해 익명 객체를 만든다.
    var clickCount = 0 // 익명 객체 안에서 프로퍼티를 선언하고 초기화한다.
    override fun mouseClicked(e: MouseEvent) { // 오버라이드한 메소드
        clickCount++
    }

    override fun mouseEntered(e: MouseEvent) {
        // ...
    }
})
```

```kotlin
public static final void main() {
  final Ref.IntRef a = new Ref.IntRef();
    a.element = 10;
    (new Thread((Runnable)(new Runnable() {
      public final void run() {
        ++a.element;
      }
  }))).start();
}
```

스코프 외부 변수를 미리 사전에 정의된 스태틱 클래스(`kotlin.jvm.internal.Ref`)에 담아서 클로저가 참조합니다. 그래서 외부 변수에 자유롭게 접근할 수 있고 수정도 가능합니다

[https://jaeyeong951.medium.com/java-클로저-vs-kotlin-클로저-c6c12da97f94](https://jaeyeong951.medium.com/java-%ED%81%B4%EB%A1%9C%EC%A0%80-vs-kotlin-%ED%81%B4%EB%A1%9C%EC%A0%80-c6c12da97f94)