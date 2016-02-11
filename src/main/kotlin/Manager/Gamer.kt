package Manager


import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Move

class Gamer(stateMachineGamer: StateMachineGamer) {
    var gamerManager: GamerManager? = null

    init {
        if (stateMachineGamer.stateMachine.roles.count() == 1) {
            gamerManager = SingleGamerManager(stateMachineGamer)
        } else {
            gamerManager = MultiplayerGamerManager(stateMachineGamer)
        }
    }

    fun solve(timeout: Long): Move {
        val moveCandidate = gamerManager!!.solve(timeout)
        return moveCandidate.move
    }
}