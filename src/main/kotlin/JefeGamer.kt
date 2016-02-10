
import Manager.Gamer
import org.ggp.base.player.gamer.statemachine.sample.SampleGamer
import org.ggp.base.util.statemachine.Move

class JefeGamer : SampleGamer() {
    var started: Long = 0
    var gamer: Gamer? = null

    override fun getName(): String? {
        return "Jefe"
    }

    override fun stateMachineMetaGame(timeout: Long) {
        super.stateMachineMetaGame(timeout)
        started = System.currentTimeMillis()
        gamer = Gamer(this, timeout)
    }

    override fun stateMachineStop() {
        super.stateMachineStop()
        println("Problem solved in ${(System.currentTimeMillis() - started) / 1000.0} s.")
    }

    override fun stateMachineSelectMove(timeout: Long): Move? {
        return gamer!!.solve(timeout)
    }
}