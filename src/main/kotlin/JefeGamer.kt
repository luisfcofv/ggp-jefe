
import org.ggp.base.player.gamer.statemachine.sample.SampleGamer
import org.ggp.base.util.statemachine.Move

class JefeGamer : SampleGamer() {
    override fun stateMachineSelectMove(timeout: Long): Move? {
        throw UnsupportedOperationException()
    }

    override fun getName(): String? {
        return "Jefe"
    }
}