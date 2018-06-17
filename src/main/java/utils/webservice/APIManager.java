package utils.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import java.util.List;
import java.util.Map;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;



import net.minidev.json.JSONArray;
import utils.file.JSONManager;

public class APIManager {
	WebServiceUtility ws;

	public APIManager(){
		ws = new WebServiceUtility();
	}

	/**
	 * this is the first endpoint call to get the agent login token and id
	 * @param data
	 * @return token, id
	 * only used by setRequestHeader function
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getAccessTokenId(Map<String, Object> data) {

		String contentType = "application/json";
		String accessTokenURL = String.valueOf(data.get("BaseURL")) + "/agentservice/login";
		ws.setContentType(contentType);
		Map<String, Object> agentLoginData = new HashMap<>();		
		agentLoginData = (Map<String, Object>) data.get("AgentLoginData");
		String response = ws.HTTPPost(accessTokenURL, agentLoginData);
		// library.HTTPPost(URL, pData);
		//		System.out.println("Token response: " + response);
		Object token = JSONManager.getValueFromJson("$.token", response);
		Object id = JSONManager.getValueFromJson("$.id", response);

		agentLoginData.put("Token", token);
		agentLoginData.put("Id", id);

		return agentLoginData;
	}


	/**
	 * this is the first endpoint call to get the agent login token and id
	 * @param data
	 * @return token, id
	 * only used by setRequestHeader function
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getAccessTokenId(Map<String, Object> data, int index) {
		String contentType = "application/json";
		String accessTokenURL = String.valueOf(data.get("BaseURL"+index)) + "/agentservice/login";
		ws.setContentType(contentType);
		Map<String, Object> agentLoginData = new HashMap<>();		
		agentLoginData = (Map<String, Object>) data.get("AgentLoginData"+index);
		String response = ws.HTTPPost(accessTokenURL, agentLoginData);
		// library.HTTPPost(URL, pData);
		System.out.println("Token response: " + response);
		Object token = JSONManager.getValueFromJson("$.token", response);
		Object id = JSONManager.getValueFromJson("$.id", response);
		agentLoginData.put("Token", token);
		agentLoginData.put("Id", id);
		return agentLoginData;
	}

	/**
	 * to set header for the request and get the agent info
	 * @param data
	 * @return agent id, agent first name, agent last name, agent email and agent role
	 *
	 */
	private Map<String, Object> setRequestHeader(Map<String, Object> data) {
		Map<String, Object> agentData = new HashMap<String, Object>();
		String contentType = "application/json";
		//get token and agent id
		Map<String, Object> accessData = getAccessTokenId(data);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		//		String contentType = String.valueOf(data.get("ContentType"));
		ws.setContentType(contentType);
		ws.setRequestHeader("x-userid", xuserid);
		ws.setRequestHeader("Authorization", "Bearer " + token);

		//store the agent data to a map
		//		String role = String.valueOf(accessData.get("Role"));
		String firstName = String.valueOf(accessData.get("FirstName"));
		String lastName = String.valueOf(accessData.get("LastName"));
		String middleName = String.valueOf(accessData.get("MiddleName"));
		String email = String.valueOf(accessData.get("Email"));

		agentData.put("Id",xuserid);
		//		agentData.put("Role", role);role response from this api is incorrect, need to call the /team end point
		agentData.put("FirstName", firstName);
		agentData.put("LastName", lastName);
		agentData.put("MiddleName", middleName);
		agentData.put("Email", email);

		return agentData;
	}

	private Map<String, Object> setRequestHeader(Map<String, Object> data, int index) {
		Map<String, Object> agentData = new HashMap<String, Object>();
		String contentType = "application/json";
		//get token and agent id
		Map<String, Object> accessData = getAccessTokenId(data, index);
		String token = String.valueOf(accessData.get("Token"));
		String xuserid = String.valueOf(accessData.get("Id"));
		//		String contentType = String.valueOf(data.get("ContentType"));
		ws.setContentType(contentType);
		ws.setRequestHeader("x-userid", xuserid);
		ws.setRequestHeader("Authorization", "Bearer " + token);

		//store the agent data to a map
		//		String role = String.valueOf(accessData.get("Role"));
		String firstName = String.valueOf(accessData.get("FirstName"));
		String lastName = String.valueOf(accessData.get("LastName"));
		String middleName = String.valueOf(accessData.get("MiddleName"));
		String email = String.valueOf(accessData.get("Email"));

		agentData.put("Id",xuserid);
		//		agentData.put("Role", role);role response from this api is incorrect, need to call the /team end point
		agentData.put("FirstName", firstName);
		agentData.put("LastName", lastName);
		agentData.put("MiddleName", middleName);
		agentData.put("Email", email);

		return agentData;
	}

