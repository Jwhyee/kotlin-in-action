# 5장 람다로 프로그래밍

람다는 기본적으로 다른 함수에 넘길 수 있는 작은 코드 조각을 뜻한다. 람다를 사용하면 쉽게 공통 코드 구조를 라이브러리 함수로 뽑아낼 수 있다.

## 람다 식과 멤버 참조

“이벤트가 발생하면 이 핸들러를 실행하자”, “데이터 구조의 모든 원소에 이 연산을 적용하자”와 같은 생각을 코드로 표현하기 위해 일련의 동작을 변수에 저장하거나 다른 함수에 넘겨야하는 경우가 자주 있다.

```java
button.setOnClickListener.onClickListener() {
	@Override
	public void onClick(View view) {
		/* 클릭 시 수행할 동작 */
	}
}
```

자바는 무명 내부 클래스를 통해 이런 목적을 달성했었고, 코드가 번잡하게 되었다.

```kotlin
button.setOnClickListener { /* 클릭 시 수행할 동작 */ }
```

코틀린 코드는 훨씬 간결하게 읽기 쉽게 작성할 수 있다.

### 람다와 컬렉션

코틀린에서는 편의를 위한 라이브러리가 많다.

`Persion`으로 이루어진 리스트가 있고 그중 가장 연장자를 찾고 싶으면 다음과 같이 작성할 수 있다.

```kotlin
val people = listOf(Person("Alice", 29), Person("Bob", 31))
println(people.maxBy { it.age })   
```

이런식으로 단지 함수나 프로퍼티를 반환하는 역할을 수행하는 람다는 멤버 참조로 대치할 수 있다.

```kotlin
println(people.maxBy(Person::age))
```

자바 컬렉션에 대해 수행하던 대부분의 작업은 람다나 멤버 참조를 인자로 취하는 라이브러리 함수를 통해 개선할 수 있다. 그렇게 람다나 멤버 참조를 인자로 받는 함수를 통해 개선한 코드는 더 짧고 더 이해하기 쉽다.

### 람다 식의 문법

```kotlin
val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
```

코틀린 람다식은 항상 중괄호로 둘러싸여 있다. 인자 목록 주변에는 괄호가 없고, 화살표가 인자 목록과 람다 본문을 구분해준다.

코드의 일부분을 둘러싸 실행할 필요가 있다면 run을 사용한다. run은 인자로 받은 람다를 실행해 주는 라이브러리다.

```kotlin
run { println(42) }
```

실행 시점에 코틀린 람다 호출에는 아무 부가 비용이 들지 않으며, 프로그램의 기본 구성요소와 비슷한 성능을 낸다.

```kotlin
people.maxBy({ p: Person -> p.age })
people.maxBy() { p -> p.age }
people.maxBy { it.age }
```

코틀린에서는 함수 호출 시 맨 뒤에 있는 인자가 람다 식이라면 그 람다를 괄호 밖으로 빼낼 수 있다는 문법 관습이 있다. 이 예제에서는 람다가 유일한 인자이고 마지막 인지이기도 하다. 따라서 괄호 뒤에 람다를 둘 수 있다.

```kotlin
val names = people.joinToString(separator = " ", transform = { p: Person -> p.name })
```

이름 붙인 인자를 사용해 람다를 넘길 수 있다.

```kotlin
val sum = { x: Int, y: Int -> {
        println("Computing the sum of $x and $y...")
        x + y
  } }
```

본문이 여러 줄로 이루어진 경우 본문의 맨 마지막에 있는 식이 람다의 결과 값이 된다.

### 현재 영역에 있는 변수에 접근

자바 메소드 안에서 무명 내부 클래스를 정의할 때 메소드의 로컬 변수를 무명 내부 클래스에서 사용할 수 있다. 람다 안에서도 같은 일은 할 수 있다. 람다를 함수 안에서 정의하면 함수의 파라미터뿐 아니라 람다 정의의 앞에 선언된 로컬 변수까지 람다에서 모두 사용할 수 있다.

