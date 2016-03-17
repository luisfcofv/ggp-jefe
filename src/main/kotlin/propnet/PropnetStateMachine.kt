package propnet

import org.ggp.base.util.gdl.grammar.Gdl
import org.ggp.base.util.gdl.grammar.GdlSentence
import org.ggp.base.util.gdl.grammar.GdlTerm
import org.ggp.base.util.propnet.architecture.PropNet
import org.ggp.base.util.propnet.architecture.components.Proposition
import org.ggp.base.util.propnet.factory.OptimizingPropNetFactory
import org.ggp.base.util.statemachine.MachineState
import org.ggp.base.util.statemachine.Move
import org.ggp.base.util.statemachine.Role
import org.ggp.base.util.statemachine.StateMachine
import java.util.*

class PropnetStateMachine : StateMachine() {
    var propNet: PropNet? = null
    var propNetOrdering: List<Proposition>? = null
    var propNetRoles: MutableList<Role>? = null;

    override fun initialize(description: MutableList<Gdl>?) {
        try {
            propNet = OptimizingPropNetFactory.create(description)
            propNetRoles = propNet!!.roles
            propNetOrdering = ordering()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun getGoal(state: MachineState?, role: Role?): Int {
        basePropositions(state!!)
        propagate()
        for (proposition in propNet!!.goalPropositions[role]!!) {
            if (proposition.value) {
                return Integer.parseInt(proposition.name.body[1].toString())
            }
        }

        return 0
    }

    override fun isTerminal(state: MachineState?): Boolean {
        basePropositions(state!!)
        propagate();
        return propNet!!.terminalProposition.value;
    }

    override fun getRoles(): MutableList<Role>? {
        return propNetRoles
    }

    override fun getInitialState(): MachineState? {
        propNet!!.initProposition.value = true

        val contents = HashSet<GdlSentence>()
        for (proposition in propNet!!.basePropositions.values) {
            if (proposition.singleInput.value) {
                contents.add(proposition.name)
            }
            proposition.value = proposition.singleInput.value
        }

        return MachineState(contents)
    }

    override fun getLegalMoves(state: MachineState?, role: Role?): MutableList<Move>? {
        basePropositions(state!!)
        propagate()

        val moves = ArrayList<Move>()

        for (proposition in propNet!!.legalPropositions[role]!!) {
            if (proposition.value) {
                moves.add(Move(proposition.name.body[1]))
            }
        }

        return moves
    }

    override fun getNextState(state: MachineState?, moves: MutableList<Move>?): MachineState? {
        val contents = HashSet<GdlSentence>()
        basePropositions(state!!)
        inputPropositions(moves!!)
        propagate()

        for (propositions in propNet!!.basePropositions.values) {
            if (propositions.singleInput.value) {
                contents.add(propositions.name)
            }
        }
        return MachineState(contents)
    }

    fun ordering(): List<Proposition> {
        val order = LinkedList<Proposition>()
        var components = ArrayList(propNet!!.components)

        for (proposition in propNet!!.basePropositions.values) {
            components.remove(proposition)
        }

        for (proposition in propNet!!.inputPropositions.values) {
            components.remove(proposition)
        }

        components.remove(propNet!!.initProposition)

        while (components.size != 0) {
            val mutableComponents = ArrayList(components)
            for (component in components) {
                var inputsNotFound = true
                for (input in component.inputs) {
                    if (components.contains(input)) {
                        inputsNotFound = false
                        break
                    }
                }

                if (inputsNotFound) {
                    mutableComponents.remove(component)

                    if (component is Proposition) {
                        order.add(component)
                    }
                }
            }

            components = mutableComponents
        }

        return order
    }

    fun propagate() {
        for (proposition in propNetOrdering!!) {
            proposition.value = proposition.singleInput.value
        }
    }

    fun basePropositions(machineState: MachineState) {
        propNet!!.initProposition.value = false
        for (proposition in propNet!!.basePropositions.values) {
            proposition.value = machineState.contents.contains(proposition.name)
        }
    }

    fun inputPropositions(moves:List<Move>) {
        val roleToMoves = HashMap<GdlTerm, GdlTerm>()
        for (index in 0..moves.size - 1) {
            roleToMoves.put(roles!![index].name, moves[index].contents)
        }

        val inputPropositions = propNet!!.inputPropositions
        for (term in inputPropositions.keys) {
            inputPropositions[term]!!.value = roleToMoves[term.body[0]] == term.body[1]
        }
    }
}