	/**
	 * to get the contact list for Partner Agent with 20 clients
	 * @param data
	 * @return totalCount, filteredCount, 
	 * ClientList: {userId, urgency, referralDataTime, phone, opportunitiesSummary,
	 * leadSummary, lastVisitedDateTime, lastName, lastActivityDateTime, firstName, email, altEmails}]
	 * parameters:
	 * 1. incPaymentDue
	 * 2. size: 20
	 * 3. sort: default, newest referral to oldest referral
	 *
	 */
	public String getPAContacts(Map<String, Object> data){
		//to set header for request	and get the agent info
		Map<String, Object> accessData = setRequestHeader(data);
		String agentID = (String) accessData.get("Id");
		String baseURL= (String) data.get("BaseURL");
		//contact endpoint: http://agent-ng.movoto.net/agentservice/agents/<agentid>/contacts?incPaymentDue=1&size=10&sort=0&startIndex=0
		String contactsURL = baseURL + "/agentservice/agents/" + agentID + "/contacts?incPaymentDue=1&size=20&sort=0&startIndex=0";
		System.out.println(contactsURL);
		String response = ws.HTTPGet(contactsURL);
		System.out.println(response);		
		return response;
	}

	/**
	 * to get the contact list for Partner Agent with X clients
	 * @param data
	 * @return totalCount, filteredCount, 
	 * ClientList: {userId, urgency, referralDataTime, phone, opportunitiesSummary,
	 * leadSummary, lastVisitedDateTime, lastName, lastActivityDateTime, firstName, email, altEmails}]
	 * parameters:
	 * 1. incPaymentDue
	 * 2. size: 20
	 * 3. sort: default, newest referral to oldest referral
	 */
	public String getPAContacts(Map<String, Object> data, int size){
		//to set header for request	and get the agent info
		Map<String, Object> accessData = setRequestHeader(data);
		String agentID = (String) accessData.get("Id");
		String baseURL= (String) data.get("BaseURL");
		//contact endpoint: http://agent-ng.movoto.net/agentservice/agents/<agentid>/contacts?incPaymentDue=1&size=10&sort=0&startIndex=0
		String contactsURL = baseURL + "/agentservice/agents/" + agentID + "/contacts?incPaymentDue=1&size="+size+"&sort=0&startIndex=0";
		System.out.println(contactsURL);
		String response = ws.HTTPGet(contactsURL);
		System.out.println(response);		
		return response;
	}

	/**
	 * to get the contact response with filter
	 * @param data
	 * @param sortIndex
	 * 0 - filter with Follow up now
	 * 1 - filter with Talked
	 * 2- filter with 
	 * 3 - filter with Met
	 * @return
	 */
	public String getPAContactsDataWithFilter(Map<String, Object> data, int filterIndex){
		//to set header for request

		Map<String, Object> accessData = setRequestHeader(data);		
		String agentID = (String)accessData.get("Id");
		String baseURL= (String) data.get("BaseURL");
		//contact endpoint: http://agent-ng.movoto.net/agentservice/agents/<agentid>/contacts?incPaymentDue=1&size=10&sort=0&startIndex=0
		String contactsURL = baseURL + "/agentservice/agents/" + agentID + "/contacts?filter=" + filterIndex + "&incPaymentDue=1&size=20&sort=0&startIndex=0";
		System.out.println(contactsURL);
		String response = ws.HTTPGet(contactsURL);
		
		System.out.println(response);		
		return response;
	}


