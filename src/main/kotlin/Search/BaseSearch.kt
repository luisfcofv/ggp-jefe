package Search

import Model.MoveCandidate
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import java.util.concurrent.Callable

open class BaseSearch(stateMachineGamer: StateMachineGamer) : Callable<MoveCandidate> {
    val stateMachineGamer = stateMachineGamer

    override fun call(): MoveCandidate? {
        throw UnsupportedOperationException()
    }
}
