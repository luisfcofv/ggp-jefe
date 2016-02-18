
import Manager.Gamer
import org.ggp.base.player.gamer.statemachine.sample.SampleGamer
import org.ggp.base.util.match.Match
import org.ggp.base.util.statemachine.Move

class JefeGamer : SampleGamer() {
    companion object {
        @JvmField
        var MATCH: Match? = null
    }


    var started: Long = 0
    var gamer: Gamer? = null

    override fun getName(): String? {
        return "Jefe"
    }

    override fun stateMachineMetaGame(timeout: Long) {
        JefeGamer.MATCH = match
        started = System.currentTimeMillis()
        gamer = Gamer(this)
    }

    override fun stateMachineStop() {
        println("Problem solved in ${(System.currentTimeMillis() - started) / 1000.0} s.")
    }

    override fun stateMachineSelectMove(timeout: Long): Move? {
        return gamer!!.solve(timeout)
    }
}