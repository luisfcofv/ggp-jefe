package Search

import Model.MoveCandidate
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState
import org.ggp.base.util.statemachine.Move
import org.ggp.base.util.statemachine.Role

class Minimax : BaseSearch {
    var opponent: Role? = null
    var finishBy: Long? = 0
    var transpositionTable = hashMapOf<Int, Int>()

    constructor(stateMachineGamer: StateMachineGamer, timeout: Long) : super(stateMachineGamer) {
        this.opponent = stateMachineGamer.stateMachine.roles.filter { r -> r != stateMachineGamer.role}[0]
        start(timeout - 300)
    }

    override fun call(): MoveCandidate? {
        return start(finishBy!!)
    }

    fun start(timeout: Long): MoveCandidate? {
        var searchStarted = System.currentTimeMillis()
        var moves = stateMachineGamer.stateMachine.getLegalMoves(stateMachineGamer.currentState, stateMachineGamer.role)
        var score = 0
        var bestMove = moves[0]

        for (move in moves) {
            var result = minscore(stateMachineGamer.currentState, move, 0, 100, timeout)

            if (result > score) {
                score = result
                bestMove = move
            }
        }

        println("Search took ${(System.currentTimeMillis() - searchStarted) / 1000.0} s.")
        return MoveCandidate(bestMove, score)
    }

    fun maxscore(currentMachineState: MachineState, alpha: Int, beta: Int, timeout: Long):Int {
        if (transpositionTable[currentMachineState.hashCode()] != null) {
            return transpositionTable[currentMachineState.hashCode()]!!
        }

        if (stateMachineGamer.stateMachine.isTerminal(currentMachineState)) {
            var score = stateMachineGamer.stateMachine.getGoal(currentMachineState, stateMachineGamer.role)
            transpositionTable[currentMachineState.hashCode()] = score
            return score
        } else if (System.currentTimeMillis() > timeout) {
            return 1
        }

        var newAlpha = alpha
        var moves = stateMachineGamer.stateMachine.getLegalMoves(currentMachineState, stateMachineGamer.role)

        for (move in moves) {
            var result = minscore(currentMachineState, move, alpha, beta, timeout)
            newAlpha = Math.max(result, newAlpha)

            if (newAlpha >= beta) {
                transpositionTable[currentMachineState.hashCode()] = beta
                return beta
            }
        }

        transpositionTable[currentMachineState.hashCode()] = newAlpha
        return newAlpha
    }

    fun minscore(currentMachineState: MachineState, action: Move, alpha: Int, beta: Int, timeout: Long):Int {
        if (System.currentTimeMillis() > timeout) {
            return 1
        }

        var newBeta = beta
        var opponentMoves = stateMachineGamer.stateMachine.getLegalMoves(currentMachineState, opponent)

        for (opponentMove in opponentMoves) {
            var moves = arrayListOf(action, action)
            moves[stateMachineGamer.stateMachine.roles!!.indexOf(opponent)] = opponentMove

            var newMachineState = stateMachineGamer.stateMachine.getNextState(currentMachineState, moves)
            var result = maxscore(newMachineState, alpha, beta, timeout)
            newBeta = Math.min(newBeta, result)

            if (beta <= alpha) {
                return alpha
            }
        }

        return newBeta
    }
}