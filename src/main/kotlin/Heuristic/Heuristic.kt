package Heuristic

import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState

interface Heuristic {
    fun evaluate(stateMachineGamer: StateMachineGamer, machineState: MachineState) : Int
}