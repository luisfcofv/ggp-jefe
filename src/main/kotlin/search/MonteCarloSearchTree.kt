package search

import model.MonteCarloNode
import model.MoveCandidate
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState
import org.ggp.base.util.statemachine.Move
import java.util.*

class MonteCarloSearchTree(stateMachineGamer: StateMachineGamer) : BaseSearch(stateMachineGamer) {
    var finishBy: Long = 0
    val C = 40
    var map = hashMapOf<Int, MonteCarloNode>()

    override fun call(): MoveCandidate? {
        map.clear()
        var searchStarted = System.currentTimeMillis()
        var moves = stateMachineGamer.stateMachine.getLegalMoves(stateMachineGamer.currentState, stateMachineGamer.role)
        var selection = moves[0]

        var startNode: MonteCarloNode?
        if (map[stateMachineGamer.currentState.hashCode()] != null) {
            startNode = map[stateMachineGamer.currentState.hashCode()]
        } else {
            startNode = MonteCarloNode(stateMachineGamer.currentState);
        }

        while (System.currentTimeMillis() < finishBy) {
            var selectedNode = select(startNode!!)
            expand(selectedNode)
            val playoutScore = playout(selectedNode)
            backPropogate(selectedNode, playoutScore)
        }

        var bestChildNode: MonteCarloNode? = null;


        for (node in startNode!!.children) {
            if (bestChildNode == null || node.utility > bestChildNode.utility) {
                bestChildNode = node;
            }
        }

        println("Child: ${bestChildNode!!.parentMove}")
        println("Search took ${(System.currentTimeMillis() - searchStarted) / 1000.0} s.")

        selection = bestChildNode.parentMove
        println("selection: $selection")
        return MoveCandidate(selection, bestChildNode.utility)
    }

    fun select(node: MonteCarloNode): MonteCarloNode {
        if (node.visits == 0 || node.children.size == 0) {
            return node
        }

        for (children in node.children) {
            if (children.visits == 0) {
                return children
            }
        }

        var score = Integer.MIN_VALUE
        var selectedNode = node

        for (children in node.children) {
            var result = selectFunction(children)
            if (result > score) {
                score = result
                selectedNode = children
            }
        }

        return select(selectedNode!!)
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

        for (move in stateMachineGamer.stateMachine.getLegalMoves(stateMachineGamer.currentState, stateMachineGamer.role)) {
            for (jointMoves in stateMachineGamer.stateMachine.getLegalJointMoves(nodeState, stateMachineGamer.role, move)) {
                var childState = stateMachineGamer.stateMachine.getNextState(nodeState, jointMoves);
                var newNode = node.createChildNode(childState, move)
                map[childState.hashCode()] = newNode
            }
        }
    }

    fun playout(node: MonteCarloNode): Int {
        return depthCharge(node.state)
    }

    fun depthCharge(machineState: MachineState): Int {
        if (stateMachineGamer.stateMachine.isTerminal(machineState)) {
            return stateMachineGamer.stateMachine.getGoal(machineState, stateMachineGamer.role)
        }

        var randomMoves = arrayListOf<Move>()
        for (role in stateMachineGamer.stateMachine.roles) {
            var moves = stateMachineGamer.stateMachine.getLegalMoves(machineState, role);
            var rand = Random();
            var randomMove = moves[rand.nextInt(moves.size)];
            randomMoves.add(randomMove)
        }

        var nextState = stateMachineGamer.stateMachine.getNextState(machineState, randomMoves);
        return depthCharge(nextState)
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