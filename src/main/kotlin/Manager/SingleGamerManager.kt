package Manager
import Model.MoveCandidate
import Search.IDDFS
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class SingleGamerManager() : GamerManager() {

    override fun searchList(stateMachineGamer: StateMachineGamer, timeout: Long, executor: ExecutorService):ArrayList<Future<MoveCandidate>> {
        var list = ArrayList<Future<MoveCandidate>>()

        list.add(executor.submit(IDDFS(stateMachineGamer, timeout)))

        return list
    }
}
