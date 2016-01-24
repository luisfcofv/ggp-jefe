package org.ggp.base.util.reflection;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.ggp.base.apps.kiosk.GameCanvas;
import org.ggp.base.player.gamer.Gamer;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;

public class ProjectSearcher {
    public static void main(String[] args)
    {
        System.out.println(GAMERS);
        System.out.println(GAME_CANVASES);
    }

    private static final Reflections REFLECTIONS = new Reflections();

    public static final LoadedClasses<Gamer> GAMERS = new LoadedClasses<>(Gamer.class);
    public static final LoadedClasses<GameCanvas> GAME_CANVASES = new LoadedClasses<>(GameCanvas.class);

    public static class LoadedClasses<T> {
        private static Predicate<Class<?>> IS_CONCRETE_CLASS = klass -> !Modifier.isAbstract(klass.getModifiers());

        private final Class<T> interfaceClass;
        private final ImmutableSet<Class<? extends T>> allClasses;
        private final ImmutableSet<Class<? extends T>> concreteClasses;

        private LoadedClasses(Class<T> interfaceClass) {
            this.interfaceClass = interfaceClass;
            this.allClasses = ImmutableSet.copyOf(REFLECTIONS.getSubTypesOf(interfaceClass));
            this.concreteClasses = ImmutableSet.copyOf(Sets.filter(allClasses, IS_CONCRETE_CLASS));
        }

        public ImmutableSet<Class<? extends T>> getConcreteClasses() {
            return concreteClasses;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("allClasses", allClasses)
                    .add("interfaceClass", interfaceClass)
                    .add("concreteClasses", concreteClasses)
                    .toString();
        }
    }
}
