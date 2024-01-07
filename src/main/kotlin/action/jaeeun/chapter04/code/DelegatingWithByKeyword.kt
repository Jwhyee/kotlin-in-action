package action.jaeeun.chapter04.code

import kotlin.reflect.KProperty

class DelegationCollection<T>: Collection<T> {
    private val innerList = arrayListOf<T>()
    override val size: Int
        get() = TODO("Not yet implemented")

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<T> {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(element: T): Boolean {
        TODO("Not yet implemented")
    }
}
class DelegatingWithByKeyword<T> (
    innerList: Collection<T> = ArrayList<T>()
) : Collection<T> by innerList {}




//////////// 더 간단한 예시 ////////////

/*
* 클래스 위임 (Class Delegation)
* */
interface Engine {
    fun start()
    fun stop()
}

class ElectricEngine : Engine {
    override fun start() {
        println("Electric engine started")
    }

    override fun stop() {
        println("Electric engine stopped")
    }
}

class Car(private val engine: Engine) : Engine by engine


/*
* 프로퍼티 위임 (Property Delegation)
* */
interface PropertyDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String)
}

class MyPropertyDelegate : PropertyDelegate {
    private var storedValue: String = "Default"

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return storedValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        storedValue = value
    }
}

class MyClass {
    var myProperty: String by MyPropertyDelegate()
}

fun main() {
    val electricEngine = ElectricEngine()
    val car = Car(electricEngine)

    car.start() // Electric engine started
    car.stop()  // Electric engine stopped


    val myClass = MyClass()
    println(myClass.myProperty) // Default

    myClass.myProperty = "New Value"
    println(myClass.myProperty) // New Value
}