	/**
	 * to get the client id, opportunityId for the given client
	 * This function will fail if we have duplicate names
	 * in this case, pass the client email to test
	 * search by email need to implement later
	 * @param data
	 * @param name
	 * @return userid, opportunityId
	 */
	public Map<String, Object> getClientIdOppId(Map<String, Object> data, String name){
		Map<String, Object> clientContactData= new HashMap<String, Object>();
		String contactDataRes = getPAContacts(data);
		System.out.println(contactDataRes);	

		int totalClientNum = (int) JSONManager.getValueFromJson("$.totalCount", contactDataRes);

		//		JSONArray firstNameList =(JSONArray) JSONManager.getValueFromJson("$.clientList[*].contactInfo.firstName", contactDataRes);

		//		System.out.println(firstNameList);	

		int index = 0;
		for(int i = 0; i< totalClientNum;i++){
			String firstName = (String)JSONManager.getValueFromJson("$.clientList[" + i + "].contactInfo.firstName", contactDataRes);
			String lastName = (String)JSONManager.getValueFromJson("$.clientList[" + i + "].contactInfo.lastName", contactDataRes);
			String clientName = firstName + " " + lastName;
			if(clientName.equals(name)){
				index = i;		
				break;
			}
		}
		System.out.println(index);
		Object userId = (String)JSONManager.getValueFromJson("$.clientList[" + index + "].contactInfo.firstName", contactDataRes);
		Object leadOppId = 0;
		int oppCount = 0;
		Object leadSummary = (Object)JSONManager.getValueFromJson("$.clientList[" + index + "].contactInfo.leadSummary", contactDataRes);
		if(leadSummary!=null){
			leadOppId = (Object)JSONManager.getValueFromJson("$.clientList[" + index + "].contactInfo.leadSummary.leads[*].leadId", contactDataRes);

		}
		else{
			oppCount = (int)JSONManager.getValueFromJson("$.clientList[" + index + "].contactInfo.opportunitiesSummary.openOpportunitiesCount", contactDataRes);
			leadOppId = (Object) JSONManager.getValueFromJson("$.clientList[" + index + "].contactInfo.opportunitiesSummary.openOpportunities[*].opportunityId", contactDataRes);
		}
		System.out.println("lead opportunity id is: " + leadOppId);
		clientContactData.put("UserId",userId);
		clientContactData.put("LeadOpportunityId",leadOppId);
		clientContactData.put("OppCount",oppCount);
		return clientContactData;
	}

	/**
	 * to get the client id, opportunityId for xth client
	 * This function will fail if we have duplicate names
	 * in this case, pass the client email to test
	 * search by email need to implement later
	 * @param data
	 * @param name
	 * @return userid and lead opportunity id, open Opportunity count
	 */
	public Map<String, Object> getClientIdOppId(Map<String, Object> data, int clientIndex){
		Map<String, Object> clientContactData= new HashMap<String, Object>();
		//to get the response from contact endpoints
		String response = getPAContacts(data);
		//to get the X clientid
		String contactId = (String)JSONManager.getValueFromJson("$.clientList[" + clientIndex + "].contactInfo.userId", response);
		int oppCount = 0;
		Object leadOppId = "";
		Object leadSummary = (Object)JSONManager.getValueFromJson("$.clientList[" + clientIndex + "].contactInfo.leadSummary", response);
		if(leadSummary!=null){
			leadOppId = (String)JSONManager.getValueFromJson("$.clientList[" + clientIndex + "].contactInfo.leadSummary.leads[*].leadId", response);			
		}
		else{
			oppCount = (int)JSONManager.getValueFromJson("$.clientList[" + clientIndex + "].contactInfo.opportunitiesSummary.openOpportunitiesCount", response);
			leadOppId = (Object) JSONManager.getValueFromJson("$.clientList[" + clientIndex + "].contactInfo.opportunitiesSummary.openOpportunities[*].opportunityId", response);
		}
		System.out.println("lead opportunity id is: " + leadOppId);
		System.out.println("lead opportunity count is: " + oppCount);
		clientContactData.put("UserId",contactId);
		clientContactData.put("OppCount",oppCount);
		clientContactData.put("LeadOpportunityId",leadOppId);		
		return clientContactData;
	}