다음 함수는 메시지의 목록을 받아 모든 메시지에 똑같은 접두사를 붙여서 출력해준다.

```kotlin
fun printMessagesWithPrefix(messages: Collection<String>, prefix: String) {
    messages.forEach {
        println("$prefix $it")
    }
}
```

자바와 다른 점 중 중요한 한 가지는 코틀린 람다 안에서는 파이널 변수가 아닌 변수에 접근할 수 있다는 점이다. 또한 람다 안에서 바깥의 변수를 변경해도 된다.

```kotlin
fun printProblemCounts(responses: Collection<String>) {
        var clientErrors = 0
        var serverErrors = 0
        responses.forEach {
            if (it.startsWith("4")) {
                clientErrors++
            } else if (it.startsWith("5")) {
                serverErrors++
            }
        }
        println("$clientErrors client errors, $serverErrors server errors")
    }
```

코틀린에서는 자바와 달리 람다에서 람다 밖 함수에 있는 파이널이 아닌 변수에 접근할 수 있고 그 변수를 변경할 수도 있다.
이 예제의 prefix, clientErrors, serverErrors는 람다 안에서 사용되는 외부 변수를 '람다가 포획한 변수'(captured variable)라고 부른다.

기본적으로 함수 안에 정의된 로컬 변수의 생명주기는 함수가 반환되면 끝난다. 하지만 어떤 함수가 자신의 로컬 변수를 포획한 람다를 반환하거나 다른 변수에 저장한다면 로컬 변수의 생명주기와
함수의 생명주기가 달라질 수 있다.(closure)

어떻게 그런 동작이 가능할까 파이널 변수를 포획한 경우에는 람다코드를 변수 값과 함께 저장한다. 파이널이 아닌 변수를 포획한 경우에는 변수를 특별한 래퍼로 감싸서
나중에 변경하거나 읽을 수 있게 한 다음, 래퍼에 대한 참조를 람다 코드와 함께 저장한다.

한 가지 꼭 알아둬야 할 함정이 있다. 람다를 이벤트 핸들러나 다른 비동기적으로 실행 되는 코드로 활용하는 경우 함수 호출이 끝난 다음에 로컬 변수가 변경될 수도 있다.
예를 들어 다음 코드는 버튼 클릭횟수를 제대로 셀 수 없다.

```kotlin
fun tryToCountButtonClicks(button: Button): Int {
    var clicks = 0
    button.onClick { clicks++ }
    return clicks
}
```

이 함수는 항상 0을 반환한다. 클릭할 때마다 clicks 변수를 증가시키는 람다를 등록했지만, 람다는 나중에 실행되기 때문에 함수가 반환되고 나서야 실행된다. 따라서 clicks 변수는 항상 0이다.


click count use closure는 람다가 포획한 변수를 어떻게 저장하는지에 대한 규칙이다. 람다가 포획한 변수를 저장하는 방식은 람다가 포획한 변수가 어떤 방식으로 사용되느냐에 따라 달라진다.

### 멤버 참조
코틀린에서는 자바 8과 마찬가지로 함수를 값으로 바꿀 수 있다. 이때 이중 콜론(::)을 사용한다.

```kotlin
val getAge = Person::age
```

::를 사용하는 식을 멤버 참조(member reference)라고 부른다. 멤버 참조는 프로퍼티나 메소드를 단 하나만 호출하는 함수 값을 만들어준다.

람다가 인자가 여럿인 다른 함수한테 작업을 위임하는 경우 람다를 정의하지 않고 직접 위임 함수에 대한 참조를 제공하면 편리하다.
    
```kotlin
val action = { person: Person, message: String -> sendEmail(person, message) }
val nextAction = ::sendEmail
```

생성자 참조(constructor reference)를 사용하면 클래스 생성 작업을 연기하거나 저장해둘 수 있다.
:: 뒤에 클래스 이름을 넣으면 생성자 참조를 만들 수 있다.

