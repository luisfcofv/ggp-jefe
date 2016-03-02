package gamer

import gamermanager.MonteCarloGamerManager
import org.ggp.base.player.gamer.statemachine.StateMachineGamer

class MonteCarloGamer(stateMachineGamer: StateMachineGamer) : Gamer(stateMachineGamer) {

    init {
        gamerManager = MonteCarloGamerManager(stateMachineGamer)
    }
}