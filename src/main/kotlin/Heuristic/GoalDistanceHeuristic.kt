package Heuristic

import JefeGamer
import com.google.common.collect.Sets
import org.ggp.base.player.gamer.statemachine.StateMachineGamer
import org.ggp.base.util.gdl.grammar.GdlPool
import org.ggp.base.util.gdl.grammar.GdlTerm
import org.ggp.base.util.prover.aima.knowledge.KnowledgeBase
import org.ggp.base.util.prover.aima.substituter.Substituter
import org.ggp.base.util.prover.aima.unifier.Unifier
import org.ggp.base.util.statemachine.MachineState
import java.util.*

class GoalDistanceHeuristic : Heuristic {
    override fun evaluate(stateMachineGamer: StateMachineGamer, machineStates: ArrayList<MachineState>) : Int {
        val machineState = machineStates[0]
        val match = JefeGamer.MATCH!!

        val rules = match.game.rules;
        val kb = KnowledgeBase(Sets.newHashSet(rules));
        var goalArguments: ArrayList<GdlTerm> = arrayListOf(stateMachineGamer.roleName, GdlPool.getConstant("100"))
        val goalSentence = GdlPool.getRelation(GdlPool.GOAL, goalArguments);
        val goalRules = kb.fetch(goalSentence)

        var minValue = 100

        for(rule in goalRules) {
            var s = Unifier.unify(rule.head, goalSentence);
            if (s != null) {
                var newRule = Substituter.substitute(rule, s);
                var ruleSet = HashSet(newRule.body)
                var symmetricDifference = Sets.symmetricDifference(ruleSet, machineState.contents)
                minValue = Math.min(minValue, symmetricDifference.size)
            }
        }

        // Risk less than 50
        return 50 - minValue
    }
}