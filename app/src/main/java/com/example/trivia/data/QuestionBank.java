package com.example.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {


    ArrayList<Question> questionArrayList = new ArrayList<>();
    private String url =  "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements.json";
    //AppController appController = new AppController();
    //RequestQueue requestQueue = appController.getRequestQueue();





    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }
    );


    public  List<Question> getQuestionsTest() {

        questionArrayList.add(new Question("Question 1 ", true));

        questionArrayList.add(new Question("Question 2 ", true));

         questionArrayList.add(new Question("Question 3 ", true));


        return questionArrayList;


    }

    public  List<Question> getQuestions(final AnswerListAsyncResponse callBack){

        Log.d("getQuestions", "getQuestions: ");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,

                new Response.Listener<JSONArray>(){

                @Override
                public void onResponse(JSONArray response) {
                    Log.d("json data", "onResponse: " +response);

                    //questionArrayList.Add(" ", " ");

                    for(int i=0; i<response.length(); i++) {


                        try {

                            //JSONArray jsonArray = response.getJSONArray();

                            Question questionObject = new Question();

                            questionObject.setAnswer(response.getJSONArray(i).get(0).toString());
                            questionObject.setAnswerTrue(response.getJSONArray(i).getBoolean(1));



                            questionArrayList.add(questionObject);

                            Log.d("json data", "onResponse: " + response.getJSONArray(1).get(0));
                            Log.d("json data", "onResponse: " + response.getJSONArray(1).get(1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    if(null != callBack) callBack.proccessFinished(questionArrayList);
                }
        }, new Response.ErrorListener(){
                    @Override
            public void onErrorResponse(VolleyError error){


                    }


        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionArrayList;
    }



    /*
    public void onResponse(JSONArray response){

        try{
            for(int i = 0; i<response.length(); i++){
                Question question = new Question();
                question.setAnswer(response.getJSONArray(i).getString(0));
                question.setAnswerTrue(response.getJSONArray(i).getString(1));
                questionArrayList.add(question);
            }
            catch(JSONException e){

                printStackTrace();

            }
        }
    }

*/



}
