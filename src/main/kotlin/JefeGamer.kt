
import org.ggp.base.player.gamer.statemachine.sample.SampleGamer
import org.ggp.base.util.statemachine.MachineState
import org.ggp.base.util.statemachine.Move
import java.util.*

class JefeGamer : SampleGamer() {
    private val random = Random()
    private var betterMove: Move? = null
    private var betterScore: Int? = 0

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

        var moves = stateMachine.getLegalMoves(currentState, role)
        betterMove = moves[random.nextInt(moves.size)]

        val finishBy:Long = timeout - 1000

        var depth = 0
        while (System.currentTimeMillis() < finishBy) {
            for (move in moves) {
                var stateMachine = stateMachine.getNextState(currentState, listOf(move))
                var found = dls(stateMachine, depth, finishBy)
                if (found != null) {
                    betterMove = move
                }
            }
        }

        return betterMove
    }

    fun dls(node: MachineState, depth: Int, finishBy:Long): MachineState?  {
        if (depth == 0 && stateMachine.isTerminal(node) && stateMachine.getGoal(node, role) != 0) {
            return node
        } else if (depth > 0) {
            if (System.currentTimeMillis() > finishBy) {
                return null
            }

            var moves = stateMachine.getLegalMoves(node, role)
            for (move in moves) {
                var stateMachine = stateMachine.getNextState(node, listOf(move))
                var found = dls(stateMachine, depth - 1, finishBy)
                if (found != null) {
                    return stateMachine
                }
            }
        }

        return null
    }

    override fun getName(): String? {
        return "Jefe"
    }
}