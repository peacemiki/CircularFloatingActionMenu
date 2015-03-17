package com.oguzdev.circularfloatingactionmenu.samples;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class MovableSystemOverlayMenuService extends Service {

    private final IBinder mBinder = new LocalBinder();

    private FloatingActionButton topCenterButton;

    private FloatingActionMenu topCenterMenu;

    private boolean serviceWillBeDismissed;

    public MovableSystemOverlayMenuService() {
    }

    public class LocalBinder extends Binder {
        MovableSystemOverlayMenuService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MovableSystemOverlayMenuService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        serviceWillBeDismissed = false;

        // Set up the large red button on the top center side
        // With custom button and content sizes and margins
        int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
        int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        ImageView fabIconStar = new ImageView(this);
        fabIconStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_important));

        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

        WindowManager.LayoutParams params2 = FloatingActionButton.Builder.getDefaultSystemWindowParams(this);
        params2.width = redActionButtonSize;
        params2.height = redActionButtonSize;

        topCenterButton = new FloatingActionButton.Builder(this)
                .setSystemOverlay(true, true)
                .setContentView(fabIconStar, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .setPosition(FloatingActionButton.POSITION_TOP_CENTER)
                .setLayoutParams(params2)
                .build();

        // Set up customized SubActionButtons for the right center menu
        SubActionButton.Builder tCSubBuilder = new SubActionButton.Builder(this);
        tCSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

        SubActionButton.Builder tCRedBuilder = new SubActionButton.Builder(this);
        tCRedBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_red_selector));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);

        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        tCSubBuilder.setLayoutParams(blueParams);
        tCRedBuilder.setLayoutParams(blueParams);

        ImageView tcIcon1 = new ImageView(this);
        ImageView tcIcon2 = new ImageView(this);
        ImageView tcIcon3 = new ImageView(this);
        ImageView tcIcon4 = new ImageView(this);
        ImageView tcIcon5 = new ImageView(this);
        ImageView tcIcon6 = new ImageView(this);

        tcIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_camera));
        tcIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_picture));
        tcIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_video));
        tcIcon4.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_location_found));
        tcIcon5.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_headphones));
        tcIcon6.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_cancel));

        SubActionButton tcSub1 = tCSubBuilder.setContentView(tcIcon1, blueContentParams).build();
        SubActionButton tcSub2 = tCSubBuilder.setContentView(tcIcon2, blueContentParams).build();
        SubActionButton tcSub3 = tCSubBuilder.setContentView(tcIcon3, blueContentParams).build();
        SubActionButton tcSub4 = tCSubBuilder.setContentView(tcIcon4, blueContentParams).build();
        SubActionButton tcSub5 = tCSubBuilder.setContentView(tcIcon5, blueContentParams).build();
        SubActionButton tcSub6 = tCRedBuilder.setContentView(tcIcon6, blueContentParams).build();


        // Build another menu with custom options
        topCenterMenu = new FloatingActionMenu.Builder(this, true)
                .addSubActionView(tcSub1, tcSub1.getLayoutParams().width, tcSub1.getLayoutParams().height)
                .addSubActionView(tcSub2, tcSub2.getLayoutParams().width, tcSub2.getLayoutParams().height)
                .addSubActionView(tcSub3, tcSub3.getLayoutParams().width, tcSub3.getLayoutParams().height)
                .addSubActionView(tcSub4, tcSub4.getLayoutParams().width, tcSub4.getLayoutParams().height)
                .addSubActionView(tcSub5, tcSub5.getLayoutParams().width, tcSub5.getLayoutParams().height)
                .addSubActionView(tcSub6, tcSub6.getLayoutParams().width, tcSub6.getLayoutParams().height)
                .setRadius(redActionMenuRadius)
                .setStartAngle(270)
                .setEndAngle(90)
                .attachTo(topCenterButton)
                .build();

        topCenterMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {

            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                if(serviceWillBeDismissed) {
                    MovableSystemOverlayMenuService.this.stopSelf();
                    serviceWillBeDismissed = false;
                }
            }
        });

        // make the red button terminate the service
        tcSub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceWillBeDismissed = true; // the order is important
                topCenterMenu.close(true);
            }
        });
    }

    @Override
    public void onDestroy() {
        if(topCenterMenu != null && topCenterMenu.isOpen()) topCenterMenu.close(false);
        if(topCenterButton != null) topCenterButton.detach();

        super.onDestroy();
    }
}
