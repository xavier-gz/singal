package gal.sli.singal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class DisplayMessageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String message = extras.getString("EXTRA_PALABRA");

        DataBaseHelper myDbHelper = new DataBaseHelper(this);


        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        myDbHelper.openDataBase();

        ArrayList<String> resultado = myDbHelper.getOneItem(message);


        if ((resultado.isEmpty()) ) {

            Toast toast = Toast.makeText(this, getString(R.string.recuncar), Toast.LENGTH_LONG);
            toast.getView();
            toast.show();

            Intent refresh = new Intent(this, MainActivity.class);
            refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(refresh);
            finish();

        } else {



            ActionBar actionBar;
            actionBar = getSupportActionBar();

            ColorDrawable colorDrawable
                    = new ColorDrawable(Color.parseColor("#933DB5"));

            if (actionBar != null) {
                actionBar.setBackgroundDrawable(colorDrawable);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_row_child, resultado)
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent)
                {
                    View row;
                    LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    if (null == convertView) {
                        row = mInflater.inflate(R.layout.list_row_child, parent, false);
                    } else {
                        row = convertView;
                    }

                    TextView tv = (TextView) row.findViewById(R.id.item);
                    tv.setText(Html.fromHtml(getItem(position)));

                    return row;
                }

            };
            ListView listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(adapter);

        }

    }


        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu2, menu);
            return true;
        }


        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle presses on the action bar items
            switch (item.getItemId()) {
                case R.id.share:
                    shareTextUrl();
                    return true;


                case R.id.info:

                    String mensaxe = getString(R.string.contacto).concat(getString(R.string.datos));

                    final SpannableString t = new SpannableString(mensaxe);
                    Linkify.addLinks(t, Linkify.ALL);

                    final AlertDialog e = new AlertDialog.Builder(this)
                            .setPositiveButton(android.R.string.ok, null)
                            .setTitle(getString(R.string.app_name))
                            .setMessage(t)
                            .create();

                    e.show();

                    ((TextView) Objects.requireNonNull(e.findViewById(android.R.id.message))).setMovementMethod(LinkMovementMethod.getInstance());

                    return true;

                case R.id.creditos:

                    String creditos = getString(R.string.edicion);

                    final SpannableString u = new SpannableString(creditos);
                    Linkify.addLinks(u, Linkify.ALL);

                    final AlertDialog f = new AlertDialog.Builder(this)
                            .setPositiveButton(android.R.string.ok, null)
                            .setTitle(getString(R.string.creditos))
                            .setMessage(u)
                            .create();

                    f.show();

                    ((TextView) Objects.requireNonNull(f.findViewById(android.R.id.message))).setMovementMethod(LinkMovementMethod.getInstance());

                    return true;

                case R.id.iraweb:
                    Uri uri = Uri.parse("http://sli.uvigo.gal/singal/");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }


        private void shareTextUrl () {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            }

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            String message = extras.getString("EXTRA_PALABRA");

            String url = "http://sli.uvigo.gal/singal/index.cgi?name=" + message + "&onde=lplus&compara=exacta";
            String cabeceira = "\"" + message + "\" " + "- " + getString(R.string.app_name);
            share.putExtra(Intent.EXTRA_SUBJECT, cabeceira);
            share.putExtra(Intent.EXTRA_TEXT, url);

            startActivity(Intent.createChooser(share, getString(R.string.share)));
        }

}