```kotlin
val createPerson = ::Person
val p = createPerson("Alice", 29)
```

확장 함수도 멤버 함수와 똑같은 방식으로 참조할 수 있다는 점을 기억하라.

```kotlin
fun Person.isAdult() = age >= 21
val predicate = Person::isAdult
```

## 컬렉션 함수형 API
함수형 프로그래밍 스타일을 사용하면 컬렉션을 다룰 때 편리하다. 대부분의 작업에 라이브러리 함수를 활용할 수 있고 그로 인해 코드를 아주 간결하게 만들 수 있다.

### filter와 map 함수
filter와 map은 컬렉션을 다룰 때 가장 유용하게 사용하는 함수다. filter는 컬렉션에서 원치 않는 원소를 제거하고 map은 컬렉션의 모든 원소를 변환한다.

```kotlin
val list = listOf(1, 2, 3, 4)
println(list.filter { it % 2 == 0 }) // [2, 4]
println(list.map { it * it }) // [1, 4, 9, 16]
```

### all, any, count, find: 컬렉션에 술어 적용
컬렉션에 술어(predicate)를 적용해 컬렉션의 원소 중 하나라도 조건을 만족하는지, 모두 만족하는지, 원소의 개수는 얼마인지, 특정 조건을 만족하는 원소는 무엇인지 확인할 수 있다.

코틀린에서는 all, any, count, find라는 네 가지 함수를 제공한다.

```kotlin
val people = listOf(Person("Alice", 29), Person("Bob", 31))
val canBeInClub27 = { p: Person -> p.age <= 27 }
println(people.all(canBeInClub27)) // false
println(people.any(canBeInClub27)) // true
println(people.count(canBeInClub27)) // 2
println(people.find(canBeInClub27)) // Person(name=Alice, age=29)
```

어떤 조건에 대해 !all을 수행한 결과와 any를 수행한 결과는 같다. (드모르강의 법칙) 
어떤 원소가 조건을 만족하지 않는다는 것은 그 조건을 만족하는 원소가 있다는 뜻이기 때문이다.
가독성을 위해 any와 all을 부정형으로 사용하는 것은 피해야 한다.

> 함수를 적재적소에 사용하라: count와 size
> count가 있다는 사실을 잊어버리고 컬렉션을 필터링한 결과의 크기를 가져오는 경우가 있다.
> ```kotlin
> val people = listOf(Person("Alice", 29), Person("Bob", 31))
> println(people.filter(canBeInClub27).size)
> ```
> 하지만 이렇게 처리하면 조건을 만족하는 모든 원소가 들어가는 중간 컬렉션이 생긴다.
> 반면 count는 조건을 만족하는 원소의 개수만을 추적하지 조건을 만족하는 원소를 따로 저장하지 않는다. 따라서 count가 훨씬 더 효율적이다.

술어를 만족하는 원소를 하나만 찾고 싶으면 find 함수를 사용한다.

```kotlin
val people = listOf(Person("Alice", 29), Person("Bob", 31))
println(people.find(canBeInClub27)) // Person(name=Alice, age=29)
```

이 식은 컬렉션에서 술어를 만족하는 첫 번째 원소를 반환하거나, 없으면 null을 반환한다.


### groupBy: 리스트를 여러 그룹으로 이뤄진 맵으로 변경
groupBy 함수는 컬렉션의 원소를 특정 특성에 따라 여러 그룹으로 나누는 역할을 한다.

```kotlin
val people = listOf(Person("Alice", 29), Person("Bob", 31), Person("Charles", 31), Person("Dan", 21))
println(people.groupBy { it.age }) // {29=[Person(name=Alice, age=29)], 31=[Person(name=Bob, age=31), Person(name=Charles, age=31)], 21=[Person(name=Dan, age=21)]}
```

