# 5. 람다로 프로그래밍

람다 식(lambda expression) 또는 람다는 기본적으로 다른 함수에 넘길 수 있는 작은 코드 조각을 의미한다.

## 1. 람다 식과 멤버 참조

### 1-1. 람다 소개 : 코드 블록을 함수 인자로 넘기기

람다 식을 사용하면 함수를 선언할 필요가 없고, 코드 블록을 직접 함수의 인자로 전달할 수 있다. 람다의 등장 전에는 아래 코드와 같이 무명 내부 클래스를 이용했다.

```java
button.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View view) {
		doSomething();
	}
})
```

위 코드를 람다를 사용한 식으로 변경하면 다음과 같이 바꿀 수 있다.

```kotlin
button.setOnClickListener { doSomething() }
```

자바 무명 클래스에 비해 훨씬 간결하고, 읽기 쉽다.

### 1-2. 람다와 컬렉션

코드에서 중복을 제거하는 것은 DRY(Do not Repeat Yourself) 원칙이 있을 정도로 프로그래밍 스타일을 개선하는 방법 중 하나이다. 컬렉션을 처리할 일이 있을 경우 대부분 비슷한 로직을 사용해 처리한다.

```kotlin
data class Person(val name: String, val age: Int)  
  
fun findTheOldest(people: List<Person>) {  
	var maxAge = 0  
	var theOldest: Person? = null
	for (person in people) {  
		if(person.age > maxAge) {  
			maxAge = person.age  
			theOldest = person  
		}  
	}  
	println(theOldest)  
}
```

위 코드와 같이 최대값 혹은, 특정한 값을 얻기 위해서는 컬렉션을 순회하며 값을 도출하는 코드를 작성해야 한다. 이런 이유로 컬렉션에는 사용자가 쓰기 편하도록 다양한 기능을 제공한다. 위 코드를 람다를 사용한 코드로 수정해보자.

```kotlin
fun main() {  
	val people = listOf(Person("Alice", 29), Person("Sponge", 52), Person("Bob", 97))  
	println(people.maxBy { it.age })  
}
```

이전 코드에 비해 훨씬 간결하며, 사용하기도 편하다. `maxBy` 함수는 가장 큰 원소를 찾기 위해 비교에 사용할 값을 돌려주는 함수를 인자로 받으며, `it`은 그 인자를 가리킨다. 혹은 멤버 참조를 사용해 조금 더 직관적으로 작성할 수 있다.

```kotlin
println(people.maxBy(Person::age))
```

이렇게 람다 혹은 멤버 참조를 이용하면 코드 다이어트가 가능하며, 이해하기 쉬워진다.

### 1-3. 람다 식의 문법

람다는 값처럼 여기저기 전달할 수 있는 동작의 모음이다. 람다를 따로 선언해서 변수에 저장할 수도 있다. 우선 람다 식을 선언하기 위한 문법을 살펴보자.

```kotlin
{x: int, y: Int -> x + y}
```

위 코드에서 x, y를 선언한 부분은 **파라미터**라 부르며, x + y는 **본문**을 나타낸다. 즉, 화살표인 `->`을 기준으로 인자 목록과 람다 본문을 구분해준다. 그러면 앞서 말한 람다 식을 변수에 저장해보자.

```kotlin 
val sum = {x: Int, y: Int -> x + y}  
println(sum(1, 2))
```

이렇게 람다가 저장된 변수를 다른 일반 함수와 마찬가지로 괄호를 통해 인자를 넣어 호출할 수 있다. 실행 시점에 코틀린 람다 호출에는 아무 부가 비용이 들지 않으며, 프로그램의 기본 구성 요소와 비슷한 성능을 낸다.

코틀린에서 람다를 작성하는 방법은 다양하다.

```kotlin
// 정식 람다 방식
people.maxBy ({ p: Person -> p.age })
// 람다 블록 분리
people.maxBy() { p: Person -> p.age }
// 빈 소괄호 제거
people.maxBy { p: Person -> p.age }
// 타입 추론
people.maxBy { it.age }
```

이전에 작성했던, `joinToString`을 개선해보자.

