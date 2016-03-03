package gamermanager

import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Move
import search.Minimax
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class MultiplayerGamerManager(stateMachineGamer: StateMachineGamer) : GamerManager(stateMachineGamer) {
    var minimax = Minimax(stateMachineGamer)

    override fun searchList(timeout: Long, executor: ExecutorService): ArrayList<Future<Move>> {
        var list = ArrayList<Future<Move>>()

        minimax.finishBy = timeout - 300
        list.add(executor.submit(minimax))

        return list
    }
}