	/**
	 * to get the whole contact detail response by client name
	 * @param
	 * test data
	 * client name
	 * @return
	 * the response from contact detail endpoint
	 */
	public String getClientContactDetail(Map<String, Object> data, String name){
		//to set header for request	and get the agent info
		Map<String, Object> accessData = setRequestHeader(data);
		String agentID = (String) accessData.get("Id");
		String baseURL= (String) data.get("BaseURL");
		Map<String, Object> clientIdOppId = getClientIdOppId(data, name);
		String contactId = (String) clientIdOppId.get("UserId");
		//contact detail endpoint: http://agent-ng.movoto.net/agentservice/agents/<agentid>/contacts/<contactid>
		String contactDetailURL = baseURL + "/agentservice/agents/" + agentID + "/contacts/"+contactId;
		System.out.println(contactDetailURL);
		String response = ws.HTTPGet(contactDetailURL);
		System.out.println(response);		
		return response;
	}

	/**
	 * to get the whole contact detail response by client index
	 * @param
	 * test data
	 * client name
	 * @return
	 * the response from contact detail endpoint
	 */
	public String getClientContactDetail(Map<String, Object> data, int clientIndex){
		//to set header for request	and get the agent info
		Map<String, Object> accessData = setRequestHeader(data);
		String agentID = (String) accessData.get("Id");
		String baseURL= (String) data.get("BaseURL");
		Map<String, Object> clientIdOppId = getClientIdOppId(data, clientIndex);
		String contactId = (String) clientIdOppId.get("UserId");
		//contact detail endpoint: http://agent-ng.movoto.net/agentservice/agents/<agentid>/contacts/<contactid>
		String contactDetailURL = baseURL + "/agentservice/agents/" + agentID + "/contacts/"+contactId;
		System.out.println(contactDetailURL);
		String response = ws.HTTPGet(contactDetailURL);
		System.out.println(response);		
		return response;
	}







	/**
	 * to get the whole contact detail response by client index
	 * @param
	 * test data
	 * client name
	 * @return
	 * the response from contact detail opportunities endpoint:
	 * 1. opportunity urgency
	 */
	public String getContactDetailOpportunities(Map<String, Object> data, int clientIndex){
		Map<String, Object> clientIdOppId = getClientIdOppId(data, clientIndex);
		String contactId = (String) clientIdOppId.get("UserId");
		//to set header for request	and get the agent info
		Map<String, Object> accessData = setRequestHeader(data);
		String agentID = (String) accessData.get("Id");
		String baseURL= (String) data.get("BaseURL");
		//contact detail endpoint: http://agent-ng.movoto.net/agentservice/agents/<agentid>/contacts/<contactid>/opportunities
		String contactOpportunityURL = baseURL + "/agentservice/agents/" + agentID + "/contacts/"+contactId+"/opportunities";
		System.out.println(contactOpportunityURL);
		String response = ws.HTTPGet(contactOpportunityURL);
		System.out.println("Response is: " + response);		
		return response;
	}


	/**
	 * to get the whole contact detail response by client name
	 * @param
	 * test data
	 * client name
	 * @return
	 * the response from contact detail opportunities endpoint:
	 * 1. opportunity urgency
	 */
	public String getContactDetailOpportunities(Map<String, Object> data, String name){
		Map<String, Object> clientIdOppId = getClientIdOppId(data, name);
		String contactId = (String) clientIdOppId.get("UserId");
		//to set header for request	and get the agent info
		Map<String, Object> accessData = setRequestHeader(data);
		String agentID = (String) accessData.get("Id");
		String baseURL= (String) data.get("BaseURL");
		//contact detail endpoint: http://agent-ng.movoto.net/agentservice/agents/<agentid>/contacts/<contactid>/opportunities
		String contactOpportunityURL = baseURL + "/agentservice/agents/" + agentID + "/contacts/"+contactId+"/opportunities";
		System.out.println(contactOpportunityURL);
		String response = ws.HTTPGet(contactOpportunityURL);
		System.out.println(response);		
		return response;
	}

	/**
	 * to get opportunities's urgency by client name
	 * @param data, client name
	 * @return urgency code, urgency text
	 */
	public Map<String, String> getUrgencyData(Map<String, Object> data, String name){
		Map<String, String> urgencyData = new HashMap<String, String>();
		String response = getContactDetailOpportunities(data, name);
		System.out.println(response);	
		JSONArray oppCount = (JSONArray) JSONManager.getValueFromJson("$.opportunities[*]", response);
		for(int i = 0;i<oppCount.size();i++){
			String urgencyStatus = (String)JSONManager.getValueFromJson("$.opportunities["+i+"].urgencyStatus", response);
			String urgencyCode = urgencyStatus.split(" - ")[0];
			urgencyData.put("urgencyStatus"+i, urgencyStatus);
			urgencyData.put("urgencyCode"+i, urgencyCode);
		}
		return urgencyData;
	}

