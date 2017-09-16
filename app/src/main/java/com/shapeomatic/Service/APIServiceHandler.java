//package com.shapeomatic.Service;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.support.v4.content.LocalBroadcastManager;
//
//import com.alex.onereport2.controller.APIEndpoint;
//import com.alex.onereport2.controller.Constants;
//import com.alex.onereport2.model.Report;
//import com.alex.onereport2.model.ReportEntry;
//import com.alex.onereport2.model.ReportEntryBase;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Created by Alex on 3/28/2017.
// */
//
//
//public class APIServiceHandler extends IntentService{
//
//    // used for the POST request updating single report entry inside a report.
//    // type 'boolean'
//    public static final String INTENT_ACTION_BOOLEAN_GET_REPORT = "getReport";
//
//    // used for the POST request updating single report entry inside a report.
//    // type 'boolean'
//    public static final String INTENT_ACTION_BOOLEAN_PUSH_REPORT_ENTRY = "pushReportEntry";
//
//    // used for the GET request when requesting report of specific unit and date.
//    // type 'long'
//    public static final String INTENT_PARAMETER_CALENDAR_DATE = "getReportDate";
//
//    // used for the GET request when requesting report of specific unit and date.
//    // type 'int'
//    public static final String INTENT_PARAMETER_INT_UNIT_ID = "unitId";
//
//    // used for the POST request updating single report entry inside a report.
//    // type 'ReportEntry'
//    public static final String INTENT_PARAMETER_REPORT_ENTRY = "reportEntry";
//
//    private Retrofit mRetrofit;
//
//    public APIServiceHandler() {
//        super("APIServiceHandler");
//
//        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//        mRetrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//    }
//
//    /**
//     * can receive two operations:
//     * INTENT_ACTION_BOOLEAN_GET_REPORT and INTENT_ACTION_BOOLEAN_PUSH_REPORT_ENTRY.
//     * for INTENT_ACTION_BOOLEAN_GET_REPORT:
//     * receives unit id and date.
//     * returns report (returns NULL if failed)
//     * for INTENT_ACTION_BOOLEAN_PUSH_REPORT_ENTRY:
//     * receives report entry to push.
//     * returns status and report entry it tried to push
//     */
//    @Override
//    protected void onHandleIntent(Intent workIntent) {
//        // Gets data from the incoming Intent
//        String dataString = workIntent.getDataString();
//        boolean getReport = workIntent.getExtras().getBoolean(INTENT_ACTION_BOOLEAN_GET_REPORT);
//        Object obj = workIntent.getSerializableExtra(INTENT_PARAMETER_CALENDAR_DATE);
//        Calendar reportDate;
//        int unitID = workIntent.getExtras().getInt(INTENT_PARAMETER_INT_UNIT_ID, -1);
//        boolean pushReportEntry = workIntent.getExtras().getBoolean(INTENT_ACTION_BOOLEAN_PUSH_REPORT_ENTRY);
//        if(getReport == true) {
//            if(unitID == -1) {
//                returnErrorToActivity("No unit sent");
//                return;
//            }
//            if(obj == null) {
//                returnErrorToActivity("No date sent");
//                return;
//            }
//            reportDate = (Calendar) obj;
//            getReportAndSendToActivity(unitID, reportDate);
//        }
//        else if(pushReportEntry == true) {
//            obj = workIntent.getExtras().get(INTENT_PARAMETER_REPORT_ENTRY);
//            if(obj == null || obj.getClass() != ReportEntry.class) {
//                returnErrorToActivity("No report entry object");
//                return;
//            }
//            ReportEntry entry = (ReportEntry) obj;
//            pushReportEntryAndSendToActivity(entry);
//        }
//    }
//
//    private void getReportAndSendToActivity(int unitId, Calendar date) {
//        APIEndpoint service = mRetrofit.create(APIEndpoint.class);
////        Call<Report[]> reportCall = service.getReports(unitId, date);
//        //TODO:
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        Call<Report[]> reportCall = service.getReports(unitId, sdf.format(date.getTime()));
//        reportCall.enqueue(new Callback<Report[]>() {
//            @Override
//            public void onResponse(Call<Report[]> call, Response<Report[]> response) {
//                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
//                    try {
//                        returnReportToActivity(null);
//                        returnErrorToActivity("server internal error: " + response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return;
//                }
//                Report[] reports = response.body();
//                if(reports.length == 0) {
//                    returnReportToActivity(null);
//                    returnErrorToActivity("no reports received for a single date.");
//                    return;
//                } else if(reports.length > 1) {
//                    returnReportToActivity(null);
//                    returnErrorToActivity("received more then one report per date.");
//                    return;
//                }
//                returnReportToActivity(response.body()[0]);
//            }
//
//            @Override
//            public void onFailure(Call<Report[]> call, Throwable t) {
//                returnReportToActivity(null);
//                returnErrorToActivity("can't reach server: " + t.toString());
//            }
//        });
//    }
//
//    private void pushReportEntryAndSendToActivity(final ReportEntry entry) {
//        APIEndpoint service = mRetrofit.create(APIEndpoint.class);
//        Call<Void> reportEntryCall = service.putReportEntry(entry.getId(), new ReportEntryTemp(entry));
//        reportEntryCall.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
//                    try {
//                        returnPushStatusToActivity(entry, false);
//                        returnErrorToActivity("server internal error: " + response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return;
//                }
//                returnPushStatusToActivity(entry, true);
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                returnPushStatusToActivity(entry, false);
//                returnErrorToActivity("can't reach server: " + t.toString());
//            }
//        });
//    }
//
//    private void returnErrorToActivity(String error) {
//        Intent localIntent = new Intent(Constants.BROADCAST_ACTION)
//                .putExtra(Constants.ERROR_RESPONSE, error);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
//    }
//
//    private void returnReportToActivity(Report report) {
//        Intent localIntent = new Intent(Constants.BROADCAST_ACTION)
//                .putExtra(Constants.REPORT_RESPONSE, report);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
//    }
//
//    private void returnPushStatusToActivity(ReportEntry entry, boolean status) {
//        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);
//        localIntent.putExtra(Constants.PUSH_ISSUCCESS_RESPONSE, status);
//        localIntent.putExtra(Constants.PUSH_REPORT_ENTRY_RESPONSE, entry);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
//    }
//}
