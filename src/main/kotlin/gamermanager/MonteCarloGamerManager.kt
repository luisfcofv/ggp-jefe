package gamermanager

import model.MoveCandidate
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import search.MonteCarloSearchTree
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class MonteCarloGamerManager(stateMachineGamer: StateMachineGamer) : GamerManager(stateMachineGamer) {
    var monteCarlo = MonteCarloSearchTree(stateMachineGamer)

    override fun searchList(timeout: Long, executor: ExecutorService): ArrayList<Future<MoveCandidate>> {
        var list = ArrayList<Future<MoveCandidate>>()

        monteCarlo.finishBy = timeout - 1000
        list.add(executor.submit(monteCarlo))

        return list
    }
}