```kotlin
fun main() {
	val names = people.joinToString(" ") { it.name }  
	println(names)
}

private fun <T> Collection<T>.joinToString(  
	separator: String = ", ",  
	prefix: String = "",  
	postfix: String = "",  
	block: ((T) -> String)? = null  
): String {  
	val result = StringBuilder(prefix)  
	for ((idx, element) in this.withIndex()) {  
		if (idx > 0) result.append(separator)  
	  
		if(block != null) {  
			result.append(block(element))  
		} else {  
			result.append(element)  
		}  
	}  
	result.append(postfix)  
	return result.toString()  
}
```

위와 같이 람다를 넘겨 원하는 원소만 `toString`으로 뽑아낼 수 있다. 간혹 컴파일러가 람다 파라미터의 타입을 추론하지 못하는 경우도 있지만, 처음에는 쓰지 않고, 람다를 작성하고, 컴파일러가 타입을 추론하지 못할 때만 명시하도록 하자.

> `joinToString`의 호출부를 살펴보면, `it.name`을 넘겨준 것을 볼 수 있다. 람다의 depth가 깊어질 경우에는 it을 남용하다가 원하는 결과를 못받을 수 있다. 때문에 it이 아닌 원소에 걸맞는 `p -> p.name`과 같이 파라미터 이름을 지정하고 사용하는 것이 좋다.

람다의 본문이 여러 줄로 이뤄진 경우 본문의 맨 마지막에 있는 식이 람다의 결과 값이 된다.

```kotlin
val sum= {x: Int, y: Int ->  
	println("Computing the sum of $x and $y")  
	x + y  
}
```

### 1-4. 현재 영역에 있는 변수에 접근

람다를 함수 안에서 정의하면 함수의 파라미터뿐 아니라 람다 정의의 앞에 선언된 로컬 변수까지 람다에서 모두 사용할 수 있다.

```kotlin
fun printMessageWithPrefix(messages: Collection<String>, prefix: String) {  
	messages.forEach { msg ->  
		println("$prefix $msg")  
	}  
}  
  
fun main() {  
val errors = listOf("403 Forbidden", "404 Not Found")  
	printMessageWithPrefix(errors, "Error :")  
}
```

위 코드처럼 `forEach`에서 함수의 파라미터 사용이 가능하다. 자바와 다른 점 중 중요한 한 가지는 코틀린 람다 안에서는 `final` 변수가 아닌 변수에 접근할 수 있고, 람다 안에서 바깥 변수를 변경할 수 있다.

```java
public class CollectionTestJava {  
	public static void main(String[] args) {  
		List<String> lists = List.of("403 Forbidden", "404 Not Found");  
		String prefix = "Error :";  
		lists.forEach(msg -> {  
			System.out.printf("%s %s", prefix, msg);  
		});  
	}  
}
```

자바 8에서만 사용 불가능하며, 이후 자바 버전에서는 `final`이 아닌 변수에 접근할 수 있다.

다시 본론으로 돌아와 아래 코드는 람다 안에서 바깥 함수의 로컬 변수를 변경하는 코드이다.

```kotlin
fun printProblemCounts(response: Collection<String>) {  
	var clientErrors = 0  
	var serverErrors = 0  
	response.forEach { res ->  
		if(res.startsWith("4")) clientErrors++  
		else if(res.startsWith("5")) serverErrors++  
	}  
	println("Client Errors : $clientErrors, Server Errors : $serverErrors")  
}
```

이렇게 람다 안에서 사용하는 외부 변수를 '람다가 포획(capture)한 변수'라고 부른다. 함수를 쓸모 있는 일급 시민으로 만드려면 포획한 변수를 제대로 처리해야 하고, 포획한 변수를 제대로 처리하려면 클로저(closure)가 꼭 필요하다. 그래서 람다를 클로저라고도 부른다.

> 클로저란, 람다에서 시작하는 모든 참조가 포함된 닫힌(closed) 객체 그래프를 코드와 함께 저장하는 데이터 구조를 의미한다.

> 일급 시민이란, 특정 권한이나 제약 없이 취급되는 개체를 의미한다. 변수에 값을 할당 가능하며, 인자로 전달 가능하고, 데이터 구조 안에 저장할 수 있어야 한다.

### 1.5 멤버 참조

앞에서 잠깐 언급이 되었는데, 말 그대로 클래스의 멤버(프로퍼티)를 참조하는 것을 의미한다.

```kotlin
val ageProperty = Person::age
```

