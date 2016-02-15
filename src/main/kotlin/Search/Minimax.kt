package Search

import Model.MinimaxEntry
import Model.MoveCandidate
import Model.Type
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState
import org.ggp.base.util.statemachine.Move

class Minimax(stateMachineGamer: StateMachineGamer) : BaseSearch(stateMachineGamer) {
    var finishBy: Long = 0
    var maxDepthReached: Boolean = true
    var transpositionTable = hashMapOf<Int, MinimaxEntry>()

    override fun call(): MoveCandidate? {
        var searchStarted = System.currentTimeMillis()
        var moves = stateMachineGamer.stateMachine.getLegalMoves(stateMachineGamer.currentState, stateMachineGamer.role)
        var score = 0
        var bestMove = moves[0]

        var depth = 0
        loop@while (System.currentTimeMillis() <= finishBy) {
            maxDepthReached = true
            depth++

            for (move in moves) {
                var result = minscore(stateMachineGamer.currentState, move, depth, 0, 100)

                if (result > score) {
                    score = result
                    bestMove = move

                    if (score == 100) {
                        break@loop
                    }
                }
            }

            if (maxDepthReached) {
                break
            }
        }

        println("Search took ${(System.currentTimeMillis() - searchStarted) / 1000.0} s.")
        return MoveCandidate(bestMove, score)
    }

    fun maxscore(currentMachineState: MachineState, depth: Int, alpha: Int, beta: Int): Int {
        if (stateMachineGamer.stateMachine.isTerminal(currentMachineState)) {
            return stateMachineGamer.stateMachine.getGoal(currentMachineState, stateMachineGamer.role)
        } else if (depth == 0 || System.currentTimeMillis() > finishBy) {
            maxDepthReached = false
            return 1
        }

        var newAlpha = alpha
        var newBeta = beta

        var entry = transpositionTable[currentMachineState.hashCode()]
        if (entry != null && entry.depth!! >= depth) {
            if (entry.flag == Type.EXACT) {
                return entry.value!!
            } else if (entry.flag == Type.LOWERBOUND) {
                newAlpha = Math.max(newAlpha, entry.value!!)
            } else if (entry.flag == Type.UPPERBOUND) {
                newBeta = Math.min(newBeta, entry.value!!)
            }

            if (newAlpha >= newBeta) {
                return entry.value!!
            }
        }

        var score = 0
        for (move in stateMachineGamer.stateMachine.getLegalMoves(currentMachineState, stateMachineGamer.role)) {
            var result = minscore(currentMachineState, move, depth, newAlpha, newBeta)
            score = Math.max(score, result)
            newAlpha = Math.max(newAlpha, result)

            if (newAlpha >= newBeta) {
                break
            }
        }

        var newEntry = MinimaxEntry()
        newEntry.value = score
        newEntry.depth = depth

        if (score <= alpha) {
            newEntry.flag = Type.UPPERBOUND
        } else if (score >= newBeta) {
            newEntry.flag = Type.LOWERBOUND
        } else {
            newEntry.flag = Type.EXACT
        }

        transpositionTable[currentMachineState.hashCode()] = newEntry
        return score
    }

    fun minscore(currentMachineState: MachineState, action: Move, depth: Int, alpha: Int, beta: Int): Int {
        var newBeta = beta

        for (opponentMove in stateMachineGamer.stateMachine.getLegalJointMoves(currentMachineState, stateMachineGamer.role, action)) {
            var newMachineState = stateMachineGamer.stateMachine.getNextState(currentMachineState, opponentMove)
            var result = maxscore(newMachineState, depth - 1, alpha, newBeta)
            newBeta = Math.min(newBeta, result)

            if (newBeta <= alpha) {
                return alpha
            }
        }

        return newBeta
    }
}