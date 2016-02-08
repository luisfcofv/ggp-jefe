package Manager

import Model.MoveCandidate
import org.ggp.base.player.gamer.statemachine.StateMachineGamer

class MultiplayerGamerManager : Gamer {
    override fun solve(stateMachineGamer: StateMachineGamer, timeout: Long): MoveCandidate? {
        return null
    }
}