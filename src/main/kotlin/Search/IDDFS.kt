package Search
import Model.MoveCandidate
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState
import java.util.concurrent.Callable

class IDDFS(stateMachineGamer: StateMachineGamer, timeout: Long): Callable<MoveCandidate> {
    val stateMachineGamer = stateMachineGamer
    val timeout = timeout
    var statesVisited = hashMapOf<Int, Int>()

    override fun call(): MoveCandidate? {
        var searchStarted = System.currentTimeMillis()

        val finishBy: Long = timeout - 300
        var moves = stateMachineGamer.stateMachine.getLegalMoves(stateMachineGamer.currentState, stateMachineGamer.role)

        var bestMove = moves[0]
        var bestScore: Int = 0
        var depth: Int = 0

        while (System.currentTimeMillis() <= finishBy) {
            statesVisited.clear()
            statesVisited[stateMachineGamer.currentState.hashCode()] = 1

            for (move in moves) {
                var machineState = stateMachineGamer.stateMachine.getNextState(stateMachineGamer.currentState, listOf(move))
                var score = recursiveDepthLimited(machineState, depth, finishBy)

                if (score > bestScore) {
                    bestScore = score
                    bestMove = move

                    if (bestScore == 100) {
                        println("Search took ${(System.currentTimeMillis() - searchStarted) / 1000.0} s.")
                        return MoveCandidate(bestMove, bestScore)
                    }
                }
            }

            depth++
        }

        println("Search took ${(System.currentTimeMillis() - searchStarted) / 1000.0} s.")
        return MoveCandidate(bestMove, bestScore)
    }

    fun recursiveDepthLimited(node: MachineState, depth: Int, finishBy:Long): Int  {
        if (statesVisited[node.hashCode()] == 1) {
            return 0
        }

        statesVisited[node.hashCode()] = 1

        if (stateMachineGamer.stateMachine.isTerminal(node)) {
            return stateMachineGamer.stateMachine.getGoal(node, stateMachineGamer.role)
        } else if (depth == 0) {
            // A non-terminal state is better than a 0 score terminal state
            return 1
        } else if (depth > 0) {
            if (System.currentTimeMillis() > finishBy) {
                return 1
            }

            var bestScore: Int = 0
            for (move in stateMachineGamer.stateMachine.getLegalMoves(node, stateMachineGamer.role)) {
                var machineState = stateMachineGamer.stateMachine.getNextState(node, listOf(move))
                var score = recursiveDepthLimited(machineState, depth - 1, finishBy)

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