위와 같이 멤버를 참조할 경우 `ageProperty`의 타입은 `KProperty1<Person, Int>`가 된다. 이는 리플렉션을 사용해 클래스의 특정 필드를 지정할 수 있으며, `val`로 선언할 경우 `KProperty1`, `var`로 선언할 경우 `KMutableProperty1`로 타입이 지정된다.

이렇게 이중 콜론(::)을 사용하는 식을 멤버 참조(member reference)라고 부른다. 여기서 Person은 클래스를 나타내며, age는 해당 클래스 내부에 있는 멤버 필드를 나타낸다.

앞서 봤던 `maxBy` 함수에서 람다로 넘겨주는 값은 특정 프로퍼티였다. 때문에 아래 코드로도 람다를 사용할 수 있는 것이다.

```kotlin
people.maxBy(Person::age)
```

이렇게 클래스 멤버를 지칭할 수도 있고, 다른 클래스의 멤버가 아닌 최상위에 선언된 함수나 프로퍼티를 참조할 수도 있다. 최상위 프로퍼티를 멤버 참조로 사용할 경우 클래스 이름을 생략하고 바로 이중 콜론(::)을 사용한다.

```kotlin
fun salute() = println("Salute!")

fun main() {
	run(::salute)
}
```

이러한 참조는 멤버 프로퍼티 뿐만 아니라 생성자 참조(constructor reference)도 가능하다. 이를 사용하면, 클래스 생성 작업을 연기하거나, 저장해둘 수 있다.

```kotlin
val action = { p: Person, msg: String -> sendEmail(p, msg) }
fun sendEmail(p: Person, msg: String) {  
	println("""  
		-----------  
		To. ${p.name}  
		$msg  
		-----------  
	""".trimIndent())  
}

fun main() {
	val personConstructor = ::Person  
	action(personConstructor("전청조", 28), "나 전청조인데 개추")
}
```

#### 바운드 멤버 참조

코틀린 1.0에서는 멤버 참조를 하기 위해 인스턴스 객체를 항상 제공해야했지만, 코틀린 1.1 부터는 바운드 멤버 참조를 지원한다.

```kotlin
val p = Person("Dmitry", 34)

// 1.0 기존 멤버 참조 방식
val personAgeFunction = Person::age
println(personAgeFunction(p))

// 1.1 바운드 멤버 참조 방식
val personAgeFunction = p::age
println(personAgeFunction())
```

## 2. 컬렉션 함수형 API

함수형 프로그래밍 스타일을 사용하면 코드 다이어트가 가능하고, 컬렉션을 다룰 때 편리하다.

### 2-1. 필수적인 함수 : filter, map

filter와 map은 컬렉션을 활용할 때, 가장 기반이 되는 함수이다.

filter 함수는 컬렉션을 순회하며, 원소 중에 주어진 술어(predicate; 참 / 거짓을 반환하는 함수)를 만족하는 원소만 담은 새로운 컬렉션을 반환한다.

```kotlin
// 출력 : [Person(name=Sponge, age=52), Person(name=Bob, age=97)]
println(people.filter { it.age >= 30 })
```

filter는 원하는 원소를 제거할 수 있지만, 람다 인자를 `predicate: (T) -> Boolean`로 받기 때문에 원소 자체를 변환할 수 없다.  만약 원소를 변환하려면 map 함수를 사용해야 한다.

```kotlin
// 출력 : [Alice, Sponge, Bob]
println(people.map { it.name })
```

30살 이상인 사람의 이름만 출력하려면 다음과 같이 코드를 작성할 수 있다.

```kotlin
// 출력 : [Sponge, Bob]
println(people.filter { it.age >= 30 }.map { it.name })
```

이제 이 중에 나이가 가장 많은 사람을 구해보자.

```kotlin
// 출력 : [Person(name=Bob, age=97)]
println(people.filter { it.age == people.maxBy { p-> p.age }.age })
```

위 코드는 굉장히 비효율적인 코드이다. 만약 people에 100명의 사람이 저장되어 있을 경우 filter에서 한 루프를 돌 때, maxBy가 100번의 순회를 도는 것이다. 이럴 경우에는 최대값을 구하는 구문을 밖으로 빼서, 계산을 반복하지 않도록 만드는 것이 좋다.

