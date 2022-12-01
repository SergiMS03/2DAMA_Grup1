package com.example.a2dama_grup1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a2dama_grup1.databinding.ActivityBarraNavBinding;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.Locale;

public class BarraNav extends AppCompatActivity {

    objectUser USER;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityBarraNavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBarraNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        USER = (objectUser) getIntent().getSerializableExtra("USER");
        setSupportActionBar(binding.appBarBarraNav.toolbar);
        binding.appBarBarraNav.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BarraNav.this, chatList.class);
                intent.putExtra("USER", (Serializable) USER);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        TextView name = binding.navView.getHeaderView(0).findViewById(R.id.navBarName);
        TextView rol = binding.navView.getHeaderView(0).findViewById(R.id.navBarRol);
        name.setText(USER.nom.toUpperCase(Locale.ROOT)+ " "+ USER.cognoms.toUpperCase(Locale.ROOT));
        rol.setText(USER.rol.toUpperCase(Locale.ROOT));
        binding.navView.getMenu().findItem(R.id.nav_home).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(BarraNav.this, user_profile.class);
                intent.putExtra("USER", (Serializable) USER);
                startActivity(intent);
                return false;
            }
        });
        binding.navView.getMenu().findItem(R.id.nav_gallery).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(BarraNav.this, login.class);
                startActivity(intent);
                return false;
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.barra_nav, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_barra_nav);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}