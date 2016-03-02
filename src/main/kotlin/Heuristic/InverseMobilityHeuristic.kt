package heuristic

import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState
import java.util.*

class InverseMobilityHeuristic : Heuristic {
    override fun evaluate(stateMachineGamer: StateMachineGamer, machineStates: ArrayList<MachineState>) : Int {
        return 100 - MobilityHeuristic().evaluate(stateMachineGamer, machineStates)
    }
}