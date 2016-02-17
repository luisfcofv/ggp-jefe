package Heuristic

import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState

class MobilityHeuristic : Heuristic {
    val MaxPossibleActions = 50

    override fun evaluate(stateMachineGamer: StateMachineGamer, machineState: MachineState) : Int {
        var value = 0

        try {
            var actions = stateMachineGamer.stateMachine.getLegalMoves(machineState, stateMachineGamer.role)
            value = (Math.min(MaxPossibleActions - 1, actions.size) * 100.0 / MaxPossibleActions).toInt();
        } finally {
            return value;
        }
    }
}