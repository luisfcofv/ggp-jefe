package Manager


import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Move

class Gamer(stateMachineGamer: StateMachineGamer, startClockTimeout: Long) {
    var gamerManager: GamerManager? = null

    init {
        if (stateMachineGamer.stateMachine.roles.count() == 1) {
            gamerManager = SingleGamerManager(stateMachineGamer, startClockTimeout)
        } else {
            gamerManager = MultiplayerGamerManager(stateMachineGamer, startClockTimeout)
        }
    }

    fun solve(timeout: Long): Move {
        val moveCandidate = gamerManager!!.solve(timeout)
        return moveCandidate.move
    }
}