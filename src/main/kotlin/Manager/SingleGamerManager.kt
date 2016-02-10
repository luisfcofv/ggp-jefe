package Manager
import Model.MoveCandidate
import Search.IDDFS
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class SingleGamerManager(stateMachineGamer: StateMachineGamer, startClockTimeout: Long) : GamerManager(stateMachineGamer, startClockTimeout) {

    override fun searchList(timeout: Long, executor: ExecutorService):ArrayList<Future<MoveCandidate>> {
        var list = ArrayList<Future<MoveCandidate>>()

        list.add(executor.submit(IDDFS(stateMachineGamer, timeout)))

        return list
    }
}
