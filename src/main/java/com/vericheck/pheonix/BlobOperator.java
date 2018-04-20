package com.vericheck.pheonix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import com.microsoft.azure.serverless.functions.annotation.*;
import com.silicus.ach.azure.fileupload.StorageServiceImpl;
import com.microsoft.azure.serverless.functions.*;

public class BlobOperator {
	
	 @FunctionName("blobUploadFunction")
     public HttpResponseMessage<String> calulatorFunction(@HttpTrigger(name = "req", methods = {"get", "post"}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
             final ExecutionContext context) {
         

         // Parse query parameter
         String query = request.getQueryParameters().get("firstNumAsString");
         String containerName = request.getBody().orElse(query);
         
         File query1 = new File(request.getQueryParameters().get("secondNumAsString"));
         //String dataToBeUploded = request.getBody().orElse(query1);
         context.getLogger().info("Java HTTP trigger processed a request.The container name is: "+containerName);

         if (containerName == null || query1==null) {
             return request.createResponse(400, "Please pass a name on the query string or in the request body");
         } else {
        	 try {
        	    	StorageServiceImpl storageServiceImpl = new StorageServiceImpl();
        	    	storageServiceImpl.createBlobContainer(containerName);
        	    	//File ach = new File("D:\\ACHData\\TempACH.csv");
        	    	Path path = query1.toPath();		
        			byte[] data = Files.readAllBytes(path);	
        			storageServiceImpl.createBlockBlob("TestFile", data);    
        			//storageServiceImpl.deleteBlob("TestAchFile");
        	    	} catch (IOException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
             return request.createResponse(200, "Done");
         }
     }

}