groupBy 함수는 키와 값으로 이뤄진 맵을 반환한다. 키는 람다를 적용했을 때의 결과고 값은 원소의 리스트다.

### flatMap과 flatten: 중첩된 컬렉션 안의 원소 처리
flatMap은 map과 비슷하지만 람다의 결과로 생성된 모든 컬렉션을 한 리스트로 모은다.

```kotlin
val strings = listOf("abc", "def")
println(strings.flatMap { it.toList() }) // [a, b, c, d, e, f]
```

flatten은 단순히 flatMap { it }과 같다.

### 지연 계산(lazy) 컬렉션 연산
코틀린 컬렉션 연산은 기본적으로 지연 계산(lazy)된다. 즉, 결과 컬렉션을 사용할 때만 연산이 이뤄진다. 따라서 컬렉션 연산을 연쇄시키면 중간 컬렉션이 생기지 않는다.

```kotlin
val people = listOf(Person("Alice", 29), Person("Bob", 31), Person("Charles", 31), Person("Dan", 21))
println(
    people.asSequence()
        .map(Person::name)
        .onEach { println("map: $it") }
        .filter { it.length < 4 }
        .onEach { println("filter: $it") }
        .take(1)
        .toList()
)
```

asSequence 함수를 사용하면 컬렉션을 지연 계산 시퀀스로 바꿀 수 있다. 시퀀스를 리스트로 만들 때는 toList를 사용한다.
왜 시퀀스를 다시 컬렉션으로 되돌려야 할까?
시퀀스의 원소를 차례로 이터레이션해야 한다면 시퀀스를 직접 써도 된다. 하지만 시퀀스 원소를 인덱스를 사용해 접근하는 등의 다른 API 메소드가 필요하다면 시퀀스를 리스트로 변환 해야한다.

### 시퀀스 연산: 중간 연산과 최종 연산
컬렉션 연산을 사용할 때는 중간 연산과 최종 연산을 구분해야 한다. 중간 연산은 다른 시퀀스를 반환하고 최종 연산은 결과를 반환한다.

```kotlin
val people = listOf(Person("Alice", 29), Person("Bob", 31), Person("Charles", 31), Person("Dan", 21))
println(
    people.asSequence()
        .map(Person::name)
        .filter { it.length < 4 }
        .onEach { println("filter: $it") }
)
```

이 코드는 아무것도 출력하지 않는다. 왜냐하면 중간 연산은 최종 연산이 없으면 아무 동작도 하지 않기 때문이다.

```kotlin
val people = listOf(Person("Alice", 29), Person("Bob", 31), Person("Charles", 31), Person("Dan", 21))
println(
    people.asSequence()
        .map(Person::name)
        .filter { it.length < 4 }
        .forEach { println(it) }
)
```

이 코드는 forEach를 최종 연산으로 사용했기 때문에 filter의 결과를 출력한다.

### 시퀀스 만들기
지금까지 살펴본 시퀀스 예제는 모두 컬렉션에 대해 asSequence를 호출해 만들었다. 하지만 시퀀스를 만드는 또 다른 방법이 있다.
generateSequence 함수를 사용하면 시드(seed)값을 시작으로 람다를 호출해 다음 원소를 계산하는 방식으로 시퀀스를 만들 수 있다.

```kotlin
val naturalNumbers = generateSequence(0) { it + 1 }
val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }
println(numbersTo100.sum()) // 5050
```

시퀀스를 사용하는 일반적인 용레 중 하나는 객체의 조산으로 이뤄진 시퀀스를 만들어내는 것이다.
어떤 객체의 조상이 자신과 같은 타입이고 모든 조상의 시퀀스에서 어떤 특성을 알고 싶을 떄가 있다.
다음 예제는 어떤 파일의 상위 디렉터리를 뒤지면서 숨김 속성을 가진 디렉터리가 있는지 검사함으로써 파일이 감춰진 디렉터리 안에 들어있는지 알아본다.

