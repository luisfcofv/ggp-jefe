package search

import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Move
import java.util.concurrent.Callable

open class BaseSearch(stateMachineGamer: StateMachineGamer) : Callable<Move> {
    val stateMachineGamer = stateMachineGamer

    override fun call(): Move? {
        throw UnsupportedOperationException()
    }
}
