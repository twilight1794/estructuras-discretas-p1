package xyz.campanita.estructurasdiscretasp1;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import xyz.campanita.estructurasdiscretasp1.databinding.ActivityListaTemasBinding;

public class ListaTemasActivity extends AppCompatActivity {

    ExpandableListView listaExpandible;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableTitleList;
    HashMap<String, List<String>> expandableDetailList;

    protected HashMap<String, List<String>> obtenerTemas(){
        HashMap<String, List<String>> expandableDetailList = new HashMap<String, List<String>>();

        // As we are populating List of fruits, vegetables and nuts, using them here
        // We can modify them as per our choice.
        // And also choice of fruits/vegetables/nuts can be changed
        List<String> fruits = new ArrayList<String>();
        fruits.add("Apple");
        fruits.add("Orange");
        fruits.add("Guava");
        fruits.add("Papaya");
        fruits.add("Pineapple");

        List<String> vegetables = new ArrayList<String>();
        vegetables.add("Tomato");
        vegetables.add("Potato");
        vegetables.add("Carrot");
        vegetables.add("Cabbage");
        vegetables.add("Cauliflower");

        List<String> nuts = new ArrayList<String>();
        nuts.add("Cashews");
        nuts.add("Badam");
        nuts.add("Pista");
        nuts.add("Raisin");
        nuts.add("Walnut");

        // Fruits are grouped under Fruits Items. Similarly the rest two are under
        // Vegetable Items and Nuts Items respecitively.
        // i.e. expandableDetailList object is used to map the group header strings to
        // their respective children using an ArrayList of Strings.
        expandableDetailList.put("Fruits Items", fruits);
        expandableDetailList.put("Vegetable Items", vegetables);
        expandableDetailList.put("Nuts Items", nuts);
        return expandableDetailList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xyz.campanita.estructurasdiscretasp1.databinding.ActivityListaTemasBinding binding = ActivityListaTemasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutCompat cont = findViewById(R.id.lista_temas_cont);
        Resources res = getResources();
        String ptt = res.getString(R.string.pref_tema_t);
        String pte = res.getString(R.string.pref_tema_e);
        int ult = 0;
        int id = R.id.lista_temas_cont;
        String[] encArray = res.getStringArray(R.array.temas);
        for (int i = 0; i<encArray.length; i++){
            // Encabezado
            ult = id;
            id = View.generateViewId();
            TextView enc = new TextView(this, null, 0, R.style.ListaTemasEncabezado);
            enc.setText(String.format(ptt, i+1) + " " + encArray[i]);
            enc.setId(id);
            cont.addView(enc);
            /*ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(cont);
            constraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
            constraintSet.connect(id, ConstraintSet.TOP, i==0?ConstraintSet.PARENT_ID:ult, i==0?ConstraintSet.TOP:ConstraintSet.BOTTOM, 0);
            constraintSet.applyTo(cont);*/

            // Lista expandible
            ult = id;
            id = View.generateViewId();
            ExpandableListView lexp = new ExpandableListView(this, null, 0, R.style.ListaTemasExpandible);
            lexp.setId(id);
            List<String> expTitulos = new ArrayList<String>(Arrays.asList(res.getStringArray(res.getIdentifier("temas_"+(i+1), "array", getPackageName()))));
            HashMap<String, List<String>> expDetalles = new HashMap<String, List<String>>();
            for (int ii = 0; ii<expTitulos.size(); ii++){
                Log.i("UUU", "temas_"+(i+1)+"_"+(ii+1));
                List<String> expConts = new ArrayList<String>(Arrays.asList(res.getStringArray(res.getIdentifier("temas_"+(i+1)+"_"+(ii+1), "array", getPackageName()))));
                expDetalles.put(expTitulos.get(ii), expConts);
            }
            ExpandableListAdapter lexpa = new CustomizedExpandableListAdapter(this, expTitulos, expDetalles);
            lexp.setAdapter(lexpa);
            cont.addView(lexp);
            /*constraintSet = new ConstraintSet();
            constraintSet.clone(cont);
            constraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
            constraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
            constraintSet.connect(id, ConstraintSet.TOP, ult, ConstraintSet.BOTTOM, 0);
            constraintSet.applyTo(cont);*/
        }
    }
}