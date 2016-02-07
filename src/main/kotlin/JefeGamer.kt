
import org.ggp.base.player.gamer.statemachine.sample.SampleGamer
import org.ggp.base.util.statemachine.MachineState
import org.ggp.base.util.statemachine.Move

class JefeGamer : SampleGamer() {
    var statesVisited = hashMapOf<Int, Int>()
    var started: Long = 0

    override fun getName(): String? {
        return "Jefe"
    }

    override fun stateMachineMetaGame(timeout: Long) {
        super.stateMachineMetaGame(timeout)
        started = System.currentTimeMillis()
    }

    override fun stateMachineStop() {
        super.stateMachineStop()
        println("Problem solved in ${(System.currentTimeMillis() - started) / 1000.0} s.")
    }

    override fun stateMachineSelectMove(timeout: Long): Move? {
        var searchStarted = System.currentTimeMillis()

        val finishBy: Long = timeout - 1000
        var moves = stateMachine.getLegalMoves(currentState, role)

        var bestMove = moves[0]
        var bestScore: Int = 0
        var depth: Int = 0

        while (System.currentTimeMillis() <= finishBy) {
            statesVisited.clear()
            statesVisited[currentState.hashCode()] = 1

            for (move in moves) {
                var machineState = stateMachine.getNextState(currentState, listOf(move))
                var score = dls(machineState, depth, finishBy)

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

    fun dls(node: MachineState, depth: Int, finishBy:Long): Int  {
        if (statesVisited[node.hashCode()] == 1) {
            return 0
        }

        statesVisited[node.hashCode()] = 1

        if (stateMachine.isTerminal(node)) {
            return stateMachine.getGoal(node, role)
        } else if (depth == 0) {
            // A non-terminal state is better than a 0 score terminal state
            return 1
        } else if (depth > 0) {
            if (System.currentTimeMillis() > finishBy) {
                return 1
            }

            var bestScore: Int = 0
            for (move in stateMachine.getLegalMoves(node, role)) {
                var machineState = stateMachine.getNextState(node, listOf(move))
                var score = dls(machineState, depth - 1, finishBy)

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