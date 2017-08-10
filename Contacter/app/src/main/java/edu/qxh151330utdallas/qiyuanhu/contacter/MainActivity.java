
/**
 *Copyright ©  2017
 *Title: Asg4-xxw151430
 *Description: Android Contact program
 *Auther: Qiyuan Hu, Xuwei Wang
 *NetID: qxh151330, xxw151430
 *Date: 03/25/2017
 *Version: 2.1.1
 */

package edu.qxh151330utdallas.qiyuanhu.contacter;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView view;
    private List<String> contacts = new ArrayList<>();
    private List<String> contactsclick = new ArrayList<>();
    private int entry;

    Intent intent;
    @Override
	//written by Xuwei Wang
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(MainActivity.this, ContactData.class);
        load();

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                entry = view.getCheckedItemPosition();
                String loadData;
                File contactfile = new File("record.txt");
                try
                {
					//read txt file
                    FileInputStream fileinput = openFileInput("record.txt");
                    InputStreamReader fReader = new InputStreamReader(fileinput);
                    BufferedReader bReader = new BufferedReader(fReader);

                    while((loadData = bReader.readLine()) != null)
                    {
                        contactsclick.add(loadData);
                    }
                    fileinput.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Send intent to ContactData to show all the details. Start ContactData activity.
                intent.putExtra("click", contactsclick.get(entry));
                startActivity(intent);
            }
        });

    }
    @Override
	//written by Xuwei Wang
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }
	
	//written by Xuwei Wang
	//load action
    private void load()
    {
        view =  (ListView) findViewById(R.id.list);
        view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        String loadData;
        File contactfile = new File("record.txt");
        if (!contactfile.exists())
        {
            try
            {
                contactfile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            FileInputStream fileinput = openFileInput("record.txt");
            InputStreamReader fReader = new InputStreamReader(fileinput);
            BufferedReader bReader = new BufferedReader(fReader);

            while((loadData = bReader.readLine()) != null)
            {
                String[] source = loadData.split("\t");
                if(source[2].equals(""))
                    contacts.add(source[0] + source[1]   );
                else
                    contacts.add(source[0] + source[1] + "\t Phone:" +  source[2] +"\t"  );
            }
            fileinput.close();

            ArrayAdapter<String> arr = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
            view.setAdapter(arr);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
	//written by Xuwei Wang
	//transfer text menu to icon menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_icon, menu);
        return true;
    }
	
    @Override
	//written by Xuwei Wang
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_new) {
            intent.putExtra("click", "");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
