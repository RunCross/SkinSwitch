package crossrun.top.skin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SkinUtils {
    public static View getView(String name, Context context, AttributeSet attrs) {
        View view = null;
        try {
            Class aclass = Class.forName(name);
            Constructor constructor = aclass.getConstructor(Context.class, AttributeSet.class);
            view = (View) constructor.newInstance(context,attrs);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return view;
    }
}
