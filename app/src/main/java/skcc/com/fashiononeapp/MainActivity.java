package skcc.com.fashiononeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import skcc.com.fashiononeapp.fragment.HomeFragment;
import skcc.com.fashiononeapp.fragment.OnlineDirectDeliveryFragment;
import skcc.com.fashiononeapp.fragment.ShopRealStockFragment;
import skcc.com.fashiononeapp.fragment.ShopStockRefundFragment;
import skcc.com.fashiononeapp.fragment.ShopStockSearchFragment;
import skcc.com.fashiononeapp.util.UserInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    HomeFragment                 homeFragment;
    ShopStockSearchFragment      shopStockSearchFragment      = new ShopStockSearchFragment();
    OnlineDirectDeliveryFragment onlineDirectDeliveryFragment = new OnlineDirectDeliveryFragment();
    ShopStockRefundFragment      shopStockRefundFragment      = new ShopStockRefundFragment();
    ShopRealStockFragment        shopRealStockFragment        = new ShopRealStockFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        homeFragment = (HomeFragment)getSupportFragmentManager().getFragments().get(0);


        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, homeFragment).commit();

            }
        });
    }

    @Override
    protected void onStart() {
        if (!UserInfo.getUser().isLogin()) {

            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
        }
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bluetooth_settings) {
            Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fr = null;
        if (id == R.id.nav_shop_stock_search) {
            fr = shopStockSearchFragment;

        } else if (id == R.id.nav_online_dd) {
            fr = onlineDirectDeliveryFragment;

        } else if (id == R.id.nav_shop_refund) {
            fr = shopStockRefundFragment;

        } else if (id == R.id.nav_shop_real_stock) {
            fr = shopRealStockFragment;

        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, fr).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private long backKeyPressedTime = 0;

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                Toast toast = Toast.makeText(getApplicationContext(), "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }

    }
}
