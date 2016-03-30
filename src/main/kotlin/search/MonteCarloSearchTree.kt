package search

import model.MonteCarloNode
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Move
import java.util.*

class MonteCarloSearchTree(stateMachineGamer: StateMachineGamer) : BaseSearch(stateMachineGamer) {
    var finishBy: Long = 0
    val C = 40
    var map = hashMapOf<Int, MonteCarloNode>()

    override fun call(): Move? {
        var startNode: MonteCarloNode?
        if (map[stateMachineGamer.currentState.hashCode()] != null) {
            startNode = map[stateMachineGamer.currentState.hashCode()]
        } else {
            startNode = MonteCarloNode(stateMachineGamer.currentState)
        }

        while (System.currentTimeMillis() < finishBy) {
            var selectedNode = select(startNode!!)
            expand(selectedNode)
            val playoutScore = playout(selectedNode)
            backPropogate(selectedNode, playoutScore)
        }

        var bestChildNode: MonteCarloNode? = null;
        for (child in startNode!!.children) {
            if (bestChildNode == null || child.utility > bestChildNode.utility) {
                bestChildNode = child
            }
        }

        var bestMove = bestChildNode?.parentMove
        if (bestMove == null) {
            val moves = stateMachineGamer.stateMachine.getLegalMoves(stateMachineGamer.currentState, stateMachineGamer.role)
            bestMove = moves[Random().nextInt(moves.size)]
        }

        return bestMove
    }

    fun select(node: MonteCarloNode): MonteCarloNode {
        if (node.visits == 0 || node.children.size == 0) {
            return node
        }

        for (child in node.children) {
            if (child.visits == 0) {
                return child
            }
        }

        var score = Integer.MIN_VALUE
        var selectedNode: MonteCarloNode? = null

        for (child in node.children) {
            var result = selectFunction(child)
            if (result > score) {
                score = result
                selectedNode = child
            }
        }

        if (selectedNode == null || selectedNode.state.hashCode() == node.state.hashCode()) {
            return node
        } else {
            return select(selectedNode)
        }
    }

    fun selectFunction(node: MonteCarloNode): Int {
        var vi = node.utility
        var np = node.visits
        var ni = node.parent?.visits as Int

        var value = vi + C * Math.sqrt(Math.log(np.toDouble()) / ni)
        return value.toInt()
    }

    fun expand(node: MonteCarloNode) {
        var nodeState = node.state;
        if (stateMachineGamer.stateMachine.isTerminal(nodeState)) {
            return
        }

        for (move in stateMachineGamer.stateMachine.getLegalMoves(nodeState, stateMachineGamer.role)) {
            for (jointMoves in stateMachineGamer.stateMachine.getLegalJointMoves(nodeState, stateMachineGamer.role, move)) {
                var childState = stateMachineGamer.stateMachine.getNextState(nodeState, jointMoves);
                var newNode = node.createChildNode(childState, move)
                map[childState.hashCode()] = newNode
            }
        }
    }

    fun playout(node: MonteCarloNode): Int {
        try {
            val finalState = stateMachineGamer.stateMachine.performDepthCharge(node.state, null)
            return stateMachineGamer.stateMachine.getGoal(finalState, stateMachineGamer.role)
        } catch (e: Exception) {
            return 0
        }
    }

    fun backPropogate(node: MonteCarloNode, score: Int) {
        node.visits += 1
        node.utility += score

        var parent = node.parent
        if (parent != null) {
            backPropogate(parent, score)
        }
    }
}