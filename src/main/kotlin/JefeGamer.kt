

import gamer.Gamer
import org.ggp.base.apps.player.detail.DetailPanel
import org.ggp.base.apps.player.detail.SimpleDetailPanel
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.game.Game
import org.ggp.base.util.match.Match
import org.ggp.base.util.statemachine.Move
import org.ggp.base.util.statemachine.StateMachine
import org.ggp.base.util.statemachine.cache.CachedStateMachine
import org.ggp.base.util.statemachine.implementation.prover.ProverStateMachine

class JefeGamer : StateMachineGamer() {
    companion object {
        @JvmField
        var MATCH: Match? = null
    }

    var started: Long = 0
    var jefeGamer: Gamer? = null

    override fun preview(g: Game?, timeout: Long) {
        // Nothing
    }

    override fun getName(): String? {
        return "Jefe"
    }

    override fun getInitialStateMachine(): StateMachine? {
        return CachedStateMachine(ProverStateMachine())
    }

    override fun getDetailPanel(): DetailPanel {
        return SimpleDetailPanel()
    }

    override fun stateMachineAbort() {
        println("stateMachineAbort")
    }

    override fun stateMachineMetaGame(timeout: Long) {
        JefeGamer.MATCH = match
        started = System.currentTimeMillis()
        jefeGamer = Gamer(this)
    }

    override fun stateMachineStop() {
        println("Problem solved in ${(System.currentTimeMillis() - started) / 1000.0} s.")
    }

    override fun stateMachineSelectMove(timeout: Long): Move? {
        return jefeGamer!!.solve(timeout)
    }
}