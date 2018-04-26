package android.wasla.support.view;

import android.content.Context;
import android.content.Intent;
import android.wasla.support.adapter.InstructorAdapter;
import android.wasla.support.model.Instructor;
import android.wasla.support.model.OnlineDataBase;
import android.wasla.support.util.MyConstants;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.webkit.ValueCallback;
import android.support.v7.widget.SearchView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import android.wasla.support.util.MyRecyclerScroll;
import com.google.firebase.database.FirebaseDatabase;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;

import android.support.design.widget.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ContactsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InstructorAdapter instructorAdapter;
    private OnlineDataBase onlineDataBase;
    private Toolbar toolbar;
    private ArrayList<Instructor> instructorsList;
    private final int AddFeedbackDialogRequestCoder=1;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private LinearLayout layoutFabShare;
    private LinearLayout layoutFabFeedback;
    private boolean fabExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        new Instabug.Builder(getApplication(), MyConstants.INSTABUG_KEY)
                .setInvocationEvent(InstabugInvocationEvent.SHAKE)
                .build();

        FirebaseDatabase.getInstance().getReference().keepSynced(true);
        setContentView(android.wasla.support.R.layout.activity_contacts);
        toolbar = (Toolbar) findViewById(android.wasla.support.R.id.toolbar);
        progressBar = findViewById(android.wasla.support.R.id.progressBar);

        floatingActionButton = (FloatingActionButton) this.findViewById(android.wasla.support.R.id.fab_setting);
        layoutFabFeedback = (LinearLayout) this.findViewById(android.wasla.support.R.id.layout_fab_feedback);
        layoutFabShare = (LinearLayout) this.findViewById(android.wasla.support.R.id.layout_fab_share);

        final Animation animation = AnimationUtils.loadAnimation(this, android.wasla.support.R.anim.simple_grow);

        setSupportActionBar(toolbar);

        if(!isNetworkConnected()) {
            Toasty.warning(this, "check the internet connection!", Toast.LENGTH_LONG, true).show();
        }

        onlineDataBase = new OnlineDataBase(this);
        instructorsList=new ArrayList<>();
        recyclerView = findViewById(android.wasla.support.R.id.rv_instructor);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        recyclerView.setOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {
                floatingActionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(3)).start();
            }

            @Override
            public void hide() {
                closeSubMenusFab();
                floatingActionButton.animate().translationY(floatingActionButton.getHeight() + 60).setInterpolator(new AccelerateInterpolator(3)).start();
            }
        });
        progressBar.setVisibility(View.VISIBLE);

        onlineDataBase.updateAvailableContacts(new ValueCallback<List<Instructor>>() {
            @Override
            public void onReceiveValue(List<Instructor> instructors) {
                instructorsList= (ArrayList<Instructor>) instructors;
                instructorAdapter = new InstructorAdapter(ContactsActivity.this, instructorsList);
                recyclerView.setAdapter(instructorAdapter);
                progressBar.setVisibility(View.INVISIBLE);
                floatingActionButton.startAnimation(animation);
                floatingActionButton.setVisibility(View.VISIBLE);

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true) {
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });

        layoutFabFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSubMenusFab();
                startActivityForResult(new Intent(ContactsActivity.this, AddFeedbackDialog.class), AddFeedbackDialogRequestCoder);
            }
        });

        layoutFabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSubMenusFab();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //TODO: share app from here
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        closeSubMenusFab();
    }

    private void closeSubMenusFab() {
        layoutFabShare.setVisibility(View.INVISIBLE);
        layoutFabFeedback.setVisibility(View.INVISIBLE);
        floatingActionButton.setImageResource(android.wasla.support.R.drawable.ic_add_black_24dp);
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab() {
        layoutFabShare.setVisibility(View.VISIBLE);
        layoutFabFeedback.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        floatingActionButton.setImageResource(android.wasla.support.R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==AddFeedbackDialogRequestCoder)
        {
            if (resultCode == RESULT_OK) {
                String feedback = data.getStringExtra(getString(android.wasla.support.R.string.feedback));
                onlineDataBase.sendFeedback(feedback, new ValueCallback<Boolean>() {
                    @Override
                    public void onReceiveValue(Boolean aBoolean) {
                        if(aBoolean)
                            Toasty.success(getBaseContext(), "Successfully send the feedback!"
                                    , Toast.LENGTH_SHORT, true).show();
                        else
                            Toasty.error(getBaseContext(), "There is an error,try again later!", Toast.LENGTH_SHORT, true).show();

                    }
                });

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(android.wasla.support.R.menu.menu_main, menu);
        MenuItem search = menu.findItem(android.wasla.support.R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
       // searchView.setBackgroundColor(getResources().getColor(R.color.White));
        search(searchView);
        return true;
    }

    private void search(final SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(instructorsList.size()>0)
                    instructorAdapter.getFilter().filter(s);
                return true;
            }
        });
    }
}
