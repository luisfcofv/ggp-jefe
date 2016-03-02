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

            // Perform depth charges for each candidate move, and keep track
            // of the total score and total attempts accumulated for each move.

            var i = 0
            while (System.currentTimeMillis() < finishBy) {
                val theScore = performDepthChargeFromMove(stateMachineGamer.currentState, moves[i])
                moveTotalPoints[i] += theScore
                moveTotalAttempts[i] += 1
                i = (i + 1) % moves.size
            }

            // Compute the expected score for each move.
            val moveExpectedPoints = DoubleArray(moves.size)
            for (i in moves.indices) {
                moveExpectedPoints[i] = moveTotalPoints[i].toDouble() / moveTotalAttempts[i]
            }

            // Find the move with the best expected score.
            var bestMove = 0
            bestMoveScore = moveExpectedPoints[0]
            for (i in 1..moves.size - 1) {
                if (moveExpectedPoints[i] > bestMoveScore) {
                    bestMoveScore = moveExpectedPoints[i]
                    bestMove = i
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