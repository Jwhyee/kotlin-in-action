코틀린 선언은 기본적으로 final이며 public 이다.
또한, 중첩 클래스는 기본적으로 내부 클래스가 아니다.

코틀린 컴파일러는 번잡스러움을 피하기 위해 유용한 메서드를 자동으로 만들어준다.
클래스를 data로 선언하면 컴파일러가 일부 표준 메서드를 생성해준다.

delegation을 사용하면 위임 처리를 위한 준비 메서드를 직접 작성할 필요가 없다.

클래스와 인스턴스를 동시에 선언하면서 만들 때 쓰는 object 키워드가 존재한다.
싱글턴 클래스, 동반 객체, 객체 식(anonymous class)를 표현할 때 object 키워드를 쓴다.

## 클래스 계층 정의
``` kotlin
interface Clickable {
	fun click()
}

class Button: Clickable {
	override fun click() = println("I was clicked")
}
```
자바에서는 상위 계층의 클래스를 상속받기 위해 extends와 implements 키워드를 사용하지만, 코틀린에서는 클래스 이름 뒤에 콜론(:)을 붙이고 클래스와 인터페이스 이름을 적는다.

인터페이스는 원하는 만큼 구현이 가능하지만 클래스는 오직 하나만 확장할 수 있다.

코틀린에서는 override 변경자를 꼭 사용해야한다.

인터페이스 메서드도 디폴트 구현을 제공할 수 있다.
자바에서는 default 키워드를 붙여 사용할 수 있지만 코틀린에서는 특별한 키워드를 사용하지 않고 메서드 본문을 메서드 시그니처 뒤에 추가하면된다.
``` kotlin
interface Clickable {
	fun click()
	fun showOff() = println("I'm clickable!")
}
```

동일한 메서드를 구현하는 다른 인터페이스가 존재하고 해당 인터페이스를 하나의 클래스에서 구현하면 어느쪽도 선택되지 않는다.
``` kotlin
class Button: Clickable, Focusable {
	override fun click() = println("...")
	override fun showOff() {
		super<Clickable>.showOff()
		super<Focusable>.showOff()
	}
}
```
위와 같이 코틀린 컴파일러는 두 메서드를 아우르는 구현을 하위 클래스에 직접 구현하게 강제한다.

### open, final, abstract 변경자: 기본적으로 final
자바에서는 기본적으로 final 클래스가 아니기때문에 모든 클래스를 다른 클래스가 상속할 수 있다.
이는 취약한 기반 클래스라는 문제를 가지고 있어 코틀린에서는 기본적으로 모든 클래스가 final 이다.

만약 어떤 클래스의 상속을 허용하려면 클래스 앞에 open 변경자를 붙여야 한다.
``` kotlin
open class RichButton: Clickable {
	fun disable() {} // 이 함수는 final 이기 때문에 오버라이드 할 수 없다.
	open fun animate() {} // open 되어있기 때문에 오버라이드 할 수 있다.
	override fun click() {} // 오버라이드한 메서드는 기본적으로 열려있다.
}
```

```
allOpen {  
    annotation("javax.persistence.Entity")  
    annotation("javax.persistence.Embeddable")  
    annotation("javax.persistence.MappedSuperclass")  
}
```
코틀린에서 JPA를 사용하는 경우 엔티티는 상속이 가능해야 하기 때문에 위와 같이 open class로 만들어주는 작업을 해야함

자바처럼 코틀린에서도 클래스를 abstract로 선언할 수 있으며, abstract로 선언한 추상 클래스는 인스턴스화할 수 없다.
``` kotlin
abstract class Animated {
	abstract fun animate()
	open open fun stopAnimating()
	fun animateTwice()
}
```

| 변경자 | 이 변경자가 붙은 멤버는... | 설명 |
| ---- | ---- | ---- |
| final | 오버라이드할 수 없음. | 클래스 멤버의 기본 변경자다. |
| open | 오버라이드할 수 있음. | 반드시 open을 명시해야 오버라이드할 수 있다. |
| abstract | 반드시 오버라이드해야 함 | 추상 클래스의 멤버에만 이 변경자를 붙일 수 있다. 추상 멤버에는 구현이 있으면 안된다. |
| override | 상위 클래스나 상위 인스턴스의 멤버를 오버라이드하는 중 | 오버라이드하는 멤버는 기본적으로 열려있다. 하위 클래스의 오버라이드를 금지하려면 final을 명시해야 한다. |

### 가시성 변경자: 기본적으로 공개
코틀린 가시성 변경자는 자바와 비슷하다.
public, protected, private 변경자가 있으며 아무 변경자도 없는 경우 선언은 모두 공개 된다.
자바의 기본 가시성 패키지 전용(package-private)은 코틀린에 없다.
코틀린은 패키지를 네임스페이스를 관리하기 위한 용도로만 사용한다.

