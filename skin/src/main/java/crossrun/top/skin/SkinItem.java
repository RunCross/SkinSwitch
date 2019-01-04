package crossrun.top.skin;

import android.view.View;
import android.widget.TextView;

import java.util.List;

public class SkinItem {
    public View view;
    public List<SkinAttr> skinAttrs;

    public SkinItem(View view, List<SkinAttr> skinAttrs) {
        this.view = view;
        this.skinAttrs = skinAttrs;
    }

    public void apply() {
        for (SkinAttr skinAttr : skinAttrs) {
            if ("background".equals(skinAttr.attrName)) {
                if ("drawable".equals(skinAttr.attrType)) {//图片背景
                    view.setBackground(SkinManager.getInstance().getDrawable(skinAttr.id));
                } else if ("color".equals(skinAttr.attrType)) {
                    view.setBackgroundColor(SkinManager.getInstance().getColor(skinAttr.id));
                }
            } else if ("textColor".equals(skinAttr.attrName) && view instanceof TextView) {
                ((TextView) view).setTextColor(SkinManager.getInstance().getColor(skinAttr.id));
            }
        }
    }
}
