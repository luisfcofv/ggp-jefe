package Search

import Model.MoveCandidate
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import java.util.concurrent.Callable

open class BaseSearch(stateMachineGamer: StateMachineGamer, timeout: Long) : Callable<MoveCandidate> {
    val stateMachineGamer = stateMachineGamer
    val finishBy = timeout - 300

    override fun call(): MoveCandidate? {
        throw UnsupportedOperationException()
    }
}
