package com.wayforlife.wayforlife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class EventDetailsAdmin extends AppCompatActivity {

    TextView eName,eDate,eDesc;
    Button removeEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details_admin);

        eName=(TextView)findViewById(R.id.EventNameId);
        eDate=(TextView)findViewById(R.id.DateSetId);
        eDesc=(TextView)findViewById(R.id.EventDescriptionId);
        removeEvent=(Button)findViewById(R.id.RemoveEventtButtonId);

        Intent getI=getIntent();

        eName.setText(getI.getStringExtra("ename"));
        eDate.setText(getI.getStringExtra("edate"));
        eDesc.setText(getI.getStringExtra("edesc"));

    }
}