```kotlin
fun File.isInsideHiddenDirectory() = generateSequence(this) { it.parentFile }.any { it.isHidden }
val file = File("/Users/svtk/.HiddenDir/a.txt")
println(file.isInsideHiddenDirectory()) // true
```

## 자바 함수형 인터페이스 활용
코틀린은 자바 함수형 인터페이스를 활용하는 방법을 제공한다. 자바 함수형 인터페이스는 하나의 추상 메소드만을 가진 인터페이스다.

5장을 시작하면서 자바 메소드에 람다를 넘기는 예제를 보여줬다.

```kotlin
button.setOnClickListener { /* 클릭 시 수행할 동작 */ }
```

Button 클래스는 setOnClickListener 메소드를 사용해 버튼의 리스너를 설정한다.
이때 인자의 타입은 OnClickListener 인터페이스다. OnClickListener는 하나의 추상 메소드를 가진 자바 함수형 인터페이스다.

```java
public interface OnClickListener {
    void onClick(View v);
}
```

OnClickListener를 구현하기 위해 사용한 람다에는 view라는 파라미터가 있다.
view의 타입은 View다. 이는 onClick메소드의 인자 타입과 같다.

```kotlin
setOnClickListener { view: View -> /* 클릭 시 수행할 동작 */ }
```

이런 코드가 작동하는 이유는 코틀린은 함수형 인터페이스를 인자로 취하는 자바 메소드를 호출할 때 람다를 넘길 수 있게 해준다.

> 자바와 달리 코틀린에서는 제대로 된 함수 타입이 존재한다. 따라서 코틀린에서 함수를 인자로 받을 필요가 있는 함수는 함수형 인터페이스가 아니라
> 함수 타입을 인자 타입으로 사용해야한다.
> 코틀린 함수를 사용할 때는 코틀린 컴파일러가 코틀린 람다를 함수형 인터페이스로 변환해주지 않는다.
> 함수 선언에서 함수 타입을 사용하는 방법은 8.1절에서 설명한다.

### 자바 메소드에 람다를 인자로 전달
함수형 인터페이스를 인자로 원하는 자바 메소드에 코틀린 람다를 전달할 수 있다.
예를들어 다음 메소드는 Runnable 인터페이스를 인자로 받는다.

```java
void postponeComputation(int delay, Runnable computation);
```
코틀린에서 람다를 이 함수에 넘길 수 있다. 컴파일러는 자동으로 람다를 Runnable 인스턴스로 변환해준다.

```kotlin
postponeComputation(1000) { println(42) }
```

여기서 'Runnable 인스턴스로 변환해준다'는 의미는 실제로는 'Runnable 인스턴스를 구현하는 클래스를 만들고 그 인스턴스를 생성해준다'는 뜻이다.

하지만 람다와 무명 객체에는 차이가 있다. 객체를 명시적으로 선언하는 경우 메소들르 호출할 때마다 새로운 객체가 생성된다.
람다를 사용하면 람다를 정의하는 스코프에 있는 변수를 마치 자신의 변수처럼 사용할 수 있다.

람다에 대해 무명 클래스를 만들고 그 클래스의 인스턴스를 만들어서 메소드에 넘긴다는 설명은 함수형 인터페이스를 받는 자바 메소드를
코틀린에서 호출할 때 쓰는 방식을 설명해주지만, 컬렉션을 확장한 메소드에 람다를 넘기는 경우 코틀린은 그런 방식을 사용하지 않는다.

코틀린 inline으로 표시된 코틀린 함수에게 람다를 넘기면 아무런 무명 클래스도 만들어지지 않는다.
대부분의 코틀린 확장 함수들은 inline으로 표시되어 있다.

지금까지 살펴본 대로 대부분읜 경우 람다와 자바 함수형 인터페이스 사이의 변환은 자동으로 이뤄진다.
컴파일러가 그 둘을 자동으로 변환할 수 있는 경우 여러분이 할일은 전혀 없다.
하지만 어쩔 수 없이 수동으로 변환해야 하는 경우가 있다.

