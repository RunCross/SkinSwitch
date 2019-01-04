package crossrun.top.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SkinManager {
    private static final SkinManager ourInstance = new SkinManager();

    public static SkinManager getInstance() {
        return ourInstance;
    }

    private SkinManager() {
    }

    private Context mContext;
    private Resources mSkinRes;
    private String mSkinPkg;


    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public void loadSkin(String apkPath) {
        if (TextUtils.isEmpty(apkPath)) {
            mSkinRes = null;
            mSkinPkg = null;
            return;
        }

        try {
            AssetManager manager = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.invoke(manager, apkPath);
            Resources res = mContext.getResources();
            mSkinRes = new Resources(manager, res.getDisplayMetrics(), res.getConfiguration());

            mSkinPkg = mContext.getPackageManager().getPackageArchiveInfo(apkPath
                    , PackageManager.GET_ACTIVITIES).packageName;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public int getColor(int id) {
        //默认的
        int color = mContext.getResources().getColor(id);
        if (mSkinRes != null) {
            //获取外部资源
            String name = mContext.getResources().getResourceEntryName(id);
            int resId = mSkinRes.getIdentifier(name, "color", mSkinPkg);
            if (resId > 0) {
                return mSkinRes.getColor(resId);
            }
        }
        return color;
    }

    public Drawable getDrawable(int id) {
        //默认的
        Drawable drawable = mContext.getResources().getDrawable(id);
        if (mSkinRes != null) {
            //获取外部资源
            String name = mContext.getResources().getResourceEntryName(id);
            int resId = mSkinRes.getIdentifier(name, "drawable", mSkinPkg);
            if (resId > 0) {
                return mSkinRes.getDrawable(resId);
            }
        }
        return drawable;

    }
}