```kotlin
val maxAge = people.maxBy { p-> p.age }.age  
println(people.filter { it.age == maxAge })
```

### 2-2. all, any, count, find : 컬렉션의 술어 적용

- all : 모든 원소가 특정 조건을 만족하는지 판단
- any : 특정 조건을 만족하는 원소가 있는지 판단
- count : 특정 조건을 만족하는 원소의 수
- find : 특정 조건을 만족하는 첫 번째 원소

위 함수들을 가지고, 사람들 중 27살 이하인지 판단하는 술어 함수를 만들어보자.

```kotlin
private val people = listOf(Person("Abraham", 11), Person("John Cinema", 11), Person("Sponge", 97), Person("Bob", 97))
private val canBeInClub27 = {p: Person -> p.age <= 27}

fun main() {  
	// 출력 : false
	println(people.all(canBeInClub27))  
	// 출력 : true
	println(people.any(canBeInClub27)) 
	// 출력 : 2
	println(people.count(canBeInClub27)) 
}
```

위 코드에서는 all을 사용해 모두가 `canBeInClub27`을 만족하지 않기 때문에 false를 출력하고, 그 중에 하나라도 참인지 판단하는 any는 true를 반환한다. 또한, 이를 만족하는 수를 구하면 2라는 수가 나오는 것을 볼 수 있다.

#### count와 size

count가 있다는 사실을 까먹고 다음과 같이 코드를 짜는 실수를 할 때가 있다.

```kotlin
val cnt = people.filter(canBeInClub27).size
// 출력 : 2
println(cnt)
```

조건에 맞는 수를 구하는게 목적인데, 위처럼 filter를 사용할 경우 해당 조건에 맞는 리스트를 반환한다. 즉, 굳이 필요하지 않은 중간 컬렉션을 만들어 비효율적이다. 내가 무엇이 필요한지 잘 생각하고, 그에 적합한 연산을 하는 선택해 효율성을 높이자.

### 2-3. groupBy : 리스트를 여러 그룹으로 이뤄진 맵으로 변경

컬렉션의 모든 원소를 어떤 특성에 따라 여러 그룹으로 나누고 싶을 때가 있다. 이럴 때, groupBy를 통해 여러 그룹으로 이뤄진 맵으로 변경할 수 있다. 사람을 나이에 따라 분류하는 코드를 통해 살펴보자.

```kotlin
// 출력 : {11=[Person(name=Abraham, age=11), ...], 97=[Person(name=Sponge, age=97), ...]}
println(people.groupBy { it.age })

// 출력 : {11=Abraham, John Cinema, 97=Sponge, Bob}
println(map.mapValues { it.value.joinToString { p -> p.name } })
```

나이를 통해 사람 그룹을 나누었기 때문에 위 코드의 반환 타입은 `Map<Int, List<Person>>`이다. 또한, 위 코드처럼 필요에 따라 이 맵을 mapKeys 혹은 mapValues 등을 사용해 변경할 수 있다.

### 2-4. flatMap과 flatten : 중첩된 컬렉션 안의 원소 처리

flatMap 함수는 먼저 인자로 주어진 람다를 컬렉션의 모든 객체에 적용(mapping)하고 람다를 적용한 결과, 얻어지는 여러 리스트를 한 리스트로 한데 모아 펼친다(flatten).

```kotlin
private val books = listOf(Book("kotlin-in-action", listOf("존시나", "다오")), Book("effective java 3/E", listOf("우영미", "다오", "배찌")))
private val stringLists = listOf(listOf("1", "2", "3"), listOf("4", "5", "6"))

fun main() {  
	val set = books.flatMap { it.authors }.toSet()
	// 출력 : [존시나, 우영미, 다오, 배찌]
	println(set) 

	// 출력 : [1, 2, 3, 4, 5, 6]
	val flat = stringLists.flatten()
	println(flat)
}
```

위처럼 리스트 안에 리스트가 있을 경우 모든 중첩된 리스트의 원소를 한 리스트에 모아야할 경우 flatMap을 통해 중복된 원소를 제거할 수 있고, 특별히 변환할 내용이 없다면 리스트의 리스트를 평평하게 펼치는 flatten을 사용하면 된다.

## 3. 지연 계산(lazy) 컬렉션 연산

