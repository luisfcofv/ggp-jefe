package Heuristic

import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState
import java.util.*

class MobilityHeuristic : Heuristic {
    val MaxPossibleActions = 50

    override fun evaluate(stateMachineGamer: StateMachineGamer, machineStates: ArrayList<MachineState>) : Int {
        var value = 0

        try {
            var machineState = machineStates[0]
            var actions = stateMachineGamer.stateMachine.getLegalMoves(machineState, stateMachineGamer.role)
            value = (Math.min(MaxPossibleActions - 1, actions.size) * 100.0 / MaxPossibleActions).toInt();
        } finally {
            return value;
        }
    }
}