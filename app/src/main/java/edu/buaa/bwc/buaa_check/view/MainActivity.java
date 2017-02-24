package edu.buaa.bwc.buaa_check.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import edu.buaa.bwc.buaa_check.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView textView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_name);
        String name = getIntent().getStringExtra("name");
        if (name != null)
            textView.setText(name);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private boolean canAdd = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (canAdd) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
            Intent intent=new Intent(this,CheckCheckAddActivity.class);
            intent.putExtra("title",getTitle());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_check_check) {
            setToolbar(true, "检查管理");
            CheckCheckFragment checkCheckFragment = CheckCheckFragment.newInstance(1);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, checkCheckFragment);
            transaction.commit();
        } else if (id == R.id.nav_spot_check) {
            setToolbar(true, "抽查管理");

        } else if (id == R.id.nav_self_check) {
            setToolbar(true, "自查管理");
            SelfCheckFragment selfCheckFragment = SelfCheckFragment.newInstance(1);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            selfCheckFragment 会替换目前在 R.id.content_main ID 所标识的布局容器中的任何片段（如有）。通过调用 addToBackStack()
//            可将替换事务保存到返回栈，以便用户能够通过按返回按钮撤消事务并回退到上一片段。
            transaction.replace(R.id.content_main, selfCheckFragment);

            //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//设置转化动画风格
            transaction.commit();
        } else if (id == R.id.nav_check_correction) {
            setToolbar(false, "检查整改");

        } else if (id == R.id.nav_spot_correction) {
            setToolbar(false, "抽查整改");

        } else if (id == R.id.nav_self_correction) {
            setToolbar(false, "自查整改");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setToolbar(boolean canAdd, String title) {
        this.canAdd = canAdd;
        invalidateOptionsMenu();
        setTitle(title);
    }
}
