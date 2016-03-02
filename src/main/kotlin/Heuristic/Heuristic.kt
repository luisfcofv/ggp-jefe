package heuristic

import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState
import java.util.*

interface Heuristic {
    fun evaluate(stateMachineGamer: StateMachineGamer, machineStates: ArrayList<MachineState>) : Int
}