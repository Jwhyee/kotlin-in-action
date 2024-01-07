class OuterClass {
    private val outerProperty = "Outer Property"

    companion object {
        const val companionObjectProperty = "companionObjectProperty"
    }

    class NestedClass {
        // 중첩 클래스는 기본적으로 바깥쪽 클래스의 인스턴스에 접근할 수 없다.
        // outerProperty에 직접 접근할 수 없다.
        fun printMessage() {
            println("Nested class message")
            println("Nested class message $companionObjectProperty")
        }
    }

    inner class InnerClass {
        // 내부 클래스는 바깥쪽 클래스의 인스턴스에 접근할 수 있다.
        // outerProperty에 접근 가능하다.
        fun printMessage() {
            println("Inner class message: $outerProperty")
            println("Inner class message: $this@OuterClass.outerProperty")

        }
    }
}
