package action.junyoung.test

import kotlin.reflect.full.memberProperties

interface ChangeDetectable {
    val field: String
    val title: String
    val t1: String
    val t2: String
}

enum class PostField : ChangeDetectable {
    C1 {
        override val field = "title"
        override val title = "제목"
        override val t1 = ""
        override val t2 = ""
    },
    C2 {
        override val field = "content"
        override val title = "내용"
        override val t1 = ""
        override val t2 = ""
    },
    C3 {
        override val field = "isDeleted"
        override val title = "삭제 여부"
        override val t1 = "삭제됨"
        override val t2 = "삭제되지 않음"
    },
    C4 {
        override val field = "category"
        override val title = "카테고리"
        override val t1 = ""
        override val t2 = ""
    }
}

enum class PostCategory(val title: String) {
    C1("자유 게시판"), C2("정보 게시판");

    override fun toString(): String {
        return title
    }
}

class Post() {
    var title: String = ""
    var content: String = ""
    var isDeleted: Boolean = false
    var category: PostCategory = PostCategory.C1
}

enum class BasicFunctionName {
    equals, hashCode, toString;

    companion object {
        fun contains(name: String): Boolean {
            return values().any { it.name == name }
        }
    }
}
/**
 * 두 객체를 비교해 변경된 부분을 찾는 함수
 * @param origin 기존 객체
 * @param target 비교 대상 객체
 * @param titles key: 필드 이름, value: 보여줄 이름
 * @param booleanTitles first: 필드 이름, second: true인 경우, third: false인 경우
 * */
inline fun <reified T: Any> findChanges(
    origin: T,
    target: T,
    titles: Map<String, String>,
    vararg booleanTitles: Triple<String, String, String>
): List<Triple<String, String, String>> {
    val clazz = T::class

    val result = mutableListOf<Triple<String, String, String>>()
    for (property in clazz.memberProperties) {
        val pName = property.name
        if (!BasicFunctionName.contains(pName)) {

            val originValue = property.get(origin).toString()
            val changedValue = property.get(target).toString()

            if (originValue != changedValue) {
                if (property.returnType.toString().lowercase().contains("boolean")) {
                    val index = booleanTitles.indexOfFirst { it.first == pName }

                    if(index == -1) error("[$pName] : 필드를 찾을 수 없습니다.")

                    val title = titles[pName] ?: error("[$pName] : 존재하지 않는 필드입니다.")

                    val trueValue = booleanTitles[index].second
                    val falseValue = booleanTitles[index].third

                    val r1 = if(originValue.toBoolean()) trueValue else falseValue
                    val r2 = if(changedValue.toBoolean()) trueValue else falseValue

                    result += Triple(title, r1, r2)

                } else {
                    val title = titles[pName] ?: error("[$pName] : 존재하지 않는 필드입니다.")
                    result += Triple(title, originValue, changedValue)
                }
            }
        }
    }
    return result
}

fun main() {
    val origin = Post().also {
        it.title = "제목1"
        it.content = "내용1"
    }
    val changed = Post().also {
        it.title = "제목2"
        it.content = "내용2"
        it.isDeleted = true
        it.category = PostCategory.C2
    }
    val titles = mapOf(
        "title" to "제목", "content" to "내용",
        "category" to "카테고리", "isDeleted" to "삭제 여부"
    )
    val booleanTitles = arrayOf(
        Triple("isDeleted", "삭제됨", "삭제되지 않음")
    )
    val changeList = findChanges(origin, changed, titles, *booleanTitles)
    println(changeList)
}
