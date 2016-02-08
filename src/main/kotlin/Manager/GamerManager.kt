package Manager

import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Move
import org.ggp.base.util.statemachine.Role

class GamerManager(roles: List<Role>) {
    var gamer: Gamer? = null

    init {
        if (roles.count() == 1) {
            gamer = SingleGamerManager()
        } else {
            gamer = MultiplayerGamerManager()
        }
    }

    fun solve(stateMachineGamer: StateMachineGamer, timeout: Long): Move {
        val moveCandidate = gamer!!.solve(stateMachineGamer, timeout)
        return moveCandidate!!.move
    }
}