package action.jaeeun.chapter04.code

// 이벤트 리스너를 정의하는 인터페이스
interface OnClickListener {
    fun onClick()
}

// 예시에서 사용할 클래스
class Button {
    // 클릭 이벤트 리스너를 저장할 프로퍼티
    var clickListener: OnClickListener? = null

    // 클릭 이벤트가 발생했을 때 호출되는 메서드
    fun click() {
        // 클릭 이벤트 리스너가 설정되어 있다면 해당 리스너의 onClick 메서드 호출
        clickListener?.onClick()
    }
}

fun main() {
    // Button 객체 생성
    val button = Button()

    // 무명 객체를 이용한 이벤트 리스너 설정
    button.clickListener = object : OnClickListener {
        override fun onClick() {
            println("Button Clicked!")
        }
    }

    // 버튼 클릭 시 이벤트 리스너의 onClick 메서드가 호출됨
    button.click()
}
