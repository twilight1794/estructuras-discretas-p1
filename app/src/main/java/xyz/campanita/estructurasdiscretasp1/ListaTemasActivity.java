package xyz.campanita.estructurasdiscretasp1;

/*import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ListaTemasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_temas);
    }
}*/

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.campanita.estructurasdiscretasp1.databinding.ActivityAcercaDeBinding;
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

        listaExpandible = (ExpandableListView) findViewById(R.id.listaExpandible);
        expandableDetailList = obtenerTemas();
        expandableTitleList = new ArrayList<String>(expandableDetailList.keySet());
        expandableListAdapter = new CustomizedExpandableListAdapter(this, expandableTitleList, expandableDetailList);
        listaExpandible.setAdapter(expandableListAdapter);

        // This method is called when the group is expanded
        listaExpandible.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
            }
        });

        // This method is called when the group is collapsed
        listaExpandible.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();
            }
        });

        // This method is called when the child in any group is clicked
        // via a toast method, it is shown to display the selected child item as a sample
        // we may need to add further steps according to the requirements
        listaExpandible.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition)
                        + " -> "
                        + expandableDetailList.get(
                        expandableTitleList.get(groupPosition)).get(
                        childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }
}