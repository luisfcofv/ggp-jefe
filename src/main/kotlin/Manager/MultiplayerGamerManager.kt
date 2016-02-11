package Manager

import Model.MoveCandidate
import Search.Minimax
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class MultiplayerGamerManager(stateMachineGamer: StateMachineGamer, startClockTimeout: Long) : GamerManager(stateMachineGamer, startClockTimeout) {
    var minimax = Minimax(stateMachineGamer, startClockTimeout)

    override fun searchList(timeout: Long, executor: ExecutorService): ArrayList<Future<MoveCandidate>> {
        var list = ArrayList<Future<MoveCandidate>>()

        minimax.finishBy = timeout - 300
        list.add(executor.submit(minimax))

        return list
    }
}