package Manager

import Model.MoveCandidate
import Search.Minimax
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Role
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class MultiplayerGamerManager() : GamerManager() {
    var roles: List<Role>? = null

    override fun searchList(stateMachineGamer: StateMachineGamer, timeout: Long, executor: ExecutorService): ArrayList<Future<MoveCandidate>> {
        var list = ArrayList<Future<MoveCandidate>>()

        list.add(executor.submit(Minimax(stateMachineGamer, timeout, roles!!)))

        return list
    }
}