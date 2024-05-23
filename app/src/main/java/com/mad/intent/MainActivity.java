package com.mad.intent;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends Activity {
    TodoDBHelper todoDBHelper;
    RecyclerView recyclerView;
    Button addButton;
    EditText addEditText;
    TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoDBHelper = new TodoDBHelper(this);
        recyclerView = findViewById(R.id.recycler);
        addButton = findViewById(R.id.add_button);
        addEditText = findViewById(R.id.add_edittext);

        List<Todo> todos = todoDBHelper.getAllTodoItems();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter =  new TodoAdapter(this, todos, todoDBHelper);
        recyclerView.setAdapter(todoAdapter);

        Button button = findViewById(R.id.send_reminder_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Todo> todos= todoDBHelper.getAllTodoItems();

                String todo="";
                for(Todo t : todos){
                    if(!t.isCompleted()) {
                        todo += t.getTask();
                        todo += '\n';
                    }
                }
                Notification.showNotification(MainActivity.this, MainActivity.class, "Pending to do", todo);

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = addEditText.getText().toString();

                if (!text.isEmpty()) {
                    long itemId = todoDBHelper.insertTodoItem(text, false);

                    if (itemId != -1) {
                        Todo newTodo = new Todo(text, false);
                        todoAdapter.addItem(newTodo);

                        int position = todoAdapter.getItemCount() - 1;
                        todoAdapter.notifyItemInserted(position);

                        Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_LONG).show();
                        addEditText.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to add item", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter something....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}