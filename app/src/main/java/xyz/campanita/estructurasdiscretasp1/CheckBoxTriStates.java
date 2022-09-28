package xyz.campanita.estructurasdiscretasp1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatCheckBox;

import java.util.HashSet;

public class CheckBoxTriStates extends AppCompatCheckBox {
    static private final int UNKNOWN = -1;
    static private final int UNCHECKED = 0;
    static private final int CHECKED = 1;
    private int state;
    private HashSet<Integer> childItems;
    private int parentItem;
    private View v;

    public CheckBoxTriStates(View v, Context context){
        super(context);
        this.v = v;
        init();
    }
    public CheckBoxTriStates(View v, Context context, AttributeSet attrs){
        super(context, attrs);
        this.v = v;
        init();
    }
    public CheckBoxTriStates(View v, Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.v = v;
        init();
    }

    private void init() {
        state = UNCHECKED;
        childItems = new HashSet<Integer>();
        parentItem = 0;
        updateBtn();

        setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                Log.i("UUU", new Integer(state).toString());
                switch (state){
                    case UNKNOWN:
                    case UNCHECKED:
                        state = CHECKED;
                        break;
                    case CHECKED:
                        state = UNCHECKED;
                        break;
                }
                updateBtn();
            }
        });
    }

    private void updateBtn(){
        int btnDrawable = 0;
        switch (state){
            case UNKNOWN:
                btnDrawable = R.drawable.ic_baseline_indeterminate_check_box_24;
                if (parentItem != 0){
                    ((CheckBoxTriStates) v.findViewById(parentItem))._checkForChildChanges(UNKNOWN);
                }
                break;
            case UNCHECKED:
                btnDrawable = R.drawable.ic_baseline_check_box_outline_blank_24;
                for (int i: childItems){
                    ((CheckBoxTriStates) v.findViewById(i)).setState(0);
                }
                if (parentItem != 0){
                    ((CheckBoxTriStates) v.findViewById(parentItem))._checkForChildChanges(UNCHECKED);
                }
                break;
            case CHECKED:
                btnDrawable = R.drawable.ic_baseline_check_box_24;
                for (int i: childItems){
                    ((CheckBoxTriStates) v.findViewById(i)).setState(1);
                }
                if (parentItem != 0){
                    ((CheckBoxTriStates) v.findViewById(parentItem))._checkForChildChanges(CHECKED);
                }
                break;
        }
        setButtonDrawable(btnDrawable);
    }
    public int getState(){
        return state;
    }
    public void setState(int state){
        this.state = state;
        updateBtn();
    }
    public int getParentItem(){
        return parentItem;
    }
    public void setParentItem(int id){
        removeParentItem();
        parentItem = id;
        ((CheckBoxTriStates) v.findViewById(parentItem))._addChildItem(this.getId());
    }
    public void removeParentItem(){
        if (parentItem != 0) {
            ((CheckBoxTriStates) v.findViewById(parentItem))._removeChildItem(this.getId());
            parentItem = 0;
        }
    }
    public void _addChildItem(int id){
        childItems.add(id);
    }
    public void _removeChildItem(int id){
        childItems.remove(id);
    }
    // Esto no llama a los metodos estandar, para evitar recursiones infinitas
    public void _checkForChildChanges(int childState){
        if (childState == CHECKED){
            for (int i: childItems){
                int e = ((CheckBoxTriStates) v.findViewById(i)).getState();
                if (e != childState){
                    state = UNKNOWN;
                    setButtonDrawable(R.drawable.ic_baseline_indeterminate_check_box_24);
                    return;
                }
            }
            state = CHECKED;
            setButtonDrawable(R.drawable.ic_baseline_check_box_24);
        } else if (childState == UNCHECKED || childState == UNKNOWN) {
            for (int i: childItems){
                int e = ((CheckBoxTriStates) v.findViewById(i)).getState();
                if (e != childState) {
                    state = UNKNOWN;
                    setButtonDrawable(R.drawable.ic_baseline_indeterminate_check_box_24);
                    return;
                }
            }
            state = UNCHECKED;
            setButtonDrawable(R.drawable.ic_baseline_check_box_outline_blank_24);
        } else if (childState == UNKNOWN){
            state = UNKNOWN;
            setButtonDrawable(R.drawable.ic_baseline_indeterminate_check_box_24);
        }
    }
}