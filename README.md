# SkinSwitch
SkinSwitch
一个简单的Android换肤demo，在学习之后，记录保存下。

`SkinAttr` 保存单个属性相关信息，包括id，name（例如background），type（例如 color），entry（例如 @color/index_log里的index_log）

`SkinItem` 将`view`和`attr` 绑定的类

`SkinUtils` 用来反射生成`View`的

`SkinFactory` 解析过滤`View`的`attr`

`SkinManager` 解析获取新的皮肤的`attr`

首先通过`LayoutInflater.from(this).setFactory(skinFactory);`来将当前界面的属性解析绑定到一个自定义的`Factory`上面，注意在 ` super.onCreate(savedInstanceState);` 之前，然后需要初始化`SkinManager.getInstance().init();`，使用的时候调用`SkinManager.getInstance().loadSkin("apkPath");`解析皮肤包，`skinFactory.apply();`应用皮肤包。

在`SkinManager.init();`中，需要注意 `AssetManager`的生产，仿照Android源码里面，利用反射自己生成一个 `AssetManager`，然后再得到皮肤包的`Resources`：
```java
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
```

在`SkinFactory` 里面 仿照原始的`Factory.onCreateView()` 解析`View` ，解析过程中过滤属性以@开头的，就是准备替换的属性值，核心方法如下：
```java
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
```

在点击换肤以后，去解析皮肤包:
```java
    public void apply() {
        //如果要换status的颜色，就就传入Activity
//        Activity activity;
//        activity.getWindow().setStatusBarColor();

        for (SkinItem skinItem : skinItems) {
            skinItem.apply();
        }
    }
```
`SkinItem`里面，实现换肤的逻辑:
```java
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
```

具体的属性值，比如颜色、背景图之类的，通过`SkinManager`解析得到:
```java
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
```

本`demo`中只实现了`color`和`drawable`，其他的同理。