패키지 전용 가시성에 대한 대안으로 코틀린에는 internal이라는 새로운 가시성 변경자를 도입했다.
internal은 모듈 내부에서만 볼 수 있음이라는 뜻이다.
모듈은 한 번에 한꺼번에 컴파일되는 코틀린 파일들을 의미한다.

intelliJ, eclipse, maven, gradle 등의 프로젝트가 모듈이 될 수 있고, 앤트 테스트가 한 번 실행될 때 함께 컴파일되는 파일의 집합도 모듈이 될 수 있다.

| 변경자 | 클래스 멤버 | 최상위 선언 |
| ---- | ---- | ---- |
| public | 모든 곳에서 볼 수 있다. | 모든 곳에서 볼 수 있다. |
| internal | 같은 모듈 안에서만 볼 수 있다. | 같은 모듈 안에서만 볼 수 있다. |
| protected | 하위 클래스 안에서만 볼 수 있다. | 적용 불가 |
| private | 같은 클래스 안에서만 볼 수 있다. | 같은 파일 안에서만 볼 수 있다. |
``` kotlin
internal open class TalkativeButton: Focusable {
	private fun yell() = println("Hey!")
	protected fun whisper() = println("Let's talk!")
}

fun TalkativeButton.giveSpeech() {
	yell()
	whisper()
}
```
코틀린은 public 함수인 giveSpeech 안에서 그보다 가시성이 더 낮은 타입인 TalkativeButton을 참조하지 못하게 한다.

자바에서는 같은 패키지 안에서 protected 멤버에 접근할 수 있지만, 코틀린에서는 그 클래스를 상속한 클래스 안에서만 보인다는 차이점이 존재한다.

### 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스
``` kotlin
class Button: View {
	class ButtonStatus: Status {}
}
```
코틀린 중첩 클래스에 아무런 변경자가 붙지 않으면 자바 static 중첩 클래스와 같다.
이를 내부 클래스로 변경해서 바깥쪽 클래스에 대한 참조를 포함하게 만들고 싶으면 inner 변경자를 붙여야 한다.

| 클래스 B 안에 정의된 클래스 A | 자바에서는 | 코틀린에서는 |
| ---- | ---- | ---- |
| 중첩 클래스(바깥쪽 클래스에 대한 참조를 저장하지 않음) | static class A | class A |
| 내부 클래스(바깥쪽 클래스에 대한 참조를 저장함) | class A | inner class A |

``` kotlin
class Outer {
	inner class Inner {
		fun getOuterReference(): Outer = this@Outer
	}
}
```
코틀린에서 바깥쪽 클래스의 인스턴스를 가리키는 참조를 표기하는 방법도 자바와 다르다.
내부 클래스 Inner 안에서 바깥쪽 클래스 Outer의 참조에 접근하려면 this@Outer 라고 써야 한다.

### 봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한
``` kotlin
sealed class Expr {
	class Num(val value: Int): Expr()
	class Sum(val left: Expr, val right: Expr): Expr()
}

when (e) {
	is Expr.Num -> e.value
	is Expr.Sum -> eval(e.right) + eval(e.left)
	// 별도의 else 분기가 없어도 된다.
}
```
클래스에 sealed 변경자를 붙이면 그 상위 클래스를 상속한 하위 클래스 정의를 제한할 수 있다.

## 뻔하지 않은 생성자와 프로퍼티를 갖는 클래스 선언
코틀린은 주 생성자와 부 생성자를 구분한다.
또한 코틀린에서는 초기화 블록을 통해 초기화 로직을 추가할 수 있다.

### 클래스 초기화: 주 생성자와 초기화 블록
``` kotlin
class User(val nickname: String)
```
이렇게 클래스 이름 뒤에 오는 괄호로 둘러싸인 코드를 주 생성자라고 한다.

클래스에 기반 클래스가 있다면 주 생성자에서 기반 클래스의 생성자를 호출해야 할 필요가 있다.
``` kotlin
open class User(val nickname: String) {}
class TwitterUser(nickname: String): User(nickname) {}
```

### 부 생성자
``` kotlin
open class View {
	constructor(ctx: Context)
	constructor(ctx: Context, attr: AttributeSet)
}
```
이 클래스는 주 생성자를 선언하지 않고 부 생성자만 2가지 선언한다.
부 생성자는 constructor 키워드로 시작하며 필요에 따라 얼마든지 부 생성자를 선언할 수 있다.

### 인터페이스에 선언된 프로퍼티 구현
``` kotlin
interface User {
	val nickname: String
}
```
이는 User 인터페이스를 구현하는 클래스가 nickname의 값을 얻을 수 있는 방법을 제공해야 한다는 뜻이다.
``` kotlin
class PrivateUser(override val nickname: String): User

class SubscribingUser(val email: String): User {
	override val nickname: String
		get() = email.substringBefore('@')
}

class FacebookUser(val accountId: Int): User {
	override val nickname = getFacebookName(accountId)
}
```

