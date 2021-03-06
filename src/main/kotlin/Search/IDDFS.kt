package search
import heuristic.GoalDistanceHeuristic
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState
import org.ggp.base.util.statemachine.Move

class IDDFS(stateMachineGamer: StateMachineGamer, timeout: Long) : BaseSearch(stateMachineGamer) {
    val finishBy = timeout - 1000
    var statesVisited = hashMapOf<Int, Int>()

    override fun call(): Move? {
        var searchStarted = System.currentTimeMillis()
        var moves = stateMachineGamer.stateMachine.getLegalMoves(stateMachineGamer.currentState, stateMachineGamer.role)

        var bestMove = moves[0]
        var bestScore: Int = 0
        var depth: Int = 0

        while (System.currentTimeMillis() <= finishBy) {
            statesVisited.clear()
            statesVisited[stateMachineGamer.currentState.hashCode()] = 1

            for (move in moves) {
                var machineState = stateMachineGamer.stateMachine.getNextState(stateMachineGamer.currentState, listOf(move))
                var score = recursiveDepthLimited(machineState, depth)

                if (score > bestScore) {
                    bestScore = score
                    bestMove = move

                    if (bestScore == 100) {
                        println("Search took ${(System.currentTimeMillis() - searchStarted) / 1000.0} s.")
                        return bestMove
                    }
                }
            }

            depth++
        }

        println("Search took ${(System.currentTimeMillis() - searchStarted) / 1000.0} s.")
        return bestMove
    }

    fun recursiveDepthLimited(node: MachineState,depth: Int): Int  {
        if (statesVisited[node.hashCode()] == 1) {
            return 0
        }

        statesVisited[node.hashCode()] = 1

        if (stateMachineGamer.stateMachine.isTerminal(node)) {
            return stateMachineGamer.stateMachine.getGoal(node, stateMachineGamer.role)
        } else if (depth == 0) {
            // A non-terminal state is better than a 0 score terminal state
            return GoalDistanceHeuristic().evaluate(stateMachineGamer, arrayListOf(node))
        } else if (depth > 0) {
            if (System.currentTimeMillis() > finishBy) {
                return 1
            }

            var bestScore: Int = 0
            for (move in stateMachineGamer.stateMachine.getLegalMoves(node, stateMachineGamer.role)) {
                var machineState = stateMachineGamer.stateMachine.getNextState(node, listOf(move))
                var score = recursiveDepthLimited(machineState, depth - 1)

                if (score > bestScore) {
                    bestScore = score

                    if (score == 100) {
                        return score
                    }
                }
            }

            return bestScore
        }

        return 0
    }
}