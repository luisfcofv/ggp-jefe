
import org.ggp.base.player.gamer.statemachine.sample.SampleGamer
import org.ggp.base.util.statemachine.MachineState
import org.ggp.base.util.statemachine.Move

class JefeGamer : SampleGamer() {

    override fun getName(): String? {
        return "Jefe"
    }

    override fun stateMachineSelectMove(timeout: Long): Move? {
        //  procedure IDDFS(root)
        //      for depth from 0 to ∞
        //          found ← DLS(root, depth)
        //          if found ≠ null
        //              return found
        //
        //  procedure DLS(node, depth)
        //      if depth = 0 and node is a goal
        //          return node
        //      else if depth > 0
        //          foreach child of node
        //              found ← DLS(child, depth−1)
        //              if found ≠ null
        //                  return found
        //      return null

        val finishBy: Long = timeout - 1000
        var moves = stateMachine.getLegalMoves(currentState, role)

        var bestMove = moves[0]
        var bestScore: Int = 0
        var depth: Int = 0
        var found: Boolean = false

        while (System.currentTimeMillis() <= finishBy) {
            for (move in moves) {
                var stateMachine = stateMachine.getNextState(currentState, listOf(move))
                var score = dls(stateMachine, depth, finishBy)
                if (score > bestScore) {
                    bestScore = score
                    bestMove = move

                    if (bestScore == 100) {
                        found = true
                        break
                    }
                }
            }

            if (found) {
                break
            }

            depth++
        }

        return bestMove
    }

    fun dls(node: MachineState, depth: Int, finishBy:Long): Int  {
        if (depth == 0 && stateMachine.isTerminal(node) && stateMachine.getGoal(node, role) != 0) {
            return stateMachine.getGoal(node, role)
        } else if (depth > 0) {
            if (System.currentTimeMillis() > finishBy) {
                return 0
            }

            var bestScore: Int = 0

            var moves = stateMachine.getLegalMoves(node, role)
            for (move in moves) {
                var stateMachine = stateMachine.getNextState(node, listOf(move))
                var score = dls(stateMachine, depth - 1, finishBy)
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