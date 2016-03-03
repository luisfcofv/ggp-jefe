package gamermanager

import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Move
import search.MonteCarloSearchTree
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class MonteCarloGamerManager(stateMachineGamer: StateMachineGamer) : GamerManager(stateMachineGamer) {
    var monteCarlo = MonteCarloSearchTree(stateMachineGamer)

    override fun searchList(timeout: Long, executor: ExecutorService): ArrayList<Future<Move>> {
        var list = ArrayList<Future<Move>>()

        monteCarlo.finishBy = timeout - 1000
        list.add(executor.submit(monteCarlo))

        return list
    }
}