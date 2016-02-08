package Manager
import Model.MoveCandidate
import Search.IDDFS
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Move
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.Future

class SingleGamerManager : Gamer {

    override fun solve(stateMachineGamer: StateMachineGamer, timeout: Long): MoveCandidate? {
        var executor = Executors.newFixedThreadPool(10)
        var list = ArrayList<Future<MoveCandidate>>()

        var search = IDDFS(stateMachineGamer, timeout)
        var submit = executor.submit(search)
        list.add(submit)

        var movesList = ArrayList<MoveCandidate>()
        for (future in list) {
            try {
                var moveCandidate = future.get()
                movesList.add(moveCandidate)
            } catch (exception: InterruptedException) {
                exception.printStackTrace()
            } catch (exception: ExecutionException) {
                exception.printStackTrace()
            }
        }

        var score = 0
        var bestMove: Move? = null
        for (moveCandidates in movesList) {
            if (moveCandidates.score > score) {
                score = moveCandidates.score
                bestMove = moveCandidates.move
            }
        }

        executor.shutdown()
        return MoveCandidate(bestMove!!, score)
    }
}
