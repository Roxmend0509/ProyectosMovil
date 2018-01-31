package roxana.net.aplicacion_notasmultimedia;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import roxana.net.aplicacion_notasmultimedia.modelo.DAONotas;
import roxana.net.aplicacion_notasmultimedia.modelo.Notas;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
ListView lsvC;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DAONotas dao=new DAONotas(getActivity());
        dao.insert(new Notas(1,"Material Movil:","-Celular \n-Android Studio \n-Cable USB"));

        View v=inflater.inflate(R.layout.fragment_main,container,false);
        lsvC=v.findViewById(R.id.lsvcontactos);
        ArrayAdapter<Notas> adp= new ArrayAdapter<Notas>(getContext(),
                android.R.layout.simple_list_item_1,dao.getAll());

        lsvC.setAdapter(adp);
        return v;




    }
}
