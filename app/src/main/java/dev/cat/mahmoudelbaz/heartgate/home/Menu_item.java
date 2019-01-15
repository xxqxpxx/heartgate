package dev.cat.mahmoudelbaz.heartgate.home;

/**
 * Created by mahmoudelbaz on 5/17/17.
 */

public class Menu_item {

    private final String nameId;
    private int backgroundColorResId;
    private int iconResId;

    public Menu_item(String nameId, int backgroundColorResId, int iconResId) {
        this.backgroundColorResId = backgroundColorResId;
        this.nameId = nameId;
        this.iconResId = iconResId;


    }

    public int getBackgroundColorResId() {
        return backgroundColorResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getName() {
        return nameId;
    }


}
