package Search

import Model.MoveCandidate
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState
import org.ggp.base.util.statemachine.Move
import org.ggp.base.util.statemachine.Role

class Minimax : BaseSearch {
    var roles: List<Role>? = null
    var opponent: Role? = null

    constructor(stateMachineGamer: StateMachineGamer, timeout: Long, roles: List<Role>) : super(stateMachineGamer, timeout) {
        this.roles = roles
        this.opponent = roles.filter { r -> r != stateMachineGamer.role}[0]
    }

    override fun call(): MoveCandidate? {
        var searchStarted = System.currentTimeMillis()
        var moves = stateMachineGamer.stateMachine.getLegalMoves(stateMachineGamer.currentState, stateMachineGamer.role)
        var score = 0
        var bestMove = moves[0]

        for (move in moves) {
            var result = minscore(stateMachineGamer.currentState, move, 0, 100)

            if (result > score) {
                score = result
                bestMove = move
            }
        }

        println("Search took ${(System.currentTimeMillis() - searchStarted) / 1000.0} s.")
        return MoveCandidate(bestMove, score)
    }

    fun maxscore(currentMachineState: MachineState, alpha: Int, beta: Int):Int {
        if (stateMachineGamer.stateMachine.isTerminal(currentMachineState)) {
            return stateMachineGamer.stateMachine.getGoal(currentMachineState, stateMachineGamer.role)
        } else if (System.currentTimeMillis() > finishBy) {
            return 1
        }

        var newAlpha = alpha
        var moves = stateMachineGamer.stateMachine.getLegalMoves(currentMachineState, stateMachineGamer.role)

        for (move in moves) {
            var result = minscore(currentMachineState, move, alpha, beta)
            newAlpha = Math.max(result, newAlpha)

            if (newAlpha >= beta) {
                return beta
            }
        }

        return newAlpha
    }

    fun minscore(currentMachineState: MachineState, action: Move, alpha: Int, beta: Int):Int {
        if (System.currentTimeMillis() > finishBy) {
            return 1
        }

        var newBeta = beta
        var opponentMoves = stateMachineGamer.stateMachine.getLegalMoves(currentMachineState, opponent)

        for (opponentMove in opponentMoves) {
            var moves = arrayListOf(action, action)
            moves[roles!!.indexOf(opponent)] = opponentMove

            var newMachineState = stateMachineGamer.stateMachine.getNextState(currentMachineState, moves)
            var result = maxscore(newMachineState, alpha, beta)
            newBeta = Math.min(newBeta, result)

            if (beta <= alpha) {
                return alpha
            }
        }

        return newBeta
    }
}