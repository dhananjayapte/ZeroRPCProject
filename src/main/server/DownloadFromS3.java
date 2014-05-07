package main.server;


import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;


public class DownloadFromS3 {
	/*

	 @author: Nitish

	 */
public static String S3download() throws IOException {
        
        AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials("qwdsad", "asd/sdq"));
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		s3.setRegion(usWest2);
		/*String filepath = "C:\\Users\\Nitish\\Desktop\\vmLog.log";
		File myFile = new File(filepath);*/
        String bucketName = "s3-bucket-zerorpc";
        String key = "vmLog.log";

        
        try {
                      
        	s3.setRegion(usWest2);
            GetObjectRequest request = new GetObjectRequest(bucketName,key);
            	  S3Object object = s3.getObject(request);
            	  S3ObjectInputStream objectContent = object.getObjectContent();
            	  IOUtils.copy(objectContent, new FileOutputStream("C://upload//"+key));

       
            System.out.println("Listing objects");
            s3.setRegion(usWest2);
            ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
                    .withBucketName(bucketName).withPrefix(key));
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println(" - " + objectSummary.getKey() + "  " +
                                   "(size = " + objectSummary.getSize() + ")");
            }
            System.out.println();

            /*
             * Delete an object - Unless versioning has been turned on for your bucket,
             * there is no way to undelete an object, so use caution when deleting objects.
             */
           //System.out.println("Deleting an object\n");
           // s3.deleteObject(bucketName, key);

            
             /* Delete a bucket - A bucket must be completely empty before it can be
             * deleted, so remember to delete any objects from your buckets before
             * you try to delete them.
             */
           // System.out.println("Deleting bucket " + bucketName + "\n");
           // s3.deleteBucket(bucketName);
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
        return "Object downloaded successfully";
    }

}
