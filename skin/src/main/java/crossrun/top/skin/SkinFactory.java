package crossrun.top.skin;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SkinFactory implements LayoutInflater.Factory {

    public static final String[] VIEW_PREFIX = {"android.view.", "android.widget.", "android.webkit."};

    private List<SkinItem> skinItems = new ArrayList<>();

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = createView(name, context, attrs);

        if (view != null) {
            //解析属性
            parseSkinAttr(view, context, attrs);
        }

        return view;
    }

    private void parseSkinAttr(View view, Context context, AttributeSet attrs) {

        List<SkinAttr> skinAttrs = new ArrayList<>();
        Resources res = context.getResources();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {

            String attrValue = attrs.getAttributeValue(i);
            //过滤
            if (attrValue.startsWith("@")) {
                int id = Integer.parseInt(attrValue.substring(1));
                SkinAttr skinAttr = new SkinAttr(id
                        , attrs.getAttributeName(i)
                        , res.getResourceTypeName(id)
                        , res.getResourceEntryName(id));
                skinAttrs.add(skinAttr);
            }
        }

        //可能需要换肤的
        if (!skinAttrs.isEmpty()) {
            SkinItem skinItem = new SkinItem(view, skinAttrs);
            skinItems.add(skinItem);
        }
    }


    public void apply() {
        //如果要换status的颜色，就就传入Activity
//        Activity activity;
//        activity.getWindow().setStatusBarColor();

        for (SkinItem skinItem : skinItems) {
            skinItem.apply();
        }
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.contains(".")) {//自定义
            view = SkinUtils.getView(name, context, attrs);
        } else {//系统
            for (String viewPrefix : VIEW_PREFIX) {

                String className = viewPrefix + name;
                view = SkinUtils.getView(className, context, attrs);
                if (view != null) {
                    break;
                }
            }
        }

        return view;
    }
}
