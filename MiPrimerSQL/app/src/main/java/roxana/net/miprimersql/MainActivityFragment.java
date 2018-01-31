package roxana.net.miprimersql;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Date;

import modelo.Contacto;
import modelo.DAOContactos;

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
        DAOContactos dao= new DAOContactos (getActivity());
        dao.insert(new Contacto(1,"Roxana La Guapa","roxanita_laguapita@gmail.com","@RoxanaLaGuapax2","4451091363","1996-09-01"));
        dao.insert(new Contacto(2,"El Marco Moxito","marcMoxo69@gmail.com","@MaArcC","4451298989","1992-09-06"));
        dao.insert(new Contacto(3,"Adriana Jese","adriis-barajas@gmail.com","@GCAdriana","4171298989","1996-01-01"));
      //  dao.update(new Contacto(2,"El Marco Moxito","roxi-ily@gmail.com","@Roxi123","4451091363","96-09-05"));

        View v=inflater.inflate(R.layout.fragment_main,container,false);
       lsvC= v.findViewById(R.id.lsvcontactos);
        ArrayAdapter<Contacto> adp=new ArrayAdapter<Contacto>(getContext(),
                android.R.layout.simple_list_item_1,dao.getAll());

        lsvC.setAdapter(adp);
    return v;
    }
}
