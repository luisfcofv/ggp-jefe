package Heuristic

import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState

class InverseMobilityHeuristic : Heuristic {
    override fun evaluate(stateMachineGamer: StateMachineGamer, machineState: MachineState) : Int {
        return 100 - MobilityHeuristic().evaluate(stateMachineGamer, machineState)
    }
}