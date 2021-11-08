package com.farmers.ownfarmer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.farmers.ownfarmer.Languages.data.PreferencesHelper;
import com.farmers.ownfarmer.Languages.ui.BaseActivity;
import com.farmers.ownfarmer.Languages.util.LocaleHelper;
import com.farmers.ownfarmer.ui.Messaging.MessageActivity;
import com.farmers.ownfarmer.ui.MyProducts.MyProductsFragment;
import com.farmers.ownfarmer.ui.MyServices.MyServicesFragment;
import com.farmers.ownfarmer.ui.addproduct.AddProductActivity;
import com.farmers.ownfarmer.ui.addservice.AddServiceActivity;
import com.farmers.ownfarmer.ui.allfarmers.AllFarmersFragment;
import com.farmers.ownfarmer.ui.home.HomeFragment;
import com.farmers.ownfarmer.ui.product.ProductFragment;
import com.farmers.ownfarmer.ui.profile.ProfileFragment;
import com.farmers.ownfarmer.ui.services.ServicesFragment;
import com.farmers.ownfarmer.ui.settings.SettingsFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    private static MainActivity instance;

    String defLanguage, language, user_type;
    SharedPreferences user_language_preference, userData_preference;
    SharedPreferences.Editor user_language_editor;

    /////firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    String userEmail, userID;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.nav_bottom_home:
                    fragment = new HomeFragment();

                    break;
                case R.id.nav_bottom_chat:

                    userEmail = userData_preference.getString("user_email", "");
                    userID = userData_preference.getString("user_id", "");

                    SignInWIthFirebase(userEmail, userEmail);

                    break;
                case R.id.nav_bottom_profile:
                    fragment = new ProfileFragment();

                    break;

                case R.id.nav_bottom_setting:
                    fragment = new SettingsFragment();

                    break;

            }
            return loadFragment(fragment);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getting instance of main activity
        instance = this;

        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
            defLanguage = locale.getLanguage();
        } else {
            //noinspection deprecation
            locale = getResources().getConfiguration().locale;
            defLanguage = locale.getLanguage();
        }


        //// shared preference
        user_language_preference = getSharedPreferences("select_language", MODE_PRIVATE);
        language = user_language_preference.getString("my_language", null);

        /////// shared preference
        user_language_preference = getSharedPreferences("select_language", MODE_PRIVATE);
        user_language_editor = user_language_preference.edit();

        //// check user type
        userData_preference = getSharedPreferences("user_data", MODE_PRIVATE);
        user_type = userData_preference.getString("user_type", null);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        SharedPreferences patientDataSP = getSharedPreferences("user_data", MODE_PRIVATE);
        String st_name = patientDataSP.getString("user_name", "No data");
        String st_email = patientDataSP.getString("user_email", "No data");


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView tv_name = (TextView) headerView.findViewById(R.id.tv_customer_name);
        TextView tv_email = (TextView) headerView.findViewById(R.id.customer_mail);
        tv_name.setText(st_name);
        tv_email.setText(st_email);

        Menu menu = navigationView.getMenu();
        MenuItem myProducts_MI = menu.findItem(R.id.nav_my_products);
        MenuItem myServices_TV_MI = menu.findItem(R.id.nav_my_services);
        MenuItem addProducts_TV_MI = menu.findItem(R.id.nav_add_products);
        MenuItem addServices_TV_MI = menu.findItem(R.id.nav_add_services);
        MenuItem services_TV_MI = menu.findItem(R.id.nav_services);
        MenuItem products_TV_MI = menu.findItem(R.id.nav_products);
        MenuItem allFarmers_TV_MI = menu.findItem(R.id.nav_all_farmers);

        if (user_type != null && !user_type.isEmpty()) {

            if (user_type.equals("1")) {
                myProducts_MI.setVisible(true);
                myServices_TV_MI.setVisible(true);
                addProducts_TV_MI.setVisible(true);
                addServices_TV_MI.setVisible(true);
                products_TV_MI.setVisible(false);
                services_TV_MI.setVisible(false);
                allFarmers_TV_MI.setVisible(false);
            } else {
                myProducts_MI.setVisible(false);
                myServices_TV_MI.setVisible(false);
                addProducts_TV_MI.setVisible(false);
                addServices_TV_MI.setVisible(false);
                products_TV_MI.setVisible(true);
                services_TV_MI.setVisible(true);
                allFarmers_TV_MI.setVisible(true);
            }

        } else {
            myProducts_MI.setVisible(false);
            myServices_TV_MI.setVisible(false);
            addProducts_TV_MI.setVisible(false);
            addServices_TV_MI.setVisible(false);
            products_TV_MI.setVisible(true);
            services_TV_MI.setVisible(true);
            allFarmers_TV_MI.setVisible(true);
        }


        //////////////// Creating Firebase Instant ///////////////
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
//                mSharedPreferences.edit().putString(PREF_DEVICE_TOKEN, "" + instanceIdResult.getToken()).commit();
                Log.e("TGED", "HERE IS TOKEN " + instanceIdResult.getToken());
            }
        });


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //Used for the navigation drawer toggle lines in toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //////////////l
        loadFragment(new HomeFragment());

        ///////////////////Bottom Navigation Listener///////////
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.nav_switch_language) {

            if (defLanguage.contains("ur")) {

                ////for urdu choose ////////

                language = "en";

                user_language_editor.putString("my_language", "eng");
                user_language_editor.apply();

                select_language(language);

            } else {

                ////for urdu choose ////////

                language = "ur";

                user_language_editor.putString("my_language", "ur");
                user_language_editor.apply();

                select_language(language);
            }

        } else if (id == R.id.nav_all_farmers) {

            loadFragment(new AllFarmersFragment());

        } else if (id == R.id.nav_add_products) {

            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_add_services) {

            Intent intent = new Intent(MainActivity.this, AddServiceActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_home) {

            loadFragment(new HomeFragment());

        } else if (id == R.id.nav_my_products) {

            loadFragment(new MyProductsFragment());

        } else if (id == R.id.nav_my_services) {

            loadFragment(new MyServicesFragment());

        } else if (id == R.id.nav_products) {

            loadFragment(new ProductFragment());

        } else if (id == R.id.nav_services) {
            loadFragment(new ServicesFragment());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }


    public static MainActivity getInstance() {
        return instance;
    }


    //////// change language
    public void select_language(String language) {
        PreferencesHelper.setLanguage(MainActivity.this, language);
        LocaleHelper.setLocale(MainActivity.this, language);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }


    //////////////// checking email in firebase
    private void SignInWIthFirebase(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);

                startActivity(intent);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e.getMessage() != null && e.getMessage().equals("The email address is already in use by another account.")) {

                    Intent intent = new Intent(getApplicationContext(), MessageActivity.class);

                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}