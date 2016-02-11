package Search

import Model.MinimaxEntry
import Model.MoveCandidate
import Model.Type
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState
import org.ggp.base.util.statemachine.Move

class Minimax(stateMachineGamer: StateMachineGamer) : BaseSearch(stateMachineGamer) {
    var finishBy: Long = 0
    var transpositionTable = hashMapOf<Int, MinimaxEntry>()

    override fun call(): MoveCandidate? {
        var searchStarted = System.currentTimeMillis()
        var moves = stateMachineGamer.stateMachine.getLegalMoves(stateMachineGamer.currentState, stateMachineGamer.role)
        var score = 0
        var depth = 0
        var bestMove = moves[0]

        loop@ while (System.currentTimeMillis() <= finishBy) {
            for (move in moves) {
                var result = minscore(stateMachineGamer.currentState, move, depth, 0, 100)

                if (result > score) {
                    score = result
                    bestMove = move

                    if (result == 100) {
                        break@loop
                    }
                }
            }

            depth++
        }

        println("Search took ${(System.currentTimeMillis() - searchStarted) / 1000.0} s.")
        return MoveCandidate(bestMove, score)
    }

    fun maxscore(currentMachineState: MachineState, depth: Int, alpha: Int, beta: Int): Int {
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

        if (stateMachineGamer.stateMachine.isTerminal(currentMachineState)) {
            return stateMachineGamer.stateMachine.getGoal(currentMachineState, stateMachineGamer.role)
        } else if (depth == 0 || System.currentTimeMillis() > finishBy) {
            return 1
        }

        var bestScore = 0
        var moves = stateMachineGamer.stateMachine.getLegalMoves(currentMachineState, stateMachineGamer.role)
        for (move in moves) {
            var result = minscore(currentMachineState, move, depth - 1, newAlpha, newBeta)
            bestScore = Math.max(bestScore, result)
            newAlpha = Math.max(result, newAlpha)

            if (newAlpha >= newBeta) {
                break
            }
        }

        var newEntry = MinimaxEntry()
        newEntry.value = bestScore
        newEntry.depth = depth

        if (bestScore <= alpha) {
            newEntry.flag = Type.UPPERBOUND
        } else if (bestScore >= newBeta) {
            newEntry.flag = Type.LOWERBOUND
        } else {
            newEntry.flag = Type.EXACT
        }

        transpositionTable[currentMachineState.hashCode()] = newEntry
        return bestScore
    }

    fun minscore(currentMachineState: MachineState, action: Move, depth: Int, alpha: Int, beta: Int): Int {
        if (System.currentTimeMillis() > finishBy) {
            return beta
        }

        var newBeta = beta
        var opponentMoves = stateMachineGamer.stateMachine.getLegalJointMoves(currentMachineState, stateMachineGamer.role, action)

        for (opponentMove in opponentMoves) {
            var newMachineState = stateMachineGamer.stateMachine.getNextState(currentMachineState, opponentMove)
            var result = maxscore(newMachineState, depth, alpha, newBeta)
            newBeta = Math.min(newBeta, result)

            if (newBeta <= alpha) {
                return alpha
            }
        }

        return newBeta
    }
}