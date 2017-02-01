package ca.zyqiuuwaterloo.HamiltonBylaws;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TextView;
import android.view.View;
import android.os.AsyncTask;
import android.widget.Button;

import java.lang.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<ByLaw> Laws2017 = new ArrayList<ByLaw>();

    private TextView parsed_content;
    Document doc;
    String a_body;
    Element table2017;
    Elements tableRows;
    StringBuilder print_to_screen;
    private TableLayout table_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //parsed_content = (TextView) findViewById(R.id.html_content);
        print_to_screen = new StringBuilder("");
        table_layout = (TableLayout) findViewById(R.id.table1);
        Button htmlTitleButton = (Button) findViewById(R.id.button);
        htmlTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsynxConnection a_connection = new AsynxConnection();
                a_connection.execute();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class AsynxConnection extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //parsed_content = (TextView) findViewById(R.id.html_content);
            try {
                doc = Jsoup.connect("https://www.hamilton.ca/government-information/by-laws-and-enforcement/by-laws-passed-in-2017/").get();
                Elements refined_1 = doc.getElementsByClass("field__item even");
                table2017 = refined_1.first();
                tableRows = table2017.getElementsByTag("tr");
                a_body = tableRows.toString();
                //a_body = "oh"; //for debugging
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            /*
            Elements inner_elements = rows_1.nextAll().first().getElementsByTag("p");
            String print_element = inner_elements.toString();
            print_to_screen.append(print_element);
            */
            int skip_first = 1;
            /*
            int number_iterate = 0;
            */
            for (Element tr_element : tableRows) {
                if (skip_first == 1) {
                    skip_first = 0;
                    continue;
                }
                /*if (number_iterate == 10) {
                    break;
                }*/
                Elements rowCells = tr_element.getElementsByTag("td");
                /*if(rowCells.isEmpty()){
                    rowCells = tr_element.getElementsByTag("td");
                }*/
                //Get Law number
                Element Cell = rowCells.get(0);
                Element numElem;
                if (Cell.getElementsByTag("p").isEmpty()) {
                    numElem = Cell;
                } else {
                    numElem = Cell.getElementsByTag("p").first();
                }
                String lawNum = numElem.text().substring(3,6);

                //Get description
                Cell = rowCells.get(1);
                Element descElem;
                if (Cell.getElementsByTag("p").isEmpty()) {
                    descElem = Cell;
                } else {
                    descElem = Cell.getElementsByTag("p").first();
                }
                String lawDesc = descElem.text();

                //Get Date
                Cell = rowCells.get(2);
                Element dateElem;
                if (Cell.getElementsByTag("p").isEmpty()) {
                    dateElem = Cell;
                } else {
                    dateElem = Cell.getElementsByTag("p").first();
                }
                String lawDate = dateElem.text();

                ByLaw newByLaw = new ByLaw(lawDesc, lawDate, lawNum);
                Laws2017.add(newByLaw);

                //print_to_screen.append(lawDesc + " " + lawDate + " " + lawNum);
                /*
                number_iterate++;
                */
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //parsed_content.setText(a_body);
            //parsed_content.setText(print_to_screen);  // for debugging
            int row_num = 2;
            for (ByLaw a_law : Laws2017) {
                /*
                if (row_num == 2) {
                    row_num++;
                    continue;
                }
                */
                TextView parsed_content2;

                /*
                String first_row = "TextView"+String.valueOf(row_num)+"1";
                int resID = getResources().getIdentifier(first_row, "id", getPackageName());
                parsed_content2 = (TextView) findViewById(resID);
                parsed_content2.setText(a_law.datePassed);

                */
                String second_row = "TextView"+String.valueOf(row_num)+"2";
                int resID2 = getResources().getIdentifier(second_row, "id", getPackageName());
                parsed_content2 = (TextView) findViewById(resID2);
                parsed_content2.setText(a_law.Description);
                row_num++;
            }

        }
    }
    private class ByLaw{
        public String Description;
        //pdf link
        public String datePassed;
        public String lawNum;

        public ByLaw (String desc, String passedOn, String number){
            Description = desc;
            datePassed = passedOn;
            lawNum = number;
        }
    }
}