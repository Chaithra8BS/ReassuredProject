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


public class APIENDTESTCASE {		
		Response response;
		String BaseURI = "http://localhost:3000/employees";	
		@Test
		public void AllTests()
		{		
		//1.Get method
			response = GetAllEmployees();
			Assert.assertEquals(response.getStatusCode(), 200);
			 //String body = response.getBody().asString();	
			 //JsonPath json= response.jsonPath();
			String body = response.getBody().asString();
			System.out.println("response of GET is: " + body);
			Assert.assertEquals(response.jsonPath().getList("employees").size(), 4);	
			int NumberofEmployees = response.jsonPath().getList("employees").size();
		//2.Post Method
			response = CreatenewEmployee("John",300000);
			String body1 = response.getBody().asString();
			System.out.println("response of POST is: " + body1);
			Assert.assertEquals(response.getStatusCode(), 201);			
			 //JsonPath json1= response.jsonPath();				
			Assert.assertEquals(response.jsonPath().get("name"), "John");
			Assert.assertEquals(response.jsonPath().getInt("id"), 5);
			int empId = response.jsonPath().getInt("id");
		//3.Put Method
			response = UpdateEmployee(empId,"Tom",500000);
			 //JsonPath json = response.jsonPath();		
			Assert.assertEquals(response.getStatusCode(), 200);
			Assert.assertEquals(response.getBody().jsonPath().get("name"), "Tom");	
			Assert.assertNotEquals(response.getBody().jsonPath().get("name"), "John");
			String body2 = response.getBody().asString();
			System.out.println("response of PUT is: " + body2);
		//4.Get single employee with emp ID.
			response = GetSinglentry(empId);
			Assert.assertEquals(response.getStatusCode(), 200);
			Assert.assertEquals(response.getBody().jsonPath().get("name"), "Tom");
			 //String body4 = response.getBody().asString();
			 //System.out.println("response Body is"+ body4);		
		//5.Delete the employee with <emp id>
			response = deletEntry(empId);
			Assert.assertEquals(response.getStatusCode(), 200);
			Assert.assertNotEquals(response.getBody().jsonPath().get("name"), "Tom");	
		//6.Get single employee with emp ID.
			response = GetSinglentry(empId);
			Assert.assertEquals(response.getStatusCode(), 404);			
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
		public Response CreatenewEmployee(String Name,  int salary) 
		{		
			RestAssured.baseURI = BaseURI;
			JSONObject jobj =  new JSONObject();
			jobj.put("name", Name);			
			jobj.put("salary", salary);		
			RequestSpecification request = RestAssured.given();
			Response response = request.contentType(ContentType.JSON)
					.accept(ContentType.JSON).body(jobj.toString()).post("/create");		
			return response;				
		}		
		//3.Put Method
		public Response UpdateEmployee(int empID, String Name, int salary) 
		{		
			JSONObject jobj =  new JSONObject();
			jobj.put("name", Name);
			jobj.put("salary", salary);	
		
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


