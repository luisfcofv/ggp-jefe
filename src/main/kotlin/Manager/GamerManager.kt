package Manager

import Model.MoveCandidate
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Move
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

open class GamerManager(stateMachineGamer: StateMachineGamer, startClockTimeout: Long) {
    val startClockTimeout = startClockTimeout
    var stateMachineGamer = stateMachineGamer

    open fun searchList(timeout: Long, executor: ExecutorService):ArrayList<Future<MoveCandidate>> {
        throw UnsupportedOperationException()
    }

    fun solve(timeout: Long): MoveCandidate {
        var executor = Executors.newFixedThreadPool(10)
        var list = searchList(timeout, executor)

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
            if (moveCandidates.score >= score) {
                score = moveCandidates.score
                bestMove = moveCandidates.move
            }
        }

        executor.shutdown()
        return MoveCandidate(bestMove!!, score)
    }
}