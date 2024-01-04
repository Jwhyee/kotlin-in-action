package action.junyoung.chapter04.part1

// 다른 클래스가 이 클래스를 상속할 수 있다.
open class RichButton : Clickable {
    // 상위 클래스에서 선언된 open 메소드를 override 했다.
    // 오버라이드한 메소드는 기본적으로 열려있다.
    override fun click() { }
    // final 함수로 지정되어 있어 오버라이드할 수 없다.
    fun disable() {
        println("RichButton Disable")
    }
    // 이 함수는 열려있어 하위 클래스에서  override할 수 있다.
    open fun animate() { }
}