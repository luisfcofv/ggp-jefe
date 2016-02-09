package Manager


import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Move
import org.ggp.base.util.statemachine.Role

class Gamer(roles: List<Role>) {
    var gamerManager: GamerManager? = null

    init {
        if (roles.count() == 1) {
            gamerManager = SingleGamerManager()
        } else {
            var multiplayerGamerManager = MultiplayerGamerManager()
            multiplayerGamerManager.roles = roles
            gamerManager = multiplayerGamerManager
        }
    }

    fun solve(stateMachineGamer: StateMachineGamer, timeout: Long): Move {
        val moveCandidate = gamerManager!!.solve(stateMachineGamer, timeout)
        return moveCandidate.move
    }
}