	/**
	 * to get opportunities's urgency by client name
	 * @param data, client name
	 * @return urgency code, urgency text
	 */
	public Map<String, String> getUrgencyData(Map<String, Object> data, int clientIndex){
		Map<String, String> urgencyData = new HashMap<String, String>();
		String response = getContactDetailOpportunities(data, clientIndex);
		System.out.println(response);	
		JSONArray oppCount = (JSONArray) JSONManager.getValueFromJson("$.opportunities[*]", response);
		for(int i = 0;i<oppCount.size();i++){
			String urgencyStatus = (String)JSONManager.getValueFromJson("$.opportunities["+i+"].urgencyStatus", response);
			String urgencyCode = urgencyStatus.split(" - ")[0];
			urgencyData.put("urgencyStatus"+i, urgencyStatus);
			urgencyData.put("urgencyCode"+i, urgencyCode);
		}
		return urgencyData;
	}



	/**
	 * to get activity response by client name
	 * @param args
	 */
	public String getActivity(Map<String, Object> data, String name){
		Map<String, Object> clientIdOppId = getClientIdOppId(data, name);
		String contactId = (String) clientIdOppId.get("UserId");
		String opportunityId = (String) clientIdOppId.get("LeadOpportunityId");
		//to set header for request	and get the agent info
		Map<String, Object> accessData = setRequestHeader(data);
		String agentID = (String) accessData.get("Id");
		String baseURL= (String) data.get("BaseURL");
		//contact detail endpoint: http://agent-ng.movoto.net/agentservice/agents/<agentid>/contacts/<contactid>/opportunities/<oppId>/activities
		String contactActivityURL = baseURL + "/agentservice/agents/" + agentID + "/contacts/"+contactId+"/opportunities/" + opportunityId + "/activities";
		System.out.println(contactActivityURL);
		String response = ws.HTTPGet(contactActivityURL);
		System.out.println(response);		

		return response;
	}

	/**
	 * to get the contact's first opportunity's activity response
	 * @param data
	 * @param index
	 * @return
	 */

	public String getActivity(Map<String, Object> data, int index){
		Map<String, Object> clientIdOppId = getClientIdOppId(data, index);
		String contactId = (String) clientIdOppId.get("UserId");
		//		int oppCount = (int) clientIdOppId.get("OppCount");
		List<String> oppIdList = new ArrayList<String>();
		oppIdList = (List<String>) clientIdOppId.get("LeadOpportunityId");
		String opportunityId = oppIdList.get(0);
		//to set header for request	and get the agent info
		Map<String, Object> accessData = setRequestHeader(data);
		String agentID = (String) accessData.get("Id");
		String baseURL= (String) data.get("BaseURL");
		//contact detail endpoint: http://agent-ng.movoto.net/agentservice/agents/<agentid>/contacts/<contactid>/opportunities/<oppId>/activities
		String contactActivityURL = baseURL + "/agentservice/agents/" + agentID + "/contacts/"+contactId+"/opportunities/" + opportunityId + "/activities";
		System.out.println(contactActivityURL);
		String response = ws.HTTPGet(contactActivityURL);
		System.out.println(response);		

		return response;
	}


	
	/**
	 * to get the agent role from team end point
	 * @param data
	 * @param index, the row # in the excel sheet
	 * @return team members[]
	 * @return agent role
	 */
	public String getTeamResponse(Map<String, Object> data, int index){
		//to get id
		Map<String, Object> map = setRequestHeader(data,index);
		String id = (String) map.get("Id");
		String baseURL= (String) data.get("BaseURL"+index);
		//to call the team api: https://agent-ng.movoto.net/agentservice/agents/123456/team
		String teamURL = baseURL +"/agentservice/agents/"+id+"/team";
		System.out.println(teamURL);		
		String response = ws.HTTPGet(teamURL);
		return response;
	}

