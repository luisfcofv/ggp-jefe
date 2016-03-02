package model

import org.ggp.base.util.statemachine.MachineState
import org.ggp.base.util.statemachine.Move

class MonteCarloNode(state: MachineState) {
    var state: MachineState
    var visits: Int = 0
    var utility: Int = 0
    var children = arrayListOf<MonteCarloNode>()
    var parent: MonteCarloNode? = null
    var parentMove: Move? = null

    init {
        this.state = state
    }

    fun createChildNode(state:MachineState, move: Move): MonteCarloNode {
        var childNode = MonteCarloNode(state)
        childNode.parent = this
        childNode.parentMove = move
        children.add(childNode);
        return childNode;
    }
}