## 컴파일러가 생성한 메서드
코틀린 클래스에서도 toString, equals, hashCode 등을 오버라이드 할 수 있다.
``` kotlin
class Client(val name: String, val postalCode: Int) {
	override fun toString() = "Client(name=$name, postalCode=$postalCode)"
}
```

어떤 클래스가 데이터를 저장하는 역할만을 수행한다면 toString, equals, hashCode를 반드시 오버라이드 해야 한다.

코틀린에서는 data 라는 변경자를 클래스 앞에 붙이면 필요한 메서드를 컴파일러가 자동으로 만들어 준다.
data 변경자가 붙은 클래스를 데이터 클래스라고 부른다.
``` kotlin
data class Client(val name: String, val postalCode: Int)
```

### 위임
``` kotlin
class DelegatingCollection<T>: Collection<T> {
	private val innerList = arrayListOf<T>()

	override val size: Int get() = innerList.size
	override val isEmpty(): Boolean = innerList.isEmpty()
	override val contains(element: T): Boolean = innerList.contains(element)
	...
}
```
이러한 위임을 언어가 제공하는 일급 시민 기능으로 지원한다는 점이 코틀린의 장점이다.
인터페이스를 구현할 때 by 키워드를 통해 그 인터페이스에 대한 구현을 다른 객체에 위임 중이라는 사실을 명시할 수 있다.
``` kotlin
class DelegatingCollection<T> {
	innerList: Collection<T> = ArrayList<T>()
} : Collection<T> by innerList {}
```
메서드 중 일부의 동작만 변경하고 싶은 경우 오버라이드 하면 컴파일러가 생성한 메서드 대신 오버라이드한 메서드가 쓰인다.
원하는 기능을 커스텀하기 위해 이는 불필요한 메서드 까지 오버라이드해야하는 불편함을 줄여준다.

## object 키워드: 클래스 선언과 인스턴스 생성
코틀린에서는 object 키워드를 다양한 상황에서 사용하지만 모든 경우 클래스를 정의하면서 동시에 인스턴스를 생성한다는 공통점이 있다.
- 객체 선언은 싱글턴을 정의하는 방법 중 하나다.
- 동반 객체는 인스턴스 메서드는 아니지만 어떤 클래스와 관련 있는 메서드와 팩토리 메서드를 담을 때 쓰인다.
- 객체 식은 자바의 무명 내부 클래스 대신 쓰인다.

### 객체 선언: 싱글턴을 쉽게 만들기
자바에서는 싱글턴 패턴을 통해 싱글턴 객체를 생성해야하지만 코틀린은 객체 선언 기능을 통해 싱글턴 객체를 쉽게 생성할 수 있도록 언어 차원에서 기본 지원한다.

``` kotlin
object Payroll {
	...
}
```
클래스와 마찬가지로 객체 선언 안에도 프로퍼티, 메서드 초기화 블록등이 들어갈 수 있다
하지만 생성자는 객체 선언에 쓸 수 없다.
일반 클래스와 다르게 싱글턴 객체는 객체 선언문이 있는 위치에서 생성자 호출 없이 즉시 만들어지기 때문이다.

### 동반 객체
코틀린은 static 키워드를 지원하지 않는 대신 최상위 함수와 객체 선언등을 활용한다.
하지만 이는 비공개 멤버에 접근할 수 없다는 단점이 있다.

클래스 안에 정의 된 객체 중 하나에 companion 이라는 특별한 표시를 붙이면 그 클래스의 동반 객체로 만들 수 있다.
그 결과 동반 객체의 멤버를 사용하는 구문은 자바의 정적 메서드 호출이나 정적 필드 사용 구문과 같아진다.
``` kotlin
class A {
	companion object {
		fun bar() {
			println("Companion object called")
		}
	}
}

A.bar()
```

### 객체 식
``` kotlin
window.addMouseListener(
	object: MouseAdapter() {
		override fun mouseClicked(e: MouseEvent) {
			...
		}
	}
)
```
MouseAdapter를 확장하는 무명 객체를 선언하여 MouseAdapter의 메서드를 오버라이드하는 코드이다.
``` kotlin
return webClient.get()  
    .uri {  
        it            
	        .scheme(host)  
            .build()  
    }  
    .accept(MediaType.APPLICATION_JSON)  
    .retrieve()  
    .bodyToMono(object : ParameterizedTypeReference<ApiResponse<ResultDto>>() {})
```
Rest API 호출의 결과를 generic으로 받을 때 사용하는 ParameterizedTypeReference를 사용하는 예이다.