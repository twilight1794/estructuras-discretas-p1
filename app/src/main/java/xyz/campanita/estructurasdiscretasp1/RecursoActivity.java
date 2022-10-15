package xyz.campanita.estructurasdiscretasp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Biblioteca;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Colaborador;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Comun;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Existencia;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Nodo;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.Recurso;
import xyz.campanita.estructurasdiscretasp1.bibliotecas.TipoRecurso;
import xyz.campanita.estructurasdiscretasp1.databinding.ActivityRecursoBinding;

public class RecursoActivity extends AppCompatActivity {
    protected Recurso r = null;
    protected boolean error = true;
    protected OkHttpClient okHttp = new OkHttpClient();

    protected DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    protected DateFormat edf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    ExecutorService mExecutor;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);

        xyz.campanita.estructurasdiscretasp1.databinding.ActivityRecursoBinding binding = ActivityRecursoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            Comun.inicializar(this);
            Intent i = getIntent();
            if (Objects.equals(i.getAction(), "android.intent.action.VIEW")) {
                // De un Intent View
                URL u = new URL(i.getDataString());
                String[] comp = u.getPath().split("/");
                if (comp.length > 4) { throw new URISyntaxException(u.toString(), "Formato incorrecto de identificador"); }
                if (!Objects.equals(comp[1], "estructurasdiscretasp1")) { throw new AppIncorrectaException(); }
                if (Objects.equals(comp[2], "isbn")){
                    boolean encontrado = false;
                    for (Recurso rec: Comun.recursos){
                        if (rec.getIsbn().contains(comp[3])) {
                            r = rec;
                            encontrado = true;
                            break;
                        }
                    }
                    if (!encontrado) { throw new IdInexistenteException(); }
                } else { throw new TipoIdInvalidoException(); }
                error = false;
            } else if (i.getIntExtra("pos", -1) != -1) {
                // De un RecyclerView
                int pos = i.getIntExtra("pos", -1);
                if (pos != -1) { r = Comun.recursos.get(pos); }
                else { throw new IndexOutOfBoundsException(); }
                error = false;
            } else { throw new UnsupportedOperationException(); }
        } catch (IOException e) {
            Comun.crearDialogoFatal(this, R.string.error_descarga).show(); finish();
        } catch (URISyntaxException e) {
            Comun.crearDialogoFatal(this, R.string.error_vista_uri).show();
        } catch (AppIncorrectaException e) {
            Comun.crearDialogoFatal(this, R.string.error_vista_app).show();
        } catch (IdInexistenteException e) {
            Comun.crearDialogoFatal(this, R.string.error_vista_encontrado).show();
        } catch (TipoIdInvalidoException e) {
            Comun.crearDialogoFatal(this, R.string.error_vista_tipo).show();
        } catch (IndexOutOfBoundsException e) {
            Comun.crearDialogoFatal(this, R.string.error_vista_indice).show();
        } catch (UnsupportedOperationException e) {
            Comun.crearDialogoFatal(this, R.string.error_vista_accion).show();
        }

        Log.i("UUU", "Llega?");
        if (!error) {
            // Tipo
            TipoRecurso valTipo = r.getTipo();
            if (valTipo == TipoRecurso.LIBRO) {
                getSupportActionBar().setSubtitle("Libro");
                findViewById(R.id.fab_abrir).setVisibility(View.GONE);
            } else {
                getSupportActionBar().setSubtitle("Libro electrónico");
            }
            // ISBN (o cualquier otro id externo)
            String valISBN = String.join(", ", r.getIsbn());
            ((TextView) findViewById(R.id.recurso_codigo)).setText(valISBN);
            // FIX: Poner tipo de id
            // Titulo
            ((TextView) findViewById(R.id.recurso_titulo)).setText(r.getTitulo());
            // Subtitulo
            String valSubtitulo = r.getSubtitulo();
            if (valSubtitulo != null) {
                ((TextView) findViewById(R.id.recurso_subtitulo)).setText(valSubtitulo);
            } else {
                findViewById(R.id.recurso_subtitulo).setVisibility(View.GONE);
            }
            // Descripcion
            String valDescripcion = r.getDescripcion();
            if (valDescripcion != null) {
                ((TextView) findViewById(R.id.recurso_descripcion)).setText(valDescripcion);
            } else {
                findViewById(R.id.recurso_descripcion).setVisibility(View.GONE);
            }
            // DatosPublicacion
            String valDatosPub = r.getDatosPublicacion();
            if (valDatosPub != null) {
                ((TextView) findViewById(R.id.recurso_datospub)).setText(valDatosPub);
            } else {
                findViewById(R.id.recurso_datospub).setVisibility(View.GONE);
            }
            // Tema
            ChipGroup gTemas = findViewById(R.id.recurso_temas_cont);
            for (String t: r.getTemas()){
                Chip c = new Chip(this);
                c.setText(t);
                c.setOnClickListener(v -> {
                    Intent i = new Intent(Intent.ACTION_WEB_SEARCH);
                    i.putExtra(SearchManager.QUERY, ((Chip) v).getText());
                    try {
                        startActivity(i);
                    } catch (ActivityNotFoundException e){
                        try {
                            String consulta = URLEncoder.encode(String.valueOf(((Chip) v).getText()), "UTF-8");
                            Uri uri = Uri.parse("https://www.google.com/?q=" + consulta);
                            i = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(i);
                        } catch (Exception ignored){}
                    }
                });
                gTemas.addView(c);
            }
            // TemaAsignatura
            Resources res = getResources();
            ArrayList<Nodo> l = r.getTemasAsignatura();
            LinearLayout cont = findViewById(R.id.recurso_temasasig_cont);
            String[] t1 = res.getStringArray(R.array.temas);
            for (int i = 0; i<l.size(); i++){
                Nodo n1 = l.get(i);
                if (n1.getExplicado()){
                    ConstraintLayout temaCont = (ConstraintLayout) getLayoutInflater().inflate(R.layout.recurso_temasasig, null);
                    if (n1.getPagina() != null){
                        ((TextView) temaCont.findViewById(R.id.recurso_temasasig_dato)).setText(n1.getPagina());
                    } else {
                        temaCont.findViewById(R.id.recurso_temasasig_dato).setVisibility(View.GONE);
                        if (n1.getSubtemas() == null){
                            temaCont.findViewById(R.id.recurso_temasasig_si).setVisibility(View.VISIBLE);
                        }
                    }
                    ((TextView) temaCont.findViewById(R.id.recurso_temasasig_num)).setText(i + 1 + ". ");
                    ((TextView) temaCont.findViewById(R.id.recurso_temasasig_titulo)).setText(t1[i]);
                    cont.addView(temaCont);
                    String[] t2 = res.getStringArray(res.getIdentifier("temas_"+(i+1), "array", getPackageName()));
                    ArrayList<Nodo> l2 = n1.getSubtemas();
                    if (l2 != null){
                        for (int j = 0; j<l2.size(); j++){
                            Nodo n2 = l2.get(j);
                            if (n2.getExplicado()){
                                ConstraintLayout temaCont2 = (ConstraintLayout) getLayoutInflater().inflate(R.layout.recurso_temasasig, null);
                                if (n2.getPagina() != null){
                                    ((TextView) temaCont2.findViewById(R.id.recurso_temasasig_dato)).setText(n2.getPagina());
                                } else {
                                    temaCont2.findViewById(R.id.recurso_temasasig_dato).setVisibility(View.GONE);
                                    temaCont2.findViewById(R.id.recurso_temasasig_si).setVisibility(View.VISIBLE);
                                }
                                ((TextView) temaCont2.findViewById(R.id.recurso_temasasig_num)).setText(String.format("%d.%d. ", i+1, j+1));
                                ((TextView) temaCont2.findViewById(R.id.recurso_temasasig_titulo)).setText(t2[i]);
                                cont.addView(temaCont2);
                            }
                        }
                    }
                }
            }
            // UrlPortada
            ImageView p = findViewById(R.id.recurso_portada);
            try {
                Bitmap valPortada = obtenerPortada(r.getUrlPortada());
                p.setImageBitmap(valPortada);
            } catch (IllegalStateException e){
                p.setImageResource(R.drawable.portada_plantilla);
            } catch (Exception e){
                Snackbar.make(this.getCurrentFocus(), R.string.error_descarga_s, Snackbar.LENGTH_LONG).show();
                p.setImageResource(R.drawable.portada_plantilla);
            }
            // Colaborador
            LinearLayout gColaboradores = findViewById(R.id.recurso_colab_cont);
            for (Colaborador c: r.getColaborador()){
                RelativeLayout cc = new RelativeLayout(gColaboradores.getContext());

                int idnombre = View.generateViewId();
                TextView nombre = new TextView(gColaboradores.getContext());
                nombre.setText(c.getNombre());
                nombre.setId(idnombre);
                cc.addView(nombre);

                TextView titulo = new TextView(gColaboradores.getContext());
                titulo.setTextAppearance(android.R.style.TextAppearance_Material_Body2);
                switch (c.getTipo()) {
                    case AUTOR:
                        titulo.setText(R.string.buscaren_c_autor); break;
                    case EDITOR:
                        titulo.setText(R.string.buscaren_c_editor); break;
                    case TRADUCTOR:
                        titulo.setText(R.string.buscaren_c_traductor); break;
                    case COLABORADOR:
                        titulo.setText(R.string.buscaren_c_colab);
                }
                cc.addView(titulo);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                cc.setLayoutParams(param);

                RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                nombre.setLayoutParams(param2);

                param2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                param2.setMarginStart(8);
                param2.addRule(RelativeLayout.END_OF, idnombre);
                titulo.setLayoutParams(param2);

                gColaboradores.addView(cc);
            }
            // Calificacion y votos
            int valCalif = r.getCalificacion();
            int valVotos = r.getVotos();
            ((RatingBar) findViewById(R.id.recurso_cal)).setRating(valCalif);
            ((TextView) findViewById(R.id.recurso_cal_votos)).setText(String.format("%.1f, (%s)", valCalif/10.0, valVotos));
            // Existencias
            mExecutor = Executors.newSingleThreadExecutor();
            mHandler = new Handler(Looper.getMainLooper());
            actualizarExistencias(this, true, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!error){
            getMenuInflater().inflate(R.menu.recurso, menu);
            super.onCreateOptionsMenu(menu);
            // Estado inicial del favorito
            MenuItem item = menu.getItem(0);
            int i = Comun.favoritos.indexOf(r.getIsbn().get(0));
            if (i != -1) {
                item.setChecked(true);
                item.setIcon(R.drawable.ic_baseline_favorite_24);
                item.setTitle(R.string.favoritos_q);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.acc_favorito:
                if (item.isChecked()){
                    Comun.favoritos.remove(r.getIsbn().get(0));
                    item.setChecked(false);
                    item.setIcon(R.drawable.ic_baseline_favorite_border_24);
                    item.setTitle(R.string.favoritos_a);
                } else {
                    Comun.favoritos.add(r.getIsbn().get(0));
                    item.setChecked(true);
                    item.setIcon(R.drawable.ic_baseline_favorite_24);
                    item.setTitle(R.string.favoritos_q);
                }
                try {
                    Comun.escribirFavoritos(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.acc_existencias:
                mExecutor = Executors.newSingleThreadExecutor();
                mHandler = new Handler(Looper.getMainLooper());
                actualizarExistencias(this, true, true);
                return true;
            case R.id.acc_qr:
                QRCodeWriter writer = new QRCodeWriter();
                // TODO: implementar varios tipos de ids
                String url = "http://app.campanita.xyz/estructurasdiscretasp1/isbn/" + r.getIsbn().get(0);
                try {
                    BitMatrix bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    QRDialogFragment dialogFragment = new QRDialogFragment(bmp, url, r.getTitulo());
                    dialogFragment.show(getSupportFragmentManager(),"qr");
                } catch (Exception e) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), R.string.error_generico, Snackbar.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Bitmap obtenerPortada(String url) throws IOException {
        URL myFileUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
        conn.connect();
        Bitmap b = BitmapFactory.decodeStream(conn.getInputStream());
        if (b.getWidth()>10) {
            return b;
        } else {
            throw new IllegalStateException(); // FIX: Crear nueva excepcion
        }
    }

    public interface OnProcessedListener {
        void onProcessed(int estado);
    }

    private void actualizarExistencias(final Context context, final boolean finished, final boolean origen){
        final OnProcessedListener listener = estado -> mHandler.post(() -> {
            TextView enc = findViewById(R.id.recurso_exist_enc);
            TextView cargando = findViewById(R.id.recurso_exist_cargando);
            LinearLayout cont = findViewById(R.id.recurso_exist_cont);
            if (estado == 1) {
                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.error_descarga_s, Snackbar.LENGTH_LONG).show();
                enc.setVisibility(View.GONE);
                cargando.setVisibility(View.GONE);
                cont.setVisibility(View.GONE);
            } else if (estado == 2){
                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.error_generico, Snackbar.LENGTH_LONG).show();
                enc.setVisibility(View.GONE);
                cargando.setVisibility(View.GONE);
                cont.setVisibility(View.GONE);
            } else {
                cargando.setVisibility(View.VISIBLE);
                cont.setVisibility(View.VISIBLE);
                cont.removeAllViews();
                for (Biblioteca b: Biblioteca.values()){
                    Existencia ex = r.getExistencia(b);
                    if (ex != null){
                        ConstraintLayout exCont = (ConstraintLayout) getLayoutInflater().inflate(R.layout.recurso_exist, null);
                        int nombreb = -1;
                        switch (b) {
                            case CENTRAL:
                                nombreb = R.string.bib_central;
                                exCont.findViewById(R.id.recurso_exist_clas).setVisibility(View.GONE);
                                break;
                            case DOVALI:
                                nombreb = R.string.bib_dovali; break;
                            case RIVERO:
                                nombreb = R.string.bib_rivero; break;
                            case ENZO:
                                nombreb = R.string.bib_enzo; break;
                            case PREPA1:
                                nombreb = R.string.bib_prepa_1; break;
                            case PREPA5:
                                nombreb = R.string.bib_prepa_5; break;
                            case CCHOR:
                                nombreb = R.string.bib_cch_or; break;
                            case VASCONCELOS:
                                nombreb = R.string.bib_vasconcelos; break;
                            case BNM:
                                nombreb = R.string.bib_bnm;
                        }
                        ((TextView) exCont.findViewById(R.id.recurso_exist_bib)).setText(nombreb);
                        TextView idText = exCont.findViewById(R.id.recurso_exist_id);
                        String id = ex.getId();
                        if (id != null){
                            idText.setText(String.format("(%s)", id));
                        } else {
                            idText.setVisibility(View.GONE);
                        }
                        TextView catText = exCont.findViewById(R.id.recurso_exist_clas);
                        String cat = ex.getCatalogo();
                        if (cat != null){
                            catText.setText("Clasificación " + cat);
                        } else {
                            catText.setVisibility(View.GONE);
                        }
                        TextView exText = exCont.findViewById(R.id.recurso_exist_exist);
                        if (ex.getDisponibilidad() != -1){
                            exText.setText(String.format("%d existencias, %d disponibles para préstamo", ex.getExistencias(), ex.getDisponibilidad()));
                            if (ex.getDisponibilidad() != ex.getExistencias()){
                                TextView exDev = exCont.findViewById(R.id.recurso_exist_dev);
                                Date fechaDev = ex.getDevolucionProxima();
                                exDev.setText("Próximo disponible el " + df.format(fechaDev));
                                exDev.setVisibility(View.VISIBLE);
                            }
                        } else {
                            int exist = ex.getExistencias();
                            exText.setText(String.format("%d existencia"+(exist!=1?"s":""), exist));
                        }
                        cont.addView(exCont);
                    }
                }
                try {
                    cargando.setVisibility(View.GONE);
                    Date ultima = Calendar.getInstance().getTime();
                    r.setUltimaAct(ultima);
                    TextView ultimaText = findViewById(R.id.recurso_exist_ultima);
                    ultimaText.setText("Actualizado el " + edf.format(ultima));
                    ultimaText.setVisibility(View.VISIBLE);
                    Comun.escribirJSON(context);
                    if (origen){
                        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.exito_act_s, Snackbar.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    enc.setVisibility(View.GONE);
                    cargando.setVisibility(View.GONE);
                    cont.setVisibility(View.GONE);
                    Snackbar.make(getWindow().getDecorView().getRootView(), R.string.error_generico, Snackbar.LENGTH_LONG).show();
                }
            }
            if (finished){
                mExecutor.shutdown();
            }
        });

        Runnable backgroundRunnable = () -> {
            try {
                if (r.getUltimaAct() == null){
                    for (Biblioteca b: Comun.consultables){
                        Log.i("UUU", "Consultando " + b.name());
                        String urlex = r.getUriExistCentral();
                        if (urlex != null){
                            Log.i("UUU", r.getUriExistCentral());
                            Request docexreq = new Request.Builder().url(r.getUriExistCentral()).addHeader("User-Agent", Comun.userAgent).get().build();
                            Document docex = Jsoup.parse(okHttp.newCall(docexreq).execute().body().string());
                            Elements tblAcervo = docex.select("#acervo table tr");
                            int exist = 0;
                            String clas = "";
                            for (Element fila: tblAcervo){
                                for (Element celda: fila.select("td")){
                                    if (Objects.equals(celda.text(), "Disponible")) { exist++; }
                                    if (celda.text().startsWith("Q")){ clas = celda.text(); }
                                }
                            }
                            r.setExistencia(b, "ID", tblAcervo.size() - 1, exist, clas, new ArrayList<>());
                            Log.i("UUU", String.format("Acervo: %d, Existencias: %d, Clasificacion: %s", tblAcervo.size() - 1, exist, clas));
                        } else if (b != Biblioteca.CENTRAL) {
                            String uri = Comun.getURIBusqueda(r.getIsbn().get(0), b);
                            Log.i("UUU", Comun.getURIBusqueda(r.getIsbn().get(0), b));
                            Request docreq = new Request.Builder().url(uri).addHeader("User-Agent",Comun.userAgent).get().build();
                            Document doc = Jsoup.parse(okHttp.newCall(docreq).execute().body().string());
                            Element comp = doc.selectFirst("strong:containsOwn(No results found!)"); // No se encontraron resultados!
                            if (comp == null){
                                Element valId = doc.selectFirst(".ratings>[name=\"biblionumber\"]");
                                String id = null;
                                if (valId != null){
                                    id = valId.attr("value");
                                }
                                Pattern p = Pattern.compile(".*([A-Z]{2}\\d+\\.\\d+ [A-Z]\\d+).*", Pattern.MULTILINE);
                                Elements tblAcervo = doc.select("#holdingst tr[typeof=\"Offer\"]");
                                int exist = tblAcervo.size();
                                Elements tblAcervoNo = tblAcervo.select("[property=availability][href=http://schema.org/OutOfStock]");
                                int disp = tblAcervoNo.size();
                                Elements tblAcervoDev = tblAcervo.select(".date_due");
                                ArrayList<Date> dev = new ArrayList<>();
                                for (Element s: tblAcervoDev){
                                    if (s.text().length() > 0) { dev.add(df.parse(s.text())); }
                                }
                                String clas = null;
                                Matcher m = p.matcher(tblAcervo.get(0).selectFirst("[property=sku]").text());
                                if (m.find()) { clas = m.group(1); }
                                Log.i("UUU", String.format("Acervo: %d, Existencias: %d, Clasificacion: %s, Fechas: %d", exist, exist-disp, clas, dev.size()));
                                r.setExistencia(b, id, exist, exist-disp, clas, dev);
                            }
                        }
                    }
                }
                listener.onProcessed(0);
            } catch (IOException e) {
                listener.onProcessed(1);
            } catch (Exception e) {
                e.printStackTrace();
                listener.onProcessed(2);
            }
        };
        mExecutor.execute(backgroundRunnable);
    }
}