앞서 살펴본 컬렉션의 함수들은 결과를 즉시(eagerly) 생성한다. 즉, 컬렉션 함수를 연쇄할 경우 매 단계마다 계산 중간 결과를 새로운 컬렉션에 임시로 담는다는 말이다.

```kotlin
people.map(Person::name).filter { it.startsWith("A") }
```

map과 filter의 반환 타입을 보면 알 수 있듯, 위 코드는 2개의 리스트를 생성하고 있다. 만약 원소가 수백만 개가 되면 효율성이 많이 떨어질 것이다. 하지만 시퀀스(sequence)를 사용하면 중간 임시 컬렉션을 사용하지 않고도 컬렉션 연산을 연쇄할 수 있다.

```kotlin
val list = people.asSequence()  
	.map(Person::name)  
	.filter { it.startsWith("A") }  
	.toList()
```

위 코드는 원본 컬렉션을 시퀀스로 변환해 해당 시퀀스를 계속해서 사용한다. 즉, 임시 리스트를 만드는 것이 아닌, 시퀀스로 변환한 원본 컬렉션을 계속해서 사용해 원소가 많은 경우 성능이 눈에 띄게 좋아진다.

시퀀스의 강점은 원소를 필요할 때 비로소 계산되어 중간 처리 결과를 저장하지 않고도 연산을 연쇄적으로 적용해서 효율적으로 계산을 수행할 수 있다. 즉, 위 코드는 연산 과정만 등록을 해놓은 상태이며, 실질적인 연산은 list를 사용하는 시점에 적용된다.

> 의문점 : 위 코드를 사용할 때마다 연산되는 것이라면, 일반 함수에 비해 덜 효율적인게 아닌가?!

### 3-1. 시퀀스 연산 실행 : 중간 연산과 최종 연산

시퀀스에 대한 연산은 중간(intermediate) 연산과 최종(terminal) 연산으로 나뉜다. 중간 연산은 다른 시퀀스를 반환하며, 해당 시퀀스는 최초 시퀀스의 원소를 변환하는 방법을 안다. 최종 연산은 결과를 반환한다.

```kotlin
// 중간 연산
// 출력 X
listOf(1, 2, 3, 4).asSequence()  
	.map { print("i : map($it) "); it * it }  
	.filter { print("i : filter($it) "); it % 2 == 0 } 

// 최종 연산
// 출력 : t : map(1) t : filter(1) .. map(4) t : filter(16) 
listOf(1, 2, 3, 4).asSequence()  
	.map { print("t : map($it) "); it * it }  
	.filter { print("t : filter($it) "); it % 2 == 0 }
	.toList()
```

중간 연산은 항상 지연 계산 되므로, map과 filter 변환이 늦쳐져서 결과를 얻을 필요가 있을 때(최종 연산이 호출될 때) 적용이 된다. 때문에 아무런 결과도 출력되지 않는다. 반면에 최종 연산(toList)을 호출할 경우 연기됐던 모든 계산이 수행된다.

> 최종 연산은 함수의 반환 타입이 `Sequence`가 아닌 다른 타입으로 변환할 때를 의미한다.

시퀀스가 아닐 경우 연산 수행 순서는 컬렉션의 모든 원소에 대해 map 함수를 진행해 새로운 컬렉션을 얻고, 해당 컬렉션을 통해 filter 함수를 수행한다. 하지만, 시퀀스에 대한 연산 수행 순서는 시퀀스의 모든 연산은 각 원소에 대해 순차적으로 적용된다. 즉, 첫 번째 원소가 변환된 다음 걸러지면서 처리되고, 다시 두 번째 원소가 처리되는 형태이다.

따라서 원소에 연산을 차례대로 적용하다, 결과가 얻어지면 그 이후의 원소에 대해서는 변환이 이루어지지 않을수도 있다. 다음 코드를 통해 확인해보자.

```kotlin
// 출력 : 4
println(listOf(1, 2, 3, 4).asSequence()  
	.map { it * it }.find { it > 3 }  
)
```

만약 위 코드가 시퀀스가 아니라 일반 컬렉션이라면, map의 결과가 먼저 평가돼 최초 컬렉션의 모든 원소가 변환된다. 그 다음 find의 조건을 통해 그에 적합한 원소를 반환한다. 하지만 시퀀스의 경우에는 앞서 말한 것과 같이 1에 대한 map을 먼저 적용하고, find에 대한 조건을 검사한다. 때문에 2에서 find에 대한 조건을 만족해 반환하므로, 3, 4에 대한 연산을 수행하지 않는다.

