/*
 * Class that handles File Control communicating to S3
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListBucketsRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3FileControl {
	// AmazonS3Client.deleteObjects
	AmazonS3 s3client;

	// Constructor
	public S3FileControl() {
		s3client = new AmazonS3Client(
				new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_EAST_1);
		s3client.setRegion(usWest2);
	}

	// Moves an object from one bucket/key to another
	public void moveObject(String fromBucket, String fromKey, String toBucket,
			String toKey) {
		s3client.copyObject(fromBucket, fromKey, toBucket, toKey);
		s3client.deleteObject(fromBucket, fromKey);
	}

	// Retrieves all objects starting with the prefix (use to get all in a folder)
	public List<S3ObjectSummary> getAllObjectSummaries(String bucket,
			String prefix) {
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
				.withBucketName(bucket).withPrefix(prefix);

		ObjectListing objectListing;
		List<S3ObjectSummary> keyList = null;
		do {
			objectListing = s3client.listObjects(listObjectsRequest);
			if (keyList == null)
				keyList = objectListing.getObjectSummaries();
			else
				keyList.addAll(objectListing.getObjectSummaries());
			listObjectsRequest.setMarker(objectListing.getNextMarker());
		} while (objectListing.isTruncated());

		return keyList;
	}

	// Retrieves all objects in current 'directory' of the bucket
	public List<S3ObjectSummary> getAllObjectSummaries(String bucket) {
		try {
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
					.withBucketName(bucket).withDelimiter("/");

			ObjectListing objectListing;
			List<S3ObjectSummary> keyList = null;

			do {
				objectListing = s3client.listObjects(listObjectsRequest);
				if (keyList == null)
					keyList = objectListing.getObjectSummaries();
				else
					keyList.addAll(objectListing.getObjectSummaries());
				listObjectsRequest.setMarker(objectListing.getNextMarker());
			} while (objectListing.isTruncated());

			return keyList;
		} catch (AmazonServiceException ase) {
			System.out
					.println("Caught an AmazonServiceException, which means your request made it "
							+ "to Amazon S3, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out
					.println("Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with S3, "
							+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
		return null;
	}

	// Retrieves the object with the specified key
	public S3Object getObject(String bucket, String key) {
		try {
			return s3client.getObject(new GetObjectRequest("InputData", key));
		} catch (AmazonServiceException ase) {
			System.out
					.println("Caught an AmazonServiceException, which means your request made it "
							+ "to Amazon S3, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out
					.println("Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with S3, "
							+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}

		return null;
	}

	public void testMethod() throws IOException {
		S3Object object = getObject("InputData", "Dataset_One.json");
		if (object != null)
			displayTextInputStream(object.getObjectContent());
	}

	private static void displayTextInputStream(InputStream input)
			throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;

			System.out.println("    " + line);
		}
		System.out.println();
	}

}