### SAM 생성자: 람다를 함수형 인터페이스로 명시적으로 변경
SAM 생성자는 코틀린에서 자바 함수형 인터페이스를 구현하는 무명 객체를 만드는 특별한 문법이다.
컴파일러가 자동으로 람다를 함수형 인터페이스로 변환해주지 않는 경우에 사용한다.

```kotlin
fun createAllDoneRunnable(): Runnable {
    return Runnable { println("All done!") }
}


createAllDoneRunnable().run()
// All done!
```

SAM 생성자의 이름은 사용하려는 함수형 인터페이스의 이름과 같다.
SAM 생성자는 그 함수현 인터페이스의 유일한 추상 메소드의 본문에 사용할 람다만을 인자로 받아서
함수형 인터페이스를 구현하는 클래스의 인스턴스를 반환한다.

람다로 생성한 함수형 인터페이스 인스턴스를 변수에 저장하는 경우에도 SAM 생성자를 사용할 수 있다.
여러 버튼에 같은 리스너를 적용하고 싶다면 다음 리스트처럼 SAM 생성자를 통해 람다를 함수형 인터페이스 인스턴스로 만들어서 변수에 저장해 활용할 수 있다.

```kotlin
val listener = OnClickListener { view ->
    val text = when (view.id) {
        R.id.button1 -> "First button"
        R.id.button2 -> "Second button"
        else -> "Unknown button"
    }
    toast(text)
}
```

listener는 어떤 버튼이 클릭됐는지에 따라 적절한 동작을 수행한다. OnClickListener를 구현하는 객체 선언을 통해
리스너를 만들 수도 있지만 SAM 생성자를 쓰는 쪽이 더 간결하다.

> 람다와 리스너 등록/해제하기
람다에는 무명 객체와 달리 인스턴스 자신을 가리키는 `this`가 없다는 사실에 유의하라.
> 따라서 람다를 변환한 무명 클래스의 인스턴스를 참조할 방법이 없다. 컴파일러 입장에서 보면 람다는
> 코드 블록일 뿐이고, 객체가 아니므로 객체처럼 람다를 참조할 수는 없다.
> 람다 안에서 `this`는 그 람다를 둘러싼 클래스의 인스턴스를 가리킨다.
> 이벤트 리스너가 이벤트를 처리하다가 자기 자신의 리스너 등록을 해제해야 한다면 람다를 사용할 수 없다.
> 그런 경우 람다 대신 무명 객체를 사용해 리스너를 구현하다. 무명 객체 안에서는
> `this`가 그 무명 객체 인스턴스 자신을 가리킨다 따라서 리스너를 해제하는 API 함수에게 this를 넘길 수 있다.
> ```kotlin
> button.setOnClickListener(object : OnClickListener {
>    override fun onClick(view: View) {
>       removeListener()
>   }
> 
>   fun removeListener() {
>     button.setOnClickListener(null)
>   }
> })
> ```

## 수신 객체 지정 람다: with와 apply
`with`와 `apply`는 수신 객체 지정 람다라는 특별한 람다를 인자로 받는다.

### with 함수
어떤 객체의 이름을 반복하지 않고도 그 객체에 대해 다양한 연산을 수행할 수 있다면 좋을 것이다.
다양한 언어가 그런 기능을 제공한다. 코틀린도 마찬가지 기능을 제공하지만 언어 구성 요소로 제공하지 않고
`with`라는 라이브러리 함수를 통해 제공한다.

`with`의 유용성을 맛보기 위해 먼저 다음 예제를 살펴보고 이를 `with`을 사용해 리펙터링 해보자.

```kotlin
fun alphabet(): String {
    val result = StringBuilder()
    for (letter in 'A'..'Z') {
        result.append(letter)
    }
    result.append("\nNow I know the alphabet!")
    return result.toString()
}

println(alphabet())
// ABCDEFGHIJKLMNOPQRSTUVWXYZ
// Now I know the alphabet!
```