### 3-2. 시퀀스 만들기

시퀀스를 만드는 다른 방법으로 `generateSequence` 함수를 사용할 수 있다.

```kotlin
val naturalNumbers = generateSequence(0) { it + 1 }  
val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }  
// 출력 : 5050
println(numbersTo100.sum())
```

위 코드에서 `naturalNumbers`, `numbersTo100`는 모두 시퀀스이므로, 연산을 지연 계산한다. 여기서는 `sum`이 최종 연산을 나타내며, 최종 연산을 수행하기 전까지는 시퀀스의 각 숫자는 계산되지 않는다.

시퀀스를 사용하는 일반적인 용례 중 하나는 객체의 조상으로 이뤄진 시퀀스를 만들어내는 것이다.

```kotlin
fun File.isInsideHiddenDirectory() =  
	generateSequence(this) { it.parentFile }.any { it.isHidden }

fun main() {
	val file = File("/.idea/.gitignore")  
	println(file.isInsideHiddenDirectory())
}
```

위 코드처럼 file의 조상인 `it.parentFile`을 통해 모든 조상의 시퀀스에서 숨김 속성을 가진 디렉터리가 하나라도 있는지 확인할 수 있다.

## 4. 자바 함수형 인터페이스 활용

코틀린에서 사용하는 API 중 상당수는 자바로 작성된 API일 가능성이 높다. 두 언어는 호환되기 때문에 코틀린 람드를 자바 API에 사용해도 아무 문제가 없다.

가장 첫 부분에서 봤듯이, 자바 8 이전에는 `setOnclickListener` 메소드에게 인자로 넘기기 위해 무명 클래스의 인스턴스를 만들어 사용했다.

```java
button.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View view) {
		doSomething();
	}
})
```

코틀린에서는 무명 클래스 인스턴스 대신 람다를 넘길 수 있다.

```kotlin
button.setOnClickListener(view -> doSomething(view))
```

이런 코드가 작동하는 이유는 `OnClickListener`에 추상 메소드가 단 하나만 있기 때문이다. 이런 인터페이스를 함수형 인터페이스(functional interface) 또는 SAM 인터페이스라고 한다.

> SAM 인터페이스란; 단일 추상 메소드(Single Abstract Method)라는 뜻이다.

### 4-1. 자바 메소드에 람다를 인자로 전달

함수형 인터페이스를 인자로 원하는 자바 메소드에  코틀린 람다를 전달할 수 있다.

```java
public class FunctionalTestJava {  
	public static void postponeComputation(int delay, Runnable computation) throws InterruptedException {  
		Thread.sleep(delay);  
		computation.run();  
	}  
}
```

```kotlin
fun main() {  
	postponeComputation(1000) { println(42) }  
}
```

Runnable 인스턴스는 사실 Runnable을 구현한 무명 클래스의 인스턴스를 의미하기 때문에 위 코드의 람다 본문은 Runnable 내부에 있는 run 함수를 구현한 것과 마찬가지이다.

람다와 무명 객체 사이에는 차이가 있다. 객체를 명시적으로 선언하는 경우 메소드를 호출할 때마다 새로운 객체가 생성되지만, 람다를 사용할 경우 메소드를 호출할 때마다 반복 사용한다. 즉, 위 코틀린 코드가 몇 번 호출이 되든, Runnable의 인스턴스를 프로그램 전체에서 단 하나만 만든 것이다.

```kotlin
val runnable = Runnable { println(42) }  
  
fun handleComputation(id: String) {  
	postponeComputation(1000) { println(id) }  
}
```

단, 위 코드와 같이 람다가 주변 영역의 변수를 포획할 경우 매 호출마다 같은 인스턴스를 사용하지 못하게 된다.

### 4-2. SAM 생성자 : 람다를 함수형 인터페이스로 명시적 변경

SAM 생성자는 람다를 함수형 인터페이스의 인스턴스로 변환할 수 있게 컴파일러가 자동으로 생성한 함수이다. 컴파일러가 자동으로 람다를 함수형 인터페이스 무명 클래스로 바꾸지 못할 경우 SAM 생성자를 사용할 수 있다.

