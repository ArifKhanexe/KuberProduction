package com.rank.kuber.Interfaces;

import com.rank.kuber.Common.AppData;
import com.rank.kuber.Model.AgentRequest;
import com.rank.kuber.Model.AgentResponse;
import com.rank.kuber.Model.CallPickedRequest;
import com.rank.kuber.Model.CallPickedResponse;
import com.rank.kuber.Model.EmptyRequest;
import com.rank.kuber.Model.GetEmployeesRequest;
import com.rank.kuber.Model.GetEmployeesResponse;
import com.rank.kuber.Model.GetFeedbackResponse;
import com.rank.kuber.Model.HangUpCustomerRequest;
import com.rank.kuber.Model.HangUpCustomerResponse;
import com.rank.kuber.Model.RegisterRequest;
import com.rank.kuber.Model.RegisterResponse;
import com.rank.kuber.Model.SaveFeedbackRequest;
import com.rank.kuber.Model.SaveFeedbackResponse;
import com.rank.kuber.Model.ServiceDownTimeResponse;
import com.rank.kuber.Model.ServiceModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST(AppData.REQUEST_TYPE_SERVICE_LIST)
    Call<ServiceModel> getservice(@Body EmptyRequest request );

    @POST(AppData.REQUEST_TYPE_REGISTER_CUSTOMER)
    Call<RegisterResponse> getregistercustomer (@Body RegisterRequest registerRequest);

    @POST(AppData.REQUEST_TYPE_AVAILABLE_AGENT)
    Call<AgentResponse> getavailableagent (@Body AgentRequest agentRequest);

    @POST(AppData.REQUEST_TYPE_HANGUP_CUSTOMER)
    Call<HangUpCustomerResponse> gethangupcustomer(@Body HangUpCustomerRequest hangUpCustomerRequest);

    @POST(AppData.REQUEST_TYPE_GET_FEEDBACK)
    Call<GetFeedbackResponse> getfeedback (@Body EmptyRequest emptyRequest);

    @POST(AppData.REQUEST_TYPE_SAVE_FEEDBACK)
    Call<SaveFeedbackResponse> getsavefeedback (@Body SaveFeedbackRequest saveFeedbackRequest);

    @POST(AppData.REQUEST_TYPE_GET_SERVICEDOWNTIME)
    Call<ServiceDownTimeResponse> getservicedowntime (@Body EmptyRequest emptyRequest);

    @POST(AppData.REQUEST_TYPE_PICKED_CALL_BY_CUSTOMER)
    Call<CallPickedResponse> getpickedcallbycustomer(@Body CallPickedRequest callPickedRequest);

    @POST(AppData.REQUEST_TYPE_GET_EMPLOYEES)
    Call<GetEmployeesResponse> getincallemployees(@Body GetEmployeesRequest getEmployeesRequest);

}
