# 함수 정의와 호출

### 인자의 이름을 통해 함수 호출
아래의 코드를 예로보게 될 경우 자바의 경우 생성자의 순서 변화로 인한 오류를 방지할 수 없다.
``` java
// 변경 전
public class Person {
	private final String firstName;
	private final String lastName;

	public Person(String firstName, String lastName) {
		this.firstName;
		this.lastName;
	}
}

// 변경 후
public class Person {
	private final String firstName;
	private final String lastName;

	public Person(String lastName, String firstName) {
		this.firstName;
		this.lastName;
	}
}

// 위의 클래스의 생성자를 통해 인스턴스 생성하고 있는데, firstName과 lastName의 순서가 변경되어도 컴파일 오류가 발생하지 않기 때문에 런타임에 문제를 일으킬 수 있다.
public void createPerson() {
	Person newPerson = new Person("Kim", "Dori");
}
```

위의 문제를 방지하기 위해 builder 패턴을 많이 사용하지만 코틀린을 사용하면 함수 호출 시 인자의 이름을 통해 값을 넣어줄 수 있기 때문에 위의 문제를 방지할 수 있다.
``` kotlin
Person(firstName="kil", lastName="Dori")
```

### 디폴트 파라미터 값
``` kotlin
fun <T> joinToString(
	collection: Collection,
	separator: String = "",
	prefix: String = "",
	postfix: String = ""
)
```
위와 같이 함수 정의에 인자가 전달되지 않았을 경우 초기화 될 기본 값을 지정할 수 있다.
기본값이 지정된 파라미터는 호출 시 필수로 값을 전달하지 않아도 된다.
이러한 코틀린의 기능은 불필요한 **오버로드된 메소드가 늘어나는 것을 방지**한다.
``` kotlin
joinToString(collection, separator=",")
```

### 유틸리티 클래스 없애기
자바에서는 유틸 역할을 하는 함수를 사용하기 위해서도 클래스를 생성해야 한다.
``` java
public class TimeUtil {
	...
}
```
코틀린을 사용하면 이러한 무의미한 클래스를 생성할 필요가 없다.
``` kotlin
package times;

fun convertZonedDateTime(localDateTime: LocalDateTime): ZonedDateTime {
	...
}
```
이러한 클래스를 선언하지 않은 함수로만 이루어진 코틀린 코드를 작성하면 컴파일 시점에
해당 함수들을 포함하는 클래스를 생성하는데 이때 클래스명은 해당 코틀린의 파일명이 된다. (TimeUtilKt.class)

스프링 애플리케이션을 실행시키는 메인함수가 아래처럼 작성되어 있을 때
메인 함수의 위치를 알려주려면 해당 메인함수가 작성되어있는 파일명에 Kt를 붙여주면 된다.
``` kotlin
package io.dori;

@SpringBootApplication
class ApiAppApplication  
  
fun main(args: Array<String>) {  
    runApplication<ApiAppApplication>(*args)  
}
```
아래는 위의 스프링 애플리케이션을 jib를 사용해 docker registry에 push하는 설정에 대한 예다.
```
jib {  
    from {  
        image = "openjdk:17.0.1-jdk-slim"  
    }  
    to {  
        image = "api/app"  
    }  
    container {  
        mainClass = "io.dori.ApiAppApplicationKt"
	}
}
```

이렇게 클래스로 감싸지 않고 함수로만 이루어진 파일의 클래스명을 지정하고 싶을 때는 @field:JavaName("") 어노테이션을 사용하면 된다.
``` kotlin
@field:JavaName("MainApplication")
package io.dori;
  
fun main(args: Array<String>) {  
    ...
}
```

프로퍼티도 최상위에 선언할 수 있다.
- val: 게터만 생성된다.
- var: 게터와 세터가 생성된다.
``` kotlin
var count: Int = 0
```
이때 const 키워드를 붙이게되면 public static final 필드로 컴파일 되게 된다.
(단, 원시 타입과 String 타입만 가능.)

### 확장 함수와 확장 프로퍼티
기존에 작성된 클래스를 수정하지 않고 새로운 기능을 추가할 수 있는 역할을 확장함수(extension function)가 해준다.
```kotlin
// fun 수신객체타입.확장함수명(파라미터): 리턴타입
fun String.lastChar(): Char = this.get(this.length - 1)
```
확장함수의 타겟이 되는 객체타입을 수신 객체 타입(receiver type)이라고 부르며 실제 호출되는 대상이 되는 값을 수신 객체(receiver object)라고 부른다.
위의 코드에서는 String이 수신 객체 타입, this가 수신 객체이다.

확장함수는 특정 클래스 내에서만 유효하게 사용할 수도 있다.
``` kotlin
class ProductService {
	fun func01() {
		...
		val lastOption = product.getLastOption()
		...
	}
	
	fun func02() {
		...
		val lastOption = product.getLastOption()
		...
	}

	private fun Product.getLastOption(): ProductOption = this.options.last()
}
```
ProductService 내부의 함수에서 상품의 마지막 옵션을 가져오는 로직이 반복적으로 필요할 때 모든 함수에서 마지막 옵션을 가져오는 로직을 직접 구현하는 것 보다 확장함수로 구현하면 직접 상품 클래스를 변경하지 않고 중복되는 코드를 줄일 수 있다.

``` kotlin
val String.lastChar: Char
	get() = get(length - 1)
```

### 가변인자, 중위 함수 호출, 라이브러리 지원
``` kotlin
val list = listOf(2, 3, 5, 7, 11)

fun listOf<T>(vararg values: T): List<T> { ... }
```
가변 길이 인자는 메서드를 호출할 때 원하는 개수만큼 값을 인자로 넘기면 자바 컴파일러가 배열에 그 값들을 넣어주는 기능이다.
코틀린의 가변 길이 인자도 자바와 비슷하며 문법이 조금 다를뿐이다.
- java: 타입 뒤에 ...을 붙임
- kotlin: 파라미터 앞에 vararg 변경자를 붙임

``` kotlin
val list = listOf("args: ", *args) // spread
```
가변 길이 인자 메서드를 호출할 때 배열을 전달하고 싶으면 스프레드 연산자를 사용하면 된다.


코틀린에서 맵을 만들 때 아래와 같은 코드를 사용할 수 있다
``` kotlin
val map = mapOf(1 to "One", 7 to "seven")
```
to라는 단어는 코틀린의 키워드가 아닌 중위 호출(infix call)이라는 특별한 방식으로 to라는 일반 메서드를 호출한 것이다.

중위 호출 시에는 수신 객체와 유일한 메서드 인자 사이에 메서드 이름을 넣는다.
- 일반 호출: to("one")
- 중위 호출: to "one"
  인자가 하나뿐인 일반 메서드나 인자가 하나뿐인 확장 함수에 중위 호출을 사용할 수 있으며 중위 호출에 사용하게 허용하고 싶으면 Infix 변경자를 함수 선언 앞에 추가해야 한다.
``` kotlin
infix fun Any.to(other: Any) = Pair(this, other)
```

``` kotlin
val (number, name) = 1 to "one"
```
중위 함수 호출을 통해 Pair 인스턴스를 반환하고 해당 Pair의 내용으로 두 변수를 초기화 하는 코드이다.
코틀린에서 이러한 기능을 구조 분해 선언 이라고 부른다.

구조 분해는 Pair외에 다른 객체에서도 사용할 수 있다
``` kotlin
for ((index, element) in collection.withIndex()) {
	...
}
```