함수형 인터페이스를 반환하는 메소드가 있다면, 직접 람다를 반환할 수 없고, 반환하고픈 람다를 SAM 생성자로 감싸야 한다.

```kotlin
fun createAllDoneRunnable(): Runnable {  
	return Runnable { println("All done!") }  
}  
  
fun main() {    
	createAllDoneRunnable().run()  
}
```

SAM 생성자는 그 함수형 인터페이스의 유일한 추상 메소드의 본문에 사용할 람다만을 인자로 받아 함수형 인터페이스를 구현하는 클래스의 인스턴스를 반환한다. 디컴파일하면 다음과 같다.

```java
```kotlin
fun createAllDoneRunnable(): Runnable {  
	return Runnable { println("All done!") }  
}  
  
fun main() {    
	createAllDoneRunnable().run()  
}
```

SAM 생성자는 그 함수형 인터페이스의 유일한 추상 메소드의 본문에 사용할 람다만을 인자로 받아 함수형 인터페이스를 구현하는 클래스의 인스턴스를 반환한다.

> SAM 생성자에 대해 이해하지 못함

## 5. 수신 객체 지정 람다 : with와 apply

이 기능들은 수신 객체를 명시하지 않고, 람다의 본문 안에서 다른 객체의 메소드를 호출하게 할 수 있다. 이런 람다를 수신 객체 지정 람다(lambda with receiver)라고 부른다.

### 5-1. with

with는 특정 객체의 이름을 반복하지 않고도, 그 객체에 대해 다양한 연산을 수행할 수 있게 해준다.

```kotlin
fun alphabet(): String {  
	val result = StringBuilder()  
	  
	for (letter in 'A'..'Z') {  
	result.append(letter)  
	}  
	  
	result.append("\nNow I know the alphabet!")  
	return result.toString()  
}
```

위 코드를 보면 result를 계속해서 반복해 작성하고 있다. 코드가 더 길어질 경우 굉장히 귀찮아질 것이다. 이 예제에 with를 적용해보자.

```kotlin
// 방식1
fun alphabet(): String {  
	val sb = StringBuilder()
	// 수신 객체 지정
	return with(sb) {  
		for (letter in 'A'..'Z') {  
			// 수신 객체의 메소드 호출
			this.append(letter)  
		}  
		// 수신 객체의 메소드 호출(this 생략)
		append("\nNow I know the alphabet!")
		// 람다에서 값 반환
		toString()  
	}  
}

// 방식2
fun alphabet() = with(StringBuilder()) {  
	for (letter in 'A'..'Z') {  
		append(letter)  
	}  
	append("\nNow I know the alphabet!")  
	toString()  
}
```

위 코드처럼 with를 사용하는 방법은 두 가지가 있다. 만약 방식2와 같이 작성했을 때, 메소드 이름이 같아 충돌이 날 경우 this를 붙여 사용하면 된다.

with가 반환하는 값은 람다 코드를 실행한 결과이며, 그 결과는 람다 식의 본문에 있는 마지막에 있는 값이다.
### 5-2. apply

때로는 람다의 결과 대신 수신 객체가 필요한 경우가 있다. with의 경우 수신 객체를 사용해 특정한 값을 반환하지만, apply의 경우 항상 자신에게 전달된 객체인 수신 객체를 반환한다.

```kotlin
fun alphabet() = StringBuilder().apply {  
	for (letter in 'A'..'Z') {  
		append(letter)  
	}  
	append("\nNow I know the alphabet!")  
}.toString()
```

위 코드를 보면 apply 자체가 반환하는 값은 `StringBuilder`에 대한 객체이다. 이후 toString()을 호출해 문자열로 반환한다. with는 일반 함수이지만, apply는 확장 함수로 정의되어 있고, 자신과 같은 타입을 반환하도록 구현되어 있다.

StringBuilder를 apply와 함께 사용할 경우 `buildString`을 사용하는 것도 좋다. 이는 이미 StringBuilder를 반환하며, apply가 적용되어 있는 라이브러리이다.

```kotlin
fun alphabet() = buildString {  
	for (letter in 'A'..'Z') {  
		append(letter)  
	}  
	append("\nNow I know the alphabet!")  
}
```