이 예제에서 `result`에 대해 다른 여러 메소드를 호출하면서 매번 `result`를 반복해야 한다.
`with`를 사용하면 `result`를 반복하지 않고도 여러 메소드를 호출할 수 있다.

```kotlin
fun alphabet(): String {
    val stringBuilder = StringBuilder()
    return with(stringBuilder) {
        for (letter in 'A'..'Z') {
            this.append(letter)
        }
        append("\nNow I know the alphabet!")
        this.toString()
    }
}

println(alphabet())
// ABCDEFGHIJKLMNOPQRSTUVWXYZ
// Now I know the alphabet!
```

`with`문은 언어가 제공하는 특별한 구문처럼 보인다. 하지만 실제로는 파라미터가 2개 있는 함수다.
여기서 첫 번째 파라미터는 `stringBuilder`이고 두 번째 파라미터는 람다다.

람다를 괄호 밖으로 빼내는 관례를 사용함에 따라 전체 함수 호출이 언어가 제공하는 특별 구문처럼 보인다.
물론 이 방식 대신 `with(stringBuilder, { ... })`라고 쓸 수도 있지만 더 읽기 나빠진다.

`with` 함수는 첫 번째 인자로 받은 객체를 두 번째 인자로 받은 람다의 수신 객체로 만든다.
인자로 받은 람다 본문에서는 `this`를 사용해 수신 객체를 참조할 수 있다.

> **수신 객체 지정 람다와 확장 함수 비교**
> `this`가 함수의 수신 객체를 가리키는 비슷한 개념을 떠올린 독자가 있을지도 모르겠다. 확장 함수 안에서
> `this`는 그 함수가 확장하는 타입의 인스턴스를 가리킨다. 그리고 그 수신 객체 `this`의 멤버를 호출할 때는 `this`를 생략할 수 있다.
> 어떤 의미에서는 확장함수를 수신 객체 지정 함수라 할 수도 있다.

> **메소드 이름 충돌**
> with에게 인자로 넘긴 클래스와 with을 사용하는 코드가 들어있는 클래스 안에 이름이 같은 메소드가 있으면
> 무슨일이 생길까? 그런 경우 this 참조 앞에 레이블을 붙이면 호출하고 싶은 메소드를 명확하게 정할 수 있다.
> ```kotlin
> class Outer {
>   fun alphabet() { /*... */ }
> 
>  class Inner {
>       fun alphabet() { /*... */ }
>       fun outerAlphabet() {
>           this@Outer.alphabet()
>      } 
>   }
> }
> ```

### apply 함수
`apply`는 `with`와 역할이 비슷하지만 항상 자신에게 전달된 객체를 반환한다는 점이 다르다.

```kotlin
fun alphabet() = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet!")
}.toString()

println(alphabet())
// ABCDEFGHIJKLMNOPQRSTUVWXYZ
// Now I know the alphabet!
```

`apply`는 확장함수로 정의되어있다. apply의 수신 객체가 전달 받은 람다의 수신 객체가 된다.
이 함수에서 apply를 실행한 결과는 `StringBuilder`객체다.

`apply`는 객체의 인스턴스를 만들면셔 즉시 프로퍼티 중 일부를 초기화해야 하는 경우 유용한다.

```kotlin
fun createViewWithCustomAttributes(context: Context) =
    TextView(context).apply {
        text = "Sample Text"
        textSize = 20.0
        setPadding(10, 0, 0, 0)
    }
```

표준 라이브러리의 `buildString` 함수는 `StringBuilder`를 만들고 초기화한 다음 람다를 실행하고
`StringBuilder`의 `toString`을 호출해 결과를 반환한다.

```kotlin
fun buildString(
    builderAction: StringBuilder.() -> Unit
): String {
    val sb = StringBuilder()
    sb.builderAction()
    return sb.toString()
}

val s = buildString {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet!")
}
```