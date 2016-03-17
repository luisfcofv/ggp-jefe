package gamer


import gamermanager.GamerManager
import gamermanager.MonteCarloGamerManager
import gamermanager.SingleGamerManager
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Move

open class Gamer(stateMachineGamer: StateMachineGamer) {
    var gamerManager: GamerManager? = null

    init {
        if (stateMachineGamer.stateMachine.roles.count() == 1) {
            gamerManager = SingleGamerManager(stateMachineGamer)
        } else {
            gamerManager = MonteCarloGamerManager(stateMachineGamer)
        }
    }

    fun solve(timeout: Long): Move {
        return gamerManager!!.solve(timeout)
    }
}