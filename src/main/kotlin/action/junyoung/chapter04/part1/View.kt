package action.junyoung.chapter04.part1

import java.io.Serializable

interface State : Serializable
interface View {
    fun getCurrentState(): State
    fun restoreState(state: State) { }
}