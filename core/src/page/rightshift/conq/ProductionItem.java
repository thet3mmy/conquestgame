package page.rightshift.conq;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.badlogic.gdx.math.Vector2;

public class ProductionItem {
    public Class<? extends Unit> target;
    public Town ownerTown;
    private Constructor<?> constr;
    public int complete;
    public int progress = 0;

    ProductionItem(int curTime, int dur, Class<? extends Unit> cl, Town own) {
        complete = curTime + dur;
        target = cl;
        constr = target.getDeclaredConstructors()[0];
        ownerTown = own;
    }

    public Unit produce() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object o = constr.newInstance(ownerTown.pos, ownerTown.owner);
        ownerTown.queue.remove(this);
        return (Unit)o;
    }

    public String getDisplayNameOfTarget() {
        try {
            Method m = target.getMethod("getDisplayName");
            Object o = m.invoke(constr.newInstance(new Vector2(), 1));
            return (String)o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
