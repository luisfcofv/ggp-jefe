package gamermanager
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.statemachine.Move
import search.IDDFS
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class SingleGamerManager(stateMachineGamer: StateMachineGamer) : GamerManager(stateMachineGamer) {

    override fun searchList(timeout: Long, executor: ExecutorService):ArrayList<Future<Move>> {
        var list = ArrayList<Future<Move>>()
        list.add(executor.submit(IDDFS(stateMachineGamer, timeout)))
        return list
    }
}
