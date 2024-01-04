package action.junyoung.chapter04.part2

import javax.naming.Context
import javax.print.attribute.AttributeSet

open class View {
    constructor(ctx: Context)
    constructor(ctx: Context, attr: AttributeSet)
}

class MyButton : View {
    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attr: AttributeSet) : super(ctx, attr)
}