	/**
	 * to get the agent role from team end point
	 * @param data
	 * @param index, the row # in the excel sheet
	 * @return team members[]
	 * @return agent role
	 */
	public String getTeamResponse(Map<String, Object> data){
		//to get id
		Map<String, Object> map = setRequestHeader(data);
		String id = (String) map.get("Id");
		String baseURL= (String) data.get("BaseURL");
		//to call the team api: https://agent-ng.movoto.net/agentservice/agents/123456/team
		String teamURL = baseURL +"/agentservice/agents/"+id+"/team";
		System.out.println(teamURL);		
		String response = ws.HTTPGet(teamURL);
		return response;
	}

	public String getFilterResponse(Map<String, Object> data, int filterIndex) {
		//to set header for request
		
		Map<String, Object> accessData = setRequestHeader(data);		
		String agentID = (String)accessData.get("Id");
		String baseURL= (String) data.get("BaseURL");
		//contact endpoint: http://agent-ng.movoto.net/agentservice/agents/<agentid>/contacts?incPaymentDue=1&size=10&sort=0&startIndex=0
		String contactsURL = baseURL + "/agentservice/agents/" + agentID + "/contacts?filter=" + filterIndex + "&incPaymentDue=1&size=20&sort=0&startIndex=0";
		System.out.println(contactsURL);
		String response = ws.HTTPGet(contactsURL);
		System.out.println(response);		
		return response;
	}
	
	/**
	 * this is used for team leader:
	 * /agents/268e3cd7-cbe6-4ded-8478-0bda…3642/contacts?filter=11&incPaymentDue=1&size=10&sort=6&startIndex=0&type=2
	 * @param data
	 * @param filterIndex
	 * @param filterType
	 * @return
	 */
	public String getFilterResponse(Map<String, Object> data, int filterIndex, int filterType) {
		//to set header for request
		
		Map<String, Object> accessData = setRequestHeader(data);		
		String agentID = (String)accessData.get("Id");
		String baseURL= (String) data.get("BaseURL");
		//contact endpoint: http://agent-ng.movoto.net/agentservice/agents/<agentid>/contacts?incPaymentDue=1&size=10&sort=0&startIndex=0
		String contactsURL = baseURL + "/agentservice/agents/" + agentID + "/contacts?filter=" + filterIndex + "&incPaymentDue=1&size=20&sort=6&startIndex=0&type" + filterType;
		System.out.println(contactsURL);
		String response = ws.HTTPGet(contactsURL);
		System.out.println(response);		
		return response;
	}
	
	/**
	 * http://agent-ng.movoto.net/agentservice/agents/cc75df81-bd9e-4c81-8b6f-1b40c733d1a6/contacts?incPaymentDue=1&size=10&sort=9&startIndex=0
	 * @param data
	 * @param sortIndex
	 * 0 - Newest Referred
	 * 1 - 
	 * 9 - Newest Assigned
	 * @return
	 */
	public String getContactsWithSort(Map<String, Object> data, int sortIndex){
		//to set header for request	and get the agent info
		Map<String, Object> accessData = setRequestHeader(data);
		String agentID = (String) accessData.get("Id");
		String baseURL= (String) data.get("BaseURL");
		//contact endpoint: http://agent-ng.movoto.net/agentservice/agents/<agentid>/contacts?incPaymentDue=1&size=10&sort=0&startIndex=0
		String contactsURL = baseURL + "/agentservice/agents/" + agentID + "/contacts?incPaymentDue=1&size=10&sort="+sortIndex+"&startIndex=0";
		System.out.println(contactsURL);
		String response = ws.HTTPGet(contactsURL);
//		System.out.println(response);		
		return response;
	}
	
	
	public String getContacts(Map<String, Object> data){
		//to set header for request	and get the agent info
		Map<String, Object> accessData = setRequestHeader(data);
		String agentID = (String) accessData.get("Id");
		String baseURL= (String) data.get("BaseURL");
		//contact endpoint: http://agent-ng.movoto.net/agentservice/agents/<agentid>/contacts?incPaymentDue=1&size=10&sort=0&startIndex=0
		String contactsURL = baseURL + "/agentservice/agents/" + agentID + "/contacts?size=200";
		System.out.println(contactsURL);
		String response = ws.HTTPGet(contactsURL);
//		System.out.println(response);		
		return response;
	}
	
}
