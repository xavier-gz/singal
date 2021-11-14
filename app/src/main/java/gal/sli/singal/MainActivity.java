package gal.sli.singal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.portada_toolbar);
        setSupportActionBar(myToolbar);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.iraweb:
                //displayPopupWindow();
                Uri uri = Uri.parse( "http://sli.uvigo.gal/singal/" );
                startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
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



            case R.id.info:

                String mensaxe = getString(R.string.contacto).concat(getString(R.string.datos));

                final SpannableString t = new SpannableString(mensaxe);
                Linkify.addLinks(t, Linkify.ALL);

                final AlertDialog e = new AlertDialog.Builder(this)
                        .setPositiveButton(android.R.string.ok, null)
                        .setTitle(getString(R.string.app_name))
                        .setMessage( t )
                        .create();

                e.show();

                ((TextView) Objects.requireNonNull(e.findViewById(android.R.id.message))).setMovementMethod(LinkMovementMethod.getInstance());

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


     public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = findViewById(R.id.editText);
        String message = editText.getText().toString();

      Bundle extras = new Bundle();
        extras.putString("EXTRA_PALABRA",message);
        intent.putExtras(extras);
        startActivity(intent);
    }


}
