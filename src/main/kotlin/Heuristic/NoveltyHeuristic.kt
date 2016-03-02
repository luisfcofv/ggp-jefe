package heuristic

import com.google.common.collect.Sets
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState
import java.util.*

class NoveltyHeuristic : Heuristic {
    override fun evaluate(stateMachineGamer: StateMachineGamer, machineStates: ArrayList<MachineState>) : Int {
        if (machineStates.size < 2) {
            return 1
        }

        var symmetricDifference = Sets.symmetricDifference(machineStates[0].contents, machineStates[1].contents)
        return symmetricDifference.size
    }
}