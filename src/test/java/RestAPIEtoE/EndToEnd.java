package RestAPIEtoE;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEnd {
	
	Response response;
	String BaseURI = "http://localhost:8088/employees";	
	@Test
	public void AllTests()
	{		
	//1.Get method
		response = GetAllEmployees();
		Assert.assertEquals(response.getStatusCode(), 200);
		 //String body = response.getBody().asString();	
		 //JsonPath json= response.jsonPath();
		Assert.assertEquals(response.jsonPath().getList("employees").size(), 3);	
		int NumberofEmployees = response.jsonPath().getList("employees").size();
	//2.Post Method
		response = CreatenewEmployee("John","Smith",300000,"abcd1@gmail.com");
		Assert.assertEquals(response.getStatusCode(), 201);
		 //JsonPath json1= response.jsonPath();
		Assert.assertEquals(response.jsonPath().getList("employees").size(), 4);
		Assert.assertEquals(response.jsonPath().get("firstName"), "John");
		Assert.assertEquals(response.jsonPath().getList("id"), 4);
		int empId = response.jsonPath().getInt("id");
	//3.Put Method
		response = UpdateEmployee(empId,"Tom","Kohli",500000);
		 //JsonPath json = response.jsonPath();		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBody().jsonPath().get("firstName"), "Tom");	
		Assert.assertNotEquals(response.getBody().jsonPath().get("firstName"), "John");
	//4.Get single employee with emp ID.
		response = GetSinglentry(empId);
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBody().jsonPath().get("lastName"), "Kohli");
		Assert.assertEquals(response.getBody().jsonPath().get("firstName"), "Tom");
		 //String body4 = response.getBody().asString();
		 //System.out.println("response Body is"+ body4);		
	//5.Delete the employee with <emp id>
		response = deletEntry(empId);
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertNotEquals(response.getBody().jsonPath().get("firstName"), "Tom");	
	//6.Get single employee with emp ID.
		response = GetSinglentry(empId);
		Assert.assertEquals(response.getStatusCode(), 400);			
	//1.Get method
		response = GetAllEmployees();
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.jsonPath().getList("employees").size(), NumberofEmployees);					
	}	
//=============================================================================================================================
//==============================================================================================================================
/////Functions/////===========================================================		
	
	//1.Get method
	public Response GetAllEmployees() 
	{
		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.get();
		return response;	
	}			
	//2.Post Method
	public Response CreatenewEmployee(String fName, String lName, int salary, String email) 
	{		
		RestAssured.baseURI = BaseURI;
		JSONObject jobj =  new JSONObject();
		jobj.put("firstName", fName);
		jobj.put("lastName", lName);
		jobj.put("salary", salary);
		jobj.put("email", email);			
		RequestSpecification request = RestAssured.given();
		Response response = request.contentType(ContentType.JSON)
				.accept(ContentType.JSON).body(jobj.toString()).post("/create");		
		return response;				
	}		
	//3.Put Method
	public Response UpdateEmployee(int empID, String fName, String lName, int sy) 
	{		
		JSONObject jobj =  new JSONObject();
		jobj.put("firstName", fName);
		jobj.put("lastName", lName);
		jobj.put("salary", sy);	
	
	RestAssured.baseURI = BaseURI;
	RequestSpecification request = RestAssured.given();
	Response response = request.contentType(ContentType.JSON)
			.accept(ContentType.JSON).body(jobj.toString()).put("/"+ empID);	
	return response;
	}		
	//4.Get single employee with emp ID.
	public Response GetSinglentry(int empID) 
	{
		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.get("/"+empID);
		return response;	
	}		
	//5.Delete the employee with <emp id>
	public Response deletEntry(int empID) 
	{
		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.delete("/"+empID);
		return response;
	}		
	}
