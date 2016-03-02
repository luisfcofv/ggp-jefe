package search

import model.MoveCandidate
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.MachineState
import org.ggp.base.util.statemachine.Move

class MonteCarloSearch(stateMachineGamer: StateMachineGamer) : BaseSearch(stateMachineGamer) {
    var finishBy: Long = 0

    override fun call(): MoveCandidate? {
        var moves = stateMachineGamer.stateMachine.getLegalMoves(stateMachineGamer.currentState, stateMachineGamer.role)
        var selection = moves[0]
        var bestMoveScore: Double = 0.0
        if (moves.size > 1) {
            val moveTotalPoints = IntArray(moves.size)
            val moveTotalAttempts = IntArray(moves.size)

            var moveIndex = 0
            while (System.currentTimeMillis() < finishBy) {
                val theScore = performDepthChargeFromMove(stateMachineGamer.currentState, moves[moveIndex])
                moveTotalPoints[moveIndex] += theScore
                moveTotalAttempts[moveIndex] += 1
                moveIndex = (moveIndex + 1) % moves.size
            }

            val moveExpectedPoints = DoubleArray(moves.size)
            for (index in moves.indices) {
                moveExpectedPoints[index] = moveTotalPoints[index].toDouble() / moveTotalAttempts[index]
            }

            var bestMove = 0
            bestMoveScore = moveExpectedPoints[0]
            for (move in 1..moves.size - 1) {
                if (moveExpectedPoints[move] > bestMoveScore) {
                    bestMoveScore = moveExpectedPoints[move]
                    bestMove = move
                }
            }
            selection = moves[bestMove]
        }

        return MoveCandidate(selection, bestMoveScore.toInt())
    }

    private val depth = IntArray(1)
    internal fun performDepthChargeFromMove(machineState: MachineState, move: Move): Int {
        val stateMachine = stateMachineGamer.stateMachine
        try {
            val finalState = stateMachine.performDepthCharge(stateMachine.getRandomNextState(machineState, stateMachineGamer.role, move), depth)
            return stateMachine.getGoal(finalState, stateMachineGamer.role)
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }
}