package Manager

import Model.MoveCandidate
import org.ggp.base.player.gamer.statemachine.StateMachineGamer

interface Gamer {
    fun solve(stateMachineGamer: StateMachineGamer, timeout: Long): MoveCandidate?
}