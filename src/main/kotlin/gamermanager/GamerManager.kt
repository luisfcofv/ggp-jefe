package gamermanager

import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Move
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

open class GamerManager(stateMachineGamer: StateMachineGamer) {
    var stateMachineGamer = stateMachineGamer

    open fun searchList(timeout: Long, executor: ExecutorService):ArrayList<Future<Move>> {
        throw UnsupportedOperationException()
    }

    fun solve(timeout: Long): Move {
        var executor = Executors.newFixedThreadPool(10)
        var list = searchList(timeout, executor)

        var movesList = ArrayList<Move>()
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

        executor.shutdown()
        return movesList[0]
    }
}