package cn.lyaotian.simple.music.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import cn.lyaotian.simple.music.R;
import cn.lyaotian.simple.music.ui.view.TitlePageIndicator;
import cn.lyaotian.simple.music.ui.view.ViewPageLayout;
import net.simonvt.menudrawer.MenuDrawer;

/**
 * Created by lyaotian on 5/29/13.
 */
public class HomeActivity extends Activity {
    public static final String TAG = "HomeActivity";
    private static final String STATE_MENUDRAWER = "net.simonvt.menudrawer.samples.WindowSample.menuDrawer";
    private static final String STATE_ACTIVE_VIEW_ID = "net.simonvt.menudrawer.samples.WindowSample.activeViewId";


    //view
    private ViewPageLayout contentLayout;
    private TitlePageIndicator indicatorView;
    private MenuDrawer mMenuDrawer;

    //data
    private int mActiveViewId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mActiveViewId = savedInstanceState.getInt(STATE_ACTIVE_VIEW_ID);
        }

        initMenuDrawer();
        findViews();
        registerListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_MENUDRAWER, mMenuDrawer.saveState());
        outState.putInt(STATE_ACTIVE_VIEW_ID, mActiveViewId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMenuDrawer.restoreState(savedInstanceState.getParcelable(STATE_MENUDRAWER));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mMenuDrawer.toggleMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initMenuDrawer(){
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW);
        mMenuDrawer.setContentView(R.layout.activity_home);
        mMenuDrawer.setMenuView(R.layout.activity_home_menudrawer);
        mMenuDrawer.peekDrawer();
    }

    private void findViews(){
        contentLayout = (ViewPageLayout) findViewById(R.id.contentLayout);
        indicatorView = (TitlePageIndicator) findViewById(R.id.indicatorView);
    }

    private void registerListener(){
        contentLayout.setOnPageChangeListener(indicatorView);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}